import kotlin.random.Random
import UpdateStatus
import org.junit.jupiter.api.Test
import java.lang.Math.min
import kotlin.collections.Map

class ProgramTest {
    companion object {
        const val defaultSeed = 42
    }

    fun checkFight(cats: Array<Cat>, r0: Double, cat: Cat): Boolean {
        if (cat.getFightCats().isEmpty()){
            return false
        }

        for (i in cats.indices) {
            if (cats[i] != cat) {
                val distance = Distance(cats[i], cat).euclideanDistance()
                if (distance < r0) {
                    if (cats[i].status != Status.FIGHT) {
                        return false
                    }
                    if (!cat.getFightCats().contains(cats[i])) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun checkWalk(cats: Array<Cat>, r0: Double, cat: Cat): Boolean {
        if (cat.getFightCats().isNotEmpty()){
            return false
        }

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

    private fun checkHiss(cats: Array<Cat>, r1: Double, cat: Cat): Boolean {
        val hissCats = cat.getHissCats()
        for (hissCat in hissCats) {
            val distance = Distance(hissCat, cat).euclideanDistance()
            if (distance > r1) {
                println(distance)
                println(r1)
                return false
            }
        }
        return true
    }

    @Test
    fun `move and update` () {
        val randomizer = Random(defaultSeed)
        val height = randomizer.nextInt(1000)
        val weight = randomizer.nextInt(1000)
        val amountCats = randomizer.nextInt(50000)
        val r0 = randomizer.nextDouble(height.coerceAtMost(weight) / 100.0)
        val r1 = r0 * 2

        val cats = createCats(amountCats, height, weight)

        for (num in 0..10) {
            Map().moveCats(cats, amountCats, height, weight)
            UpdateStatus(cats, r0, r1)

            for (i in cats.indices) {
                when (cats[i].status) {
                    Status.FIGHT -> assert(checkFight(cats, r0, cats[i]))
                    Status.HISS -> assert(checkHiss(cats, r1, cats[i]))
                    Status.WALK -> assert(checkWalk(cats, r0, cats[i]))
                }
            }
        }
    }
}