package com.example.chatsouris.game

import kotlin.random.Random

object LevelGenerator {
    private const val SPACING_JITTER = 120
    private const val START_JITTER = 100

    /** Déterministe : même [def] (donc même graine), même niveau. */
    fun generate(def: LevelDef): LevelLayout {
        val rng = Random(def.seed)
        val obstacles = mutableListOf<Obstacle>()
        var x = Params.START_SAFE_ZONE + rng.nextInt(START_JITTER + 1)
        while (true) {
            val kind = def.allowedObstacles[rng.nextInt(def.allowedObstacles.size)]
            if (x + kind.width > def.length - Params.END_MARGIN) break
            obstacles += Obstacle(kind, x)
            x += kind.width + def.obstacleSpacing + rng.nextInt(SPACING_JITTER + 1)
        }
        return LevelLayout(def, obstacles)
    }
}
