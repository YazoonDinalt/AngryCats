import kotlin.random.Random

class UpdateStatus(private val cats: MutableList<Cat>, private val r0: Double, private val r1: Double, private val nameDistance: NameDistance = NameDistance.Euclidean) {

    init {
        updateCatStatus()
    }
    
    private fun distance(cat1: Cat, cat2: Cat): Float {
        return when (nameDistance) {
            NameDistance.Euclidean -> Distance(cat1, cat2).euclideanDistance()
            NameDistance.Manhattan -> Distance(cat1, cat2).manhattanDistance()
            NameDistance.Chebyshev -> Distance(cat1, cat2).chebyshevDistance()
            NameDistance.Curvilinear -> Distance(cat1, cat2).curvilinearDistance()
        }
    }

    private fun breedingForWar(cat: Cat) {
        cat.status = Status.FIGHT
        if (cat.getNeighboringBreending().isEmpty()) {
            return
        } else {
            for (c in cat.getNeighboringBreending()) {
                c.removeNeighboringBreending(cat)
            }

            for (c in cat.getNeighboringBreending()) {
                breedingForWar(c)
            }
        }

    }
    
    private fun updateBreeding() {
        val catsByLocation = cats.groupBy { it.x to it.y }

        for ((_, catsAtLocation) in catsByLocation) {
            val males = catsAtLocation.filter { it.sex == Sex.Male }
            val females = catsAtLocation.filter { it.sex == Sex.Female }

            if (males.isEmpty() || females.isEmpty()) {
                continue
            }

            males.forEach { it.status = Status.BREENDING }
            females.forEach { it.status = Status.BREENDING }

            for ((_, catsAtLocation2) in catsByLocation) {
                if (catsAtLocation2 != catsAtLocation) {
                    val males2 = catsAtLocation2.filter { it.sex == Sex.Male }
                    val females2 = catsAtLocation2.filter { it.sex == Sex.Female }

                    if (males2.isEmpty() || females2.isEmpty()) {
                        continue
                    }

                    if (distance(catsAtLocation[0], catsAtLocation2[0]) <= r0) {
                        for (cat in catsAtLocation2) {
                            catsAtLocation[0].addNeighboringBreending(cat)
                        }
                    }
                }
            }
        }
    }
    
    private fun updateFight() {
        for (i in cats.indices) {
            val currentCat = cats[i]
            if (currentCat.status != Status.DEAD) {
                for (j in i + 1 until cats.size) {
                    val otherCat = cats[j]
                    if (otherCat.status != Status.DEAD) {
                        if ((currentCat.status != Status.BREENDING || otherCat.status != Status.BREENDING) && currentCat != otherCat) {
                            val distance = distance(currentCat, otherCat)
                            if (distance <= r0) {
                                if (currentCat.status == Status.BREENDING) {

                                    for (cat in currentCat.getNeighboringBreending()) {
                                        cat.removeNeighboringBreending(currentCat)
                                    }

                                    for (cat in currentCat.getNeighboringBreending()) {
                                        breedingForWar(cat)
                                    }
                                }
                                if (otherCat.status == Status.BREENDING) {

                                    for (cat in otherCat.getNeighboringBreending()) {
                                        cat.removeNeighboringBreending(otherCat)
                                    }

                                    for (cat in otherCat.getNeighboringBreending()) {
                                        breedingForWar(cat)
                                    }
                                }

                                currentCat.status = Status.FIGHT
                                otherCat.status = Status.FIGHT


                                if (currentCat.x == otherCat.x && currentCat.y == otherCat.y) {
                                    println("Коты в x = ${currentCat.x}, y = ${currentCat.y} дерутся")
                                } else {
                                    println("$currentCat дерется c $otherCat")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private fun updateHiss() {
        for (i in cats.indices) {
            val currentCat = cats[i]
            if (currentCat.status == Status.WALK) {
                for (j in i + 1 until cats.size) {
                    val otherCat = cats[j]
                    val distance = distance(currentCat, otherCat)
                    if (distance <= r1 && currentCat.status != Status.FIGHT) {
                        val probability = 1 / distance
                        val randomValue = Random.nextDouble()

                        if (randomValue <= probability) {
                            currentCat.status = Status.HISS
                            currentCat.addHiisCat(otherCat)
                            otherCat.addHiisCat(currentCat)
                            println("$currentCat шипит на $otherCat")

                            if (otherCat.status == Status.WALK) {
                                otherCat.status = Status.HISS
                                println("$otherCat шипит на $currentCat")
                            }
                            break
                        }
                    }
                }
            }
        }
    }

    private fun updateCatStatus() {

        updateBreeding()
        
        updateFight()

        updateHiss()

    }
}