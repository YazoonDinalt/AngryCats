import kotlin.random.Random

enum class Status {
    WALK,
    HISS,
    FIGHT,
    None
}

class Cat (
    var x: Int,
    var y: Int,
    var status: Status
) {
    override fun toString(): String {
        return "Cat(X=$x, Y=$y, Status=$status)"
    }
}

data class CatInMap (
    var number: Int,
    var cat: Cat
)

fun createCats(amount: Int, height: Int, weight: Int): Array<Cat> {
    // Инициализируем генератор случайных чисел
    val random = Random(SEED) // Создаем новый объект Random с заданным SEED

    val cats = Array(amount) {  Cat(0, 0, Status.None) }

    for (i in 0 until amount) {
        val x = random.nextInt(weight)
        val y = random.nextInt(height)
        cats[i] = (Cat(x, y, Status.WALK))
    }

    return cats
}