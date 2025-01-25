package org.example.lib.utils

/**

 Interface for processing and working with configs and ability to add multiple configs

 */
interface IAppConfig {
    var height: Int
    var width: Int
    var amountCats: Int
    var r0: Double
    var r1: Double
    var time: Long
    var isReady: Boolean
}