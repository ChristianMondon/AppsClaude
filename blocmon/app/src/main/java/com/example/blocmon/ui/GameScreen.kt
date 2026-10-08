package com.example.blocmon.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blocmon.game.Bestiary
import com.example.blocmon.game.Block
import com.example.blocmon.game.Dice
import com.example.blocmon.game.Dir
import com.example.blocmon.game.GameState
import com.example.blocmon.game.Pos
import com.example.blocmon.game.Species
import kotlin.random.Random

private const val VIEW = 9

@Composable
fun GameScreen() {
    var state by remember { mutableStateOf(GameState.new(seed = Random.nextLong())) }
    val dice = remember { Dice { Random.nextDouble() } }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Balles : ${state.balls}   ·   Capturés : ${state.caught.size}",
            fontSize = 18.sp, fontWeight = FontWeight.Bold,
        )
        WorldView(state, Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(8.dp)))
        Text(state.message.ifEmpty { " " }, fontSize = 16.sp)
        Collection(state.caught)
        DPad { dir -> state = state.move(dir, dice) }
    }

    state.encounter?.let { enc ->
        AlertDialog(
            onDismissRequest = {},
            title = { Text("${enc.species.name} sauvage !") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(Modifier.size(96.dp).clip(RoundedCornerShape(12.dp)).background(Color(enc.species.color)))
                    Text("Balles restantes : ${state.balls}")
                    if (state.message.isNotEmpty()) Text(state.message)
                }
            },
            confirmButton = {
                TextButton(onClick = { state = state.throwBall(dice) }, enabled = state.balls > 0) {
                    Text("Lancer la balle")
                }
            },
            dismissButton = { TextButton(onClick = { state = state.flee() }) { Text("Fuir") } },
        )
    }
}

@Composable
private fun WorldView(state: GameState, modifier: Modifier) {
    Canvas(modifier) {
        val cell = size.minDimension / VIEW
        val half = VIEW / 2
        for (row in 0 until VIEW) for (col in 0 until VIEW) {
            val block = state.world[Pos(state.pos.x - half + col, state.pos.y - half + row)]
            val topLeft = Offset(col * cell, row * cell)
            drawRect(blockColor(block), topLeft, Size(cell, cell))
            if (block == Block.TREE) {
                drawRect(Color(0xFF5D4037), Offset(topLeft.x + cell * 0.4f, topLeft.y + cell * 0.6f), Size(cell * 0.2f, cell * 0.4f))
            }
            drawRect(Color.Black.copy(alpha = 0.12f), topLeft, Size(cell, cell), style = Stroke(1f))
        }
        drawRect(
            Color(0xFF1565C0),
            Offset(half * cell + cell * 0.2f, half * cell + cell * 0.2f),
            Size(cell * 0.6f, cell * 0.6f),
        )
    }
}

private fun blockColor(block: Block?) = when (block) {
    Block.GRASS -> Color(0xFF7CB342)
    Block.TALL_GRASS -> Color(0xFF33691E)
    Block.SAND -> Color(0xFFFFE082)
    Block.WATER -> Color(0xFF29B6F6)
    Block.STONE -> Color(0xFF9E9E9E)
    Block.TREE -> Color(0xFF2E7D32)
    null -> Color(0xFF212121)
}

@Composable
private fun Collection(caught: List<Species>) {
    val counts = Bestiary.all.map { it to caught.count { c -> c.id == it.id } }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        for ((species, n) in counts) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(28.dp).clip(RoundedCornerShape(6.dp))
                        .background(if (n > 0) Color(species.color) else Color.LightGray),
                )
                Text(if (n > 0) "${species.name} ×$n" else "???", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun DPad(onMove: (Dir) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        PadButton("▲") { onMove(Dir.UP) }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            PadButton("◀") { onMove(Dir.LEFT) }
            PadButton("▼") { onMove(Dir.DOWN) }
            PadButton("▶") { onMove(Dir.RIGHT) }
        }
    }
}

@Composable
private fun PadButton(label: String, onClick: () -> Unit) =
    Button(onClick = onClick, modifier = Modifier.size(72.dp)) { Text(label, fontSize = 24.sp) }
