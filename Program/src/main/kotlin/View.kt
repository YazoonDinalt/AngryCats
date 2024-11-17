import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.awt.Dimension


fun presenter(queueCats: SynchronizedQueue<Array<Array<CatInMap>>>) = application {
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
            mainWindow(queueCats)
        }
    }
}

@Composable
fun mainWindow(queueCats: SynchronizedQueue<Array<Array<CatInMap>>>) {
    val scope = rememberCoroutineScope()
    var array by remember { mutableStateOf<Array<IntArray>?>(null) }
    var previousArray by remember { mutableStateOf<Array<IntArray>?>(null) }
    LaunchedEffect(queueCats) {
        scope.launch {
            queueCats.asFlow()
                .map { it.let { Translate.translateCatMapIn2dArray(it) } }
                .collect { newArray ->
                    previousArray = array
                    array = newArray
                }
        }
    }


    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(modifier = Modifier
        .fillMaxSize().
        pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, dragAmount ->
                    if (change.positionChange() != Offset.Zero) change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            )
        }
        .clipToBounds()
    ) {
        array?.let { displayArrayCanvas(it, offsetX, offsetY) } ?: displayPlaceholder()
    }
}

fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}

@Composable
fun displayPlaceholder(){
}

@Composable
fun displayArrayCanvas(array: Array<IntArray>, offsetX: Float = 0f, offsetY: Float = 0f, cellSize: Dp = 50.dp) {
    val cellWidth = cellSize.toPx(LocalDensity.current)
    val cellHeight = cellSize.toPx(LocalDensity.current)
    val images = mapOf(
        1 to painterResource("cat_h.png"),
        2 to painterResource("fight_cat.png"),
        3 to painterResource("cats_normal.png"),
        4 to painterResource("dead_cat.jpg"),
        5 to painterResource("two_cats.png")
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Column {
            array.forEachIndexed { rowIndex, row ->
                Row {
                    row.forEachIndexed { columnIndex, cell ->
                        val painter = images.getOrDefault(cell, null)
                        if (painter != null) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(cellWidth.dp)
                                    .height(cellHeight.dp)
                                    .offset(
                                        x = (columnIndex * cellWidth + offsetX).dp,
                                        y = (rowIndex * cellHeight + offsetY).dp
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

