package com.example.chatsouris.game

object Levels {
    val level1 = LevelDef(
        id = 1,
        length = 4000f,
        seed = 1001L,
        catBaseSpeed = 100f,
        mouseSpeed = 98f,
        mouseAccel = 0f,
        initialGap = 100f,
        maxGap = 240f,
        obstacleSpacing = 160f,
        allowedObstacles = listOf(ObstacleKind.BARRIER, ObstacleKind.HOLE),
    )

    val all = listOf(level1)
}
