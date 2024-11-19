package cats

import SEED
import kotlin.random.Random

enum class Sex {
    Male,
    Female
}

enum class Status {
    WALK,
    HISS,
    FIGHT,
    DEAD,
    BREENDING
}

val random = Random(SEED)

class Cat (
    var x: Int,
    var y: Int,
    val sex: Sex,
    var age: Int,
    var status: Status
) {
    private var hissCats: MutableList<Cat> = mutableListOf() // Использование MutableList
    private var breending: MutableList<Cat> = mutableListOf()

    fun getNeighboringBreending(): MutableList<Cat>{
        return breending
    }
    fun addNeighboringBreending(cat: Cat) {
        breending.add(cat)
    }

    fun removeNeighboringBreending(cat: Cat) {
        breending.remove(cat)
    }

    fun removeAllNeighboringBreending() {
        breending = mutableListOf()
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

    return (Cat(x, y, sex, age, Status.WALK))
}

fun createCats(amount: Int, height: Int, width: Int): MutableList<Cat> {
    val cats: MutableList<Cat> = mutableListOf()

    for (i in 0 until amount) {
        val cat = createCat(width, height)
        cats.add(cat)
    }

    return cats
}