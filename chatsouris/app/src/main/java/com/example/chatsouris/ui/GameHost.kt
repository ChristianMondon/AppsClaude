package com.example.chatsouris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.withFrameNanos
import com.example.chatsouris.game.GameState
import com.example.chatsouris.game.Input
import com.example.chatsouris.game.Levels
import com.example.chatsouris.game.Params
import com.example.chatsouris.game.Status
import com.example.chatsouris.game.step
import com.example.chatsouris.render.SpriteSet
import com.example.chatsouris.render.drawGame

private class JumpRequest { var pending = false }

/** Boucle de jeu à pas fixe et écran de jeu (M1 : niveau 1, toucher = sauter, toucher après la fin = rejouer). */
@Composable
fun GameHost() {
    val sprites = remember { SpriteSet() }
    val jump = remember { JumpRequest() }
    var state by remember { mutableStateOf(GameState.initial(Levels.level1)) }

    LaunchedEffect(Unit) {
        var last = 0L
        var accumulator = 0f
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) {
                    accumulator += ((now - last) / 1_000_000_000f).coerceAtMost(Params.MAX_FRAME_TIME)
                    while (accumulator >= Params.FIXED_DT) {
                        state = step(state, Input(jump.pending), Params.FIXED_DT)
                        jump.pending = false
                        accumulator -= Params.FIXED_DT
                    }
                }
                last = now
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (state.status == Status.PLAYING) {
                            jump.pending = true
                        } else {
                            state = GameState.initial(Levels.level1)
                        }
                    },
                )
            },
    ) {
        Canvas(Modifier.fillMaxSize()) { drawGame(state, sprites) }
        if (state.status != Status.PLAYING) {
            val won = state.status == Status.WON
            Text(
                text = if (won) "Souris attrapée ! Touchez pour rejouer" else "La souris s'est échappée… Touchez pour rejouer",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
