import view.presenter
import kotlinx.coroutines.*
import view.CatForPresenter

const val SEED = 18
const val AMBIT = 5
fun main() {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val queueCats = ChannelQueue<MutableList<CatForPresenter>>()
    var cats = mutableListOf<Cat>()

    scope.launch {
        while(!Config.isReady.value) cats = createCats(Config.amountCats.value, Config.height.value, Config.width.value)

        while (true) {
            if (Config.isReady.value) {
                UpdateStatus(cats, Config.r0.value, Config.r1.value)
                val catsForQueue = mutableListOf<CatForPresenter>()
                cats.forEach { catsForQueue.add(CatForPresenter(it.x, it.y, it.status)) }
                queueCats.enqueue(catsForQueue)
                Print(Map(Config.width.value, Config.height.value, cats).visualCatsMap())
                Map(Config.width.value, Config.height.value, cats).moveCats()
                delay(500L)
            }
        }
    }

    presenter(queueCats)
}

