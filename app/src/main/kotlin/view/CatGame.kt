package org.example.app.view

import cats.*
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.graphics.Color
import utils.Config

class CatGame(private val queueCats: BlockingQueue<MutableList<CatForPresenter>>) : ApplicationAdapter(),
    InputProcessor {

    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var walkTexture: Texture
    private lateinit var hissTexture: Texture
    private lateinit var fightTexture: Texture
    private lateinit var deadTexture: Texture
    private lateinit var breedingTexture: Texture
    private lateinit var viewport: Viewport
    private val cameraSpeed = 600f
    private val viewportWidth = 800f
    private val viewportHeight = 480f

    private var cats = mutableListOf<CatForPresenter>()
    private var localCats = mutableListOf<CatForPresenter>()
    private var targetPositions: MutableList<Pair<Float, Float>> = mutableListOf()
    private val statusChangeDelay = Config.time
    private val animationDuration = 500L

    override fun create() {
        setView()
        setTexture()
        if (!queueCats.isEmpty()) cats = queueCats.dequeue()!!
    }

    override fun render() {
        if (cats.size > 0) {
            changeCameraAndRoom()
            updateCatPositions()
            renderCatsMove()
        }
    }

    private fun changeCameraAndRoom() {
        handleInput()
        viewport.apply()
        camera.update()
        batch.projectionMatrix = camera.combined

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        shapeRenderer.projectionMatrix = camera.combined
        batch.projectionMatrix = camera.combined

        drawRooms(shapeRenderer, barrierList, camera)
    }

    private fun updateCatPositions() {
        if (TimeUtils.timeSinceMillis(cats[0].animationStartTime) >= statusChangeDelay.value) {
            val newPositions = getNewCatPositions()
            if (newPositions != null) {
                targetPositions = MutableList(newPositions.size) { Pair(0f, 0f) }
                for (i in cats.indices) {
                    targetPositions[i] = newPositions[i]
                    cats[i].animationStartTime = TimeUtils.millis()
                }
            }
        }
    }

    private fun renderCatsMove() {
        batch.begin()
        if (targetPositions.size >= cats.size) {
            for (i in cats.indices) {
                val cat = cats[i]
                val currentTexture = when (cat.status) {
                    Status.WALK -> walkTexture
                    Status.HISS -> hissTexture
                    Status.FIGHT -> fightTexture
                    Status.DEAD -> deadTexture
                    Status.BREEDING -> breedingTexture
                }
                val progress =
                    (TimeUtils.timeSinceMillis(cat.animationStartTime)
                        .toFloat() / animationDuration.toFloat()).coerceIn(0f, 1f)
                val interpolatedX = Interpolation.linear.apply(cat.x.toFloat(), targetPositions[i].first, progress)
                val interpolatedY = Interpolation.linear.apply(cat.y.toFloat(), targetPositions[i].second, progress)
                batch.draw(currentTexture, interpolatedX, interpolatedY, 50f, 50f)
                cat.x = interpolatedX.toInt()
                cat.y = interpolatedY.toInt()
            }
        }
        batch.end()
    }

    private fun drawRooms(shapeRenderer: ShapeRenderer, barrierList: List<Room>, camera: OrthographicCamera) {
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLACK


        for (room in barrierList) {
            val x = room.leftX * Gdx.graphics.width.toFloat() / 5
            val y = room.leftY * Gdx.graphics.height.toFloat() / 5
            val width = (room.rightX - room.leftX) * Gdx.graphics.width.toFloat() / 5
            val height = (room.rightY - room.leftY) * Gdx.graphics.height.toFloat() / 5

            if (width <= 0 || height <= 0) continue

            shapeRenderer.rect(x, y, width, height)
        }

        shapeRenderer.end()
    }

    private fun setView() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        camera = OrthographicCamera(viewportWidth, viewportHeight)
        viewport = FitViewport(800f, 600f, camera)
        camera.setToOrtho(false, 800f, 600f)
        camera.position.set(viewportWidth / 2, viewportHeight / 2, 0f)
        camera.update()
        Gdx.input.inputProcessor = this
    }

    private fun setTexture() {
        walkTexture = Texture("cats_normal.png")
        hissTexture = Texture("cat_h.png")
        fightTexture = Texture("fight_cat.png")
        deadTexture = Texture("dead_cat.jpg")
        breedingTexture = Texture("two_cats.png")
    }

    private fun handleInput() {
        val deltaTime = Gdx.graphics.deltaTime
        val speedMultiplier = cameraSpeed * deltaTime


        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0f, speedMultiplier)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0f, -speedMultiplier)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-speedMultiplier, 0f)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(speedMultiplier, 0f)
        }
    }

    override fun scrolled(p0: Float, p1: Float): Boolean {
        val zoomFactor = 0.1f
        camera.zoom += (p1 * zoomFactor)
        camera.zoom = camera.zoom.coerceIn(0.2f, 2f)
        return true
    }

    private fun getNewCatPositions(): List<Pair<Float, Float>>? {
        val newPositions = mutableListOf<Pair<Float, Float>>()
        if (!queueCats.isEmpty()) localCats = queueCats.dequeue()!!
        if (localCats.size > 0) {
                if (localCats.size > cats.size) {

                    for (index in 0 until localCats.size) {
                        if (index > cats.size - 1) cats.add(localCats[index])
                        cats[index].status = localCats[index].status
                    }
                }
                for (i in 0 until cats.size) {
                    newPositions.add(
                        Pair(
                            localCats[i].x.toFloat() * Gdx.graphics.width.toFloat() / 5,
                            localCats[i].y.toFloat() * Gdx.graphics.height.toFloat() / 5
                        )
                    )
                }
                return newPositions
            }
        return if (newPositions.isEmpty()) null else newPositions
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
        walkTexture.dispose()
        hissTexture.dispose()
        fightTexture.dispose()
        deadTexture.dispose()
        breedingTexture.dispose()
    }

    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

}