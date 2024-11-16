import kotlin.random.Random

class UpdateStatus(private val cats: Array<Cat>, private val r0: Double, private val r1: Double, private val nameDistance: NameDistance = NameDistance.Euclidean) {

    init {
        updateCatStatus()
    }
    private fun updateCatStatus() {
        for (i in cats.indices) {
            for (j in i + 1 until cats.size) {
                val distance = Distance(cats[i], cats[j]).euclideanDistance()
                if (distance <= r0) {
                    cats[i].status = Status.FIGHT
                    cats[j].status = Status.FIGHT

                    println("${cats[i]} дерется ${cats[j]}")

                    cats[i].addFightCats(cats[j])
                    cats[j].addFightCats(cats[i])
                }
            }
        }

        for (i in cats.indices) {
            if (cats[i].status == Status.WALK) {
                for (j in i + 1 until cats.size) {
                    val distance = Distance(cats[i], cats[j]).euclideanDistance()
                    if (distance <= r1 && cats[i].status != Status.FIGHT) {
                        val probability = 1 / distance
                        val randomValue = Random.nextDouble()

                        if (randomValue <= probability) {
                            cats[i].status = Status.HISS
                            cats[i].addHiisCat(cats[j])
                            cats[j].addHiisCat(cats[i])
                            println("${cats[i]} шипит на ${cats[j]}")

                            if (cats[j].status != Status.FIGHT) {
                                cats[j].status = Status.HISS
                                println("${cats[j]} шипит на $cats[i]")
                            }
                        }
                    }
                }
            }
        }
    }
}