package com.example.unibites.preferences.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.unibites.R
import com.example.unibites.preferences.repository.PreferencesViewModel
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesSurface
import com.example.unibites.ui.home.FilterTitle
import com.example.unibites.ui.theme.UniBitesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PreferencesDetail(
    navBackStackEntry: NavBackStackEntry,
    viewModel: PreferencesViewModel,
    onNavigateToHome: (NavBackStackEntry) -> Unit
) {
    var priceRange by remember { mutableStateOf(0) }
    var distanceLimit by remember { mutableStateOf(1) }
    var isVegan by remember { mutableStateOf(false) }
    var allergies by remember { mutableStateOf(emptyList<String>()) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    val rangeText = when (distanceLimit) {
        1 -> "1 KM"
        2 -> "3 KM"
        3 -> "5+ KM"
        else -> "1 KM"
    }

    val priceRangeText = when (priceRange) {
        1 -> "$0 - $10,000"
        2 -> "$10,000 - $20,000"
        3 -> "$20,000 - $30,000"
        4 -> "$30,000 - $40,000"
        5 -> "$40,000+"
        else -> "$0 - $10,000"
    }

    LaunchedEffect(key1 = viewModel.uiState.preferencesSaved) {
        if (viewModel.uiState.preferencesSaved) {
            onNavigateToHome(navBackStackEntry)
        }
    }

    UniBitesTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Configura tus preferencias",
                            style = MaterialTheme.typography.headlineSmall,
                            color = UniBitesTheme.colors.textSecondary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = UniBitesTheme.colors.brandSecondary
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Is vegan checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isVegan,
                            onCheckedChange = { isVegan = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = UniBitesTheme.colors.textPrimary
                            )
                        )
                        Text(
                            text = "¿Eres vegano?",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Distance limit selection
                    FlowRow {
                        FilterTitle(text = stringResource(id = R.string.distance))
                    }
                    Slider(
                        value = distanceLimit.toFloat(),
                        onValueChange = { distanceLimit = it.toInt() },
                        valueRange = 1f..3f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = UniBitesTheme.colors.brand,
                            activeTrackColor = UniBitesTheme.colors.brand
                        )
                    )
                    Text(
                        text = rangeText,
                        color = UniBitesTheme.colors.textPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    FlowRow {
                        FilterTitle(text = stringResource(id = R.string.price_range))
                    }

                    Slider(
                        value = priceRange.toFloat(),
                        onValueChange = { priceRange = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = UniBitesTheme.colors.brand,
                            activeTrackColor = UniBitesTheme.colors.brand
                        )
                    )
                    Text(
                        text = priceRangeText,
                        color = UniBitesTheme.colors.textPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            allergies = allergies + ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UniBitesTheme.colors.brandSecondary
                        )
                    ) {
                        Text("Presiona este botón para agregar tus alergias si tienes")
                    }
                    allergies.forEachIndexed { index, allergy ->
                        OutlinedTextField(
                            value = allergy,
                            onValueChange = { newAllergy ->
                                allergies = allergies.toMutableList().apply {
                                    this[index] = newAllergy
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Alergia ${index + 1}") },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        allergies = allergies.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    },
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remueve la alergia"
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            viewModel.savePreferences(
                                priceRange,
                                distanceLimit,
                                isVegan,
                                allergies,
                                onSuccessSave = {
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Preferences saved successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        savePreferencesToLocalStorage(
                                            context,
                                            hashMapOf(
                                                "priceRange" to priceRange,
                                                "distanceLimit" to distanceLimit,
                                                "isVegan" to isVegan,
                                                "allergies" to allergies.toSet()
                                            )
                                        )
                                    }
                                },
                                onErrorSave = { errorMessage ->
                                    coroutineScope.launch {
                                        Toast.makeText(
                                            context,
                                            "Error: $errorMessage",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UniBitesTheme.colors.brand
                        ),
                        enabled = !viewModel.uiState.loading
                    ) {
                        if (viewModel.uiState.loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = UniBitesTheme.colors.iconPrimary
                            )
                        } else {
                            Text("Guarda tus preferencias")
                        }
                    }
                }
            }
        )
    }
}

private fun savePreferencesToLocalStorage(context: Context, preferences: HashMap<String, Any>) {
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    sharedPreferences.edit()
        .putInt("priceRange", preferences["priceRange"] as? Int ?: 0)
        .putInt("distanceLimit", preferences["distanceLimit"] as? Int ?: 0)
        .putBoolean("isVegan", preferences["isVegan"] as? Boolean ?: false)
        .putStringSet("allergies", preferences["allergies"] as? Set<String> ?: emptySet())
        .apply()
}


