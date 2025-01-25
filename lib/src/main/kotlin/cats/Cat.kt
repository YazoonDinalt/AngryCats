package org.example.lib.cats

import kotlin.random.Random

// Random number generator
const val SEED = 18
val random = Random(SEED)

/**

 Enums containing basic information about cats

 */
enum class Sex {
    Male,
    Female,
}

enum class Status {
    WALK,
    HISS,
    FIGHT,
    DEAD,
    BREEDING,
}

/**

A class containing information about cats and who they interact with

 */

class Room(
    val leftX: Int,
    val leftY: Int,
    val rightX: Int,
    val rightY: Int,
    val number: Int,
)

class Cat(
    var x: Int,
    var y: Int,
    val sex: Sex,
    var age: Int,
    var status: Status,
    var room: Room,
) {
    private var hissCats: MutableList<Cat> = mutableListOf() // List of cats hissing with
    private var breeding: MutableList<Cat> = mutableListOf() // List of cats breeding with

    // Get all the breeding Cats
    fun getNeighboringBreeding(): MutableList<Cat> {
        return breeding
    }

    // Add a breeding Cat
    fun addNeighboringBreeding(cat: Cat) {
        breeding.add(cat)
    }

    // Remove a breeding Cat
    fun removeNeighboringBreeding(cat: Cat) {
        breeding.remove(cat)
    }

    // Remove all the breeding Cats
    fun removeAllNeighboringBreeding() {
        breeding = mutableListOf()
    }

    // Add a hissing Cat
    fun addHissCat(cat: Cat) {
        hissCats.add(cat)
    }

    // Remove all the hissing Cats
    fun removeAllHissCats() {
        hissCats = mutableListOf()
    }

    // Get all the hissing Cats
    fun getHissCats(): MutableList<Cat> {
        return hissCats
    }

    // Info string
    override fun toString(): String {
        return "Cat(X=$x, Y=$y, Status=$status, Age=$age, Sex=$sex)"
    }
}

// Create one cat
fun createCat(
    width: Int,
    height: Int,
    age: Int = random.nextInt(10),
    x: Int = random.nextInt(width),
    y: Int = random.nextInt(height),
): Cat {
    val sex = if (random.nextBoolean()) Sex.Male else Sex.Female
    val room = Room(-1, -1, height, width, 0)

    return (Cat(x, y, sex, age, Status.WALK, room))
}

// Create multiple cats
fun createCats(
    amount: Int,
    height: Int,
    width: Int,
): MutableList<Cat> {
    val cats: MutableList<Cat> = mutableListOf()

    for (i in 0 until amount) {
        val cat = createCat(width, height)
        cats.add(cat)
    }

    return cats
}
