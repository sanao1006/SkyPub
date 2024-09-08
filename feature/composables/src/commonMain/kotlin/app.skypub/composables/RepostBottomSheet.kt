package app.skypub.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun a() {
    var openModal by rememberSaveable { mutableStateOf(false) }
    if (openModal) RepostBottomSheet(onDismiss = { openModal = it })
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RepostBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: (Boolean) -> Unit,
    onclick: (RepostItem) -> Unit = {}
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss(false) },
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RepostItem.entries.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        onDismiss(false)
                        onclick(it)
                    }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = it.icon, "")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it.value,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

enum class RepostItem(val value: String, val icon: ImageVector) {
    REPOST("Repost", Icons.Default.Repeat),
    QUOTE("Quote", Icons.Default.FormatQuote),
}
