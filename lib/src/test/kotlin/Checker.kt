import org.example.lib.cats.Cat
import org.example.lib.cats.Status
import org.example.lib.utils.Distance

object Checker {
    private fun checkFightOrBreeding(
        cats: MutableList<Cat>,
        r0: Double,
        cat: Cat,
        inputStatus: Status,
    ): Boolean {
        for (i in cats.indices) {
            val currentCat = cats[i]
            val distance = Distance(currentCat, cat).euclideanDistance()
            if (currentCat != cat && distance < r0 && currentCat.status != inputStatus) {
                return false
            }
        }
        return true
    }

    fun checkFight(
        cats: MutableList<Cat>,
        r0: Double,
        cat: Cat,
    ): Boolean {
        return checkFightOrBreeding(cats, r0, cat, Status.FIGHT)
    }

    fun checkBreeding(
        cats: MutableList<Cat>,
        r0: Double,
        cat: Cat,
    ): Boolean {
        return checkFightOrBreeding(cats, r0, cat, Status.BREEDING)
    }

    fun checkWalk(
        cats: MutableList<Cat>,
        r0: Double,
        cat: Cat,
    ): Boolean {
        for (i in cats.indices) {
            if (cats[i] != cat) {
                val distance = Distance(cats[i], cat).euclideanDistance()
                if (distance < r0) {
                    return false
                }
            }
        }
        return true
    }

    fun checkHiss(
        r1: Double,
        cat: Cat,
    ): Boolean {
        val hissCats = cat.getHissCats()
        for (hissCat in hissCats) {
            val distance = Distance(hissCat, cat).euclideanDistance()
            if (distance > r1) {
                return false
            }
        }
        return true
    }

    fun checkDead(
        cat: Cat,
        width: Int,
        height: Int,
    ): Boolean {
        if (cat.status == Status.DEAD && cat.x == -width && cat.y == -height) {
            return true
        }
        return true
    }
}
