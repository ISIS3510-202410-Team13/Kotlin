package com.example.unibites.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.unibites.ui.theme.UniBitesTheme

/**
 * An alternative to [androidx.compose.material.Snackbar] utilizing
 * [com.example.unibites.ui.theme.UniBitesColors]
 */
@Composable
fun UniBitesSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    actionOnNewLine: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color = UniBitesTheme.colors.uiBackground,
    contentColor: Color = UniBitesTheme.colors.textSecondary,
    actionColor: Color = UniBitesTheme.colors.brand,
    elevation: Dp = 6.dp
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        actionColor = actionColor,
        elevation = elevation
    )
}
