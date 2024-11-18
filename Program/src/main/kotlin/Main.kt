import view.presenter
import kotlinx.coroutines.*

const val SEED = 18
const val AMBIT = 5
fun main() {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val queueCats = SynchronizedQueue<Array<Cat>>()
    var cats = arrayOf<Cat>()

    scope.launch {
        while(!Config.isReady.value) cats = createCats(Config.amountCats.value, Config.height.value, Config.width.value)

        while (true) {
            if (Config.isReady.value) startCalculate(cats, queueCats)
        }
    }

    presenter(queueCats)
}

suspend fun startCalculate(cats: Array<Cat>, queueCats: SynchronizedQueue<Array<Cat>>) {
    UpdateStatus(cats, Config.r0.value, Config.r1.value)
    Print(Map().visualCatsMap(cats,  Config.width.value, Config.height.value))
    Map().moveCats(cats, Config.amountCats.value, Config.height.value,  Config.width.value)
    queueCats.enqueue(cats)
    delay(500L)
}