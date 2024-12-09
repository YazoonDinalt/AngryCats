import cats.*
import org.junit.jupiter.api.Test

class ProgramTest {
    @Test
    fun `big test move and update` () {
        val height = 1000
        val width = 1000
        val amountCats = 50000
        val r0 = 8.0
        val r1 = r0 * 2

        val cats = createCats(amountCats, height, width)

        for (num in 0..3) {
            Map(width, height, cats, false).moveCats()
            UpdateStatus(cats, r0, r1, false)

            for (i in cats.indices) {
                when (cats[i].status) {
                    Status.FIGHT -> assert(Checker.checkFight(cats, r0, cats[i]))
                    Status.HISS -> assert(Checker.checkHiss(r1, cats[i]))
                    Status.WALK -> assert(Checker.checkWalk(cats, r0, cats[i]))
                    Status.BREEDING -> assert(Checker.checkBreending(cats, r0, cats[i]))
                    Status.DEAD -> assert(Checker.checkDead(cats[i], width, height))
                }
            }
        }
    }

    @Test
    fun `test all breeding`() {
        val r0 = 0.0
        val r1 = r0 * 2
        val room = Room(-1, -1, 3, 3, 0)

        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 3, Status.WALK, room),
            Cat(0, 1, Sex.Female, 3, Status.WALK, room),
            Cat(0, 2, Sex.Female, 3, Status.WALK, room),
            Cat(1, 0, Sex.Female, 3, Status.WALK, room),
            Cat(1, 1, Sex.Female, 3, Status.WALK, room),
            Cat(1, 2, Sex.Female, 3, Status.WALK, room),
            Cat(2, 0, Sex.Female, 3, Status.WALK, room),
            Cat(2, 1, Sex.Female, 3, Status.WALK, room),
            Cat(2, 2, Sex.Female, 3, Status.WALK, room),
            Cat(0, 0, Sex.Male, 3, Status.WALK, room),
            Cat(0, 1, Sex.Male, 3, Status.WALK, room),
            Cat(0, 2, Sex.Male, 3, Status.WALK, room),
            Cat(1, 0, Sex.Male, 3, Status.WALK, room),
            Cat(1, 1, Sex.Male, 3, Status.WALK, room),
            Cat(1, 2, Sex.Male, 3, Status.WALK, room),
            Cat(2, 0, Sex.Male, 3, Status.WALK, room),
            Cat(2, 1, Sex.Male, 3, Status.WALK, room),
            Cat(2, 2, Sex.Male, 3, Status.WALK, room),
        )

        UpdateStatus(cats, r0, r1, false)

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.BREEDING -> assert(Checker.checkBreending(cats, r0, cats[i]))
                else -> assert(false)
            }
        }

        Map(3, 3, cats, false).moveCats()

        assert(cats.size == 27)
    }

    @Test
    fun `test not breeding because of the freak`() {
        val r0 = 1.0
        val r1 = r0 * 2
        val room = Room(-1, -1, 3, 3, 0)

        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 3, Status.WALK, room),
            Cat(0, 1, Sex.Female, 3, Status.WALK, room),
            Cat(0, 2, Sex.Female, 3, Status.WALK, room),
            Cat(1, 0, Sex.Female, 3, Status.WALK, room),
            Cat(1, 1, Sex.Female, 3, Status.WALK, room),
            Cat(1, 2, Sex.Female, 3, Status.WALK, room),
            Cat(2, 0, Sex.Female, 3, Status.WALK, room),
            Cat(2, 1, Sex.Female, 3, Status.WALK, room),
            Cat(2, 2, Sex.Female, 3, Status.WALK, room),
            Cat(0, 0, Sex.Male, 3, Status.WALK, room),
            Cat(0, 1, Sex.Male, 3, Status.WALK, room),
            Cat(0, 2, Sex.Male, 3, Status.WALK, room),
            Cat(1, 0, Sex.Male, 3, Status.WALK, room),
            Cat(1, 1, Sex.Male, 3, Status.WALK, room),
            Cat(1, 2, Sex.Male, 3, Status.WALK, room),
            Cat(2, 0, Sex.Male, 3, Status.WALK, room),
            Cat(2, 1, Sex.Male, 3, Status.WALK, room),
        )

        UpdateStatus(cats, r0, r1,false)

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
        val room = Room(-1, -1, 3, 3, 0)
        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 30, Status.WALK, room),
            Cat(0, 1, Sex.Female, 30, Status.WALK, room),
            Cat(0, 2, Sex.Female, 30, Status.WALK, room),
            Cat(1, 0, Sex.Female, 30, Status.WALK, room),
            Cat(1, 1, Sex.Female, 30, Status.WALK, room),
            Cat(1, 2, Sex.Female, 30, Status.WALK, room),
            Cat(2, 0, Sex.Female, 30, Status.WALK, room),
            Cat(2, 1, Sex.Female, 30, Status.WALK, room),
            Cat(2, 2, Sex.Female, 30, Status.WALK, room),
            Cat(0, 0, Sex.Female, 30, Status.WALK, room),
            Cat(0, 1, Sex.Female, 30, Status.WALK, room),
            Cat(0, 2, Sex.Female, 30, Status.WALK, room),
            Cat(1, 0, Sex.Female, 30, Status.WALK, room),
            Cat(1, 1, Sex.Female, 30, Status.WALK, room),
            Cat(1, 2, Sex.Female, 30, Status.WALK, room),
            Cat(2, 0, Sex.Female, 30, Status.WALK, room),
            Cat(2, 1, Sex.Female, 30, Status.WALK, room),
            Cat(2, 2, Sex.Female, 30, Status.WALK, room),
        )

        Map(3, 3, cats, false).moveCats()

        for (cat in cats) {
            when (cat.status) {
                Status.DEAD -> assert(Checker.checkDead(cat, 3, 3))
                else -> assert(false)
            }
        }
    }

    @Test
    fun `test cat in new room`() {
        val room = Room(-1, -1, 3, 3, 0)
        val cats = mutableListOf(
            Cat(1, 1, Sex.Female, 3, Status.WALK, room)
        )

        val map = Map(3, 3, cats, false)

        map.addBarrier(0, 0, 2, 2, cats)

        for (i in 0..10) {
            map.moveCats()
            if (cats[0].x != 1 || cats[0].y != 1) {
                assert(false)
            }
        }
    }

    @Test
    fun `test cat not in new room`() {
        val room = Room(-1, -1, 5, 5, 0)
        val cats = mutableListOf(
            Cat(0, 0, Sex.Female, 3, Status.WALK, room),
            Cat(0, 1, Sex.Female, 3, Status.WALK, room),
            Cat(0, 2, Sex.Female, 3, Status.WALK, room),
            Cat(0, 3, Sex.Female, 3, Status.WALK, room),
            Cat(0, 4, Sex.Female, 3, Status.WALK, room),
            Cat(4, 0, Sex.Female, 3, Status.WALK, room),
            Cat(4, 1, Sex.Female, 3, Status.WALK, room),
            Cat(4, 2, Sex.Female, 3, Status.WALK, room),
            Cat(4, 3, Sex.Female, 3, Status.WALK, room),
            Cat(4, 4, Sex.Female, 3, Status.WALK, room),
            Cat(1, 0, Sex.Female, 3, Status.WALK, room),
            Cat(2, 0, Sex.Female, 3, Status.WALK, room),
            Cat(3, 0, Sex.Female, 3, Status.WALK, room),
            Cat(1, 4, Sex.Female, 3, Status.WALK, room),
            Cat(2, 4, Sex.Female, 3, Status.WALK, room),
            Cat(3, 4, Sex.Female, 3, Status.WALK, room)
        )
        val map = Map(5, 5, cats, false)

        map.addBarrier(1, 1, 3, 3, cats)

        for (i in 0..10) {
            map.moveCats()
            for (cat in cats) {
                if (cat.x in 1..3 && cat.y in 1..3) {
                    assert(false)
                }
            }
        }
    }
}