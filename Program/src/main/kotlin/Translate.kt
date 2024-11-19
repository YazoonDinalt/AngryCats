object Translate {

    fun catsToGrid(cats: MutableList<CatForPresenter>, gridWidth: Int, gridHeight: Int): Array<IntArray> {
        val grid = Array(gridHeight) { IntArray(gridWidth) { 0 } }
        cats.forEach { cat ->
            if (cat.x in 0 until gridWidth && cat.y in 0 until gridHeight) {
                grid[cat.y][cat.x] = when (cat.status) {
                    Status.WALK -> 1
                    Status.FIGHT -> 2
                    Status.HISS -> 3
                    Status.DEAD -> 4
                    Status.BREENDING -> 5
                }
            }

        }
        grid.forEach {
            it.forEach { digit -> print("$digit ") }
            println()
        }
        return grid
    }

}