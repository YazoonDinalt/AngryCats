import kotlin.random.Random

class UpdateStatus(private val catsMap: Array<Array<CatInMap>>, private val r0: Double, private val r1: Double, private val nameDistance: NameDistance = NameDistance.Euclidean) {

    fun updateStatus() {
        check(r0, Status.FIGHT)
        check(r1, Status.HISS)
    }

    private fun check(r: Double, status: Status) {
        // Проходим по всем котам в catsMap
        for (i in catsMap.indices) {
            for (j in 0 until catsMap[i].size) {
                // Проверяем, что текущий кот не {0, ...}
                if (catsMap[i][j].number != 0) {
                    val currentCat = catsMap[i][j].cat
                    addStatus(r, currentCat, status)
                }
            }
        }
    }

    fun addStatus(r: Double, cat: Cat, status: Status) {
        var leftStart = cat.x - r0.toInt() - 1
        var rightStart = cat.x + r0.toInt() + 1

        if (leftStart < 0) {
            leftStart = 0
        }
        if (rightStart >= catsMap.size) {
            rightStart = catsMap.size - 1
        }

        var startY = cat.y - r0.toInt()
        if (startY < 0) { startY = 0 }

        for (y in startY until catsMap[leftStart].size) {
            var c = -1
            var x = leftStart
            val right = rightStart

            if (y >= catsMap[x].size) {
                break
            }

            while (x <= right) {
                val distance =
                    when (nameDistance) {
                        NameDistance.Euclidean ->
                            Distance(catsMap[x][y].cat, cat).euclideanDistance()

                        NameDistance.Manhattan ->
                            Distance(catsMap[x][y].cat, cat).manhattanDistance()

                        NameDistance.Chebyshev ->
                            Distance(catsMap[x][y].cat, cat).chebyshevDistance()

                        NameDistance.Curvilinear ->
                            Distance(catsMap[x][y].cat, cat).curvilinearDistance()
                    }

                // Проверяем, что не текущий кот и что расстояние не превышает r0
                if (x == cat.x && y == cat.y || distance > r) {
                    x++
                    continue
                }

                // Добавляем статус в зависимости от типа
                when (status) {
                    Status.FIGHT -> checkFight(cat, x, y)
                    Status.HISS -> checkHiss(cat, x, y, distance)
                    else -> {
                        println("WTF")
                    }
                }

                c++
                x++
            }

            if (c < 0) {
                break
            }
        }
    }

    private fun checkFight(cat: Cat, x: Int, y: Int) {
        if (catsMap[x][y].number != 0) {
            catsMap[x][y].cat.status = Status.FIGHT
            cat.status = Status.FIGHT
            println("$cat дерется с ${catsMap[x][y].cat}")
        }
    }

    private fun checkHiss(cat: Cat, x: Int, y: Int, distance: Float) {
        if (catsMap[x][y].number != 0 && cat.status == Status.WALK) {
            // Вероятность
            val probability = 1 / distance

            // Генерируем случайное число от 0 до 1
            val randomValue = Random.nextDouble()

            if (randomValue <= probability) {
                cat.status = Status.HISS
                println("$cat шипит на ${catsMap[x][y].cat}")
                if (catsMap[x][y].cat.status != Status.FIGHT) {
                    catsMap[x][y].cat.status = Status.HISS
                    println("${catsMap[x][y].cat} шипит на $cat")
                }
            }
        }
    }
}