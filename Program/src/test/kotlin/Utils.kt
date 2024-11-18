object Checker {
    fun checkFight(cats: MutableList<Cat>, r0: Double, cat: Cat): Boolean {
        for (i in cats.indices) {
            if (cats[i] != cat) {
                val distance = Distance(cats[i], cat).euclideanDistance()
                if (distance < r0) {
                    if (cats[i].status != Status.FIGHT) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun checkBreending(cats: MutableList<Cat>, r0: Double, cat: Cat): Boolean {
        for (i in cats.indices) {
            if (cats[i] != cat) {
                val distance = Distance(cats[i], cat).euclideanDistance()
                if (distance < r0 && cats[i].status != Status.BREENDING) {
                    println(distance)
                    println(cat)
                    println( cats[i] )
                    return false
                }
            }
        }
        return true
    }

    fun checkWalk(cats: MutableList<Cat>, r0: Double, cat: Cat): Boolean {
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

    fun checkHiss(r1: Double, cat: Cat): Boolean {
        val hissCats = cat.getHissCats()
        for (hissCat in hissCats) {
            val distance = Distance(hissCat, cat).euclideanDistance()
            if (distance > r1) {
                return false
            }
        }
        return true
    }

    fun checkDead(cat: Cat, weight: Int, height: Int): Boolean {
        if (cat.status == Status.DEAD && cat.x == -weight && cat.y == -height) {
            return true
        }
        return true
    }

}