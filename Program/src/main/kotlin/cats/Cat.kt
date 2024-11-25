package cats

import SEED
import kotlinx.atomicfu.atomic
import kotlin.random.Random

val random = Random(SEED)

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

class Cat (
    var x: Int,
    var y: Int,
    val sex: Sex,
    var age: Int,
    var status: Status
) {
    private var hissCats: MutableList<Cat> = mutableListOf() // Список котов на которых шипит
    private var breeding = atomic(mutableListOf<Cat>()) // Список размножающихся котов в радиусе r0

    // Получение списка размножающихся котов в радиусе r0
    fun getNeighboringBreeding(): MutableList<Cat>{
        return breeding.value
    }

    // Добавление в список размножающихся котов в радиусе r0
    fun addNeighboringBreeding(cat: Cat) {
        breeding.value.add(cat)
    }

    // Удаление кота из списка размножающихся котов в радиусе r0
    fun removeNeighboringBreeding(cat: Cat) {
        breeding.value.remove(cat)
    }

    // Удаление всех котов из списка размножающихся котов в радиусе r0
    fun removeAllNeighboringBreeding() {
        breeding = atomic(mutableListOf())
    }

    // Добавление в список шипящих котов
    fun addHiisCat(cat: Cat) {
        hissCats.add(cat)
    }

    // Удаление всех котов из списка щипящих котов
    fun removeAllHissCats() {
        hissCats = mutableListOf()
    }

    // Получение списка шипящих котов
    fun getHissCats(): MutableList<Cat> {
        return hissCats
    }

    override fun toString(): String {
        return "Cat(X=$x, Y=$y, Status=$status, Age=$age, Sex=$sex)"
    }
}

// Создание нового кота
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

// Создание списка новых котов
fun createCats(amount: Int, height: Int, width: Int): MutableList<Cat> {
    val cats: MutableList<Cat> = mutableListOf()

    for (i in 0 until amount) {
        val cat = createCat(width, height)
        cats.add(cat)
    }

    return cats
}