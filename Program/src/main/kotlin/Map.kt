import kotlin.collections.Map
import kotlin.random.Random

class Map {
    fun createCatsMap(cats: Array<Cat>, height: Int, weight: Int): Array<Array<CatInMap>> {
        val catsMap = Array(weight) { Array(height) { CatInMap(0, Cat(0, 0, Status.None)) } }

        for (cat in cats) {
            if (catsMap[cat.x][cat.y].number != 0) {
                catsMap[cat.x][cat.y].number++
                catsMap[cat.x][cat.y].cat.status = Status.FIGHT
                catsMap[cat.x][cat.y].cat = cat
                cat.status = Status.FIGHT
                println("Коты в x = ${cat.x} y = ${cat.y} дерутся")
                continue
            }

            catsMap[cat.x][cat.y].number++
            catsMap[cat.x][cat.y].cat = cat
        }

        for (y in 0 until height) {
            for (x in 0 until weight) {
                if (catsMap[x][y].number == 0) {
                    catsMap[x][y].cat = Cat(x, y, Status.None)
                }
            }
        }

        return catsMap
    }

    fun visualCatsMap(catsMap: Array<Array<CatInMap>>): Array<Array<String>> {
        val visCatsMap = Array(catsMap.size) { Array(catsMap[0].size) { "0" } }

        for (i in catsMap.indices) {
            for (j in 0 until catsMap[i].size) {
                if (catsMap[i][j].number == 0) {
                    continue
                }

                when (catsMap[i][j].cat.status) {
                    Status.FIGHT -> visCatsMap[i][j] = "F"
                    Status.WALK -> visCatsMap[i][j] = "W"
                    Status.HISS -> visCatsMap[i][j] = "H"
                    else -> println("WTF")
                }
            }
        }

        return visCatsMap
    }

    private fun mixingCats(cats: Array<Cat>, amountCats: Int): MutableList<Int> {
        val random = Random(SEED) // Создаем новый объект Random с заданным SEED
        val idCats = mutableListOf<Int>()

        while (idCats.size < amountCats) {
            val randomIndex = random.nextInt(cats.size)

            if (!idCats.contains(randomIndex)) {
                idCats.add(randomIndex)
            }
        }

        return idCats

    }

    private fun move(distance: Int): Int {
        val random = Random(SEED) // Создаем новый объект Random с заданным SEED
        val randomOp = random.nextDouble()

        var move =
            if (randomOp < 0.5) -1
            else 1

        if (distance / AMBIT >= 2) {
            move = if (randomOp < 0.5) {
                -Random.nextInt(1, 1)
            } else {
                Random.nextInt(1, distance / AMBIT)
            }
        }

        return move

    }
    fun moveCatsMap(cats: Array<Cat>, amountCats: Int, height: Int, weight: Int) {
        val idCats = mixingCats(cats, amountCats)
        
        for (idCat in idCats) {
            val currentCat = cats[idCat]
            print("$currentCat ушел в ")

            currentCat.x = currentCat.x + move(weight)
            currentCat.y = currentCat.y + move(height)
            currentCat.status = Status.WALK

            if (currentCat.x >= weight) {
                currentCat.x = currentCat.x - weight
            } else if ((currentCat.x < 0)) {
                currentCat.x = currentCat.x + weight
            }


            if (currentCat.y >= height) {
                currentCat.y = currentCat.y - height
            } else if ((currentCat.y < 0)) {
                currentCat.y = currentCat.y + height
            }

            println("x = ${currentCat.x}, y = ${currentCat.y}")
        }
    }
}