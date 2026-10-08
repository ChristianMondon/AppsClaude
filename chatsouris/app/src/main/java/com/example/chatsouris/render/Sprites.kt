package com.example.chatsouris.render

/** Sprites en pixel art : un caractère = un pixel (couleurs dans [Palette]). Généré puis retouchable à la main. */
class Sprite(vararg val rows: String) {
    val width: Int = rows.first().length
    val height: Int = rows.size
}

object Sprites {
    val catRunA = Sprite(
        "................",
        ".........kk.kk..",
        "..k.....kopkpok.",
        ".kOk....koooook.",
        "kOk.....koooook.",
        "kOkkkkkkkoowkok.",
        ".kOoooooooooopk.",
        "..koOoOoOoooook.",
        "..koooooooooook.",
        "..kooooooookkk..",
        "..kooooooook....",
        "...kOOkkkOOk....",
        "...kOOk.kOOk....",
        "...kOOk.kOOk....",
        "....kk...kk.....",
        "................",
    )

    val catRunB = Sprite(
        "................",
        ".........kk.kk..",
        "..k.....kopkpok.",
        ".kOk....koooook.",
        "kOk.....koooook.",
        "kOkkkkkkkoowkok.",
        ".kOoooooooooopk.",
        "..koOoOoOoooook.",
        "..koooooooooook.",
        "..kooooooookkk..",
        "..kooooooook....",
        "..kOOkkOOkOOk...",
        "..kOOkkOOkOOk...",
        "..kOOk.kkkOOk...",
        "...kk.....kk....",
        "................",
    )

    val catJump = Sprite(
        "................",
        ".........kk.kk..",
        "..k.....kopkpok.",
        ".kOk....koooook.",
        "kOk.....koooook.",
        "kOkkkkkkkoowkok.",
        ".kOoooooooooopk.",
        "..koOoOoOoooook.",
        "..koooooooooook.",
        "..kooooooookkk..",
        "..kooooooookk...",
        ".kOOkOOkkOOOOk..",
        ".kOOkkk..kkOOk..",
        "..kk.......kk...",
        "................",
        "................",
    )

    val catStumble = Sprite(
        "................",
        ".........kk.kk..",
        "..k.....kopkpok.",
        ".kOk....koooook.",
        "kOk.....koooook.",
        "kOkkkkkkkookkok.",
        ".kOoooooooooopk.",
        "..koOoOoOoooopk.",
        "..koooooooooook.",
        "..kooooooookkk..",
        "..kooooooook....",
        "..kOOkkkkOOk....",
        "..kOOk..kOOk....",
        "..kOOk..kOOk....",
        "..kOOk..kOOk....",
        "...kk....kk.....",
    )

    val mouseRunA = Sprite(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "...........kk...",
        "..........kppk..",
        "....kkkkkkggggk.",
        "...kggggggggkgk.",
        "k..kggGgGggggpk.",
        "pkkpggggggggggk.",
        "kppkggggggggkk..",
        ".kk.kGGkkkGGk...",
        "....kGGk.kGGk...",
        ".....kk...kk....",
    )

    val mouseRunB = Sprite(
        "................",
        "................",
        "................",
        "................",
        "................",
        "................",
        "...........kk...",
        "..........kppk..",
        "....kkkkkkggggk.",
        "...kggggggggkgk.",
        "k..kggGgGggggpk.",
        "pkkpggggggggggk.",
        "kppkggggggggkk..",
        ".kkkGGkkGGkGGk..",
        "....kk.kGGkkk...",
        "........kk......",
    )

    val barrier = Sprite(
        "...kk......kk...",
        "..kBBk....kBBk..",
        "..kBBkkkkkkBBk..",
        "..kbbbbbbbbbbk..",
        "..kbbbbbbbbbbk..",
        "..kbbbbbbbbbbk..",
        "..kBBkkkkkkBBk..",
        "..kBBk....kBBk..",
        "..kBBkkkkkkBBk..",
        "..kbbbbbbbbbbk..",
        "..kbbbbbbbbbbk..",
        "..kbbbbbbbbbbk..",
        "..kBBkkkkkkBBk..",
        "..kBBk....kBBk..",
        "..kBBk....kBBk..",
        "...kk......kk...",
    )

    val lavaA = Sprite(
        "yrryrryr",
        "rrrrrrrr",
        "rryrrrrr",
        "rrrrrrrr",
    )

    val lavaB = Sprite(
        "rryrryrr",
        "rrrrrrrr",
        "rrrrryrr",
        "rrrrrrrr",
    )

    val ground = Sprite(
        "eeeeeeeeeeeeeeee",
        "eeeeeeeeeeeeeeee",
        "eeeeeeeeeeeeeeee",
        "EEEEEEEEEEEEEEEE",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbBbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbBbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbBbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbBbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbBbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbBbbbbb",
        "bbbbbbbbbbbbbbbb",
        "bbbbbbbbbbbbbbbb",
    )

    val cloud = Sprite(
        "........................",
        "........................",
        "......wwwwwww...........",
        "......wwwwwwwwwwww......",
        "......wwwwwwwwwwww......",
        "...wwwwwwwwwwwwwwwwww...",
        "...wwwwwwwwwwwwwwwwww...",
        "...wwwwwwwwwwwwwwwwww...",
        "...wwwwwwwwwwwwwwwwww...",
        "........................",
    )

    val hill = Sprite(
        "................................................................",
        "................................................................",
        "................................................................",
        "................................................................",
        "................................e...............................",
        "...........................eeeeeEeeeee..........................",
        ".........................eeEEEEEEEEEEEee........................",
        ".......................eeEEEEEEEEEEEEEEEee......................",
        "......................eEEEEEEEEEEEEEEEEEEEe.....................",
        "....................eeEEEEEEEEEEEEEEEEEEEEEee...................",
        "...................eEEEEEEEEEEEEEEEEEEEEEEEEEe..................",
        "..................eEEEEEEEEEEEEEEEEEEEEEEEEEEEe.................",
        "................eeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee...............",
        "...............eEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe..............",
        "..............eEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe.............",
        ".............eEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe............",
        "...........eeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee..........",
        "..........eEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe.........",
        "........eeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee.......",
        "......eeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee.....",
        "eeeeeeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEeeeee",
        "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE",
        "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE",
        "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE",
    )

    val all: List<Sprite> = listOf(catRunA, catRunB, catJump, catStumble, mouseRunA, mouseRunB, barrier, lavaA, lavaB, ground, cloud, hill)
}
