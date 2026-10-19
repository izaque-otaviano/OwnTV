package tv.own.owntv.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Personalização visual (branch custom/apple-style).
 *
 * Every value the custom look changes lives here, so an upstream merge only touches the few lines
 * in the original files that point at this one. The look: neutral graphite neutrals instead of
 * Stage's green-tinted ones, and focus as a white capsule with dark text (the accent no longer
 * tints Stage chrome — the player HUD still follows the Accent setting).
 */

/** The custom neutrals, as copies of Stage's palettes so new upstream fields keep their defaults. */
object CustomPalettes {
    val Dark: StagePalette = StagePalette.Dark.copy(
        text = Color(0xFFF5F5F7),
        muted = Color(0xFFAEAEB2),
        dim = Color(0xFF8E8E93),
        itemText = Color(0xFFE5E5EA),
        menuItemText = Color(0xFFE5E5EA),
        tagText = Color(0xFFD1D1D6),
        page = Color(0xFF1C1C1E),
        glassTint = Color(0xFF2C2C2E),
        glassOff = Color(0xFF2C2C2E),
        body = Color(0xFFD1D1D6),
        navText = Color(0xFFE5E5EA),
        fxText = Color.White,
        fxLine = Color(0xFFE5E5EA),
        scrim = Color.Black.copy(alpha = 0.45f),
        wash = Color(28, 28, 30),
        segmentOn = Color.White.copy(alpha = 0.16f),
        knobOff = Color(0xFFD1D1D6),
    )
    val Light: StagePalette = StagePalette.Light.copy(
        text = Color(0xFF1D1D1F),
        muted = Color(0xFF6E6E73),
        dim = Color(0xFF8E8E93),
        itemText = Color(0xFF1D1D1F),
        menuItemText = Color(0xFF1D1D1F),
        tagText = Color(0xFF3A3A3C),
        ink = Color(0xFF1D1D1F),
        page = Color(0xFFF2F2F7),
        body = Color(0xFF3A3A3C),
        navText = Color(0xFF3A3A3C),
        fxText = Color(0xFF1D1D1F),
        fxLine = Color(0xFF3A3A3C),
        scrim = Color.Black.copy(alpha = 0.25f),
        wash = Color(242, 242, 247),
        shadowTint = Color.Black,
        shadowScale = 0.35f,
    )
}

/** Focus: a near-white capsule with dark text in dark; the reverse in light, where white would vanish. */
object CustomFocus {
    val DarkFill = Color(0xFFF5F5F7)
    val DarkOn = Color(0xFF1D1D1F)
    val LightFill = Color(0xFF1D1D1F)
    val LightOn = Color.White

    /** How much a focused element grows: buttons, posters and still cards. */
    const val ButtonLift = 1.06f
    const val PosterLift = 1.10f
}

/** The page behind every screen: a cool graphite fade, darker at the bottom. */
object CustomBackground {
    val DarkTop = Color(0xFF2A2B30)
    val DarkMid = Color(0xFF212226)
    val DarkBottom = Color(0xFF1A1B1E)
    val LightTop = Color(0xFFF5F5F7)
    val LightMid = Color(0xFFEFEFF4)
    val LightBottom = Color(0xFFE8E8ED)
}
