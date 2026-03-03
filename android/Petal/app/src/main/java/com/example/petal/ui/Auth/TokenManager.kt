package com.example.petal.ui.Auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class TokenManager(private val context: Context) {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USERNAME = stringPreferencesKey("username")
    }

    suspend fun saveTokens(access: String, refresh: String, username: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = access
            prefs[REFRESH_TOKEN] = refresh
            prefs[USERNAME] = username
        }
    }
    suspend fun getUsername(): String? {
        return context.dataStore.data
            .map { it[USERNAME] }
            .first()
    }
    suspend fun getAccessToken(): String? {
        return context.dataStore.data
            .map { it[ACCESS_TOKEN] }
            .first()
    }

    suspend fun getRefreshToken(): String? {
        return context.dataStore.data
            .map { it[REFRESH_TOKEN] }
            .first()
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data
        .map { it[ACCESS_TOKEN] }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}