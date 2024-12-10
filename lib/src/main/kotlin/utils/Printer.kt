package org.example.lib.utils

/**

Class for rendering the current map

 */

class Print<T>(private val catsMap: Array<Array<T>>) {
    init {
        print()
    }

    private fun print() {
        println()
        for (y in 0 until catsMap[0].size) {
            for (element in catsMap) {
                print("${element[y]}  ")
            }
            println()
        }
    }
}
