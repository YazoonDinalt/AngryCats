package utils

import kotlinx.atomicfu.atomic

/**

 In this object all the data for the startup is stored

 */

object Config {
    val height = atomic(10)
    val width = atomic(10)
    val amountCats = atomic(500)
    val r0 = atomic(1.0)
    val r1 = atomic(10.0)
    val time = atomic(500L)
    val isReady = atomic(true)
}
