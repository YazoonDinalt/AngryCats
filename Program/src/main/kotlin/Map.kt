import kotlin.collections.Map
import kotlin.random.Random

class Map {
    fun visualCatsMap(cats: Array<Cat>, weight: Int, height: Int): Array<Array<String>> {
        val visCatsMap = Array(weight) { Array(height) { "0" } }

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.FIGHT -> visCatsMap[cats[i].x][cats[i].y] = "F"
                Status.WALK -> visCatsMap[cats[i].x][cats[i].y] = "W"
                Status.HISS -> visCatsMap[cats[i].x][cats[i].y] = "H"
                else -> println("WTF")
            }
        }

        return visCatsMap
    }

    private fun move(distance: Int): Int {
        val random = Random(SEED) // Создаем новый объект Random с заданным SEED
        val randomOp = random.nextDouble()

        var move =
            if (randomOp < 0.5) -1
            else 1

        if (distance / AMBIT >= 2) {
            move = if (randomOp < 0.5) {
                -Random.nextInt(1, distance / AMBIT)
            } else {
                Random.nextInt(1, distance / AMBIT)
            }
        }

        return move

    }
    fun moveCats(cats: Array<Cat>, amountCats: Int, height: Int, weight: Int) {
        for (cat in cats) {
            print("$cat ушел в ")

            cat.x = cat.x + move(weight)
            cat.y = cat.y + move(height)
            cat.status = Status.WALK

            if (cat.x >= weight) {
                cat.x = cat.x - weight
            } else if ((cat.x < 0)) {
                cat.x = cat.x + weight
            }

            if (cat.y >= height) {
                cat.y = cat.y - height
            } else if ((cat.y < 0)) {
                cat.y = cat.y + height
            }

            println("x = ${cat.x}, y = ${cat.y}")

            for (fightCat in cat.getFightCats()) {
                fightCat.removeFightCat(cat)

                if (fightCat.getFightCats().isEmpty()) {
                    fightCat.status = Status.WALK
                }
            }
            cat.removeAllFightCats()

            for (hissCat in cat.getHissCats()) {
                hissCat.removeHissCat(cat)

                if (hissCat.getHissCats().isEmpty() && hissCat.status != Status.FIGHT) {
                    hissCat.status = Status.WALK
                }
            }
            cat.removeAllHissCats()
        }
    }
}



