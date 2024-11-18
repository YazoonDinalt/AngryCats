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

fun createCats(amount: Int, height: Int, weight: Int): Array<Cat> {
    // Инициализируем генератор случайных чисел
    val random = Random(SEED) // Создаем новый объект Random с заданным SEED

    val cats = Array(amount) {  Cat(0, 0, Sex.Male, 0, Status.WALK) }

    for (i in 0 until amount) {
        val x = random.nextInt(weight)
        val y = random.nextInt(height)
        val age = random.nextInt(10)
        val sex =
            if (Random.nextBoolean()) {
                Sex.Male
            } else {
                Sex.Female
            }

        cats[i] = Cat(x, y, sex, age, Status.WALK)
    }

    return cats
}