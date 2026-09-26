package com.foodkcn.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.foodkcn.game.ui.navigation.KCNNavGraph
import com.foodkcn.game.ui.theme.KCNGameTheme
import com.foodkcn.game.viewmodel.GameViewModel
import com.foodkcn.game.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels {
        ViewModelFactory((application as KCNApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as KCNApplication

        setContent {
            KCNGameTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isLoggedIn by remember { mutableStateOf(false) }

                    // Kiểm tra xem đã có token lưu sẵn (đăng nhập trước đó) hay chưa
                    LaunchedEffect(Unit) {
                        val token = app.tokenManager.getTokenOnce()
                        isLoggedIn = !token.isNullOrEmpty()
                    }

                    KCNNavGraph(
                        viewModel = viewModel,
                        isLoggedIn = isLoggedIn,
                        onLoggedIn = { isLoggedIn = true }
                    )
                }
            }
        }
    }
}
