package com.example.unibites.SignIn.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.example.unibites.R
import com.example.unibites.SignIn.repository.SignInViewModel


@Composable
fun SignInDetail(
        navBackStackEntry: NavBackStackEntry,
        viewModel: SignInViewModel,
        onNavigateHome: (NavBackStackEntry) -> Unit,
        onClick: () -> Unit
){
    LaunchedEffect(key1 = viewModel.uiState.loggedIn) {
        if(viewModel.uiState.loggedIn){
            onNavigateHome(navBackStackEntry)
        }
    }
    body(onClick)
}

@Composable
fun header() {
    // TODO()
}

@Composable
fun body(
    onClick: () -> Unit
) {
    Button(onClick = { onClick() }) {
        Text(text = stringResource(R.string.signin))
    }
}

@Composable
fun title() {
    // TODO()
}