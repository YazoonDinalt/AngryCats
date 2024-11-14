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
    private var fightCats: MutableList<Cat> = mutableListOf() // Использование MutableList
    private var hissCats: MutableList<Cat> = mutableListOf() // Использование MutableList
    fun addFightCats(cat: Cat) {
        fightCats.add(cat)
    }

    fun removeFightCats(cat: Cat) {
        var delete = cat
        for (fc in fightCats) {
            if (fc.x == cat.x && fc.y == cat.y) {
                delete = fc
            }
        }
        fightCats.remove(delete)
    }
    fun getFightCats(): MutableList<Cat> {
        return fightCats
    }

    fun addHiisCats(cat: Cat) {
        hissCats.add(cat)
    }

    fun removeHissCats(cat: Cat) {
        var delete = cat
        for (hc in hissCats) {
            if (hc.x == cat.x && hc.y == cat.y) {
                delete = hc
            }
        }
        hissCats.remove(delete)
    }
    fun getHissCats(): MutableList<Cat> {
        return hissCats
    }
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