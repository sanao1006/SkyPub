package app.skypub.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.skypub.feature.auth.AuthViewModel
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                val viewmodel: AuthViewModel = koinInject<AuthViewModel>()
                App()
            }
        }
    }
}

private fun initKoin() {
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}