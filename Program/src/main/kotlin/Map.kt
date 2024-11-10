import kotlin.random.Random

class Map {
    fun createCatsMap(cats: Array<Cat>, height: Int, weight: Int): Array<Array<CatInMap>> {
        val catsMap = Array(weight) { Array(height) { CatInMap(0, Cat(0, 0, Status.None)) } }

        for (cat in cats) {
            if (catsMap[cat.x][cat.y].number != 0) {
                catsMap[cat.x][cat.y].number++
                catsMap[cat.x][cat.y].cat = cat
                catsMap[cat.x][cat.y].cat.status = Status.FIGHT
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



    fun moveCatsMap(cats: Array<Cat>, amountCats: Int, height: Int, weight: Int) {
        val random = Random(SEED.toLong()) // Создаем новый объект Random с заданным SEED
        val idCats = mutableListOf<Int>()

        while (idCats.size < amountCats) {
            val randomIndex = random.nextInt(cats.size)

            if (!idCats.contains(randomIndex)) {
                idCats.add(randomIndex)
            }
        }

        for (idCat in idCats) {
            var randomWeight = 1
            var randomHeight = 1
            if (weight / AMBIT != 0 ) randomWeight = Random.nextInt(weight / AMBIT)
            if (height / AMBIT != 0 ) randomHeight = Random.nextInt(height / AMBIT)
            cats[idCat].x = cats[idCat].x + randomWeight
            cats[idCat].y = cats[idCat].y + randomHeight
            cats[idCat].status = Status.WALK

            if (cats[idCat].x >= weight) {
                cats[idCat].x = cats[idCat].x - weight
            }
            if (cats[idCat].y >= height) {
                cats[idCat].y = cats[idCat].y - height
            }
        }
    }
}