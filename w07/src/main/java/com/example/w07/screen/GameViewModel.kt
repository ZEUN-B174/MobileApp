package com.example.w07.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w07.data.GameData
import com.example.w07.data.GameUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var stopwatchJob: Job? = null

    fun onStartClicked() {
        if (uiState.value.gameData.isRunning) return

        _uiState.update {
            it.copy(gameData = GameData(isRunning = true))
        }

        stopwatchJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - uiState.value.gameData.currentTimeMs
            while (true) {
                delay(10L)
                _uiState.update {
                    it.copy(gameData = it.gameData.copy(currentTimeMs = System.currentTimeMillis() - startTime))
                }
            }
        }
    }

    fun onStopClicked(finalTimeMs: Long) {
        _uiState.update {
            it.copy(gameData = it.gameData.copy(isRunning = false))
        }
        stopwatchJob?.cancel()
    }
}