package com.example.unibites.ui.snackdetail

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.unibites.R
import com.example.unibites.maps.ui.MyUniMap
import com.example.unibites.model.Snack
import com.example.unibites.model.SnackCollection
import com.example.unibites.model.SnackRepo
import com.example.unibites.model.snacks
import com.example.unibites.ui.components.UniBitesButton
import com.example.unibites.ui.components.UniBitesDivider
import com.example.unibites.ui.components.UniBitesSurface
import com.example.unibites.ui.components.SnackCollection
import com.example.unibites.ui.components.SnackImage
import com.example.unibites.ui.home.HomeViewModel
import com.example.unibites.ui.theme.UniBitesTheme
import com.example.unibites.ui.theme.Neutral8
import com.example.unibites.ui.utils.formatPrice
import com.example.unibites.ui.utils.mirroringBackIcon
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)


fun getSnackFromId(snackId: String): Snack {
    val homeViewModel = HomeViewModel()
    var snack = homeViewModel.getSnack(snackId)
    if (snack == null) {
        snack = snacks.get(0)
    }
    return snack
}

@Composable
fun SnackDetail(
    snackId: String,
    upPress: () -> Unit,
    onMapClick: () -> Unit,
    snackDetailState: SnackDetailState
) {
    val snack = remember(snackId) { getSnackFromId(snackId) }
    val related = remember(snackId) { listOf<SnackCollection>() }

    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        if(snackDetailState.loading){
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                color = UniBitesTheme.colors.iconPrimary
            )
        }
        Body(related, scroll, onMapClick, snack)
        Title(snack) { scroll.value }
        Image(snack.imageUrl) { scroll.value }
        Up(upPress)
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(UniBitesTheme.colors.tornado1))
    )
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

@Composable
private fun Body(
    related: List<SnackCollection>,
    scroll: ScrollState,
    onMapClick: () -> Unit,
    snack: Snack
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            UniBitesSurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.Description),
                        style = MaterialTheme.typography.overline,
                        color = UniBitesTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        text = snack.description,
                        style = MaterialTheme.typography.body1,
                        color = UniBitesTheme.colors.textHelp,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = HzPadding
                    )
                    val textButton = if (seeMore) {
                        stringResource(id = R.string.see_more)
                    } else {
                        stringResource(id = R.string.see_less)
                    }
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center,
                        color = UniBitesTheme.colors.textLink,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(100.dp)
                    ) {

                       UniBitesButton(onClick = onMapClick) {
                           Text(stringResource(R.string.ver_en_el_mapa))
                       }
                    }

                    UniBitesDivider()

                    related.forEach { snackCollection ->
                        key(snackCollection.id) {
                            SnackCollection(
                                snackCollection = snackCollection,
                                onSnackClick = { },
                                onReviewClick = { },
                                highlight = false
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(snack: Snack, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = UniBitesTheme.colors.uiBackground)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = snack.name,
            style = MaterialTheme.typography.h4,
            color = UniBitesTheme.colors.textSecondary,
            modifier = HzPadding
        )
        Text(
            text = snack.tagline,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            color = UniBitesTheme.colors.textHelp,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Spacer(Modifier.height(8.dp))
        UniBitesDivider()
    }
}

@Composable
private fun Image(
    imageUrl: String,
    scrollProvider: () -> Int,
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.statusBarsPadding()
    ) {
        SnackImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun SnackDetailPreview() {
    UniBitesTheme {
        SnackDetail(
            snackId = ""+1L,
            upPress = { },
            onMapClick = { },
            snackDetailState = SnackDetailState()
        )
    }
}
