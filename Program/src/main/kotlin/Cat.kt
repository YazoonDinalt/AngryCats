import kotlin.random.Random

enum class Status {
    WALK,
    HISS,
    FIGHT
}

class Cat (
    var x: Int,
    var y: Int,
    var status: Status
) {
    private var fightCats: MutableList<Cat> = mutableListOf() // Использование MutableList
    private var hissCats: MutableList<Cat> = mutableListOf() // Использование MutableList
    fun addFightCats(cat: Cat) {
        fightCats.add(cat)
    }

    fun removeFightCat(cat: Cat) {
        fightCats.remove(cat)
    }

    fun removeAllFightCats() {
        fightCats = mutableListOf()
    }
    fun getFightCats(): MutableList<Cat> {
        return fightCats
    }

    fun addHiisCat(cat: Cat) {
        hissCats.add(cat)
    }

    fun removeHissCat(cat: Cat) {
        hissCats.remove(cat)
    }

    fun removeAllHissCats() {
        hissCats = mutableListOf()
    }
    fun getHissCats(): MutableList<Cat> {
        return hissCats
    }

    override fun toString(): String {
        return "Cat(X=$x, Y=$y, Status=$status)"
    }
}

fun createCats(amount: Int, height: Int, weight: Int): Array<Cat> {
    // Инициализируем генератор случайных чисел
    val random = Random(SEED) // Создаем новый объект Random с заданным SEED

    val cats = Array(amount) {  Cat(0, 0, Status.WALK) }

    for (i in 0 until amount) {
        val x = random.nextInt(weight)
        val y = random.nextInt(height)
        cats[i] = (Cat(x, y, Status.WALK))
    }

    return cats
}