package com.example.chatsouris.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LevelGeneratorTest {
    private val def = Levels.level1

    @Test fun sameDefinitionSameLevel() {
        assertEquals(LevelGenerator.generate(def), LevelGenerator.generate(def))
    }

    @Test fun differentSeedDifferentLevel() {
        assertNotEquals(
            LevelGenerator.generate(def).obstacles,
            LevelGenerator.generate(def.copy(seed = def.seed + 1)).obstacles,
        )
    }

    @Test fun invariantsHoldForManySeeds() {
        for (seed in 1L..50L) {
            val d = def.copy(seed = seed)
            val obs = LevelGenerator.generate(d).obstacles
            assertTrue("au moins quelques obstacles", obs.size >= 10)
            assertTrue("zone de départ vide", obs.first().x >= Params.START_SAFE_ZONE)
            assertTrue("marge de fin", obs.all { it.x + it.kind.width <= d.length - Params.END_MARGIN })
            assertTrue("types autorisés", obs.all { it.kind in d.allowedObstacles })
            obs.zipWithNext().forEach { (a, b) ->
                assertTrue("espacement", b.x - (a.x + a.kind.width) >= d.obstacleSpacing)
            }
        }
    }

    @Test fun initialStateMatchesDefinition() {
        val s = GameState.initial(def)
        assertEquals(0f, s.cat.x, 0f)
        assertEquals(def.initialGap, s.gap, 0.001f)
        assertEquals(Status.PLAYING, s.status)
    }
}
