import kotlin.random.Random

class Map (private val weight: Int, private val height: Int, private val cats: Array<Cat>){
    fun visualCatsMap(): Array<Array<String>> {
        val visCatsMap = Array(weight) { Array(height) { "0" } }

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.FIGHT -> visCatsMap[cats[i].x][cats[i].y] = "F"
                Status.WALK -> visCatsMap[cats[i].x][cats[i].y] = "W"
                Status.HISS -> visCatsMap[cats[i].x][cats[i].y] = "H"
                Status.DEAD -> visCatsMap[cats[i].x][cats[i].y] = "D"
                Status.BREENDING -> visCatsMap[cats[i].x][cats[i].y] = "S"
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
        for (cat in cats) {
            print("$cat ушел в ")

            cat.x += move(weight)
            cat.y += move(height)
            cat.age += 1
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

            cat.removeAllHissCats()
            cat.removeAllNeighboringBreending()
        }
    }
}



