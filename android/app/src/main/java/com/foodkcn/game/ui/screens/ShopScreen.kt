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

// 🛒 Mua sắm: danh sách sản phẩm, mua bằng xu
@Composable
fun ShopScreen(viewModel: GameViewModel) {
    val productsState by viewModel.products.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadProducts() }

    Column(Modifier.fillMaxSize()) {
        Text("🛒 Mua sắm", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            productsState.loading -> LoadingBox()
            productsState.error != null -> ErrorBox(productsState.error!!)
            else -> LazyColumn {
                items(productsState.data ?: emptyList()) { product ->
                    GameCard(
                        title = product.name,
                        subtitle = "Danh mục: ${product.category ?: "khác"} • Còn ${product.stock}",
                        trailingText = "${product.price} xu",
                        actionLabel = "Mua",
                        onAction = { viewModel.buyProduct(product.id, 1) }
                    )
                }
            }
        }
    }
}
