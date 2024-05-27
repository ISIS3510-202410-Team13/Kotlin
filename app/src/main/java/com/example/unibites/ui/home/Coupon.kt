package com.example.unibites.ui.home

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.unibites.R
import com.example.unibites.coupon.CouponPreferences
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesScaffold
import com.example.unibites.ui.theme.UniBitesTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.LocalDate



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Coupon(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var coupons by remember { mutableStateOf<List<Coupon>>(emptyList()) }
    val couponPreferences = remember { CouponPreferences(context) }
    var couponCode by remember { mutableStateOf("") }
    var tabSelected by remember { mutableStateOf(0) }
    var showQRDialog by remember { mutableStateOf(false) }  // State for QR code dialog visibility
    var selectedCoupon by remember { mutableStateOf<Coupon?>(null) }
    val tabs = listOf("Disponibles", "Vencidos")

    fun validateCoupon(context: Context, couponCode: String, coupons: List<Coupon>, onResult: (Boolean) -> Unit) {
        val foundCoupon = coupons.firstOrNull { it.id == couponCode }
        if (foundCoupon != null && foundCoupon.isValid) {
            onResult(true)  // Set the result to true to show the QR dialog
            selectedCoupon = foundCoupon  // Set the selected coupon if valid

        } else {
            Toast.makeText(context, "No se encontró el cupón.", Toast.LENGTH_LONG).show()
            onResult(false)  // Set the result to false to not show the QR dialog
        }
    }

    LaunchedEffect(Unit) {
        val cachedCoupons = couponPreferences.getCoupons()
        if (cachedCoupons != null && !couponPreferences.shouldInvalidateCache()) {
            coupons = cachedCoupons
        } else {
            scope.launch {
                val db = FirebaseFirestore.getInstance()
                val result = db.collection("coupons").get().await()
                val fetchedCoupons = result.documents.mapNotNull { doc ->
                    Coupon(
                        description = doc.getString("description") ?: "",
                        endDate = doc.getString("end_date") ?: "",
                        id = doc.getString("id") ?: "",
                        isPublic = doc.getBoolean("is_public") ?: false,
                        isValid = doc.getBoolean("is_valid") ?: false,
                        restaurant = doc.getString("restaurant") ?: ""
                    )
                }
                coupons = fetchedCoupons
                couponPreferences.saveCoupons(fetchedCoupons)
            }
        }
    }

    UniBitesScaffold(
        bottomBar = {
            UniBitesBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.COUPON.route,
                navigateToRoute = onNavigateToRoute
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Spacer(Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(24.dp)
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(62.dp))

            Text(
                "Cupones",
                textAlign = TextAlign.Left,
                color = UniBitesTheme.colors.textPrimary,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
            Spacer(Modifier.height(16.dp))

            // Coupon input and validation
            OutlinedTextField(
                value = couponCode,
                onValueChange = { couponCode = it },
                label = { Text("Ingresa tu Cupón") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UniBitesTheme.colors.textPrimary,
                    unfocusedBorderColor = UniBitesTheme.colors.textPrimary,
                    focusedLabelColor = UniBitesTheme.colors.textPrimary,
                    cursorColor = UniBitesTheme.colors.textSecondary,
                ),
            )
            UniBitesButton(
                onClick = {
                    validateCoupon(context, couponCode, coupons){
                        showQRDialog = it
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Validar")
            }
            if (showQRDialog && selectedCoupon != null) {
                QRCodeDialog(selectedCoupon!!, onDismiss = {
                    showQRDialog = false
                    selectedCoupon = null
                })
            }

            Spacer(Modifier.height(24.dp))
            // Tabs for Disponibles and Vencidos
            TabRow(
                selectedTabIndex = tabSelected,
                containerColor = UniBitesTheme.colors.uiBackground,
                contentColor = UniBitesTheme.colors.sea300,
                indicator = { tabPositions ->
                    if (tabSelected < tabPositions.size) {
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[tabSelected]),
                            color = UniBitesTheme.colors.textPrimary
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = tabSelected == index,
                        onClick = { tabSelected = index },
                        text = { Text(title) },


                    )
                }
            }

            // Display coupons list
            when (tabSelected) {
                0 -> CouponsList("Disponibles", coupons.filter { it.isPublic && it.isValid }, context) { coupon ->
                    selectedCoupon = coupon
                    showQRDialog = true
                }
                1 -> CouponsList("Vencidos", coupons.filter { it.isPublic && !it.isValid }, context) { coupon ->
                    Toast.makeText(context, "Este código QR ya no es valido.", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}


@Composable
fun CouponsList(type: String, coupons: List<Coupon>, context: Context, onCouponSelected: (Coupon) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedCoupon by remember { mutableStateOf<Coupon?>(null) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
        items(coupons) { coupon ->
            CouponCard(coupon) { selected ->
                if (selected.isValid && type == "Disponibles") {
                    selectedCoupon = selected

                    showDialog = true
                } else {
                    Toast.makeText(context, "Este código QR ya no es valido.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    selectedCoupon?.let { coupon ->
        if (showDialog) {
            QRCodeDialog(coupon, onDismiss = {
                showDialog = false
                selectedCoupon = null
            })
        }
    }
}


@Composable
fun CouponCard(coupon: Coupon, onClick: (Coupon) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = UniBitesTheme.colors.uiBorder,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(coupon) },


    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = coupon.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Valido para: ${coupon.restaurant}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Valido Hasta: ${coupon.endDate}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
@Composable
fun QRCodeDialog(coupon: Coupon, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = UniBitesTheme.colors.sea300,
            ),
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Valido para: ${coupon.restaurant}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Valido Hasta: ${coupon.endDate}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.qrcode),
                    contentDescription = "Codigo QR"
                )
                Spacer(modifier = Modifier.height(24.dp))
                UniBitesButton(onClick = { onDismiss() }) {
                    Text("Cerrar")
                }
            }
        }
    }
}

data class Coupon(
    val description: String,
    val endDate: String,
    val id: String,
    val isPublic: Boolean,
    val isValid: Boolean,
    val restaurant: String
)
