package com.example.unibites.ui.home

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unibites.R
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesScaffold
import com.example.unibites.ui.theme.UniBitesTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Profile(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClickSignOut: () -> Unit
) {
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val userEmail = firebaseUser?.email ?: ""
    val context = LocalContext.current


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
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            // Display user email
            Text(
                "¡Hola! $userEmail",
                textAlign = TextAlign.Center,
                color = UniBitesTheme.colors.textPrimary,
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Preferences:",
                color = UniBitesTheme.colors.textPrimary
            )
            PreferencesSummary(context)

            Spacer(Modifier.height(24.dp))


            UniBitesButton(onClick = onClickSignOut) {
                Text(text = stringResource(R.string.sign_out))
            }
        }
    }
}

@Composable
fun PreferencesSummary(context: Context) {
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    val priceRange = sharedPreferences.getInt("priceRange", 0)
    val distanceLimit = sharedPreferences.getInt("distanceLimit", 0)
    val isVegan = sharedPreferences.getBoolean("isVegan", false)
    val allergies = sharedPreferences.getStringSet("allergies", emptySet()) ?: emptySet()

    Column {
        Text("Price Range: $priceRange")
        Text("Distance Limit: $distanceLimit")
        Text("Is Vegan: $isVegan")
        Text("Allergies: ${allergies.joinToString(", ")}")
    }
}