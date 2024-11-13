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
        val numCats =
            if (amountCats > 250) 250
            else 2

        while (idCats.size < numCats) {
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

        if (distance / AMBIT >= 2 && randomOp < 0.5) {
            move = - Random.nextInt(1, distance / AMBIT)
        } else if ((distance / AMBIT >= 2 && randomOp > 0.5)) {
            move = Random.nextInt(1, distance / AMBIT)
        }

        return move

    }
    fun moveCatsMap(catsMap: Array<Array<CatInMap>>, cats: Array<Cat>, amountCats: Int, height: Int, weight: Int, r0: Float, r1: Float) {
        val idCats = mixingCats(cats, amountCats)
        for (idCat in idCats) {
            val currentCat = cats[idCat]
            val fightCats = catsMap[currentCat.x][currentCat.y].cat.getFightCats().toList()
            val hissCats = catsMap[currentCat.x][currentCat.y].cat.getHissCats().toList()

            print("${cats[idCat]} ушел в ")

            val startCat = Cat(cats[idCat].x, cats[idCat].y, cats[idCat].status)

            catsMap[cats[idCat].x][cats[idCat].y].number--
            if (catsMap[cats[idCat].x][cats[idCat].y].number == 1 && catsMap[cats[idCat].x][cats[idCat].y].cat.getFightCats().isEmpty()) {
                catsMap[cats[idCat].x][cats[idCat].y].cat.status = Status.WALK
            } else if (catsMap[cats[idCat].x][cats[idCat].y].number >= 1) {
                catsMap[cats[idCat].x][cats[idCat].y].cat.status = Status.FIGHT
            } else if (catsMap[cats[idCat].x][cats[idCat].y].number == 0) {
                catsMap[cats[idCat].x][cats[idCat].y].cat.status = Status.None
            }

            val moveX = move(weight)
            val moveY = move(height)

            cats[idCat].x = cats[idCat].x + moveX
            cats[idCat].y = cats[idCat].y + moveY
            cats[idCat].status = Status.WALK

            if (cats[idCat].x >= weight) {
                cats[idCat].x = cats[idCat].x - weight
            } else if (cats[idCat].x < 0) {
                cats[idCat].x = cats[idCat].x + weight
            }

            if (cats[idCat].y >= height) {
                cats[idCat].y = cats[idCat].y - height
            } else if (cats[idCat].y < 0) {
                cats[idCat].y = cats[idCat].y + height
            }

            println("x = ${cats[idCat].x}, y = ${cats[idCat].y}")

            catsMap[cats[idCat].x][cats[idCat].y].number++
            if (catsMap[cats[idCat].x][cats[idCat].y].number > 1) {
                catsMap[cats[idCat].x][cats[idCat].y].cat.status = Status.FIGHT
                cats[idCat].status = Status.FIGHT
                println("${cats[idCat]} Дерется в клетке")
            } else if (catsMap[cats[idCat].x][cats[idCat].y].number == 1) {
                catsMap[cats[idCat].x][cats[idCat].y].cat.status = Status.WALK
                cats[idCat].status = Status.WALK
            }

            if (fightCats.isNotEmpty() || hissCats.isNotEmpty()) {
                if (catsMap[startCat.x][startCat.y].number == 0) {
                    for (cat in fightCats) {
                        println("$cat       ${cat.getFightCats()}")
                        println(cats[idCat])
                        cats[idCat].removeFightCats(cat)
                        cat.removeFightCats(cats[idCat])
                        cat.removeFightCats(startCat)

                        if (cat.getFightCats().isEmpty()) catsMap[cat.x][cat.y].cat.status = Status.WALK
                    }

                    for (cat in hissCats) {
                        cats[idCat].removeHissCats(cat)
                        cat.removeHissCats(cats[idCat])
                        if (cat.getHissCats().isEmpty() && cat.status != Status.FIGHT) cat.status = Status.WALK
                    }
                }
            }

            UpdateStatus(catsMap, r0, r1).addStatus(r0, catsMap[cats[idCat].x][cats[idCat].y].cat, Status.FIGHT)
            UpdateStatus(catsMap, r0, r1).addStatus(r1, catsMap[cats[idCat].x][cats[idCat].y].cat, Status.HISS)
            cats[idCat].status = catsMap[cats[idCat].x][cats[idCat].y].cat.status
        }
    }
}