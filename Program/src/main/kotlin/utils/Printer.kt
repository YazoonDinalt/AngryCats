package utils

// Вывод карты с котами в консоль
class Print<T> (private val catsMap: Array<Array<T>>){
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
