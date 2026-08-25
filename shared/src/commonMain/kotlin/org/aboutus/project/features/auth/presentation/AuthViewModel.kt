package org.aboutus.project.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aboutus.project.features.welcome.domain.AuthRepository
import org.aboutus.project.features.welcome.domain.WelcomePreferences

data class AuthUiState(val loading: Boolean = false, val error: Boolean = false)
class AuthViewModel(
    private val repository: AuthRepository,
    private val preferences: WelcomePreferences
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState());
    val state = _state.asStateFlow()
    fun enter(onSuccess: () -> Unit) = viewModelScope.launch {
        _state.value = AuthUiState(loading = true); repository.signInAnonymously()
        .onSuccess { preferences.loggedIn = true; onSuccess() }
        .onFailure { _state.value = AuthUiState(error = true) }
    }

    fun restore(onSuccess: () -> Unit) {
        preferences.loggedIn = true; onSuccess()
    }
}
