package com.example.morpion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.morpion.game.GameState
import com.example.morpion.game.Outcome
import com.example.morpion.game.Player

@Composable
fun GameScreen() {
    var state by remember { mutableStateOf(GameState()) }
    var winsX by rememberSaveable { mutableIntStateOf(0) }
    var winsO by rememberSaveable { mutableIntStateOf(0) }
    var draws by rememberSaveable { mutableIntStateOf(0) }

    val outcome = state.outcome
    val winLine = (outcome as? Outcome.Won)?.line.orEmpty()

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Score("X", winsX); Score("Égalités", draws); Score("O", winsO)
        }
        Text(
            when (outcome) {
                Outcome.InProgress -> "Tour de ${state.current}"
                is Outcome.Won -> "${outcome.winner} gagne !"
                Outcome.Draw -> "Égalité"
            },
            fontSize = 24.sp, fontWeight = FontWeight.Bold,
        )
        Column(Modifier.fillMaxWidth().aspectRatio(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (row in 0..2) {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (col in 0..2) {
                        val i = row * 3 + col
                        Cell(state.cells[i], i in winLine, Modifier.weight(1f)) {
                            val before = state.outcome
                            state = state.play(i)
                            if (before == Outcome.InProgress) when (val o = state.outcome) {
                                is Outcome.Won -> if (o.winner == Player.X) winsX++ else winsO++
                                Outcome.Draw -> draws++
                                Outcome.InProgress -> Unit
                            }
                        }
                    }
                }
            }
        }
        Button(onClick = { state = GameState() }) { Text("Rejouer") }
    }
}

@Composable
private fun Score(label: String, value: Int) =
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label); Text("$value", fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }

@Composable
private fun Cell(player: Player?, highlighted: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.fillMaxSize().clip(RoundedCornerShape(12.dp))
            .background(if (highlighted) Color(0xFFA5D6A7) else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            player?.name.orEmpty(), fontSize = 56.sp, fontWeight = FontWeight.Bold,
            color = if (player == Player.X) Color(0xFF1565C0) else Color(0xFFC62828),
        )
    }
}
