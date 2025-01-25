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
import org.example.lib.utils.DefaultConfig
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
    // Initial settings of scope and arrays of cats
    val scope = rememberCoroutineScope()
    var array by remember { mutableStateOf<Array<IntArray>?>(null) }
    var previousArray by remember { mutableStateOf<Array<IntArray>?>(null) }

    // Launch an effect that listen the changes of cats' actions
    LaunchedEffect(queueCats) {
        scope.launch {
            queueCats.asFlow()
                .map { Translate.catsToGrid(it, DefaultConfig.width, DefaultConfig.height) }
                .collect { newArray ->
                    previousArray = array
                    array = newArray
                }
        }
    }

    // Variables for dragging the screen
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Main view box
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Processing the drag action
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
        // Get Back button top left
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

        // If the cat's array is not empty, then display
        array?.let { displayArrayCanvas(it, offsetX, offsetY) }
    }
}

/**

 Converts a value in Dp (Density-independent Pixels) to pixels (px) based on the screen density.

 */
fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}

/**

 This function contains the logic for rendering cats on grid on the screen, where each cell
 in the grid corresponds to a cat is represented by an image based on its state.

*/
@Composable
fun displayArrayCanvas(
    array: Array<IntArray>,
    offsetX: Float = 0f,
    offsetY: Float = 0f,
    cellSize: Dp = 50.dp,
) {
    // Initial settings
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

    // Rendering in a form of table
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
