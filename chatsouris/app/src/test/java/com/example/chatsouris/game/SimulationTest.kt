package com.example.chatsouris.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Un « joueur parfait » doit pouvoir gagner : le niveau est faisable. */
class SimulationTest {
    private fun robotJumps(s: GameState): Boolean {
        if (!s.cat.onGround) return false
        val next = s.level.obstacles.firstOrNull { it.x + it.kind.width > s.cat.x - Params.CAT_HIT_W / 2 }
            ?: return false
        val d = next.x - s.cat.x
        return d in 0f..20f
    }

    private fun play(def: LevelDef): Pair<GameState, Boolean> {
        var s = GameState.initial(def)
        var everStumbled = false
        var frames = 0
        while (s.status == Status.PLAYING && frames < 60 * 300) {
            s = step(s, Input(robotJumps(s)), Params.FIXED_DT)
            if (s.cat.stumbleLeft > 0f) everStumbled = true
            frames++
        }
        return s to everStumbled
    }

    @Test fun perfectPlayerWinsLevel1WithoutStumbling() {
        val (s, stumbled) = play(Levels.level1)
        assertEquals(Status.WON, s.status)
        assertTrue(!stumbled)
    }

    @Test fun perfectPlayerWinsManyGeneratedVariants() {
        for (seed in 1L..30L) {
            val (s, stumbled) = play(Levels.level1.copy(seed = seed))
            assertEquals("graine $seed", Status.WON, s.status)
            assertTrue("graine $seed sans trébucher", !stumbled)
        }
    }

    @Test fun playerWhoNeverJumpsLoses() {
        var s = GameState.initial(Levels.level1)
        var frames = 0
        while (s.status == Status.PLAYING && frames < 60 * 300) {
            s = step(s, Input(), Params.FIXED_DT)
            frames++
        }
        assertEquals(Status.LOST, s.status)
    }
}
