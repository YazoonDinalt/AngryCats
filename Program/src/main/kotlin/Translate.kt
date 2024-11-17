
object Translate {

    fun translateCatMapIn2dArray(catsMap: Array<Array<CatInMap>>) : Array<IntArray> {
        val visCatsMap = Array(catsMap.size) { IntArray(catsMap[0].size) { 0 } }

        for (i in catsMap.indices) {
            for (j in 0 until catsMap[i].size) {
                if (catsMap[i][j].number == 0) {
                    continue
                }

                when (catsMap[i][j].cat.status) {
                    Status.FIGHT -> visCatsMap[i][j] = 2
                    Status.WALK -> visCatsMap[i][j] = 3
                    Status.HISS -> visCatsMap[i][j] = 1
                    else -> println("WTF")
                }
            }
        }

        return visCatsMap
    }

}