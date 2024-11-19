import cats.Cat
import cats.ChannelQueue
import cats.UpdateStatus
import cats.createCats
import utils.Config
import view.presenter
import kotlinx.coroutines.*
import view.CatForPresenter

const val SEED = 18
const val AMBIT = 5
fun main() {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val queueCats = ChannelQueue<MutableList<CatForPresenter>>()

    scope.launch {
        while (true) if (Config.isReady.value) start(queueCats)
    }

    presenter(queueCats)
}

suspend fun start(queueCats: ChannelQueue<MutableList<CatForPresenter>>) = coroutineScope{
    val cats = createCats(Config.amountCats.value, Config.height.value, Config.width.value)

    while (true) {
        compute(cats, queueCats)
        if (!Config.isReady.value) return@coroutineScope
    }
}

suspend fun compute(cats: MutableList<Cat>, queueCats: ChannelQueue<MutableList<CatForPresenter>>) {
    UpdateStatus(cats, Config.r0.value, Config.r1.value, false)
    val catsForQueue = mutableListOf<CatForPresenter>()
    cats.forEach { catsForQueue.add(CatForPresenter(it.x, it.y, it.status)) }
    queueCats.enqueue(catsForQueue)
   //Print(Map(Utils.Config.width.value, Utils.Config.height.value, cats, false).visualCatsMap())
    Map(Config.width.value, Config.height.value, cats, false ).moveCats()
    delay(Config.time.value)
}
