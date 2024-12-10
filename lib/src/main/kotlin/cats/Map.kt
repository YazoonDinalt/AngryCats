package org.example.lib.cats

import kotlinx.coroutines.*
import kotlin.random.Random

const val AMBIT = 5

/**

This is the class that is responsible for moving cats ,

 */

class Map (
    private val width: Int,
    private val height: Int,
    private val cats: MutableList<Cat>,
    private val log: Boolean = false
) {
    private var roomNumber = 0
    private val barrierList: MutableList<Room> = mutableListOf(Room(-1, -1, width, height, 0))
    fun visualCatsMap(): Array<Array<String>> {
        val visCatsMap = Array(width + 1) { Array(height) { "0" } }

        for (barrier in barrierList) {
            for (i in barrier.leftX .. barrier.rightX) {
                visCatsMap[i][barrier.leftX] = "-"
                visCatsMap[i][barrier.rightX] = "-"
            }
            for (i in barrier.leftY .. barrier.rightY) {
                visCatsMap[barrier.leftY][i] = "|"
                visCatsMap[barrier.rightY][i] = "|"
            }
        }

        for (i in cats.indices) {
            when (cats[i].status) {
                Status.FIGHT -> visCatsMap[cats[i].x][cats[i].y] = "F"
                Status.WALK -> visCatsMap[cats[i].x][cats[i].y] = "W"
                Status.HISS -> visCatsMap[cats[i].x][cats[i].y] = "H"
                Status.DEAD -> visCatsMap[width][height - 1] = "D"
                Status.BREEDING -> visCatsMap[cats[i].x][cats[i].y] = "B"
            }
        }

        return visCatsMap
    }

    private fun move(distance: Int): Int {
        val randomOp = Random.nextBoolean()

        var move =
            if (randomOp) -1
            else 1

        if (distance / AMBIT >= 2) {
            move *= Random.nextInt(1, distance / AMBIT)
        }

        return move
    }

    fun moveCats() {
        runBlocking {
            suspendMoveCats(barrierList)
        }
    }

    private suspend fun suspendMoveCats(barrierList: MutableList<Room>) {
        val newCatList = mutableListOf<Cat>()

        coroutineScope {
            cats.map { cat ->
                async(Dispatchers.Default) {
                    if (cat.status == Status.BREEDING && cat.sex == Sex.Female) {
                        val newCat = createCat(width, height, 0, cat.x, cat.y)
                        synchronized(newCatList){
                            newCatList.add(newCat)
                        }
                        if (log) println("В x = ${cat.x}, y = ${cat.y} появился новый котенок")
                    }

                    cat.age += 1

                    if (cat.age > 30) {
                        if (cat.age < 32 && log) println("$cat умер")
                        cat.status = Status.DEAD
                        cat.x = -width
                        cat.y = -height
                    } else {
                        if (log) print("$cat ушел в ")

                        var moveX = move(cat.room.rightX - cat.room.leftX)
                        var moveY = move(cat.room.rightY - cat.room.leftY)
                        cat.status = Status.WALK

                        if (cat.x + moveX >= cat.room.rightX) {
                            moveX = cat.room.rightX - cat.x
                            cat.x = cat.room.rightX - 1
                        } else if (cat.x + moveX <= cat.room.leftX) {
                            moveX = cat.x - cat.room.leftX
                            cat.x = cat.room.leftX + 1
                        } else {
                            cat.x += moveX
                        }

                        if (cat.y + moveY >= cat.room.rightY) {
                            moveY = cat.room.rightY - cat.y
                            cat.y = cat.room.rightY - 1
                        } else if (cat.y + moveY <= cat.room.leftY) {
                            moveY = cat.y - cat.room.leftY
                            cat.y = cat.room.leftY + 1
                        } else {
                            cat.y += moveY
                        }

                        for (barrier in barrierList) {
                            if (barrier.leftX > cat.room.leftX
                                && barrier.leftY > cat.room.leftY
                                && barrier.rightX < cat.room.rightX
                                && barrier.rightY < cat.room.rightY
                            ) {
                                val startCat = Point(cat.x - moveX, cat.y - moveY)
                                val endCat = Point(cat.x, cat.y)
                                val leftDown = Point(barrier.leftX, barrier.leftY)
                                val leftUp = Point(barrier.leftX, barrier.rightY)
                                val rightUp = Point(barrier.rightX, barrier.rightY)
                                val rightDown = Point(barrier.rightX, barrier.leftY)
                                if ((cat.x in barrier.leftX .. barrier.rightX
                                    && cat.y in barrier.leftY .. barrier.rightY)
                                    || intersects(startCat, endCat, leftDown, leftUp)
                                    || intersects(startCat, endCat, leftUp, rightUp)
                                    || intersects(startCat, endCat, rightUp, rightDown)
                                    || intersects(startCat, endCat, leftDown, rightDown)
                                ) {
                                    if (cat.x - moveX < barrier.leftX) {
                                        cat.x = barrier.leftX - 1
                                    } else if (cat.x - moveX > barrier.rightX) {
                                        cat.x = barrier.rightX + 1
                                    }

                                    if (cat.y - moveY < barrier.leftY) {
                                        cat.y = barrier.leftY - 1
                                    } else if (cat.y - moveY > barrier.rightY) {
                                        cat.y = barrier.rightY + 1
                                    }
                                }
                            }
                        }

                        if (log) println("x = ${cat.x}, y = ${cat.y}")
                    }

                    cat.removeAllHissCats()
                    cat.removeAllNeighboringBreeding()
                }
            }.awaitAll()
        }

        for (newCat in newCatList) {
            cats.add(newCat)
        }
    }

    fun addBarrier(leftX: Int, leftY: Int, rightX: Int, rightY: Int, cats: MutableList<Cat>) {
        roomNumber++
        barrierList.add(Room(leftX, leftY, rightX, rightY, roomNumber))
        runBlocking {
            suspendAddBarrier(leftX, leftY, rightX, rightY, cats)
        }
    }

    private suspend fun suspendAddBarrier(leftX: Int, leftY: Int, rightX: Int, rightY: Int, cats: MutableList<Cat>) {
        val room = Room(leftX, leftY, rightX, rightY, roomNumber)
        coroutineScope {
            cats.map { cat ->
                async(Dispatchers.Default) {
                    if (cat.x in leftX..rightX && cat.y in leftY..rightY) {
                        cat.room = room

                        if (cat.x == leftX) {
                            cat.x = leftX + 1
                        } else if (cat.x == rightX) {
                            cat.x = rightX - 1
                        }

                        if (cat.y == leftY) {
                            cat.y = leftY + 1
                        } else if (cat.y == rightY) {
                            cat.y = rightY - 1
                        }
                    }
                }
            }.awaitAll()
        }
    }
    data class Point(val x: Int, val y: Int)

    private fun crossProduct(p1: Point, p2: Point, p3: Point): Int {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)
    }

    private fun intersects(p1: Point, p2: Point, p3: Point, p4: Point): Boolean {
        val cp1 = crossProduct(p1, p2, p3)
        val cp2 = crossProduct(p1, p2, p4)
        val cp3 = crossProduct(p3, p4, p1)
        val cp4 = crossProduct(p3, p4, p2)

        if (((cp1 > 0 && cp2 < 0) || (cp1 < 0 && cp2 > 0)) &&
            ((cp3 > 0 && cp4 < 0) || (cp3 < 0 && cp4 > 0))) {
            return true
        }

        if (cp1 == 0 && onSegment(p1, p2, p3)) return true
        if (cp2 == 0 && onSegment(p1, p2, p4)) return true
        if (cp3 == 0 && onSegment(p3, p4, p1)) return true
        if (cp4 == 0 && onSegment(p3, p4, p2)) return true

        return false
    }

    private fun onSegment(p1: Point, p2: Point, p: Point): Boolean {
        return minOf(p1.x, p2.x) <= p.x && p.x <= maxOf(p1.x, p2.x) &&
                minOf(p1.y, p2.y) <= p.y && p.y <= maxOf(p1.y, p2.y)
    }
}

