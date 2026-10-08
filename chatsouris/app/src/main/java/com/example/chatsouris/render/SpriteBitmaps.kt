package com.example.chatsouris.render

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/** Convertit un [Sprite] en bitmap, une seule fois au démarrage. */
fun Sprite.toImageBitmap(): ImageBitmap {
    val pixels = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            pixels[y * width + x] = Palette.colors[rows[y][x]] ?: 0
        }
    }
    return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888).asImageBitmap()
}

class SpriteSet {
    private val cache = HashMap<Sprite, ImageBitmap>()
    operator fun get(sprite: Sprite): ImageBitmap = cache.getOrPut(sprite) { sprite.toImageBitmap() }
}
