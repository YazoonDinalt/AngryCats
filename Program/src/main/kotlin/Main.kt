const val SEED = 111
const val AMBIT = 5
fun main() {
    val height = 5
    val width = 5
    val amountCats = 10
    val r0 = 1.0
    val r1 = 10.0

    val cats = createCats(amountCats, height, width)


    cats[0].addNeighboringBreending(cats[2])
    cats[0].addNeighboringBreending(cats[3])

    cats[2].addNeighboringBreending(cats[0])
    cats[2].addNeighboringBreending(cats[1])
    cats[2].addNeighboringBreending(cats[4])
    cats[2].addNeighboringBreending(cats[5])

    cats[4].addNeighboringBreending(cats[2])
    cats[4].addNeighboringBreending(cats[3])
//
//    for (cat in cats[0].getSose()) {
//        papa(cat, cats[0])
//    }

    Print(Map(width, height, cats).visualCatsMap())

    UpdateStatus(cats, r0, r1)
    Print(Map(width, height, cats).visualCatsMap())

    Map(width, height, cats).moveCats()

    UpdateStatus(cats, r0, r1)
    Print(Map(width, height, cats).visualCatsMap())

    Map(width, height, cats).moveCats()

    UpdateStatus(cats, r0, r1)
    Print(Map(width, height, cats).visualCatsMap())
}