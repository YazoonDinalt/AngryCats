import org.junit.jupiter.api.Test

class ProgramTest {
    @Test
    fun `big test move and update` () {
        val height = 1000
        val weight = 1000
        val amountCats = 50000
        val r0 = 8.0
        val r1 = r0 * 2

        val cats = createCats(amountCats, height, weight)

        for (num in 0..3) {
            Map(weight, height, cats).moveCats()
            UpdateStatus(cats, r0, r1)

            for (i in cats.indices) {
                when (cats[i].status) {
                    Status.FIGHT -> assert(Checker.checkFight(cats, r0, cats[i]))
                    Status.HISS -> assert(Checker.checkHiss(r1, cats[i]))
                    Status.WALK -> assert(Checker.checkWalk(cats, r0, cats[i]))
                    Status.BREENDING -> assert(Checker.checkBreending(cats, r0, cats[i]))
                    Status.DEAD -> assert(Checker.checkDead(cats[i], weight, height))
                }
            }
        }
    }

    @Test
    fun `test all breending`() {
        val r0 = 0.0
        val r1 = r0 * 2

        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 3, Status.WALK),
            Cat(0, 1, Sex.Female, 3, Status.WALK),
            Cat(0, 2, Sex.Female, 3, Status.WALK),
            Cat(1, 0, Sex.Female, 3, Status.WALK),
            Cat(1, 1, Sex.Female, 3, Status.WALK),
            Cat(1, 2, Sex.Female, 3, Status.WALK),
            Cat(2, 0, Sex.Female, 3, Status.WALK),
            Cat(2, 1, Sex.Female, 3, Status.WALK),
            Cat(2, 2, Sex.Female, 3, Status.WALK),
            Cat(0, 0, Sex.Male, 3, Status.WALK),
            Cat(0, 1, Sex.Male, 3, Status.WALK),
            Cat(0, 2, Sex.Male, 3, Status.WALK),
            Cat(1, 0, Sex.Male, 3, Status.WALK),
            Cat(1, 1, Sex.Male, 3, Status.WALK),
            Cat(1, 2, Sex.Male, 3, Status.WALK),
            Cat(2, 0, Sex.Male, 3, Status.WALK),
            Cat(2, 1, Sex.Male, 3, Status.WALK),
            Cat(2, 2, Sex.Male, 3, Status.WALK),
        )

        UpdateStatus(cats, r0, r1)

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.BREENDING -> assert(Checker.checkBreending(cats, r0, cats[i]))
                else -> assert(false)
            }
        }

        Map(3, 3, cats).moveCats()

        assert(cats.size == 27)
    }

    @Test
    fun `test not breending because of the freak`() {
        val r0 = 1.0
        val r1 = r0 * 2

        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 3, Status.WALK),
            Cat(0, 1, Sex.Female, 3, Status.WALK),
            Cat(0, 2, Sex.Female, 3, Status.WALK),
            Cat(1, 0, Sex.Female, 3, Status.WALK),
            Cat(1, 1, Sex.Female, 3, Status.WALK),
            Cat(1, 2, Sex.Female, 3, Status.WALK),
            Cat(2, 0, Sex.Female, 3, Status.WALK),
            Cat(2, 1, Sex.Female, 3, Status.WALK),
            Cat(2, 2, Sex.Female, 3, Status.WALK),
            Cat(0, 0, Sex.Male, 3, Status.WALK),
            Cat(0, 1, Sex.Male, 3, Status.WALK),
            Cat(0, 2, Sex.Male, 3, Status.WALK),
            Cat(1, 0, Sex.Male, 3, Status.WALK),
            Cat(1, 1, Sex.Male, 3, Status.WALK),
            Cat(1, 2, Sex.Male, 3, Status.WALK),
            Cat(2, 0, Sex.Male, 3, Status.WALK),
            Cat(2, 1, Sex.Male, 3, Status.WALK),
        )

        UpdateStatus(cats, r0, r1)

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.FIGHT -> assert(Checker.checkFight(cats, r0, cats[i]))
                else -> {
                    assert(false)
                }
            }
        }
    }

    @Test
    fun `test all dead`() {
        val r0 = 0.0
        val r1 = r0 * 2

        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 15, Status.WALK),
            Cat(0, 1, Sex.Female, 15, Status.WALK),
            Cat(0, 2, Sex.Female, 15, Status.WALK),
            Cat(1, 0, Sex.Female, 15, Status.WALK),
            Cat(1, 1, Sex.Female, 15, Status.WALK),
            Cat(1, 2, Sex.Female, 15, Status.WALK),
            Cat(2, 0, Sex.Female, 15, Status.WALK),
            Cat(2, 1, Sex.Female, 15, Status.WALK),
            Cat(2, 2, Sex.Female, 15, Status.WALK),
            Cat(0, 0, Sex.Female, 15, Status.WALK),
            Cat(0, 1, Sex.Female, 15, Status.WALK),
            Cat(0, 2, Sex.Female, 15, Status.WALK),
            Cat(1, 0, Sex.Female, 15, Status.WALK),
            Cat(1, 1, Sex.Female, 15, Status.WALK),
            Cat(1, 2, Sex.Female, 15, Status.WALK),
            Cat(2, 0, Sex.Female, 15, Status.WALK),
            Cat(2, 1, Sex.Female, 15, Status.WALK),
            Cat(2, 2, Sex.Female, 15, Status.WALK),
        )

        Map(3, 3, cats).moveCats()

        for (cat in cats) {
            when (cat.status) {
                Status.DEAD -> assert(Checker.checkDead(cat, 3, 3))
                else -> assert(false)
            }
        }
    }
}