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

// 🎁 Phần thưởng: danh sách thưởng nhận được từ nhiệm vụ, sự kiện...
@Composable
fun RewardScreen(viewModel: GameViewModel) {
    val rewardsState by viewModel.rewards.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadRewards() }

    Column(Modifier.fillMaxSize()) {
        Text("🎁 Phần thưởng", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            rewardsState.loading -> LoadingBox()
            rewardsState.error != null -> ErrorBox(rewardsState.error!!)
            else -> LazyColumn {
                items(rewardsState.data ?: emptyList()) { reward ->
                    GameCard(
                        title = reward.title,
                        subtitle = "Loại: ${reward.type}",
                        trailingText = "+${reward.coin} xu, +${reward.gem} kim cương",
                        actionLabel = if (reward.claimed == 1) "Đã nhận" else "Nhận",
                        onAction = { if (reward.claimed == 0) viewModel.claimReward(reward.id) }
                    )
                }
            }
        }
    }
}
