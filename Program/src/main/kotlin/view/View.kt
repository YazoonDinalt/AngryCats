package view

import ChannelQueue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import java.awt.Dimension

fun presenter(queueCats: ChannelQueue<MutableList<CatForPresenter>>) = application {
    var navigationStack by remember { mutableStateOf(listOf<Screen>(Screen.FirstScreen)) }
    val currentScreen = navigationStack.last()
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
            when (currentScreen) {
                Screen.FirstScreen ->
                    startScreen {
                        navigationStack = navigationStack + Screen.SecondScreen
                    }
                Screen.SecondScreen ->
                    getConfigData {
                        navigationStack = navigationStack + Screen.ThirdScreen
                        Config.isReady.value = true
                    }
                Screen.ThirdScreen -> mainWindow(queueCats)
            }
        }
    }
}


@Composable
fun startScreen(changeScreen: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 70.dp, end = 70.dp, bottom = 20.dp, top = 350.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text("AngryCats", fontSize = 120.sp, fontWeight = FontWeight.Bold)

        Button(
            onClick = changeScreen,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Go!")
        }
    }
}
