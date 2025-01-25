package org.example.lib.utils

import kotlinx.atomicfu.atomic

/**

 In this object all the data for the DefaultConfig config

 */

object DefaultConfig : IAppConfig {
    override var height: Int by atomic(100)
    override var width: Int by atomic(100)
    override var amountCats: Int by atomic(500)
    override var r0: Double by atomic(1.0)
    override var r1: Double by atomic(10.0)
    override var time: Long by atomic(500L)
    override var isReady: Boolean by atomic(false)
}