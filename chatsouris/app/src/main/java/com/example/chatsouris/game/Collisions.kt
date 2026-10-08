package com.example.chatsouris.game

object Collisions {
    /** Vrai si le chat touche [o] (trou : seulement s'il est au sol, sur plus de [Params.HOLE_TOLERANCE] px). */
    fun hits(cat: Cat, o: Obstacle): Boolean {
        val catLeft = cat.x - Params.CAT_HIT_W / 2
        val catRight = cat.x + Params.CAT_HIT_W / 2
        val oRight = o.x + o.kind.width
        if (o.kind == ObstacleKind.HOLE) {
            if (!cat.onGround) return false
            return minOf(catRight, oRight) - maxOf(catLeft, o.x) > Params.HOLE_TOLERANCE
        }
        val overlapX = catRight > o.x && catLeft < oRight
        val overlapY = cat.y < o.kind.height && cat.y + Params.CAT_HIT_H > 0f
        return overlapX && overlapY
    }
}
