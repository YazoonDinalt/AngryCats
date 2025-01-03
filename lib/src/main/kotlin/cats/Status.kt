package org.example.lib.cats

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.example.lib.utils.Distance
import org.example.lib.utils.NameDistance
import kotlin.random.Random

/**

A class that updates statuses for all cats

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
        updateCatStatus()
    }

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

    private suspend fun updateBreeding() {
        val catsByLocation = cats.groupBy { it.x to it.y }

        coroutineScope {
            catsByLocation.map { (_, catsAtLocation) ->
                async(Dispatchers.Default) {
                    val males = catsAtLocation.filter { it.sex == Sex.Male }
                    val females = catsAtLocation.filter { it.sex == Sex.Female }

                    if (males.isNotEmpty() && females.isNotEmpty() && catsAtLocation[0].status != Status.DEAD) {
                        males.forEach { it.status = Status.BREEDING }
                        females.forEach { it.status = Status.BREEDING }

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

    private suspend fun updateFight() {
        coroutineScope {
            cats.indices.map { i ->
                async(Dispatchers.Default) {
                    val currentCat = cats[i]
                    if (currentCat.status != Status.DEAD) {
                        for (j in i + 1 until cats.size) {
                            val otherCat = cats[j]
                            val distance = distance(currentCat, otherCat)
                            if (otherCat.status != Status.DEAD &&
                                currentCat.room.number == otherCat.room.number &&
                                (currentCat.status != Status.BREEDING || otherCat.status != Status.BREEDING) &&
                                currentCat != otherCat &&
                                distance <= r0
                            ) {
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

                                currentCat.status = Status.FIGHT
                                otherCat.status = Status.FIGHT

                                if (currentCat.x == otherCat.x && currentCat.y == otherCat.y && log) {
                                    println("Коты в x = ${currentCat.x}, y = ${currentCat.y} дерутся")
                                } else if (log) {
                                    println("$currentCat дерется c $otherCat")
                                }
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }

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
                                val probability = 1 / distance
                                val randomValue = Random.nextDouble()

                                if (randomValue <= probability) {
                                    currentCat.status = Status.HISS
                                    currentCat.addHiisCat(otherCat)
                                    otherCat.addHiisCat(currentCat)
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

    private fun updateCatStatus() {
        runBlocking {
            updateBreeding()
        }
        runBlocking {
            updateFight()
        }

        runBlocking {
            updateHiss()
        }
    }
}
