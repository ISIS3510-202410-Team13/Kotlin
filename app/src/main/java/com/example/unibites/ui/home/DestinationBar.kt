package com.example.unibites.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides.Companion.Horizontal
import androidx.compose.foundation.layout.WindowInsetsSides.Companion.Top
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unibites.R
import com.example.unibites.ui.components.UniBitesDivider
import com.example.unibites.ui.theme.AlphaNearOpaque
import com.example.unibites.ui.theme.UniBitesTheme

@Composable
fun LocationBar(modifier: Modifier = Modifier) {
    TopAppBar(
        backgroundColor = UniBitesTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
        contentColor = UniBitesTheme.colors.textSecondary,
        contentPadding = WindowInsets.systemBars.only(Horizontal + Top).asPaddingValues(),
        elevation = 0.dp,
        modifier = modifier
    ) {
        Text(
            text = "Universidad de los Andes",
            style = MaterialTheme.typography.subtitle1,
            color = UniBitesTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(
            onClick = { /* todo */ },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                tint = UniBitesTheme.colors.brand,
                contentDescription = stringResource(R.string.label_select_location)
            )
        }
    }
    UniBitesDivider()
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun PreviewLocationBar() {
    UniBitesTheme {
        LocationBar()
    }
}
