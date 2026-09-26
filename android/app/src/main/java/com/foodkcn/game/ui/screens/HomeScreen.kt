package com.foodkcn.game.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodkcn.game.ui.components.ErrorBox
import com.foodkcn.game.ui.components.GameCard
import com.foodkcn.game.ui.components.LoadingBox
import com.foodkcn.game.ui.components.TopStatusBar
import com.foodkcn.game.viewmodel.GameViewModel

// 🏭 Màn hình chính: danh sách Khu công nghiệp (KCN) để nhân vật gia nhập
@Composable
fun HomeScreen(viewModel: GameViewModel) {
    val character by viewModel.character.collectAsState()
    val wallet by viewModel.wallet.collectAsState()
    val kcnState by viewModel.kcnList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        viewModel.loadKcnList()
    }

    Column(Modifier.fillMaxSize()) {
        TopStatusBar(
            characterName = character.data?.name ?: "...",
            level = character.data?.level ?: 1,
            coin = wallet.data?.coin ?: 0
        )

        Text(
            "Chọn khu công nghiệp để bắt đầu",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        when {
            kcnState.loading -> LoadingBox()
            kcnState.error != null -> ErrorBox(kcnState.error!!)
            else -> LazyColumn {
                items(kcnState.data ?: emptyList()) { kcn ->
                    GameCard(
                        title = kcn.name,
                        subtitle = kcn.description,
                        trailingText = "Tối đa ${kcn.max_players} người",
                        actionLabel = "Vào KCN",
                        onAction = { viewModel.joinKcn(kcn.id) }
                    )
                }
            }
        }
    }
}
