package com.foodkcn.game.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodkcn.game.ui.components.ErrorBox
import com.foodkcn.game.ui.components.GameCard
import com.foodkcn.game.ui.components.LoadingBox
import com.foodkcn.game.viewmodel.GameViewModel

// 🏪 Cửa hàng / người bán: xem danh sách và tự đăng ký mở cửa hàng riêng
@Composable
fun SellerScreen(viewModel: GameViewModel) {
    val sellersState by viewModel.sellers.collectAsState()
    var shopName by remember { mutableStateOf("") }
    LaunchedEffect(Unit) { viewModel.loadSellers() }

    Column(Modifier.fillMaxSize()) {
        Text("🏪 Cửa hàng / Người bán", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = shopName, onValueChange = { shopName = it },
                label = { Text("Tên cửa hàng mới") }, modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (shopName.isNotBlank()) {
                    viewModel.registerSeller(shopName, null)
                    shopName = ""
                }
            }) { Text("Mở") }
        }

        Spacer(Modifier.height(8.dp))

        when {
            sellersState.loading -> LoadingBox()
            sellersState.error != null -> ErrorBox(sellersState.error!!)
            else -> LazyColumn {
                items(sellersState.data ?: emptyList()) { seller ->
                    GameCard(
                        title = seller.shop_name,
                        subtitle = "Chủ cửa hàng #${seller.character_id ?: "?"}",
                        trailingText = null,
                        actionLabel = "Ghé thăm",
                        onAction = { /* TODO: mở màn hình chi tiết cửa hàng */ }
                    )
                }
            }
        }
    }
}
