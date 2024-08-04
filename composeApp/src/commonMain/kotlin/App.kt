import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.skypub.feature.auth.AuthScreenNavigation
import app.skypub.home.HomeScreen
import cafe.adriel.voyager.navigator.Navigator
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.map
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    Napier.base(DebugAntilog())
    val dataStore: DataStore<Preferences> = koinInject<DataStore<Preferences>>()
    val tokenPair = dataStore.data.map {
        it[stringPreferencesKey("access_jwt")] to it[stringPreferencesKey("refresh_jwt")]

    }.collectAsState(initial = "empty_access_jwt_token" to "empty_refresh_jwt_token").value
    val isAlreadyLogin =
        tokenPair.first != "empty_access_jwt_token" && tokenPair.second != "empty_refresh_jwt_token"
    MaterialTheme {
        KoinContext {
            if (isAlreadyLogin) {
                Navigator(HomeScreen())
            } else {
                Navigator(AuthScreenNavigation())
            }
        }
    }
}