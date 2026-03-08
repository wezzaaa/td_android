package fr.isen.sahar.thegreatestcocktailapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentGold,
    secondary = AccentRose,
    tertiary = AccentPurple,
    background = BackgroundBottom,
    surface = BackgroundTop,
    onPrimary = BackgroundBottom,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AccentGold,
    secondary = AccentRose,
    tertiary = AccentPurple,
    background = BackgroundBottom,
    surface = BackgroundTop,
    onPrimary = BackgroundBottom,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun TheGreatestCocktailAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}