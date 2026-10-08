package com.example.morpion.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class TicTacToeTest {
    private fun play(vararg moves: Int) = moves.fold(GameState()) { s, i -> s.play(i) }

    @Test fun validMovePlacesSymbolAndSwitchesPlayer() {
        val s = GameState().play(4)
        assertEquals(Player.X, s.cells[4])
        assertEquals(Player.O, s.current)
    }

    @Test fun occupiedCellIsIgnored() {
        val s = GameState().play(0)
        assertSame(s, s.play(0))
    }

    @Test fun allEightLinesWin() {
        for (line in GameState.LINES) {
            // X joue la ligne, O joue des cases hors ligne sans gagner
            val others = (0..8).filter { it !in line }.take(2)
            val s = play(line[0], others[0], line[1], others[1], line[2])
            assertEquals(Outcome.Won(Player.X, line), s.outcome)
        }
    }

    @Test fun fullBoardWithoutLineIsDraw() {
        // X O X / X O O / O X X
        val s = play(0, 1, 2, 4, 3, 5, 7, 6, 8)
        assertEquals(Outcome.Draw, s.outcome)
    }

    @Test fun noMoveAfterGameOver() {
        val won = play(0, 3, 1, 4, 2)
        assertTrue(won.outcome is Outcome.Won)
        assertSame(won, won.play(8))
    }

    @Test fun outOfRangeIndexIgnored() {
        val s = GameState()
        assertSame(s, s.play(9))
        assertSame(s, s.play(-1))
    }
}
