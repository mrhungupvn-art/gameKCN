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

// ⭐ Danh sách kỹ năng: nâng cấp bằng xu
@Composable
fun SkillScreen(viewModel: GameViewModel) {
    val skillsState by viewModel.skills.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadSkills() }

    Column(Modifier.fillMaxSize()) {
        Text("⭐ Kỹ năng", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            skillsState.loading -> LoadingBox()
            skillsState.error != null -> ErrorBox(skillsState.error!!)
            else -> LazyColumn {
                items(skillsState.data ?: emptyList()) { skill ->
                    GameCard(
                        title = skill.name,
                        subtitle = skill.description,
                        trailingText = "Cấp ${skill.my_level}/${skill.max_level}",
                        actionLabel = "Nâng cấp",
                        onAction = { viewModel.upgradeSkill(skill.id) }
                    )
                }
            }
        }
    }
}
