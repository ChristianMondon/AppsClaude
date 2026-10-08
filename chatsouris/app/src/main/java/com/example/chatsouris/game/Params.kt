package com.example.chatsouris.game

/** Valeurs d'équilibrage et de dimensionnement (voir la spécification, sections 4 à 6). */
object Params {
    const val SCREEN_W = 320
    const val SCREEN_H = 180
    const val GROUND_SCREEN_Y = 148
    const val CAT_SCREEN_X = 72f

    const val FIXED_DT = 1f / 60f
    const val MAX_FRAME_TIME = 0.1f

    const val GRAVITY = 900f
    const val JUMP_V = 280f
    const val JUMP_BUFFER = 0.1f

    const val CAT_HIT_W = 10f
    const val CAT_HIT_H = 12f
    const val HOLE_TOLERANCE = 4f

    const val STUMBLE_TIME = 0.8f
    const val STUMBLE_SPEED_FACTOR = 0.4f
    const val INVULN_TIME = 1.0f

    const val START_SAFE_ZONE = 300f
    const val END_MARGIN = 100f
}
