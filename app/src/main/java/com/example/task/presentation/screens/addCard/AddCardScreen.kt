package com.example.task.presentation.screens.addCard

import com.example.task.presentation.utils.buttons.CustomBackButton
import com.example.task.presentation.utils.buttons.SaveButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.task.R
import com.example.task.presentation.utils.navGraph.popBackStackSafely
import com.example.task.presentation.utils.textFields.masks.CreditCardVisualTransformation
import com.example.task.presentation.utils.textFields.masks.ExpiryDateVisualTransformation
import com.example.task.presentation.utils.theme.FigTree
import com.example.task.presentation.utils.theme.addCardBackground

@Composable
fun AddCardScreen(
    navController: NavController,
    viewModel: AddCardViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {

                CustomBackButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp),
                    onClick = {
                        navController.popBackStackSafely()
                    }
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Add Card",
                    color = Color.Black,
                    fontFamily = FigTree,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 20.dp)
                    .graphicsLayer {
                        shadowElevation = 10.dp.toPx() // y=1
                        shape = RoundedCornerShape(16.dp)
                        clip = true
                    }
                    .drawBehind {
                        val shadowColor = Color(0xFFF2F2F2) // qora, 20% opacity
                        val cornerRadius = 16.dp.toPx()

                        drawRoundRect(
                            color = shadowColor,
                            topLeft = Offset(0f, 1.dp.toPx()), // y = 1
                            size = Size(size.width, size.height),
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                            alpha = 1f
                        )
                    }
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp) // optional: agar radius bo‘lsa
                    )
                    .background(
                        color = addCardBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {

                Column(modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 15.dp, end = 15.dp, bottom = 15.dp, top = 50.dp))
                {
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.cardNumber,
                        hint = "0000000000000000",
                        isSelected = state.cardNumberIsSelected,
                        onSelected = { selected ->
                            if (selected) {
                                viewModel.handleIntent(AddCardIntents.SelectCardNumber)
                            }
                        },
                        visualTransformation = CreditCardVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(
                        modifier = Modifier.width(100.dp),
                        text = state.expiryDate,
                        hint = "0000",
                        isSelected = state.expireDateIsSelected,
                        onSelected = { selected ->
                            if (selected) {
                                viewModel.handleIntent(AddCardIntents.SelectExpiryDate)
                            }
                        },
                        visualTransformation = ExpiryDateVisualTransformation(),
                    )
                }
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {

                SaveButton(
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 30.dp),
                    enabled = state.isSaveEnabled)
                {
                    viewModel.handleIntent(AddCardIntents.AddCard(state.cardNumber))
                    navController.popBackStackSafely()
                }

                MyCustomKeyboards { key ->
                    viewModel.handleIntent(AddCardIntents.KeyboardInput(key))
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    modifier : Modifier,
    text : String,
    hint : String,
    isSelected : Boolean,
    onSelected : (Boolean) -> Unit,
    visualTransformation: VisualTransformation
) {
    Box(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onSelected(true)
            }
            .background(Color.Transparent, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Black else Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(top = 8.dp, bottom = 8.dp, start = 15.dp, end = 15.dp),
    ) {
        Text(
            text = visualTransformation.filter(AnnotatedString(
                text.ifEmpty { hint }
            )).text.text,
            fontFamily = FigTree,
            color = if (text.isEmpty()) Color.Gray else Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun MyCustomKeyboards(
    onChange: (String) -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = Color.Black,
        disabledContainerColor = Color.White,
        disabledContentColor = Color.Black
    )

    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "←") // "" = bo‘sh joy, ← = backspace
    )

    rows.forEach { row ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            row.forEach { label ->
                when (label) {
                    "" -> Spacer(modifier = Modifier.weight(1f)) // bo‘sh joy
                    "←" -> Button(
                        onClick = { onChange("") },
                        modifier = Modifier.weight(1f),
                        colors = buttonColors
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_backspace),
                            contentDescription = "Backspace",
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                    else -> Button(
                        onClick = { onChange(label) },
                        modifier = Modifier.weight(1f),
                        colors = buttonColors
                    ) {
                        Text(
                            text = label,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
