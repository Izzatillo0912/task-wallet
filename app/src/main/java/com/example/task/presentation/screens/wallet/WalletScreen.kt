package com.example.task.presentation.screens.wallet

import android.util.Log
import com.example.task.presentation.bottomSheets.addPromoCode.AddPromoCodeBottomSheet
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.task.R
import com.example.task.presentation.utils.theme.FigTree
import com.example.task.presentation.utils.CustomSwitch
import com.example.task.presentation.utils.navGraph.navigateSafely
import com.example.task.presentation.utils.theme.ButtonGray
import com.example.task.presentation.utils.theme.additionalCardColor
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    navController : NavController,
    viewModel: WalletViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val pagerState = rememberPagerState(initialPage = 0) { state.cards.size }

    val animatedStartPadding by animateDpAsState(
        targetValue = if (pagerState.currentPage == 0) 15.dp else 40.dp,
        label = "startPadding"
    )

    val animatedEndPadding by animateDpAsState(
        targetValue = if (pagerState.currentPage == pagerState.pageCount - 1) 15.dp else 40.dp,
        label = "endPadding"
    )

    LaunchedEffect(Unit) {
        viewModel.handleIntent(WalletScreenIntents.GetCards)
    }

    LaunchedEffect(state.enabledCardId) {
        state.enabledCardId?.let { cardID->
            pagerState.animateScrollToPage(cardID)
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column {

            Text(
                "Wallet",
                modifier = Modifier.padding(start = 15.dp),
                fontFamily = FigTree,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            )

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(
                    start = animatedStartPadding,
                    end = animatedEndPadding
                ),
                beyondViewportPageCount = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) { page->

                val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue.coerceIn(0f, 1f)
                val scale = 1f - (pageOffset * 0.12f)


                BalanceCard(scale = scale, data = state.cards[page])
            }

            Spacer(modifier = Modifier.height(20.dp))

            IdentificationCard(modifier = Modifier.padding(horizontal = 15.dp))

            Spacer(modifier = Modifier.height(20.dp))

            WalletAdditionalCard(
                modifier = Modifier.padding(horizontal = 15.dp),
                icon = R.drawable.ic_promo_code,
                text = "Add promo code",
                enabledSwitch = false,
                isChecked = false,
                onClicked = {
                    viewModel.handleIntent(WalletScreenIntents.ShowPromoSheet(true))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            WalletAdditionalCard(
                modifier = Modifier.padding(horizontal = 15.dp),
                icon = R.drawable.ic_cash,
                text = "Cash",
                enabledSwitch = true,
                isChecked = state.cashEnabled,
                onCheckedChange = { checked->
                    viewModel.handleIntent(WalletScreenIntents.CashMethodEnable(checked))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                state.cards.forEach { card ->
                    WalletAdditionalCard(
                        icon = R.drawable.ic_cards,
                        text = "Card ****${card.number.takeLast(4)}",
                        enabledSwitch = true,
                        isChecked = card.isSelected,
                        onCheckedChange = { checked ->
                            viewModel.handleIntent(WalletScreenIntents.CardMethodEnable(cardId = card.id, checked))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            WalletAdditionalCard(
                modifier = Modifier.padding(horizontal = 15.dp),
                icon = R.drawable.ic_add_card,
                text = "Add new card",
                enabledSwitch = false,
                isChecked = false,
                onClicked = {
                    navController.navigateSafely("addCard")
                }
            )
        }
    }

    if (state.showPromoBottomSheet) {
        AddPromoCodeBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                viewModel.handleIntent(WalletScreenIntents.ShowPromoSheet(false))
            }
        )
    }

}

@Composable
fun IdentificationCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                color = Color.Black, width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        Row {
            Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = "Identification")
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Identification required",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.ic_arrow_up), contentDescription = "")
        }
    }
}

@Composable
fun WalletAdditionalCard(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    enabledSwitch: Boolean,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    onClicked: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 2.dp.toPx() // y=1
                shape = RoundedCornerShape(14.dp)
                clip = true
            }
            .drawBehind {
                val shadowColor = Color(0xFFF2F2F2) // qora, 20% opacity
                val cornerRadius = 10.dp.toPx()

                drawRoundRect(
                    color = shadowColor,
                    topLeft = Offset(0f, 1.dp.toPx()), // y = 1
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    alpha = 1f
                )
            }
            .height(IntrinsicSize.Min),
        onClick = onClicked,
        shape = RoundedCornerShape(10.dp),
        color = additionalCardColor,
        tonalElevation = 2.dp, // Compose Material 3 uses tonalElevation for shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Cash",
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }

            if (enabledSwitch) {
                CustomSwitch(
                    onCheckedChange = onCheckedChange,
                    checked = isChecked
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = ""
                )
            }
        }
    }
}