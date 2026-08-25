package org.aboutus.project.features.location.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aboutus.project.features.location.domain.DeviceLocationProvider
import org.aboutus.project.features.location.domain.UserLocationPreferences

data class LocationUiState(val loading: Boolean = false, val failed: Boolean = false)
class LocationViewModel(private val preferences: UserLocationPreferences) : ViewModel() {
    private val _state = MutableStateFlow(LocationUiState()); val state = _state.asStateFlow()
    fun request(provider: DeviceLocationProvider, onDone: () -> Unit) = viewModelScope.launch {
        _state.value = LocationUiState(loading = true)
        provider.requestLocation().onSuccess { preferences.save(it); onDone() }.onFailure { _state.value = LocationUiState(failed = true) }
    }
    fun skip(onDone: () -> Unit) = onDone()
}
