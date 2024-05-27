package com.example.unibites.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.unibites.R
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesScaffold
import com.example.unibites.ui.theme.UniBitesTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun Coupon(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var coupons by remember { mutableStateOf<List<Coupon>>(emptyList()) }
    var couponCode by remember { mutableStateOf("") }
    var tabSelected by remember { mutableStateOf(0) }
    var showQRDialog by remember { mutableStateOf(false) }
    var selectedCoupon by remember { mutableStateOf<Coupon?>(null) }
    val tabs = listOf("Disponibles", "Vencidos")

    LaunchedEffect(Unit) {
        scope.launch {
            coupons = fetchCoupons()
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
                    validateCoupon(context, couponCode, coupons) { isValid ->
                        if (isValid) {
                            selectedCoupon = coupons.firstOrNull { it.id == couponCode }
                            showQRDialog = true
                        }
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

            TabRow(
                selectedTabIndex = tabSelected,
                containerColor = UniBitesTheme.colors.uiBackground,
                contentColor = UniBitesTheme.colors.sea300,
                indicator = { tabPositions ->
                    if (tabSelected < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
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
                        text = { Text(title) }
                    )
                }
            }

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

suspend fun fetchCoupons(): List<Coupon> {
    return try {
        val db = FirebaseFirestore.getInstance()
        val result = db.collection("coupons").get().await()
        result.documents.mapNotNull { doc ->
            Coupon(
                description = doc.getString("description") ?: "",
                endDate = doc.getString("end_date") ?: "",
                id = doc.getString("id") ?: "",
                isPublic = doc.getBoolean("is_public") ?: false,
                isValid = doc.getBoolean("is_valid") ?: false,
                restaurant = doc.getString("restaurant") ?: ""
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}

fun validateCoupon(context: Context, couponCode: String, coupons: List<Coupon>, onResult: (Boolean) -> Unit) {
    val foundCoupon = coupons.firstOrNull { it.id == couponCode }
    if (foundCoupon != null && foundCoupon.isValid) {
        onResult(true)
        Toast.makeText(context, "Cupón válido.", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, "No se encontró el cupón.", Toast.LENGTH_LONG).show()
        onResult(false)
    }
}

@Composable
fun CouponsList(type: String, coupons: List<Coupon>, context: Context, onCouponSelected: (Coupon) -> Unit) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp)
    ) {
        items(coupons) { coupon ->
            CouponCard(coupon) { selected ->
                if (selected.isValid && type == "Disponibles") {
                    onCouponSelected(selected)
                } else {
                    Toast.makeText(context, "Este código QR ya no es valido.", Toast.LENGTH_LONG).show()
                }
            }
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
            .clickable { onClick(coupon) }
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
