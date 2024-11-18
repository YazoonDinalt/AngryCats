import kotlinx.atomicfu.atomic

object Config {
    val height = atomic(100)
    val width = atomic(100)
    val amountCats = atomic(500)
    val r0 = atomic(1.0)
    val r1 = atomic(10.0)
    val isReady = atomic(false)
}