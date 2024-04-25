package com.example.unibites.signup.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.unibites.R
import com.example.unibites.signup.repositorio.SignUpViewModel
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesSurface
import com.example.unibites.ui.home.search.NoResults
import com.example.unibites.ui.theme.UniBitesTheme
import kotlinx.coroutines.launch

@Composable
fun SignUpDetail(
    navBackStackEntry: NavBackStackEntry,
    viewModel: SignUpViewModel,
    onNavigateHome: (NavBackStackEntry) -> Unit,
    onNavigateToSignIn: (NavBackStackEntry) -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val buttonEnabled by remember {
        derivedStateOf {
            email.isNotEmpty() && password.isNotEmpty() && !emailError && !passwordError
        }
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.uiState.registered) {
        if(viewModel.uiState.registered){
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
                    label = { Text(stringResource(R.string.emai)) },
                    isError = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    supportingText = {
                        if (emailError) {
                            Text("Por favor ingrese un correo electrónico válido")
                        }
                    },
                    placeholder = {
                        Text(
                            text = ("Ingrese un correo electrónico"),
                            style = TextStyle(
                                color = UniBitesTheme.colors.textHelp,
                            )
                        )
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
                        passwordError = it.length < 6 || it.length > 20
                    },
                    label = { Text(stringResource(R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordError,
                    supportingText = {
                        if (passwordError) {
                            Text("La contraseña debe tener al menos 6 caracteres y menos de 20 caraceteres")
                        }
                    },
                    placeholder = {
                        Text(
                            text = ("Ingrese una contraseña"),
                            style = TextStyle(
                                color = UniBitesTheme.colors.textHelp,
                            )
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UniBitesTheme.colors.textPrimary,
                        unfocusedBorderColor = UniBitesTheme.colors.textPrimary,
                        focusedLabelColor = UniBitesTheme.colors.textPrimary,
                        cursorColor = UniBitesTheme.colors.textSecondary,
                    ),
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text("Ya tienes una cuenta? Inicia Sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onNavigateToSignIn(navBackStackEntry) } // Add click modifier
                )
                Spacer(modifier = Modifier.height(16.dp))
                Body(
                    onClick = {
                        if (buttonEnabled) {
                            viewModel.signUp(
                                email,
                                password,
                                onSuccessRegister = {
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Registro satisfactorio",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onNavigateHome(navBackStackEntry)

                                    }
                                },
                                onErrorSignup = { errorMessage : String ->
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Error: $errorMessage",
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
    Text(
            text = "Registrate",
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
            enabled = enabled && !loading
    ) {
        if (loading) {
            CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    color = UniBitesTheme.colors.iconPrimary
            )
        } else {
            Text("Enviar registro")
        }
    }
}

@Composable
fun Title() {
    Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.bodyLarge
    )
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"
    return email.matches(emailPattern.toRegex())
}