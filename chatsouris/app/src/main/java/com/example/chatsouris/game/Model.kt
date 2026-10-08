package com.example.chatsouris.game

enum class ObstacleKind(val width: Float, val height: Float) {
    BARRIER(12f, 16f),
    HOLE(28f, 0f),
    LAVA(24f, 4f),
}

/** [x] est le bord gauche ; l'obstacle repose sur le sol (y = 0). */
data class Obstacle(val kind: ObstacleKind, val x: Float)

/** [x] est le centre du chat ; [y] la hauteur des pieds au-dessus du sol. */
data class Cat(
    val x: Float = 0f,
    val y: Float = 0f,
    val vy: Float = 0f,
    val onGround: Boolean = true,
    val stumbleLeft: Float = 0f,
    val invulnerableLeft: Float = 0f,
    val jumpBufferLeft: Float = 0f,
)

enum class Status { PLAYING, WON, LOST }

data class Input(val jump: Boolean = false)

data class LevelDef(
    val id: Int,
    val length: Float,
    val seed: Long,
    val catBaseSpeed: Float,
    val mouseSpeed: Float,
    val mouseAccel: Float,
    val initialGap: Float,
    val maxGap: Float,
    val obstacleSpacing: Float,
    val allowedObstacles: List<ObstacleKind>,
)

data class LevelLayout(val def: LevelDef, val obstacles: List<Obstacle>)

data class GameState(
    val level: LevelLayout,
    val cat: Cat,
    val mouseX: Float,
    val time: Float = 0f,
    val status: Status = Status.PLAYING,
) {
    val gap: Float get() = mouseX - cat.x

    companion object {
        fun initial(def: LevelDef) = GameState(
            level = LevelGenerator.generate(def),
            cat = Cat(),
            mouseX = def.initialGap,
        )
    }
}
