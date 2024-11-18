const val SEED = 18
const val AMBIT = 5
fun main() {
    val height = 5
    val weight = 5
    val amountCats = 7
    val r0 = 1.0
    val r1 = 10.0

    val cats = createCats(amountCats, height, weight)

    UpdateStatus(cats, r0, r1)
    Print(Map().visualCatsMap(cats, weight, height))

    Map().moveCats(cats, amountCats, height, weight)
    UpdateStatus(cats, r0, r1)

    Print(Map().visualCatsMap(cats, weight, height))

    Map().moveCats(cats, amountCats, height, weight)
    UpdateStatus(cats, r0, r1)

    Print(Map().visualCatsMap(cats, weight, height))
}