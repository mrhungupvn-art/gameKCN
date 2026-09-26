package com.foodkcn.game.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodkcn.game.ui.components.ErrorBox
import com.foodkcn.game.ui.components.GameCard
import com.foodkcn.game.ui.components.LoadingBox
import com.foodkcn.game.viewmodel.GameViewModel

// 🏠 Danh sách nhà: mua nhà để nâng cấp cuộc sống nhân vật
@Composable
fun HouseScreen(viewModel: GameViewModel) {
    val housesState by viewModel.houses.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadHouses() }

    Column(Modifier.fillMaxSize()) {
        Text("🏠 Nhà", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            housesState.loading -> LoadingBox()
            housesState.error != null -> ErrorBox(housesState.error!!)
            else -> LazyColumn {
                items(housesState.data ?: emptyList()) { house ->
                    GameCard(
                        title = house.name,
                        subtitle = null,
                        trailingText = "${house.price} xu",
                        actionLabel = "Mua",
                        onAction = { viewModel.buyHouse(house.id) }
                    )
                }
            }
        }
    }
}
