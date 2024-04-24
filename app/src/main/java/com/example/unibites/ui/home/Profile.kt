package com.example.unibites.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unibites.R
import com.example.unibites.ui.components.UniBitesScaffold
import com.example.unibites.ui.theme.UniBitesTheme

@Composable
fun Profile(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClickSignOut: () -> Unit
) {
    UniBitesScaffold(
        bottomBar = {
            UniBitesBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.PROFILE.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(24.dp)
                .padding(paddingValues)
        ) {
            Button(onClick = onClickSignOut) {
                Text(text = stringResource(R.string.sign_out))
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun ProfilePreview() {
    UniBitesTheme {
        Profile(onNavigateToRoute = { }, onClickSignOut = { })
    }
}
