package org.example.app

import kotlinx.coroutines.*
import org.example.lib.cats.ChannelQueue
import org.example.lib.cats.CatForPresenter
import org.example.lib.cats.createCats
import org.example.lib.cats.Cat
import org.example.lib.cats.Map
import org.example.lib.cats.UpdateStatus
import org.example.lib.utils.IAppConfig

class AppController(
    private val queueCats: ChannelQueue<MutableList<CatForPresenter>>,
    private val config: IAppConfig
) {
    suspend fun run() {
        while (true) {
            if (config.isReady) startProcessing()
            delay(100) // Задержка для предотвращения перегрузки
        }
    }

    private suspend fun startProcessing() {
        val cats = createCats(config.amountCats, config.height, config.width)

        while (config.isReady) {
            processCats(cats)
        }
    }

    private suspend fun processCats(cats: MutableList<Cat>) {
        UpdateStatus(cats, config.r0, config.r1, false)
        val catsForQueue = cats.map { CatForPresenter(it.x, it.y, it.status) }
        queueCats.enqueue(catsForQueue.toMutableList())

        val map = Map(config.width, config.height, cats, false)
        map.moveCats()

        delay(config.time)
    }
}
