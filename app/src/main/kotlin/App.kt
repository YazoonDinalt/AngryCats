package org.example.app

import kotlinx.coroutines.*
import org.example.app.view.presenter
import org.example.lib.cats.CatForPresenter
import org.example.lib.cats.ChannelQueue
import org.example.lib.utils.DefaultConfig

/**

Main function marking the start

 */
fun main() {
    // Initial settings
    var config = DefaultConfig
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val queueCats = ChannelQueue<MutableList<CatForPresenter>>()

    // Loading the logic
    scope.launch { AppController(queueCats, config).run() }

    // Loading the representation
    presenter(queueCats, config)
}
