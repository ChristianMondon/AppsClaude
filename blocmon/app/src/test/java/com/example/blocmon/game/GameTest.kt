package com.example.blocmon.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class GameTest {
    private class SeqDice(vararg v: Double) : Dice {
        private val values = v.toList()
        private var i = 0
        override fun roll() = values[minOf(i++, values.size - 1)]
    }

    private fun strip(vararg b: Block) = World(b.size, 1, b.toList())
    private fun state(w: World, pos: Pos = Pos(0, 0), balls: Int = 10) = GameState(w, pos, balls)

    @Test fun sameSeedSameWorld() {
        assertEquals(World.generate(7).cells(), World.generate(7).cells())
        assertNotEquals(World.generate(7).cells(), World.generate(8).cells())
    }

    @Test fun spawnIsWalkableAndWorldIsVaried() {
        val w = World.generate(42)
        assertEquals(true, w[w.spawn]?.walkable)
        assertTrue(Block.TALL_GRASS in w.cells())
        assertTrue(w.cells().toSet().size >= 4)
    }

    @Test fun obstacleAndEdgeBlockMove() {
        val s = state(strip(Block.GRASS, Block.WATER))
        assertSame(s, s.move(Dir.RIGHT, SeqDice(0.9)))
        assertSame(s, s.move(Dir.LEFT, SeqDice(0.9)))
    }

    @Test fun validMoveUpdatesPositionAndSteps() {
        val s = state(strip(Block.GRASS, Block.SAND)).move(Dir.RIGHT, SeqDice(0.9))
        assertEquals(Pos(1, 0), s.pos)
        assertEquals(1, s.steps)
    }

    @Test fun encounterOnlyOnTallGrassWithProbability() {
        val w = strip(Block.GRASS, Block.TALL_GRASS, Block.GRASS)
        val hit = state(w).move(Dir.RIGHT, SeqDice(0.0, 0.0))
        assertEquals("herbon", hit.encounter?.species?.id)
        assertNull(state(w).move(Dir.RIGHT, SeqDice(0.99)).encounter)
        assertNull(state(w, Pos(1, 0)).move(Dir.RIGHT, SeqDice(0.0)).encounter)
    }

    @Test fun noMoveDuringEncounter() {
        val s = state(strip(Block.GRASS, Block.TALL_GRASS, Block.GRASS)).move(Dir.RIGHT, SeqDice(0.0, 0.0))
        assertSame(s, s.move(Dir.RIGHT, SeqDice(0.9)))
    }

    private fun inEncounter() =
        state(strip(Block.GRASS, Block.TALL_GRASS)).move(Dir.RIGHT, SeqDice(0.0, 0.0))

    @Test fun throwCatches() {
        val s = inEncounter().throwBall(SeqDice(0.0))
        assertEquals(9, s.balls)
        assertEquals(listOf("herbon"), s.caught.map { it.id })
        assertNull(s.encounter)
    }

    @Test fun throwMissCreatureStays() {
        val s = inEncounter().throwBall(SeqDice(0.99, 0.99))
        assertEquals(9, s.balls)
        assertTrue(s.caught.isEmpty())
        assertEquals("herbon", s.encounter?.species?.id)
    }

    @Test fun throwMissCreatureFlees() {
        val s = inEncounter().throwBall(SeqDice(0.99, 0.0))
        assertEquals(9, s.balls)
        assertTrue(s.caught.isEmpty())
        assertNull(s.encounter)
    }

    @Test fun noThrowWithoutBalls() {
        val s = inEncounter().copy(balls = 0)
        val after = s.throwBall(SeqDice(0.0))
        assertEquals(0, after.balls)
        assertTrue(after.caught.isEmpty())
        assertEquals(s.encounter, after.encounter)
    }

    @Test fun fleeEndsEncounter() {
        val s = inEncounter().flee()
        assertNull(s.encounter)
        assertTrue(s.caught.isEmpty())
    }

    @Test fun ballRegeneratesEveryTwentySteps() {
        val w = World(30, 1, List(30) { Block.GRASS })
        var s = state(w, balls = 5)
        repeat(19) { s = s.move(Dir.RIGHT, SeqDice(0.9)) }
        assertEquals(5, s.balls)
        s = s.move(Dir.RIGHT, SeqDice(0.9))
        assertEquals(6, s.balls)
        assertEquals(10, state(w, balls = 10).copy(steps = 19).move(Dir.RIGHT, SeqDice(0.9)).balls)
    }

    @Test fun bestiaryRespectsRarity() {
        assertEquals("herbon", Bestiary.pick(0.0).id)
        assertEquals("flamblo", Bestiary.pick(0.999).id)
    }
}
