package com.foodkcn.game.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Lưu token đăng nhập + characterId cục bộ trên máy (DataStore)
val Context.dataStore by preferencesDataStore(name = "kcn_game_prefs")

class TokenManager(private val context: Context) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val CHARACTER_ID_KEY = intPreferencesKey("character_id")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val characterIdFlow: Flow<Int?> = context.dataStore.data.map { it[CHARACTER_ID_KEY] }

    suspend fun getTokenOnce(): String? = context.dataStore.data.first()[TOKEN_KEY]

    suspend fun saveSession(token: String, characterId: Int?) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            if (characterId != null) prefs[CHARACTER_ID_KEY] = characterId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
