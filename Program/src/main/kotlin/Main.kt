import view.presenter
import kotlinx.coroutines.*

const val SEED = 18
const val AMBIT = 5
fun main() {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val queueCats = SynchronizedQueue<MutableList<Cat>>()
    var cats = mutableListOf<Cat>()

    scope.launch {
        while(!Config.isReady.value) cats = createCats(Config.amountCats.value, Config.height.value, Config.width.value)

        while (true) {
            if (Config.isReady.value) {
                UpdateStatus(cats, Config.r0.value, Config.r1.value)
                val catsForQueue = mutableListOf<Cat>()
                cats.forEach { catsForQueue.add(Cat(it.x, it.y, it.sex, it.age, it.status)) }
                queueCats.enqueue(catsForQueue.toMutableList())
                Print(Map(Config.width.value, Config.height.value, cats).visualCatsMap())
                Map(Config.width.value, Config.height.value, cats).moveCats()
                delay(500L)
            }
        }
    }

    presenter(queueCats)
}


