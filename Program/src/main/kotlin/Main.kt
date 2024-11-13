import kotlin.math.*
const val SEED = 10
const val AMBIT = 5

fun main() {
    val height = 5
    val weight = 5
    val amountCats = 7
    val r0 = 1.0F
    val r1 = 10.0F

    val cats = createCats(amountCats, height, weight)
    val catsMap = Map().createCatsMap(cats, height, weight)

    UpdateStatus(catsMap, r0, r1).updateStatus()

    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(catsMap, cats, amountCats, height, weight, r0, r1)
    //val newCatsMap = Map().createCatsMap(cats, height, weight)

    Print(catsMap)
    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(catsMap, cats, amountCats, height, weight, r0, r1)

    Print(catsMap)
    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(catsMap, cats, amountCats, height, weight, r0, r1)

    Print(catsMap)
    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(catsMap, cats, amountCats, height, weight, r0, r1)

    Print(catsMap)
    Print(Map().visualCatsMap(catsMap))


    //UpdateStatus(newCatsMap, r0, r1)
    //Print(Map().visualCatsMap(newCatsMap))

}
