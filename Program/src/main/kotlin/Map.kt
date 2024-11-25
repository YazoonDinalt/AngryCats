import cats.Cat
import cats.Sex
import cats.Status
import cats.createCat
import kotlinx.coroutines.*
import kotlin.random.Random

class Map (private val width: Int, private val height: Int, private val cats: MutableList<Cat>, private val log: Boolean = false) {

    private val maxAge = 30

    // Создание массива для консольной визуализации
    fun visualCatsMap(): Array<Array<String>> {
        val visCatsMap = Array(width + 1) { Array(height) { "0" } }

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.FIGHT -> visCatsMap[cats[i].x][cats[i].y] = "F"
                Status.WALK -> visCatsMap[cats[i].x][cats[i].y] = "W"
                Status.HISS -> visCatsMap[cats[i].x][cats[i].y] = "H"
                Status.DEAD -> visCatsMap[width][height - 1] = "D"
                Status.BREEDING -> visCatsMap[cats[i].x][cats[i].y] = "B"
            }
        }

        return visCatsMap
    }

    // Расчет случайного расстояния для перемещения от 1 до (distance / AMBIT)
    private fun move(distance: Int): Int {
        val randomOp = Random.nextBoolean()

        var move =
            if (randomOp) -1
            else 1

        if (distance / AMBIT >= 2) {
            move *= Random.nextInt(1, distance / AMBIT)
        }

        return move

    }

    // Перемещение кота на случайное расстояние
    fun moveCats() {
        runBlocking {
            suspendMoveCats()
        }
    }

    private suspend fun suspendMoveCats() {
        val newCatList = mutableListOf<Cat>()

        coroutineScope {
            cats.map { cat ->
                async(Dispatchers.Default) {
                    if (cat.status == Status.BREEDING && cat.sex == Sex.Female) {
                        val newCat = createCat(width, height, 0, cat.x, cat.y)
                        synchronized(newCatList){
                            newCatList.add(newCat)
                        }
                        if (log) println("В x = ${cat.x}, y = ${cat.y} появился новый котенок")
                    }

                    cat.age += 1

                    if (cat.age > maxAge) {
                        if (cat.age < maxAge + 2 && log) println("$cat умер")
                        cat.status = Status.DEAD
                        cat.x = -width
                        cat.y = -height
                    } else {
                        if (log) print("$cat ушел в ")
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

                        if (log) println("x = ${cat.x}, y = ${cat.y}")
                    }

                    cat.removeAllHissCats()
                    cat.removeAllNeighboringBreeding()
                }
            }.awaitAll()
        }

        for (newCat in newCatList) {
            cats.add(newCat)
        }
    }
}



