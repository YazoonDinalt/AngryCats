const val SEED = 18
const val AMBIT = 5
fun main() {
    val height = 5
    val weight = 5
    val amountCats = 7
    val r0 = 1.0
    val r1 = 10.0

    val cats = createCats(amountCats, height, weight)
    var catsMap = Map().createCatsMap(cats, height, weight)

    UpdateStatus(catsMap, r0, r1).updateStatus()
    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(cats, amountCats, height, weight)
    catsMap = Map().createCatsMap(cats, height, weight)
    UpdateStatus(catsMap, r0, r1).updateStatus()

    Print(Map().visualCatsMap(catsMap))

    Map().moveCatsMap(cats, amountCats, height, weight)
    catsMap = Map().createCatsMap(cats, height, weight)
    UpdateStatus(catsMap, r0, r1).updateStatus()

    Print(Map().visualCatsMap(catsMap))
}