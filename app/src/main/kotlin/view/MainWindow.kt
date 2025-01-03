package org.example.app.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.lib.cats.CatForPresenter
import org.example.lib.cats.ChannelQueue
import org.example.lib.utils.Config
import org.example.lib.utils.Translate

/**

 The main screen, which is where the cat interactions are displayed

*/

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun mainWindow(
    queueCats: ChannelQueue<MutableList<CatForPresenter>>,
    backScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var array by remember { mutableStateOf<Array<IntArray>?>(null) }
    var previousArray by remember { mutableStateOf<Array<IntArray>?>(null) }

    LaunchedEffect(queueCats) {
        scope.launch {
            queueCats.asFlow()
                .map { Translate.catsToGrid(it, Config.width.value, Config.height.value) }
                .collect { newArray ->
                    previousArray = array
                    array = newArray
                }
        }
    }

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            if (change.positionChange() != Offset.Zero) change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        },
                    )
                }
                .clipToBounds(),
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(top = 25.dp, start = 25.dp), contentAlignment = Alignment.TopStart) {
            Image(
                modifier =
                    Modifier
                        .padding(bottom = 5.dp, end = 10.dp)
                        .size(60.dp)
                        .onClick { backScreen() },
                painter = painterResource("back.png"),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }

        array?.let { displayArrayCanvas(it, offsetX, offsetY) }
    }
}

fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}

/**

 This function contains the logic for rendering cats

*/

@Composable
fun displayArrayCanvas(
    array: Array<IntArray>,
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    cellSize: Dp = 50.dp,
) {
    val cellWidth = cellSize.toPx(LocalDensity.current)
    val cellHeight = cellSize.toPx(LocalDensity.current)
    val images =
        mapOf(
            1 to painterResource("cats_normal.png"),
            2 to painterResource("fight_cat.png"),
            3 to painterResource("cat_h.png"),
            4 to painterResource("dead_cat.jpg"),
            5 to painterResource("two_cats.png"),
        )
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        contentAlignment = Alignment.Center,
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
                                modifier =
                                    Modifier
                                        .width(cellWidth.dp)
                                        .height(cellHeight.dp)
                                        .offset(
                                            x = (columnIndex * cellWidth + offsetX).dp,
                                            y = (rowIndex * cellHeight + offsetY).dp,
                                        ),
                            )
                        }
                    }
                }
            }
        }
    }
}
