package cats

import SEED
import kotlin.random.Random

/**

 Classes containing basic information about cats

 */

enum class Sex {
    Male,
    Female
}

enum class Status {
    WALK,
    HISS,
    FIGHT,
    DEAD,
    BREEDING
}

val random = Random(SEED)

/**

A class containing information about cats and who they interact with

 */

class Room (
    val leftX: Int,
    val leftY: Int,
    val rightX: Int,
    val rightY: Int,
    val number: Int
)

class Cat (
    var x: Int,
    var y: Int,
    val sex: Sex,
    var age: Int,
    var status: Status,
    var room: Room
) {
    private var hissCats: MutableList<Cat> = mutableListOf() // Список котов на которых шипит
    private var breeding: MutableList<Cat> = mutableListOf() // Список размножающихся котов в радиусе r0

    fun getNeighboringBreeding(): MutableList<Cat>{
        return breeding
    }
    fun addNeighboringBreeding(cat: Cat) {
        breeding.add(cat)
    }

    fun removeNeighboringBreeding(cat: Cat) {
        breeding.remove(cat)
    }

    fun removeAllNeighboringBreeding() {
        breeding = mutableListOf()
    }

    fun addHiisCat(cat: Cat) {
        hissCats.add(cat)
    }

    fun removeAllHissCats() {
        hissCats = mutableListOf()
    }
    fun getHissCats(): MutableList<Cat> {
        return hissCats
    }

    override fun toString(): String {
        return "Cat(X=$x, Y=$y, Status=$status, Age=$age, Sex=$sex)"
    }
}

fun createCat(
    width: Int,
    height: Int,
    age: Int = random.nextInt(10),
    x: Int = random.nextInt(width),
    y: Int = random.nextInt(height)
): Cat {
    val sex = if (random.nextBoolean()) Sex.Male else Sex.Female
    val room = Room(-1, -1, height, width, 0)

    return (Cat(x, y, sex, age, Status.WALK, room))
}

fun createCats(amount: Int, height: Int, width: Int): MutableList<Cat> {
    val cats: MutableList<Cat> = mutableListOf()

    for (i in 0 until amount) {
        val cat = createCat(width, height)
        cats.add(cat)
    }

    return cats
}