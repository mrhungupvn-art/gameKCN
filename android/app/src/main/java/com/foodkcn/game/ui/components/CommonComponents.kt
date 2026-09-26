package com.foodkcn.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodkcn.game.ui.theme.KcnGold

// Thanh trạng thái trên cùng: tên nhân vật, level, xu — dùng chung mọi màn hình
@Composable
fun TopStatusBar(characterName: String, level: Int, coin: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(characterName, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text("Cấp $level", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🪙", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(4.dp))
            Text("$coin", color = KcnGold, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun LoadingBox() {
    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorBox(message: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
        Text("⚠️ $message", color = MaterialTheme.colorScheme.error)
    }
}

// Thẻ chung cho danh sách (KCN, nghề nghiệp, nhà, xe, sản phẩm...)
@Composable
fun GameCard(
    title: String,
    subtitle: String? = null,
    trailingText: String? = null,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                if (subtitle != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                if (trailingText != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(trailingText, style = MaterialTheme.typography.bodyMedium, color = KcnGold)
                }
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onAction) { Text(actionLabel) }
        }
    }
}
