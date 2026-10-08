package com.example.morpion.game

enum class Player { X, O;
    fun other() = if (this == X) O else X
}

sealed interface Outcome {
    data object InProgress : Outcome
    data class Won(val winner: Player, val line: List<Int>) : Outcome
    data object Draw : Outcome
}

data class GameState(
    val cells: List<Player?> = List(9) { null },
    val current: Player = Player.X,
) {
    val outcome: Outcome = computeOutcome(cells)

    /** Joue la case [index] ; renvoie l'état inchangé si le coup est invalide. */
    fun play(index: Int): GameState {
        if (outcome != Outcome.InProgress || index !in 0..8 || cells[index] != null) return this
        val next = cells.toMutableList().also { it[index] = current }
        return GameState(next, current.other())
    }

    companion object {
        val LINES = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6),
        )

        private fun computeOutcome(cells: List<Player?>): Outcome {
            for (line in LINES) {
                val p = cells[line[0]]
                if (p != null && line.all { cells[it] == p }) return Outcome.Won(p, line)
            }
            return if (cells.all { it != null }) Outcome.Draw else Outcome.InProgress
        }
    }
}
