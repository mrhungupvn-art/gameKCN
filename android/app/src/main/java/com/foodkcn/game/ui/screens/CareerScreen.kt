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

// 💼 Danh sách nghề nghiệp / công ty: ứng tuyển và đi làm để nhận lương
@Composable
fun CareerScreen(viewModel: GameViewModel) {
    val careersState by viewModel.careers.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadCareers() }

    Column(Modifier.fillMaxSize()) {
        Text("💼 Nghề nghiệp / Công ty", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        when {
            careersState.loading -> LoadingBox()
            careersState.error != null -> ErrorBox(careersState.error!!)
            else -> LazyColumn {
                items(careersState.data ?: emptyList()) { career ->
                    GameCard(
                        title = career.name,
                        subtitle = career.company_name,
                        trailingText = "Lương cơ bản: ${career.base_salary} xu • Yêu cầu cấp ${career.required_level}",
                        actionLabel = "Ứng tuyển",
                        onAction = { viewModel.applyCareer(career.id) }
                    )
                    GameCard(
                        title = "  ↳ Đi làm hôm nay",
                        subtitle = null,
                        trailingText = null,
                        actionLabel = "Đi làm",
                        onAction = { viewModel.workCareer(career.id) }
                    )
                }
            }
        }
    }
}
