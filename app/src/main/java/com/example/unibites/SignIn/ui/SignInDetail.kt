package com.example.unibites.SignIn.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.unibites.R
import com.example.unibites.SignIn.repository.SignInViewModel
import com.example.unibites.ui.theme.UniBitesTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.unibites.signup.ui.isValidEmail
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesSurface
import kotlinx.coroutines.launch


@Composable
fun SignInDetail(
        navBackStackEntry: NavBackStackEntry,
        viewModel: SignInViewModel,
        onNavigateHome: (NavBackStackEntry) -> Unit,
        onNavigateToSignUp: (NavBackStackEntry) -> Unit, // Add this parameter
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    val buttonEnabled by remember {
        derivedStateOf {
            email.isNotEmpty() && password.isNotEmpty() && !emailError
        }
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.uiState.loggedIn) {
        if(viewModel.uiState.loggedIn){
            onNavigateHome(navBackStackEntry)
        }
    }
    UniBitesTheme {
        UniBitesSurface(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header()
                Spacer(modifier = Modifier.height(16.dp))
                Title()
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !isValidEmail(it)
                    },
                    label = { Text("Correo Electrónico") },
                    isError = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    supportingText = {
                        if (emailError) {
                            Text("Por favor ingrese un correo electrónico válido")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UniBitesTheme.colors.textPrimary,
                        unfocusedBorderColor = UniBitesTheme.colors.textPrimary,
                        focusedLabelColor = UniBitesTheme.colors.textPrimary,
                        cursorColor = UniBitesTheme.colors.textSecondary,
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UniBitesTheme.colors.textPrimary,
                        unfocusedBorderColor = UniBitesTheme.colors.textPrimary,
                        focusedLabelColor = UniBitesTheme.colors.textPrimary,
                        cursorColor = UniBitesTheme.colors.textSecondary,
                    ),
                    )
                Spacer(modifier = Modifier.height(32.dp))
                Text("No tienes una cuenta? Regístrate",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onNavigateToSignUp(navBackStackEntry) } // Add click modifier
                )
                Spacer(modifier = Modifier.height(16.dp))
                Body(
                    onClick = {
                        if (buttonEnabled) {
                            viewModel.signIn(
                                email,
                                password,
                                onSuccessSignIn = {
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Inicio de sesión satisfactorio",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onNavigateHome(navBackStackEntry)

                                    }
                                },
                                onErrorSignIn = {
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Contraseña o correo incorrecto, intenta de nuevo.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            )
                        }
                    },
                    loading = viewModel.uiState.loading,
                    enabled = buttonEnabled
                )
            }
        }
    }

}

@Composable
fun Header() {
    androidx.compose.material3.Text(
        text = "Inicia Sesión",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Body(
    onClick: () -> Unit,
    loading: Boolean,
    enabled: Boolean,
) {
    UniBitesButton(
        onClick = onClick,
        enabled = !loading && enabled,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(26.dp),
                color = UniBitesTheme.colors.iconPrimary
            )
        } else {
            Text("Inicia Sesión")
        }
    }
}

@Composable
fun Title() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Conoce los mejores restaurantes cerca de tu universidad",
            style = MaterialTheme.typography.titleSmall
        )
    }
}