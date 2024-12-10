package org.example.app

import kotlinx.coroutines.*
import org.example.app.view.presenter
import org.example.lib.cats.Cat
import org.example.lib.cats.CatForPresenter
import org.example.lib.cats.ChannelQueue
import org.example.lib.cats.Map
import org.example.lib.cats.UpdateStatus
import org.example.lib.cats.createCats
import org.example.lib.utils.Config

fun main() {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val queueCats = ChannelQueue<MutableList<CatForPresenter>>()

    scope.launch {
        while (true) if (Config.isReady.value) start(queueCats)
    }

    presenter(queueCats)
}

/**

Main function marking the start

 */

suspend fun start(queueCats: ChannelQueue<MutableList<CatForPresenter>>) =
    coroutineScope {
        val cats = createCats(Config.amountCats.value, Config.height.value, Config.width.value)

        while (true) {
            compute(cats, queueCats)
            if (!Config.isReady.value) return@coroutineScope
        }
    }

/**

The function that is responsible for calculations

 */

suspend fun compute(
    cats: MutableList<Cat>,
    queueCats: ChannelQueue<MutableList<CatForPresenter>>,
) {
    UpdateStatus(cats, Config.r0.value, Config.r1.value, false)
    val catsForQueue = mutableListOf<CatForPresenter>()
    cats.forEach { catsForQueue.add(CatForPresenter(it.x, it.y, it.status)) }
    queueCats.enqueue(catsForQueue)
    // Print(Map(Utils.Config.width.value, Utils.Config.height.value, cats, false).visualCatsMap())
    Map(Config.width.value, Config.height.value, cats, false).moveCats()
    delay(Config.time.value)
}
