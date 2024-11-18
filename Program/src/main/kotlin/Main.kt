import view.presenter
import kotlinx.coroutines.*

const val SEED = 18
const val AMBIT = 5
fun main() {
    val height = 100
    val weight = 100
    val amountCats = 500
    val r0 = 1.0
    val r1 = 10.0

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val cats = createCats(amountCats, height, weight)
    val queueCats = SynchronizedQueue<Array<Cat>>()
    scope.launch {
        while (true) {
            UpdateStatus(cats, r0, r1)
            Print(Map().visualCatsMap(cats, weight, height))

            Map().moveCats(cats, amountCats, height, weight)
            queueCats.enqueue(cats)
            delay(500L)
            UpdateStatus(cats, r0, r1)

            Print(Map().visualCatsMap(cats, weight, height))

            Map().moveCats(cats, amountCats, height, weight)
            queueCats.enqueue(cats)

            delay(500L)

            UpdateStatus(cats, r0, r1)

            Print(Map().visualCatsMap(cats, weight, height))
        }
    }
    presenter(queueCats)
}
