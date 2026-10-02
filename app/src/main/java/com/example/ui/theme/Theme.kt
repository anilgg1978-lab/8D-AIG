package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PrecisionIndustrialColorScheme = lightColorScheme(
    primary = QResolveColors.Primary,
    onPrimary = QResolveColors.OnPrimary,
    primaryContainer = QResolveColors.PrimaryContainer,
    onPrimaryContainer = QResolveColors.OnPrimaryContainer,
    inversePrimary = QResolveColors.InversePrimary,
    secondary = QResolveColors.Secondary,
    onSecondary = QResolveColors.OnSecondary,
    secondaryContainer = QResolveColors.SecondaryContainer,
    onSecondaryContainer = QResolveColors.OnSecondaryContainer,
    tertiary = QResolveColors.Tertiary,
    onTertiary = QResolveColors.OnTertiary,
    tertiaryContainer = QResolveColors.TertiaryContainer,
    onTertiaryContainer = QResolveColors.OnTertiaryContainer,
    background = QResolveColors.Surface,
    onBackground = QResolveColors.OnSurface,
    surface = QResolveColors.Surface,
    onSurface = QResolveColors.OnSurface,
    surfaceVariant = QResolveColors.SurfaceContainerHighest,
    onSurfaceVariant = QResolveColors.OnSurfaceVariant,
    surfaceTint = QResolveColors.SurfaceTint,
    inverseSurface = QResolveColors.InverseSurface,
    inverseOnSurface = QResolveColors.InverseOnSurface,
    error = QResolveColors.Error,
    onError = QResolveColors.OnError,
    errorContainer = QResolveColors.ErrorContainer,
    onErrorContainer = QResolveColors.OnErrorContainer,
    outline = QResolveColors.Outline,
    outlineVariant = QResolveColors.OutlineVariant,
    surfaceContainerLowest = QResolveColors.SurfaceContainerLowest,
    surfaceContainerLow = QResolveColors.SurfaceContainerLow,
    surfaceContainer = QResolveColors.SurfaceContainer,
    surfaceContainerHigh = QResolveColors.SurfaceContainerHigh,
    surfaceContainerHighest = QResolveColors.SurfaceContainerHighest
)

@Composable
fun QResolveTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PrecisionIndustrialColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    QResolveTheme(content = content)
}
