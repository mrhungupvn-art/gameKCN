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

// 🚲 Danh sách phương tiện: mua xe để tăng tốc độ di chuyển
@Composable
fun VehicleScreen(viewModel: GameViewModel) {
    val vehiclesState by viewModel.vehicles.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadVehicles() }

    Column(Modifier.fillMaxSize()) {
        Text("🚲 Xe / Phương tiện", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            vehiclesState.loading -> LoadingBox()
            vehiclesState.error != null -> ErrorBox(vehiclesState.error!!)
            else -> LazyColumn {
                items(vehiclesState.data ?: emptyList()) { vehicle ->
                    GameCard(
                        title = vehicle.name,
                        subtitle = "Loại: ${vehicle.type}",
                        trailingText = "${vehicle.price} xu • +${vehicle.speed_bonus} tốc độ",
                        actionLabel = "Mua",
                        onAction = { viewModel.buyVehicle(vehicle.id) }
                    )
                }
            }
        }
    }
}
