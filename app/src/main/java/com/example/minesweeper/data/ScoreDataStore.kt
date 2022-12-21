package com.example.minesweeper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private const val HIGH_SCORES = "high_scores"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = HIGH_SCORES
)

class ScoreDataStore(context: Context) {
    private val GAME_TYPE = intPreferencesKey("game_type")
    private val PLAYER_NAME = stringPreferencesKey("player_name")
    private val EASY_HIGH_SCORE = intPreferencesKey("easy_high_score")
    private val EASY_NAME = stringPreferencesKey("easy_name")
    private val MEDIUM_HIGH_SCORE = intPreferencesKey("medium_high_score")
    private val MEDIUM_NAME = stringPreferencesKey("medium_name")
    private val HARD_HIGH_SCORE = intPreferencesKey("hard_high_score")
    private val HARD_NAME = stringPreferencesKey("hard_name")

    val gameTypeFlow: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[GAME_TYPE] ?: 2
        }

    suspend fun saveGameType(gameType: Int, context: Context) {
        context.dataStore.edit {
            it[GAME_TYPE] = gameType
        }
    }

    val playerNameFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[PLAYER_NAME] ?: "Developer"
        }

    suspend fun savePlayerName(playerName: String, context: Context) {
        context.dataStore.edit {
            it[PLAYER_NAME] = playerName
        }
    }

    val easyScoreFlow: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[EASY_HIGH_SCORE] ?: 999
        }

    suspend fun saveEasyHighScore(score: Int, context: Context) {
        context.dataStore.edit {
            it[EASY_HIGH_SCORE] = score
        }
    }

    val easyPlayerNameFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[EASY_NAME] ?: "Developer"
        }

    suspend fun saveEasyPlayerName(playerName: String, context: Context) {
        context.dataStore.edit {
            it[EASY_NAME] = playerName
        }
    }

    val mediumScoreFlow: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[MEDIUM_HIGH_SCORE] ?: 999
        }

    suspend fun saveMediumHighScore(score: Int, context: Context) {
        context.dataStore.edit {
            it[MEDIUM_HIGH_SCORE] = score
        }
    }

    val mediumPlayerNameFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[MEDIUM_NAME] ?: "Developer"
        }

    suspend fun saveMediumPlayerName(playerName: String, context: Context) {
        context.dataStore.edit {
            it[EASY_NAME] = playerName
        }
    }

    val hardScoreFlow: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[HARD_HIGH_SCORE] ?: 999
        }

    suspend fun saveHardHighScore(score: Int, context: Context) {
        context.dataStore.edit {
            it[HARD_HIGH_SCORE] = score
        }
    }

    val hardPlayerNameFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[HARD_NAME] ?: "Developer"
        }

    suspend fun saveHardPlayerName(playerName: String, context: Context) {
        context.dataStore.edit {
            it[HARD_NAME] = playerName
        }
    }
}

