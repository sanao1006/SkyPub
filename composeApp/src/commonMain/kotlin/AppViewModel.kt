import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.skypub.data.repository.InitializeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppViewModel(
    private val initializeRepository: InitializeRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _isAlreadyLogin = MutableStateFlow(false)
    val isAlreadyLogin = _isAlreadyLogin.asStateFlow()

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
        }
    }
}