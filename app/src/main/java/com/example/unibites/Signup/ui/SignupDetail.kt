package com.example.unibites.Signup.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.example.unibites.R
import com.example.unibites.Signup.repository.SignUpViewModel


@Composable
fun SignupDetail(
    navBackStackEntry: NavBackStackEntry,
    viewModel: SignUpViewModel,
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
        Text(text = stringResource(R.string.Signup))
    }
}

@Composable
fun title() {
    // TODO()
}