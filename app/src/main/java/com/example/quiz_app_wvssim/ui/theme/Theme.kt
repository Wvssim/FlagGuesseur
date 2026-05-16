package com.example.quiz_app_wvssim.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AtlasNocturne = darkColorScheme(
    primary          = Gold,
    onPrimary        = Abyss,
    primaryContainer = GoldDim,
    onPrimaryContainer = SnowWhite,
    secondary        = Emerald,
    onSecondary      = Abyss,
    secondaryContainer = EmeraldDim,
    onSecondaryContainer = SnowWhite,
    tertiary         = SkyBlue,
    onTertiary       = Abyss,
    error            = CoralRed,
    onError          = Abyss,
    errorContainer   = CoralDim,
    background       = Abyss,
    onBackground     = SnowWhite,
    surface          = NavySurface,
    onSurface        = SnowWhite,
    surfaceVariant   = GlassCard,
    onSurfaceVariant = CloudGray,
    outline          = MistGray,
    outlineVariant   = GlassCard,
    inverseSurface   = SnowWhite,
    inverseOnSurface = Abyss
)

@Composable
fun QuizAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AtlasNocturne,
        typography  = QuizTypography,
        content     = content
    )
}
