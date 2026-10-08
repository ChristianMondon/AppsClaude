package com.example.blocmon.game

import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

enum class Block(val walkable: Boolean) {
    GRASS(true), TALL_GRASS(true), SAND(true), WATER(false), STONE(false), TREE(false)
}

enum class Dir(val dx: Int, val dy: Int) { UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0) }

data class Pos(val x: Int, val y: Int) {
    operator fun plus(d: Dir) = Pos(x + d.dx, y + d.dy)
}

class World(val width: Int, val height: Int, private val blocks: List<Block>) {
    init { require(blocks.size == width * height) }

    val spawn = Pos(width / 2, height / 2)

    fun inside(p: Pos) = p.x in 0 until width && p.y in 0 until height

    operator fun get(p: Pos): Block? = if (inside(p)) blocks[p.y * width + p.x] else null

    fun cells(): List<Block> = blocks

    companion object {
        fun generate(seed: Long, width: Int = 48, height: Int = 48): World {
            val rng = Random(seed)
            val ph = DoubleArray(6) { rng.nextDouble() * 2 * PI }
            val blocks = ArrayList<Block>(width * height)
            for (y in 0 until height) for (x in 0 until width) {
                val elevation = sin(x * 0.21 + ph[0]) + sin(y * 0.17 + ph[1]) +
                    0.7 * sin((x + y) * 0.13 + ph[2]) + 0.5 * sin((x - y) * 0.31 + ph[3])
                val moisture = sin(x * 0.15 + ph[4]) + sin(y * 0.23 + ph[5])
                val noise = rng.nextDouble()
                blocks += when {
                    elevation < -1.4 -> Block.WATER
                    elevation < -1.0 -> Block.SAND
                    elevation > 1.8 -> Block.STONE
                    moisture > 0.6 -> Block.TALL_GRASS
                    noise < 0.06 -> Block.TREE
                    else -> Block.GRASS
                }
            }
            val cx = width / 2
            val cy = height / 2
            for (dy in -1..1) for (dx in -1..1) blocks[(cy + dy) * width + (cx + dx)] = Block.GRASS
            return World(width, height, blocks)
        }
    }
}
