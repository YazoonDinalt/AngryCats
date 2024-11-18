import kotlin.random.Random

class Map (private val width: Int, private val height: Int, private val cats: MutableList<Cat>){
    fun visualCatsMap(): Array<Array<String>> {
        val visCatsMap = Array(width + 1) { Array(height) { "0" } }

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.FIGHT -> visCatsMap[cats[i].x][cats[i].y] = "F"
                Status.WALK -> visCatsMap[cats[i].x][cats[i].y] = "W"
                Status.HISS -> visCatsMap[cats[i].x][cats[i].y] = "H"
                Status.DEAD -> visCatsMap[width][height - 1] = "D"
                Status.BREENDING -> visCatsMap[cats[i].x][cats[i].y] = "B"
            }
        }

        return visCatsMap
    }

    private fun move(distance: Int): Int {
        val randomOp = Random.nextDouble()

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
    fun moveCats() {
        val newCatList = mutableListOf<Cat>()
        for (cat in cats) {

            if (cat.status == Status.BREENDING && cat.sex == Sex.Female) {
                val newCat = createCat(width, height, 0, cat.x, cat.y)
                newCatList.add(newCat)
                println("В x = ${cat.x}, y = ${cat.y} появился новый котенок")
            }

            cat.age += 1

            if (cat.age > 15) {
                if (cat.age < 17) { println("$cat умер") }
                cat.status = Status.DEAD
                cat.x = -width
                cat.y = -height
            } else {
                print("$cat ушел в ")

                cat.x += move(width)
                cat.y += move(height)
                cat.status = Status.WALK

                if (cat.x >= width) {
                    cat.x = cat.x - width
                } else if ((cat.x < 0)) {
                    cat.x = cat.x + width
                }

                if (cat.y >= height) {
                    cat.y = cat.y - height
                } else if ((cat.y < 0)) {
                    cat.y = cat.y + height
                }

                println("x = ${cat.x}, y = ${cat.y}")
            }

            cat.removeAllHissCats()
            cat.removeAllNeighboringBreending()
        }


        for (newCat in newCatList) {
            cats.add(newCat)
        }
    }
}



