package com.foodkcn.game.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodkcn.game.ui.components.LoadingBox
import com.foodkcn.game.viewmodel.GameViewModel

// 👤 Thông tin nhân vật: cấp độ, EXP, HP, năng lượng
@Composable
fun CharacterScreen(viewModel: GameViewModel) {
    val character by viewModel.character.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadProfile() }

    val c = character.data
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("👤 Nhân vật", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        if (character.loading || c == null) {
            LoadingBox()
        } else {
            InfoRow("Tên", c.name)
            InfoRow("Giới tính", c.gender)
            InfoRow("Cấp độ", "${c.level}")
            InfoRow("Kinh nghiệm", "${c.exp} EXP")
            InfoRow("Máu (HP)", "${c.hp}")
            InfoRow("Năng lượng", "${c.energy}")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
    Divider()
}
