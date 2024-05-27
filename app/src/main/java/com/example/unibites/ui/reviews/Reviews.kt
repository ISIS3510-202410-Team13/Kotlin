package com.example.unibites.ui.reviews

import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.unibites.R
import com.example.unibites.ui.components.UniBitesScaffold
import com.example.unibites.ui.components.UniBitesSurface
import com.example.unibites.ui.home.HomeSections
import com.example.unibites.ui.home.UniBitesBottomBar

import com.example.unibites.ui.snackdetail.getSnackFromId
import com.example.unibites.ui.theme.Neutral8
import com.example.unibites.ui.theme.UniBitesTheme
import com.example.unibites.ui.utils.mirroringBackIcon
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

@Composable
fun Reviews(restaurantName: String, onNavigateToRoute: (String) -> Unit, upPress: () -> Unit) {
    val reviews = remember { mutableStateListOf<Review>() }
    val db = FirebaseFirestore.getInstance()


    LaunchedEffect(restaurantName) {
        withContext(Dispatchers.Default){
            db.collection("reviews")
                .whereEqualTo("restaurant", restaurantName)
                .get()
                .addOnSuccessListener { result ->
                    reviews.clear()
                    reviews.addAll(result.documents.mapNotNull { it.toObject(Review::class.java) })
                }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .background(Brush.horizontalGradient(UniBitesTheme.colors.tornado1))
        )
        UniBitesSurface(Modifier.fillMaxWidth()) {
            Column {
                Spacer(Modifier.height(16.dp))

                Up {
                    upPress()
                }

                if (reviews.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No hay reseñas", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    Spacer(Modifier.height(20.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Reseñas de $restaurantName",
                            textAlign = TextAlign.Left,
                            color = UniBitesTheme.colors.textPrimary,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Calificación promedio: 4.5",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        LazyColumn {
                            items(reviews) { review ->
                                ReviewItem(review)
                            }
                        }
                    }
                }
            }
        }
    }
}


fun getAvatarIndex(username: String, max: Int): Int {
    return username.hashCode().absoluteValue % max
}


@Composable
fun ReviewItem(review: Review) {
    val avatarImages = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.avatar4
    )
    val avatarIndex = getAvatarIndex(review.user, avatarImages.size)
    val avatarResource = avatarImages[avatarIndex]
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = UniBitesTheme.colors.sea300,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = avatarResource),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(review.user, fontWeight = FontWeight.Bold)
                Text(review.text, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = UniBitesTheme.colors.iconInteractive,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

data class Review(
    val restaurant: String = "",
    val text: String = "",
    val user: String = ""
)
