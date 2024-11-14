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

    private fun updatingInteractions(catsMap: Array<Array<CatInMap>>, fightCats: MutableList<Cat>, hissCats: MutableList<Cat>, currentCat: Cat, startCat: Cat) {
        for (fightCat in fightCats.toList()) {
            currentCat.removeFightCats(fightCat)
            fightCat.removeFightCats(currentCat)
            fightCat.removeFightCats(startCat)

            if (fightCat.getFightCats().isEmpty())
                catsMap[fightCat.x][fightCat.y].cat.status = Status.WALK
            fightCat.status = Status.WALK
        }

        for (hissCat in hissCats) {
            currentCat.removeHissCats(hissCat)
            hissCat.removeHissCats(currentCat)
            hissCat.removeHissCats(startCat)

            if (hissCat.getHissCats().isEmpty() && hissCat.status != Status.FIGHT)
                catsMap[hissCat.x][hissCat.y].cat.status = Status.WALK
            hissCat.status = Status.WALK
        }
    }
    fun moveCatsMap(catsMap: Array<Array<CatInMap>>, cats: Array<Cat>, amountCats: Int, height: Int, weight: Int, r0: Float, r1: Float) {
        val idCats = mixingCats(cats, amountCats)
        for (idCat in idCats) {
            val currentCat = cats[idCat]
            val currentCatInMap = catsMap[currentCat.x][currentCat.y]
            val startCat = Cat(currentCat.x, currentCat.y, currentCat.status)
            val fightCats = currentCatInMap.cat.getFightCats()
            val hissCats = currentCatInMap.cat.getHissCats()

            print("$currentCat ушел в ")

            currentCatInMap.number--
            if (currentCatInMap.number == 1 && fightCats.isEmpty()) {
                currentCatInMap.cat.status = Status.WALK
            } else if (currentCatInMap.number >= 1) {
                currentCatInMap.cat.status = Status.FIGHT
            } else if (currentCatInMap.number == 0) {
                currentCatInMap.cat.status = Status.None
            }

            currentCat.x = currentCat.x + move(weight)
            currentCat.y = currentCat.y + move(height)
            currentCat.status = Status.WALK

            if (currentCat.x >= weight) {
                currentCat.x = currentCat.x - weight
            } else if (currentCat.x < 0) {
                currentCat.x = currentCat.x + weight
            }

            if (currentCat.y >= height) {
                currentCat.y = currentCat.y - height
            } else if (currentCat.y < 0) {
                currentCat.y = currentCat.y + height
            }

            println("x = ${currentCat.x}, y = ${currentCat.y}")

            val moveCurrentCatInMap = catsMap[currentCat.x][currentCat.y]

            moveCurrentCatInMap.number++
            if (moveCurrentCatInMap.number > 1) {
                moveCurrentCatInMap.cat.status = Status.FIGHT
                currentCat.status = Status.FIGHT
                println("$currentCat дерется в своей клетке")
            } else if (moveCurrentCatInMap.number == 1) {
                moveCurrentCatInMap.cat.status = Status.WALK
                currentCat.status = Status.WALK
            }

            if (fightCats.isNotEmpty() || hissCats.isNotEmpty()) {
                if (currentCatInMap.number == 0) {
                    updatingInteractions(catsMap, fightCats, hissCats, currentCat, startCat)
                }
            }

            UpdateStatus(catsMap, r0, r1).addStatus(r0, catsMap[currentCat.x][currentCat.y].cat, Status.FIGHT)
            UpdateStatus(catsMap, r0, r1).addStatus(r1, catsMap[currentCat.x][currentCat.y].cat, Status.HISS)
            currentCat.status = catsMap[currentCat.x][currentCat.y].cat.status
        }
    }
}