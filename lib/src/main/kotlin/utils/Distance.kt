package org.example.lib.utils

import org.example.lib.cats.Cat
import kotlin.math.*

/**

 List of distances that are supported

 */

enum class NameDistance {
    Euclidean,
    Manhattan,
    Chebyshev,
}

/**

 Class for distance calculation by supported methods

 */

class Distance(cat1: Cat, cat2: Cat) {
    private val catLeftX = min(cat1.x, cat2.x)
    private val catRightX = max(cat1.x, cat2.x)

    private val catLeftY = min(cat1.y, cat2.y)
    private val catRightY = max(cat1.y, cat2.y)

    private val distanceX = catRightX - catLeftX
    private val distanceY = catLeftY - catRightY

    fun euclideanDistance(): Float {
        return sqrt((distanceX * distanceX + distanceY * distanceY).toFloat())
    }

    fun manhattanDistance(): Float {
        return (abs(distanceX) + abs(distanceY)).toFloat()
    }

    fun chebyshevDistance(): Float {
        return max(distanceX.toFloat(), distanceY.toFloat())
    }
}
