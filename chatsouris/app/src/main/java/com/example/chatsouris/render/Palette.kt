package com.example.chatsouris.render

/** Palette unique du jeu (16 entrées avec la transparence). Couleurs ARGB. */
object Palette {
    const val TRANSPARENT = '.'

    val colors: Map<Char, Int> = mapOf(
        'k' to 0xFF1B1B2F.toInt(), // contour, quasi noir
        'w' to 0xFFFFFFFF.toInt(),
        'o' to 0xFFF29E38.toInt(), // orange du chat
        'O' to 0xFFC4701A.toInt(),
        'p' to 0xFFF4A3B8.toInt(), // rose
        'g' to 0xFFB0B0B8.toInt(), // gris de la souris
        'G' to 0xFF70707C.toInt(),
        'b' to 0xFF8B5A2B.toInt(), // bois / terre
        'B' to 0xFF5C3A1A.toInt(),
        'e' to 0xFF5DBB4A.toInt(), // herbe
        'E' to 0xFF2E7D32.toInt(),
        'r' to 0xFFE03C31.toInt(), // lave
        'y' to 0xFFFFD84A.toInt(),
        's' to 0xFF8FD3F4.toInt(), // ciel
    )

    const val SKY: Int = 0xFF8FD3F4.toInt()
    const val PIT: Int = 0xFF1B1B2F.toInt()
}
