package com.foodkcn.game.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foodkcn.game.ui.components.ErrorBox
import com.foodkcn.game.ui.components.LoadingBox
import com.foodkcn.game.viewmodel.GameViewModel

// 🔐 Màn hình đăng nhập / đăng ký tài khoản
@Composable
fun LoginScreen(viewModel: GameViewModel, onLoggedIn: () -> Unit) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var characterName by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState.data) {
        if (authState.data?.success == true) onLoggedIn()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏭 KCN GAME", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(4.dp))
        Text("game.foodkcn.com", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("Tài khoản") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (isRegisterMode) {
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = characterName, onValueChange = { characterName = it },
                label = { Text("Tên nhân vật") }, modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (isRegisterMode) {
                    viewModel.register(username, password, null, characterName, "nam")
                } else {
                    viewModel.login(username, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegisterMode) "Đăng ký" else "Đăng nhập")
        }

        Spacer(Modifier.height(12.dp))
        TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
            Text(if (isRegisterMode) "Đã có tài khoản? Đăng nhập" else "Chưa có tài khoản? Đăng ký ngay")
        }

        if (authState.loading) LoadingBox()
        authState.error?.let { ErrorBox(it) }
    }
}
