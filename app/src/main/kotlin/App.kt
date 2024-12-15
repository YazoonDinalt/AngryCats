package org.example.app

import cats.*
import cats.Map
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.example.app.view.CatGame
import org.example.app.view.presenter
import utils.Config
import utils.Config.height
import utils.Config.width
import kotlin.random.Random

fun main() {
    val queueCats = BlockingQueue<MutableList<CatForPresenter>>(10)
    for (i in 0 until 5) generateBarrier()

    Thread { if (Config.isReady.value) start(queueCats) }.start()
    presenter(queueCats)
//    val config = Lwjgl3ApplicationConfiguration()
//    config.setTitle("Cat Game")
//    config.setWindowedMode(800, 600)
//    Lwjgl3Application(CatGame(queueCats), config)
}

/**

Main function marking the start

 */

fun start(queueCats: BlockingQueue<MutableList<CatForPresenter>>) {
    val cats = createCats(Config.amountCats.value, height.value, width.value)
    val gameMap = Map(width.value, height.value, cats, false)
    while (true) {
        compute(cats, queueCats, gameMap)
        if (!Config.isReady.value) return
    }
}

/**

The function that is responsible for calculations

 */

fun compute(
    cats: MutableList<Cat>,
    queueCats: BlockingQueue<MutableList<CatForPresenter>>,
    gameMap: Map,
) {
    UpdateStatus(cats, Config.r0.value, Config.r1.value, false)
    val catsForQueue = mutableListOf<CatForPresenter>()
    cats.forEach { catsForQueue.add(CatForPresenter(it.x, it.y, it.status)) }
    queueCats.enqueue(catsForQueue)
    gameMap.moveCats()
    Thread.sleep(Config.time.value)
}

fun generateBarrier() {
    if (width.value * height.value > 100) {
        val maxBarrierWidth = width.value / 4
        val maxBarrierHeight = height.value / 4
        val leftX = Random.nextInt(0, width.value - maxBarrierWidth)
        val barrierWidth = Random.nextInt(0, maxBarrierWidth + 1)
        val rightX = leftX + barrierWidth
        val barrierHeight = Random.nextInt(0, maxBarrierHeight + 1)
        val leftY = leftX +  barrierHeight
        val rightY = rightX + barrierHeight
        roomNumber++
        barrierList.add(Room(leftX, leftY, rightX, rightY, roomNumber))
    }
}
