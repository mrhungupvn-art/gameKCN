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

// ❤️ Kết duyên: xem danh sách nhân vật khác và tỏ tình
@Composable
fun RomanceScreen(viewModel: GameViewModel) {
    val candidatesState by viewModel.romanceCandidates.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadRomanceCandidates() }

    Column(Modifier.fillMaxSize()) {
        Text("❤️ Kết duyên", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            candidatesState.loading -> LoadingBox()
            candidatesState.error != null -> ErrorBox(candidatesState.error!!)
            else -> LazyColumn {
                items(candidatesState.data ?: emptyList()) { person ->
                    GameCard(
                        title = person.name,
                        subtitle = "${person.gender} • Cấp ${person.level}",
                        trailingText = null,
                        actionLabel = "Tỏ tình",
                        onAction = { viewModel.proposeRomance(person.id) }
                    )
                }
            }
        }
    }
}
