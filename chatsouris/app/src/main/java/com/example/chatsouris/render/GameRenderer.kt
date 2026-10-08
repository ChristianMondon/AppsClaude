package com.example.chatsouris.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.chatsouris.game.GameState
import com.example.chatsouris.game.ObstacleKind
import com.example.chatsouris.game.Params
import kotlin.math.floor

private const val GROUND = Params.GROUND_SCREEN_Y.toFloat()
private const val FRAME_TIME = 0.12f

/** Dessine l'écran virtuel 320×180, agrandi par un facteur entier et centré (pixels nets). */
fun DrawScope.drawGame(state: GameState, sprites: SpriteSet) {
    val factor = floor(minOf(size.width / Params.SCREEN_W, size.height / Params.SCREEN_H)).coerceAtLeast(1f)
    val left = (size.width - Params.SCREEN_W * factor) / 2f
    val top = (size.height - Params.SCREEN_H * factor) / 2f
    translate(left, top) {
        scale(factor, Offset.Zero) {
            drawWorld(state, sprites)
        }
    }
}

private fun DrawScope.image(sprites: SpriteSet, sprite: Sprite, x: Float, y: Float) {
    val img = sprites[sprite]
    drawImage(
        image = img,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(sprite.width, sprite.height),
        dstOffset = IntOffset(floor(x).toInt(), floor(y).toInt()),
        dstSize = IntSize(sprite.width, sprite.height),
        filterQuality = FilterQuality.None,
    )
}

private fun DrawScope.drawWorld(state: GameState, sprites: SpriteSet) {
    val cam = state.cat.x - Params.CAT_SCREEN_X
    val frame = ((state.time / FRAME_TIME).toInt()) % 2

    drawRect(Color(Palette.SKY), Offset.Zero, Size(Params.SCREEN_W.toFloat(), Params.SCREEN_H.toFloat()))

    // fond : nuages (lents) et collines
    tileRow(sprites, Sprites.cloud, cam * 0.1f, 20f, 160)
    tileRow(sprites, Sprites.hill, cam * 0.35f, GROUND - Sprites.hill.height, 64)

    // sol
    var gx = -(cam.mod(16f))
    while (gx < Params.SCREEN_W) {
        image(sprites, Sprites.ground, gx, GROUND)
        gx += 16f
    }

    // obstacles
    for (o in state.level.obstacles) {
        val sx = o.x - cam
        if (sx > Params.SCREEN_W || sx + o.kind.width < 0f) continue
        when (o.kind) {
            ObstacleKind.BARRIER -> image(sprites, Sprites.barrier, sx - 2f, GROUND - 16f)
            ObstacleKind.HOLE -> drawRect(Color(Palette.PIT), Offset(floor(sx), GROUND), Size(o.kind.width, 32f))
            ObstacleKind.LAVA -> {
                val lava = if (frame == 0) Sprites.lavaA else Sprites.lavaB
                var lx = sx
                while (lx < sx + o.kind.width) {
                    image(sprites, lava, lx, GROUND - 4f)
                    lx += 8f
                }
            }
        }
    }

    // souris
    val mouseScreenX = Params.CAT_SCREEN_X + state.gap
    if (mouseScreenX < Params.SCREEN_W + 8f) {
        image(sprites, if (frame == 0) Sprites.mouseRunA else Sprites.mouseRunB, mouseScreenX - 8f, GROUND - 16f)
    }

    // chat (clignote quand il est protégé après un choc)
    val cat = state.cat
    val visible = cat.invulnerableLeft <= 0f || ((state.time * 12f).toInt() % 2 == 0)
    if (visible) {
        val sprite = when {
            cat.stumbleLeft > 0f -> Sprites.catStumble
            !cat.onGround -> Sprites.catJump
            frame == 0 -> Sprites.catRunA
            else -> Sprites.catRunB
        }
        image(sprites, sprite, Params.CAT_SCREEN_X - 8f, GROUND - 15f - cat.y)
    }

    drawGapBar(state)
}

private fun DrawScope.tileRow(sprites: SpriteSet, sprite: Sprite, offset: Float, y: Float, spacing: Int) {
    var x = -(offset.mod(spacing.toFloat()))
    while (x < Params.SCREEN_W) {
        image(sprites, sprite, x, y)
        x += spacing
    }
}

/** Barre d'écart chat-souris : pleine = la souris est loin. */
private fun DrawScope.drawGapBar(state: GameState) {
    val barW = 100f
    val x0 = (Params.SCREEN_W - barW) / 2f
    val ratio = (state.gap / state.level.def.maxGap).coerceIn(0f, 1f)
    val color = when {
        ratio < 0.5f -> Color(0xFF5DBB4A)
        ratio < 0.8f -> Color(0xFFFFD84A)
        else -> Color(0xFFE03C31)
    }
    drawRect(Color(0xFF1B1B2F), Offset(x0 - 1f, 7f), Size(barW + 2f, 8f))
    drawRect(Color(0xFFFFFFFF), Offset(x0, 8f), Size(barW, 6f))
    drawRect(color, Offset(x0, 8f), Size(barW * ratio, 6f))
}
