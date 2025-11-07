package com.powluiz.forexwearos.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powluiz.forexwearos.data.model.ForexDisplay
import com.powluiz.forexwearos.data.repository.ForexRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ForexUiState {
    object Loading : ForexUiState()
    data class Success(val currencies: List<ForexDisplay>) : ForexUiState()
    data class Error(val message: String) : ForexUiState()
}

class ForexViewModel : ViewModel() {

    private val repository = ForexRepository()

    private val _uiState = MutableStateFlow<ForexUiState>(ForexUiState.Loading)
    val uiState: StateFlow<ForexUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _uiState.value = ForexUiState.Loading

            repository.getMainCurrencies()
                .onSuccess { currencies ->
                    _uiState.value = ForexUiState.Success(currencies)
                }
                .onFailure { exception ->
                    _uiState.value = ForexUiState.Error(
                        exception.message ?: "Erro ao carregar dados"
                    )
                }
        }
    }

    fun refreshData() {
        loadCurrencies()
    }
}