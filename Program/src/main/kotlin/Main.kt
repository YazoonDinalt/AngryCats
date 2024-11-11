import kotlin.math.*
const val SEED = 10
const val AMBIT = 5

fun main() {
    val height = 4
    val weight = 4
    val amountCats = 4
    val r0 = 0.0
    val r1 = 10.0

    val cats = createCats(amountCats, height, weight)
    val catsMap = Map().createCatsMap(cats, height, weight)

    UpdateStatus(catsMap, r0, r1)

    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(cats, amountCats, height, weight)
    val newCatsMap = Map().createCatsMap(cats, height, weight)

    UpdateStatus(newCatsMap, r0, r1)

    Print(newCatsMap)

    Print(Map().visualCatsMap(newCatsMap))

}
