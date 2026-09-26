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

// 📋 Danh sách nhiệm vụ: nhận / hoàn thành để lấy thưởng xu + exp
@Composable
fun QuestScreen(viewModel: GameViewModel) {
    val questsState by viewModel.quests.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadQuests() }

    Column(Modifier.fillMaxSize()) {
        Text("📋 Nhiệm vụ", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            questsState.loading -> LoadingBox()
            questsState.error != null -> ErrorBox(questsState.error!!)
            else -> LazyColumn {
                items(questsState.data ?: emptyList()) { quest ->
                    val status = quest.status
                    val actionLabel = when (status) {
                        null -> "Nhận nhiệm vụ"
                        "in_progress" -> "Hoàn thành"
                        else -> "Đã hoàn thành"
                    }
                    GameCard(
                        title = quest.title,
                        subtitle = quest.description,
                        trailingText = "Thưởng: ${quest.reward_coin} xu • ${quest.reward_exp} EXP",
                        actionLabel = actionLabel,
                        onAction = {
                            when (status) {
                                null -> viewModel.acceptQuest(quest.id)
                                "in_progress" -> viewModel.completeQuest(quest.id)
                                else -> {}
                            }
                        }
                    )
                }
            }
        }
    }
}
