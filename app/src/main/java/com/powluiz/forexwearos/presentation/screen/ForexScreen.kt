package com.powluiz.forexwearos.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.powluiz.forexwearos.data.model.ForexDisplay
import com.powluiz.forexwearos.presentation.viewmodel.ForexUiState
import com.powluiz.forexwearos.presentation.viewmodel.ForexViewModel

@Composable
fun ForexScreen(
    viewModel: ForexViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scalingLazyListState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = scalingLazyListState)
        }
    ) {
        when (val state = uiState) {
            is ForexUiState.Loading -> {
                LoadingScreen()
            }
            is ForexUiState.Success -> {
                CurrencyList(
                    currencies = state.currencies,
                    scalingLazyListState = scalingLazyListState,
                    onRefresh = { viewModel.refreshData() }
                )
            }
            is ForexUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.refreshData() }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Erro",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Tentar novamente")
        }
    }
}

@Composable
fun CurrencyList(
    currencies: List<ForexDisplay>,
    scalingLazyListState: ScalingLazyListState,
    onRefresh: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scalingLazyListState,
        contentPadding = PaddingValues(
            top = 32.dp,
            bottom = 32.dp,
            start = 8.dp,
            end = 8.dp
        )
    ) {
        item {
            ListHeader {
                Text(
                    text = "Câmbio",
                    textAlign = TextAlign.Center
                )
            }
        }

        items(currencies.size) { index ->
            CurrencyCard(currency = currencies[index])
        }

        item {
            Chip(
                onClick = onRefresh,
                label = {
                    Text(
                        text = "Atualizar",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ChipDefaults.primaryChipColors()
            )
        }
    }
}

@Composable
fun CurrencyCard(currency: ForexDisplay) {
    Card(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = currency.pair,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "R$ ${currency.bidPrice}",
                    fontSize = 13.sp
                )

                Text(
                    text = currency.pctChange,
                    fontSize = 12.sp,
                    color = if (currency.isPositive) Color.Green else Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = currency.variation,
                fontSize = 11.sp,
                color = if (currency.isPositive) Color.Green else Color.Red
            )
        }
    }
}