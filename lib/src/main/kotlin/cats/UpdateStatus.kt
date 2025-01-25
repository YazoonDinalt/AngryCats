package org.example.lib.cats

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.lib.utils.Distance
import org.example.lib.utils.NameDistance
import kotlin.random.Random

/**
 * A class responsible for updating the statuses of all cats based on their interactions,
 * distances, and specific behavior rules such as breeding, fighting, and hissing.
 */
val mutex = Mutex()

class UpdateStatus(
    private val cats: MutableList<Cat>,
    private val r0: Double,
    private val r1: Double,
    private val log: Boolean,
    private val nameDistance: NameDistance = NameDistance.Euclidean,
) {
    init {
        // Begin updating cat statuses when the class is initialized.
        updateCatStatus()
    }

    /**
     * Calculates the distance between two cats based on the specified method.
     */
    private fun distance(
        cat1: Cat,
        cat2: Cat,
    ): Float {
        return when (nameDistance) {
            NameDistance.Euclidean -> Distance(cat1, cat2).euclideanDistance()
            NameDistance.Manhattan -> Distance(cat1, cat2).manhattanDistance()
            NameDistance.Chebyshev -> Distance(cat1, cat2).chebyshevDistance()
        }
    }

    /**
     * Recursively updates the status of a cat and its neighbors to 'FIGHT'
     * when a breeding conflict occurs.
     */
    private fun breedingForWar(cat: Cat) {
        cat.status = Status.FIGHT
        if (cat.getNeighboringBreeding().isEmpty()) {
            return
        } else {
            for (c in cat.getNeighboringBreeding()) {
                c.removeNeighboringBreeding(cat)
            }
            for (c in cat.getNeighboringBreeding()) {
                breedingForWar(c)
            }
        }
    }

    /**
     * Updates the breeding status of cats based on their location and proximity.
     */
    private suspend fun updateBreeding() {
        // Group cats by their (x, y) coordinates.
        val catsByLocation = cats.groupBy { it.x to it.y }

        coroutineScope {
            catsByLocation.map { (_, catsAtLocation) ->
                async(Dispatchers.Default) {
                    val males = catsAtLocation.filter { it.sex == Sex.Male }
                    val females = catsAtLocation.filter { it.sex == Sex.Female }

                    // Breeding occurs if both males and females are present.
                    if (males.isNotEmpty() && females.isNotEmpty() && catsAtLocation[0].status != Status.DEAD) {
                        males.forEach { it.status = Status.BREEDING }
                        females.forEach { it.status = Status.BREEDING }

                        // Find neighboring cats within distance r0.
                        for ((_, catsAtLocation2) in catsByLocation) {
                            if (catsAtLocation2 != catsAtLocation) {
                                val males2 = catsAtLocation2.filter { it.sex == Sex.Male }
                                val females2 = catsAtLocation2.filter { it.sex == Sex.Female }

                                if (
                                    males2.isEmpty() ||
                                    females2.isEmpty() ||
                                    catsAtLocation2[0].status == Status.DEAD ||
                                    catsAtLocation[0].room.number != catsAtLocation2[0].room.number
                                ) {
                                    continue
                                }

                                if (distance(catsAtLocation[0], catsAtLocation2[0]) <= r0) {
                                    for (cat in catsAtLocation2) {
                                        catsAtLocation[0].addNeighboringBreeding(cat)
                                    }
                                }
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }

    /**
     * Updates the fighting status of cats if they are within distance r0
     * and meet specific conditions.
     */
    private suspend fun updateFight() {
        coroutineScope {
            cats.indices.map { i ->
                async(Dispatchers.Default) {
                    val currentCat = cats[i]
                    if (currentCat.status != Status.DEAD) {
                        for (j in i + 1 until cats.size) {
                            val otherCat = cats[j]
                            val distance = distance(currentCat, otherCat)

                            // Trigger a fight if conditions are met.
                            if (otherCat.status != Status.DEAD &&
                                currentCat.room.number == otherCat.room.number &&
                                (currentCat.status != Status.BREEDING || otherCat.status != Status.BREEDING) &&
                                currentCat != otherCat &&
                                distance <= r0
                            ) {
                                // Resolve breeding conflicts during fights.
                                if (currentCat.status == Status.BREEDING) {
                                    mutex.withLock {
                                        breedingForWar(currentCat)
                                    }
                                }

                                if (otherCat.status == Status.BREEDING) {
                                    mutex.withLock {
                                        breedingForWar(otherCat)
                                    }
                                }

                                // Update both cats' statuses to 'FIGHT'.
                                currentCat.status = Status.FIGHT
                                otherCat.status = Status.FIGHT

                                if (log) {
                                    if (currentCat.x == otherCat.x && currentCat.y == otherCat.y) {
                                        println("Коты в x = ${currentCat.x}, y = ${currentCat.y} дерутся")
                                    } else {
                                        println("$currentCat дерется c $otherCat")
                                    }
                                }
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }

    /**
     * Updates the hissing status of cats when they are within distance r1
     * and conditions are met.
     */
    private suspend fun updateHiss() {
        coroutineScope {
            cats.indices.map { i ->
                async(Dispatchers.Default) {
                    val currentCat = cats[i]
                    if (currentCat.status == Status.WALK) {
                        for (j in i + 1 until cats.size) {
                            val otherCat = cats[j]
                            val distance = distance(currentCat, otherCat)

                            if (distance <= r1 &&
                                currentCat.status != Status.FIGHT &&
                                currentCat.room.number == otherCat.room.number
                            ) {
                                // Random chance of triggering hissing based on proximity.
                                val probability = 1 / distance
                                val randomValue = Random.nextDouble()

                                if (randomValue <= probability) {
                                    currentCat.status = Status.HISS
                                    currentCat.addHissCat(otherCat)
                                    otherCat.addHissCat(currentCat)
                                    if (log) {
                                        println("$currentCat шипит на $otherCat")
                                    }
                                    if (otherCat.status == Status.WALK) {
                                        otherCat.status = Status.HISS
                                        if (log) {
                                            println("$otherCat шипит на $currentCat")
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }

    /**
     * Sequentially updates the statuses of all cats: breeding, fighting, and hissing.
     */
    private fun updateCatStatus() {
        runBlocking { updateBreeding() }
        runBlocking { updateFight() }
        runBlocking { updateHiss() }
    }
}
