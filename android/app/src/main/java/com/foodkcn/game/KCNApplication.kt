package com.foodkcn.game

import android.app.Application
import com.foodkcn.game.data.local.TokenManager
import com.foodkcn.game.data.remote.ApiClient
import com.foodkcn.game.data.remote.ApiService
import com.foodkcn.game.data.repository.GameRepository

// Application class: khởi tạo 1 lần duy nhất TokenManager, ApiService, Repository dùng chung toàn app
class KCNApplication : Application() {
    lateinit var tokenManager: TokenManager
        private set
    lateinit var apiService: ApiService
        private set
    lateinit var repository: GameRepository
        private set

    override fun onCreate() {
        super.onCreate()
        tokenManager = TokenManager(applicationContext)
        apiService = ApiClient.create(tokenManager)
        repository = GameRepository(apiService, tokenManager)
    }
}
