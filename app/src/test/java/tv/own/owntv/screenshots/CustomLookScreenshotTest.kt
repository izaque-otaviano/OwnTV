package tv.own.owntv.screenshots

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.em
import androidx.compose.runtime.LaunchedEffect
import androidx.tv.material3.Text
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import tv.own.owntv.core.nav.MainSection
import tv.own.owntv.core.settings.SettingsRepository.NavLength
import tv.own.owntv.core.settings.SettingsRepository.NavSize
import tv.own.owntv.core.theme.AccentColor
import tv.own.owntv.features.shell.components.RailState
import tv.own.owntv.features.shell.components.StageRail
import tv.own.owntv.ui.components.OwnTVIcon
import tv.own.owntv.ui.stage.StageButton
import tv.own.owntv.ui.stage.StagePoster
import tv.own.owntv.ui.stage.StageStill
import tv.own.owntv.ui.stage.stageBackground
import tv.own.owntv.ui.theme.OwnTVTheme
import tv.own.owntv.ui.theme.StageColors
import tv.own.owntv.ui.theme.ThemeMode
import tv.own.owntv.ui.theme.mpx
import tv.own.owntv.ui.theme.stageText
import java.io.File

/**
 * Personalização: renders the custom look's real components (rail, buttons, cards, posters) with
 * sample data and writes PNGs to app/build/screenshots, so the look can be reviewed without a TV.
 * Pictures are coloured placeholders — nothing is loaded from the network. Only runs with
 * -Powntv.screenshots=true (see app/build.gradle.kts).
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], application = android.app.Application::class, qualifiers = "pt-rBR-w1067dp-h600dp-land-320dpi")
class CustomLookScreenshotTest {

    @get:Rule
    val rule = createComposeRule()

    private val out = File("build/screenshots").apply { mkdirs() }

    private fun save(name: String) {
        rule.waitForIdle()
        val bitmap = rule.onRoot().captureToImage().asAndroidBitmap()
        File(out, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test
    fun home_menuFechado() {
        rule.setContent { Screen { HomeMock(railState = RailState.CAPSULE, focusPlay = true) } }
        rule.onNodeWithTag(PLAY).requestFocus()
        save("01_home_menu_fechado")
    }

    @Test
    fun home_menuAberto() {
        rule.setContent { Screen { HomeMock(railState = RailState.OPEN, focusPlay = false) } }
        save("02_home_menu_aberto")
    }

    @Test
    fun fileiras() {
        rule.setContent { Screen { RowsMock() } }
        rule.onNodeWithTag(FIRST_STILL).requestFocus()
        save("03_fileiras_foco_continuar")
    }

    @Test
    fun fileiras_focoNoRanking() {
        rule.setContent { Screen { RowsMock() } }
        rule.onNodeWithTag(FIRST_POSTER).requestFocus()
        save("04_fileiras_foco_ranking")
    }
}

private const val PLAY = "play"
private const val FIRST_STILL = "still0"
private const val FIRST_POSTER = "poster0"

@Composable
private fun Screen(content: @Composable () -> Unit) {
    OwnTVTheme(themeMode = ThemeMode.DARK, accent = AccentColor.TEAL, systemInDarkTheme = true) {
        Box(Modifier.fillMaxSize().stageBackground(Color.White)) { content() }
    }
}

/** A stand-in for a backdrop or poster: a soft two-colour gradient. */
private fun art(a: Long, b: Long) = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(a), Color(b))))

private val stills = listOf(
    Triple("Falando a Real", "T2, E3 · 3 min", 0.2f) to (0xFF7FB3D5 to 0xFF2E86C1),
    Triple("Olá, Amanhã!", "T1, E5 · 32 min", 0.5f) to (0xFF5B2C6F to 0xFF2874A6),
    Triple("Acima de Qualquer Suspeita", "T1, E1 · 15 min", 0.3f) to (0xFF212F3D to 0xFF5D6D7E),
    Triple("F1: O Filme", "2 h e 4 min", 0.1f) to (0xFFAAB7B8 to 0xFF566573),
    Triple("Silo", "T1, E1 · 44 min", 0.6f) to (0xFF196F3D to 0xFF52BE80),
)

private val posters = listOf(
    "Ted Lasso" to (0xFFB03A2E to 0xFF1F3A93),
    "Matéria Escura" to (0xFF1B4F72 to 0xFF85C1E9),
    "Slow Horses" to (0xFF154360 to 0xFF5DADE2),
    "Silo" to (0xFF7E5109 to 0xFF117A65),
    "Widow's Bay" to (0xFF0B5345 to 0xFF2E86C1),
    "Desaparecida" to (0xFF616A6B to 0xFFD5DBDB),
    "Severance" to (0xFF1C2833 to 0xFF85929E),
)

