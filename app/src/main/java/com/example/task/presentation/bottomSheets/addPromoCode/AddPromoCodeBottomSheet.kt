package com.example.task.presentation.bottomSheets.addPromoCode

import com.example.task.presentation.utils.buttons.CustomBackButton
import com.example.task.presentation.utils.buttons.SaveButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task.presentation.utils.theme.FigTree
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPromoCodeBottomSheet(
    sheetState: SheetState,
    onDismissRequest : () -> Unit,
    viewModel: AddPromoCodeViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(!sheetState.isVisible) {
        viewModel.handleIntent(AddPromoCodeIntent.ClearState)
    }

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.handleIntent(AddPromoCodeIntent.ClearState)
            onDismissRequest()
        },
        dragHandle = null,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                CustomBackButton(
                    modifier = Modifier
                        .padding(top = 15.dp, bottom = 15.dp)
                        .size(48.dp),
                    onClick = { onDismissRequest() }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Enter promo code",
                    color = Color.Black,
                    fontFamily = FigTree,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = state.promoCode,
                onValueChange = {
                    viewModel.handleIntent(AddPromoCodeIntent.PromoCodeChanged(it))
                },
                placeholder = {
                    Text(
                        "Promo code : ",
                        style = TextStyle(
                            color = Color.Gray,
                            fontFamily = FigTree,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Gray,
                    cursorColor = Color.Blue,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontFamily = FigTree,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            SaveButton(
                modifier = Modifier.padding(bottom = 15.dp),
                enabled = state.isSaveEnabled
            ) {
                viewModel.handleIntent(AddPromoCodeIntent.SaveClicked)
                onDismissRequest()
            }
        }
    }
}