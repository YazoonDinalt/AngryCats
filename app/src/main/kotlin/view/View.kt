package org.example.app.view

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import cats.BlockingQueue
import cats.CatForPresenter
import cats.Room
import cats.barrierList
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import utils.Config
import java.awt.Dimension

/**

 This composable function is responsible for the window in which the cats will be displayed

*/

fun presenter(queueCats: BlockingQueue<MutableList<CatForPresenter>>) =
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
                    Screen.ThirdScreen -> {
                        barrierList[0] = Room(0,0,Config.width.value+1, Config.height.value+1, 0)
                        val config = Lwjgl3ApplicationConfiguration()
                        config.setTitle("Cat Game")
                        config.setWindowedMode(800, 600)
                        Lwjgl3Application(CatGame(queueCats), config)
                    }
                }
            }
        }
    }