@Composable
private fun HomeMock(railState: RailState, focusPlay: Boolean) {
    val selected = remember { FocusRequester() }
    val content = remember { FocusRequester() }
    // The open rail as the shell shows it: focus on the selected section.
    if (!focusPlay) LaunchedEffect(Unit) { selected.requestFocus() }
    Box(Modifier.fillMaxSize()) {
        // The hero's backdrop: a cold blue picture fading into the page at the left and bottom.
        Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF9CC3E6), Color(0xFF2B5C8A), Color(0xFF14263A)))))
        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(0f to Color.Black.copy(alpha = 0.55f), 0.55f to Color.Transparent)))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(0.55f to Color.Transparent, 1f to Color(0xFF1A1B1E))))

        Column(Modifier.padding(start = 110.mpx, top = 430.mpx)) {
            Text("Novo", style = stageText(17, 700), color = StageColors.Text,
                modifier = Modifier.background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(8.mpx)).padding(horizontal = 12.mpx, vertical = 4.mpx))
            Spacer(Modifier.height(14.mpx))
            Text("TENZING", style = stageText(96, 300, 0.2.em), color = Color.White)
            Spacer(Modifier.height(10.mpx))
            Text("2025 · Drama, Aventura · 2 h 4 min", style = stageText(21, 600), color = StageColors.Body)
            Spacer(Modifier.height(16.mpx))
            Text(
                "Junte-se à arriscada jornada para ser o primeiro a conquistar o Monte Everest. Baseado em uma história real extraordinária.",
                style = stageText(22, 400), color = StageColors.Body, modifier = Modifier.width(760.mpx),
            )
            Spacer(Modifier.height(30.mpx))
            Row(horizontalArrangement = Arrangement.spacedBy(14.mpx), verticalAlignment = Alignment.CenterVertically) {
                StageButton("Reproduzir", {}, icon = OwnTVIcon.PLAY, iconFilled = true, modifier = Modifier.testTag(PLAY))
                StageButton(null, {}, icon = OwnTVIcon.PLAY_CIRCLE, round = true)
                StageButton(null, {}, icon = OwnTVIcon.LAYERS, round = true)
                StageButton(null, {}, icon = OwnTVIcon.INFO, round = true)
                StageButton(null, {}, icon = OwnTVIcon.FAVORITE, round = true)
            }
        }
        // The page dots, centred at the hero's foot.
        Row(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 250.mpx)
                .background(Color.Black.copy(alpha = 0.32f), RoundedCornerShape(14.mpx)).padding(horizontal = 12.mpx, vertical = 9.mpx),
            horizontalArrangement = Arrangement.spacedBy(8.mpx), verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(30.mpx, 10.mpx).background(Color.White, RoundedCornerShape(5.mpx)))
            repeat(7) { Box(Modifier.size(8.mpx).background(Color.White.copy(alpha = 0.45f), CircleShape)) }
        }
        Column(Modifier.align(Alignment.BottomStart).padding(start = 110.mpx, bottom = 0.mpx)) {
            Text("Continue Assistindo", style = stageText(26, 700), color = StageColors.Text)
            Spacer(Modifier.height(14.mpx))
            Row(horizontalArrangement = Arrangement.spacedBy(24.mpx)) {
                stills.forEach { (s, c) ->
                    StageStill(s.first, s.second, {}, 336.mpx, 189.mpx, 19, 15, progress = s.third) { Box(art(c.first, c.second)) }
                }
            }
        }

        StageRail(
            state = railState,
            size = NavSize.NORMAL,
            length = NavLength.FIT,
            selected = MainSection.HOME,
            visibleSections = MainSection.allBrowse,
            onSelect = {},
            count = { null },
            detail = { null },
            profileName = "Izaque",
            profileLine = "",
            onSwitchProfile = {},
            onPickAvatar = {},
            selectedItemFocusRequester = selected,
            contentFocusRequester = content,
            onFocused = {},
            nowPlaying = null,
            onNowPlaying = {},
        )
    }
}

@Composable
private fun RowsMock() {
    Column(Modifier.fillMaxSize().padding(start = 110.mpx, top = 60.mpx)) {
        Text("Continue Assistindo", style = stageText(26, 700), color = StageColors.Text)
        Spacer(Modifier.height(16.mpx))
        Row(horizontalArrangement = Arrangement.spacedBy(24.mpx)) {
            stills.forEachIndexed { i, (s, c) ->
                StageStill(
                    s.first, s.second, {}, 384.mpx, 216.mpx, 20, 16, progress = s.third,
                    modifier = if (i == 0) Modifier.testTag(FIRST_STILL) else Modifier,
                ) { Box(art(c.first, c.second)) }
            }
        }
        Spacer(Modifier.height(50.mpx))
        Text("Em alta", style = stageText(26, 700), color = StageColors.Text)
        Spacer(Modifier.height(16.mpx))
        Row(horizontalArrangement = Arrangement.spacedBy(20.mpx)) {
            posters.forEachIndexed { i, (title, c) ->
                StagePoster(
                    title, {}, 196.mpx, 294.mpx, rank = i + 1, titleOnArt = true,
                    modifier = if (i == 0) Modifier.testTag(FIRST_POSTER) else Modifier,
                ) { Box(art(c.first, c.second)) }
            }
        }
        Spacer(Modifier.height(50.mpx))
        Row(horizontalArrangement = Arrangement.spacedBy(14.mpx)) {
            StageButton("Reproduzir", {}, icon = OwnTVIcon.PLAY, iconFilled = true)
            StageButton(null, {}, icon = OwnTVIcon.INFO, round = true)
            Box(Modifier.fillMaxWidth())
        }
    }
}
