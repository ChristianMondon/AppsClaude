package com.example.chatsouris.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class StepTest {
    private val dt = Params.FIXED_DT

    private fun def(
        length: Float = 4000f,
        mouseSpeed: Float = 98f,
        initialGap: Float = 100f,
        maxGap: Float = 240f,
        obstacles: List<ObstacleKind> = listOf(ObstacleKind.BARRIER),
    ) = LevelDef(1, length, 1L, 100f, mouseSpeed, 0f, initialGap, maxGap, 160f, obstacles)

    /** Niveau sans obstacle généré, avec les obstacles donnés placés à la main. */
    private fun state(vararg obstacles: Obstacle, d: LevelDef = def()) = GameState(
        level = LevelLayout(d, obstacles.toList()),
        cat = Cat(),
        mouseX = d.initialGap,
    )

    private fun run(s: GameState, seconds: Float, jumpAt: Set<Int> = emptySet()): GameState {
        var cur = s
        repeat((seconds / dt + 0.5f).toInt()) { i -> cur = step(cur, Input(jump = i in jumpAt), dt) }
        return cur
    }

    // US2 : le chat avance toujours
    @Test fun catAdvancesAtBaseSpeed() {
        val s = run(state(), 1f)
        assertEquals(100f, s.cat.x, 1f)
    }

    // US1 : saut
    @Test fun jumpLeavesGroundAndLandsWithExpectedHeightAndDuration() {
        var s = state()
        var maxY = 0f
        var airFrames = 0
        s = step(s, Input(jump = true), dt)
        assertFalse(s.cat.onGround)
        while (!s.cat.onGround) {
            maxY = maxOf(maxY, s.cat.y)
            airFrames++
            s = step(s, Input(), dt)
        }
        assertEquals(41.5f, maxY, 3f)
        assertEquals(0.62f, airFrames * dt, 0.04f)
    }

    @Test fun noDoubleJumpInTheAir() {
        var s = step(state(), Input(jump = true), dt)
        repeat(18) { s = step(s, Input(), dt) } // proche du sommet
        val before = s.cat.vy
        s = step(s, Input(jump = true), dt)
        assertTrue("la vitesse verticale continue de diminuer", s.cat.vy < before)
    }

    @Test fun jumpBufferJumpsOnLanding() {
        var s = step(state(), Input(jump = true), dt)
        while (s.cat.y > 2f || s.cat.vy > 0f) s = step(s, Input(), dt) // juste avant l'atterrissage
        s = step(s, Input(jump = true), dt) // appui un peu en avance
        var jumped = false
        repeat(10) {
            s = step(s, Input(), dt)
            if (!s.cat.onGround) jumped = true
        }
        assertTrue(jumped)
    }

    // US3 : obstacles
    @Test fun barrierMakesCatStumbleAndSlowsIt() {
        val s = run(state(Obstacle(ObstacleKind.BARRIER, 30f)), 0.5f)
        assertTrue(s.cat.stumbleLeft > 0f)
        assertTrue(s.cat.invulnerableLeft > 0f)
        val before = s.cat.x
        val after = step(s, Input(), dt).cat.x
        assertEquals(100f * Params.STUMBLE_SPEED_FACTOR * dt, after - before, 0.01f)
    }

    @Test fun jumpingOverBarrierAvoidsStumble() {
        val s0 = state(Obstacle(ObstacleKind.BARRIER, 40f))
        var s = s0
        var stumbled = false
        repeat(150) { i ->
            val jump = s.cat.onGround && 40f - s.cat.x <= 25f && 40f - s.cat.x > 0f
            s = step(s, Input(jump), dt)
            if (s.cat.stumbleLeft > 0f) stumbled = true
        }
        assertFalse(stumbled)
    }

    @Test fun lavaStumblesOnGroundAndNotWhenJumping() {
        val hit = run(state(Obstacle(ObstacleKind.LAVA, 30f)), 0.5f)
        assertTrue(hit.cat.stumbleLeft > 0f)
        var s = state(Obstacle(ObstacleKind.LAVA, 40f))
        var stumbled = false
        repeat(150) {
            val jump = s.cat.onGround && 40f - s.cat.x <= 20f && 40f - s.cat.x > 0f
            s = step(s, Input(jump), dt)
            if (s.cat.stumbleLeft > 0f) stumbled = true
        }
        assertFalse(stumbled)
    }

    @Test fun holeStumblesOnGroundButNotWhenJumpedOver() {
        val hit = run(state(Obstacle(ObstacleKind.HOLE, 30f)), 0.5f)
        assertTrue(hit.cat.stumbleLeft > 0f)
        var s = state(Obstacle(ObstacleKind.HOLE, 40f))
        var stumbled = false
        repeat(150) {
            val jump = s.cat.onGround && 40f - s.cat.x <= 20f && 40f - s.cat.x > 0f
            s = step(s, Input(jump), dt)
            if (s.cat.stumbleLeft > 0f) stumbled = true
        }
        assertFalse(stumbled)
    }

    @Test fun noSecondStumbleWhileInvulnerable() {
        // deux barrières rapprochées : la seconde ne relance pas le trébuchement
        val s0 = state(Obstacle(ObstacleKind.BARRIER, 30f), Obstacle(ObstacleKind.BARRIER, 45f))
        var s = s0
        var starts = 0
        var prev = 0f
        repeat(120) {
            s = step(s, Input(), dt)
            if (s.cat.stumbleLeft > prev + 0.01f) starts++
            prev = s.cat.stumbleLeft
        }
        assertEquals(1, starts)
    }

    @Test fun stumbleEndsAndSpeedRecovers() {
        val s = run(state(Obstacle(ObstacleKind.BARRIER, 30f)), 2f)
        assertEquals(0f, s.cat.stumbleLeft, 0.001f)
        val before = s.cat.x
        assertEquals(100f * dt, step(s, Input(), dt).cat.x - before, 0.01f)
    }

    // US7 / US8 / US9 : écart, victoire, défaite
    @Test fun gapAtStart() {
        assertEquals(100f, state().gap, 0.001f)
    }

    @Test fun stumbleWidensGap() {
        val clean = run(state(), 3f).gap
        val stumbled = run(state(Obstacle(ObstacleKind.BARRIER, 30f)), 3f).gap
        assertTrue(stumbled > clean + 20f)
    }

    @Test fun winWhenGapReachesZero() {
        val s = run(state(d = def(mouseSpeed = 50f, initialGap = 30f)), 2f)
        assertEquals(Status.WON, s.status)
    }

    @Test fun loseWhenGapReachesMax() {
        val s = run(state(d = def(mouseSpeed = 200f, maxGap = 150f)), 3f)
        assertEquals(Status.LOST, s.status)
    }

    @Test fun mouseIsBlockedAtEndOfTrackSoLevelEndsInVictory() {
        val d = def(length = 600f, mouseSpeed = 100f, initialGap = 100f)
        val s = run(state(d = d), 10f)
        assertEquals(Status.WON, s.status)
        assertTrue(s.mouseX <= 600f)
    }

    @Test fun nothingChangesAfterTheEnd() {
        val won = run(state(d = def(mouseSpeed = 50f, initialGap = 30f)), 2f)
        assertSame(won, step(won, Input(jump = true), dt))
    }
}
