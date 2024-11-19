package view

import CatForPresenter
import ChannelQueue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.Dimension

fun presenter(queueCats: ChannelQueue<MutableList<CatForPresenter>>) = application {
    var showSecondScreen by remember { mutableStateOf(false) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "AngryCats",
        state = rememberWindowState(
            position = WindowPosition(alignment = Alignment.Center),
            size = DpSize(1200.dp, 1200.dp),
        ),
    ) {
        window.minimumSize = Dimension(800, 800)
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                surface = Color(red = 235, green = 235, blue = 237)
            )
        ) {
            if (!showSecondScreen) {
                getConfigData {
                    showSecondScreen = true
                    Config.isReady.value = true
                }
            } else {
                mainWindow(queueCats)
            }
        }
    }
}
