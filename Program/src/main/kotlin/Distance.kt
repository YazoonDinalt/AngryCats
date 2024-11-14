import kotlin.math.*

enum class NameDistance {
    Euclidean,
    Manhattan,
    Chebyshev,
    Curvilinear
}

class Distance(private val cat1: Cat, private val cat2: Cat) {
    fun euclideanDistance(): Float {
        return sqrt(((cat1.x - cat2.x)*(cat1.x-cat2.x) + (cat1.y-cat2.y)*(cat1.y-cat2.y)).toFloat())
    }

    fun manhattanDistance(): Float {
        return (abs(cat1.x - cat2.x) + abs(cat1.y-cat2.y)).toFloat()
    }

    fun chebyshevDistance(): Float {
        return max((cat1.x - cat2.x).toFloat(), (cat1.y - cat2.y).toFloat())
    }

    fun curvilinearDistance(): Float {
        // Переход к полярным координатам
        val r1 = sqrt((cat1.x * cat1.x + cat1.y * cat1.y).toFloat())
        val phi1 = atan((cat1.y / cat1.x).toFloat())

        val r2 = sqrt((cat2.x * cat2.x + cat2.y * cat2.y).toFloat())
        val phi2 = atan((cat2.y / cat2.x).toFloat())

        return sqrt(r1 * r1 - 2 * r1 * r2 * cos(phi1 - phi2))
    }

}