package app.skypub.project

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import app.skypub.data.repository.InitializeRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initializeRepository: InitializeRepository by inject()
        lifecycleScope.launch {
             initializeRepository.initializeToken()
        }
        setContent {
            KoinContext {
                App()
            }
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}