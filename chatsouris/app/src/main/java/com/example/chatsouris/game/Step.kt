package com.example.chatsouris.game

/** Fait avancer le jeu de [dt] secondes. Pure et déterministe. */
fun step(s: GameState, input: Input, dt: Float): GameState {
    if (s.status != Status.PLAYING) return s
    val def = s.level.def
    val c = s.cat

    var buffer = if (input.jump) Params.JUMP_BUFFER else maxOf(0f, c.jumpBufferLeft - dt)
    var y = c.y
    var vy = c.vy
    var onGround = c.onGround
    if (buffer > 0f && onGround) {
        vy = Params.JUMP_V
        onGround = false
        buffer = 0f
    }
    if (!onGround) {
        vy -= Params.GRAVITY * dt
        y += vy * dt
        if (y <= 0f) {
            y = 0f
            vy = 0f
            onGround = true
        }
    }

    val speed = def.catBaseSpeed * if (c.stumbleLeft > 0f) Params.STUMBLE_SPEED_FACTOR else 1f
    var stumble = maxOf(0f, c.stumbleLeft - dt)
    var invulnerable = maxOf(0f, c.invulnerableLeft - dt)
    var cat = c.copy(x = c.x + speed * dt, y = y, vy = vy, onGround = onGround, jumpBufferLeft = buffer)
    if (invulnerable <= 0f && s.level.obstacles.any { Collisions.hits(cat, it) }) {
        stumble = Params.STUMBLE_TIME
        invulnerable = Params.INVULN_TIME
    }
    cat = cat.copy(stumbleLeft = stumble, invulnerableLeft = invulnerable)

    val time = s.time + dt
    val mouseX = minOf(s.mouseX + (def.mouseSpeed + def.mouseAccel * time) * dt, def.length)
    val gap = mouseX - cat.x
    val status = when {
        gap <= 0f -> Status.WON
        gap >= def.maxGap -> Status.LOST
        else -> Status.PLAYING
    }
    return s.copy(cat = cat, mouseX = mouseX, time = time, status = status)
}
