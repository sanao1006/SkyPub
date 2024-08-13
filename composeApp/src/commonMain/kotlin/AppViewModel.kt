import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.common.ProfileUiState
import app.skypub.data.repository.InitializeRepository
import arrow.core.Either
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppViewModel(
    private val initializeRepository: InitializeRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _isAlreadyLogin: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isAlreadyLogin = _isAlreadyLogin.asStateFlow()

    private val _profileUiState = MutableStateFlow(ProfileUiState("", "", "", 0, 0))
    val profileUiState = _profileUiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeRepository.initializeToken()
            updateLoginState()
        }
    }

    private fun updateLoginState() {
        viewModelScope.launch {
            val accessJwt = dataStore.data.first()[stringPreferencesKey("access_jwt")]
            val refreshJwt = dataStore.data.first()[stringPreferencesKey("refresh_jwt")]
            _isAlreadyLogin.value = !accessJwt.isNullOrEmpty() && !refreshJwt.isNullOrEmpty()
            if (_isAlreadyLogin.value === true) fetchProfile()
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            when (val result = initializeRepository.getProfile()) {
                is Either.Right -> {
                    _profileUiState.value = ProfileUiState(
                        avatar = result.value.avatar,
                        handle = result.value.handle,
                        displayName = result.value.displayName,
                        followsCount = result.value.followsCount,
                        followersCount = result.value.followersCount
                    )
                }

                is Either.Left -> {
                    Napier.e(tag = "getProfileError") {
                        "message: ${result.value.message}"
                    }
                }
            }
        }
    }
}