package com.example.blocmon.game

/** Une espèce de créature. [color] est un ARGB ; [weight] sa rareté (plus grand = plus fréquent). */
data class Species(val id: String, val name: String, val color: Long, val catchRate: Double, val weight: Int)

object Bestiary {
    val all = listOf(
        Species("herbon", "Herbon", 0xFF66BB6A, 0.60, 50),
        Species("aquaby", "Aquaby", 0xFF42A5F5, 0.45, 30),
        Species("pierrot", "Pierrot", 0xFF8D6E63, 0.30, 15),
        Species("flamblo", "Flamblo", 0xFFFF7043, 0.15, 5),
    )

    /** Tire une espèce pondérée par rareté, [roll] dans [0, 1). */
    fun pick(roll: Double): Species {
        var left = roll * all.sumOf { it.weight }
        for (s in all) {
            left -= s.weight
            if (left < 0) return s
        }
        return all.last()
    }
}
