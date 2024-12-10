package org.example.app.view

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.example.lib.cats.CatForPresenter
import org.example.lib.cats.ChannelQueue
import org.example.lib.utils.Config
import java.awt.Dimension

/**

 This composable function is responsible for the window in which the cats will be displayed

*/

fun presenter(queueCats: ChannelQueue<MutableList<CatForPresenter>>) =
    application {
        var navigationStack by remember { mutableStateOf(listOf<Screen>(Screen.FirstScreen)) }
        val currentScreen = navigationStack.last()
        Window(
            onCloseRequest = ::exitApplication,
            title = "AngryCats",
            state =
                rememberWindowState(
                    position = WindowPosition(alignment = Alignment.Center),
                    size = DpSize(1200.dp, 1200.dp),
                ),
        ) {
            window.minimumSize = Dimension(800, 800)
            MaterialTheme(
                colorScheme =
                    MaterialTheme.colorScheme.copy(
                        surface = Color(red = 235, green = 235, blue = 237),
                    ),
            ) {
                when (currentScreen) {
                    Screen.FirstScreen ->
                        startScreen {
                            navigationStack += Screen.SecondScreen
                        }
                    Screen.SecondScreen ->
                        getConfigData {
                            navigationStack += Screen.ThirdScreen
                            Config.isReady.value = true
                        }
                    Screen.ThirdScreen ->
                        mainWindow(queueCats) {
                            navigationStack -= Screen.ThirdScreen
                            Config.isReady.value = false
                        }
                }
            }
        }
    }
