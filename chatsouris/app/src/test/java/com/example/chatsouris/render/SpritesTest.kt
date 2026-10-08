package com.example.chatsouris.render

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpritesTest {
    @Test fun everySpriteIsRectangular() {
        for (s in Sprites.all) {
            assertTrue(s.rows.all { it.length == s.width })
        }
    }

    @Test fun everyPixelIsInThePalette() {
        for (s in Sprites.all) {
            for (row in s.rows) for (c in row) {
                assertTrue("pixel '$c' hors palette", c == Palette.TRANSPARENT || c in Palette.colors)
            }
        }
    }

    @Test fun paletteHasAtMostSixteenEntriesWithTransparency() {
        assertTrue(Palette.colors.size + 1 <= 16)
    }

    @Test fun characterSpritesAreSixteenBySixteen() {
        for (s in listOf(Sprites.catRunA, Sprites.catRunB, Sprites.catJump, Sprites.catStumble,
            Sprites.mouseRunA, Sprites.mouseRunB, Sprites.barrier)) {
            assertEquals(16, s.width)
            assertEquals(16, s.height)
        }
    }
}
