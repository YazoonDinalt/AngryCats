import org.example.lib.cats.CatForPresenter
import org.example.lib.cats.Cat
import org.example.lib.cats.createCat
import org.example.lib.cats.createCats
import org.example.lib.cats.Map
import org.example.lib.cats.Room
import org.example.lib.cats.Sex
import org.example.lib.cats.Status
import org.example.lib.utils.Print
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class CatForPresenterTest {
    @Test
    fun `test cat creation`() {
        val cat = CatForPresenter(x = 5, y = 10, status = Status.WALK)

        assertEquals(5, cat.x)
        assertEquals(10, cat.y)
        assertEquals(Status.WALK, cat.status)
    }

    @Test
    fun `test cat status update`() {
        val cat = CatForPresenter(x = 5, y = 10, status = Status.BREEDING)

        cat.status = Status.DEAD

        assertEquals(Status.DEAD, cat.status)
    }

    @Test
    fun `test cat movement`() {
        val cat = CatForPresenter(x = 5, y = 10, status = Status.FIGHT)

        // Предположим, что кот перемещается на 2 по X и на 3 по Y
        cat.x += 2
        cat.y += 3

        assertEquals(7, cat.x)
        assertEquals(13, cat.y)
    }
}


class MapTest {
    val height = 5
    val width = 5
    val amountCats = 2
    val r0 = 1.0
    val r1 = 3.0
    @Test
    fun `test visualCatsMap with no cats and no barriers`() {
        val map = Map(width, height, mutableListOf()).visualCatsMap()

        // Проверяем, что карта имеет размерность 6x5, как и ожидалось
        assertEquals(6, map.size)
        assertEquals(5, map[0].size)

        // Проверяем, что все клетки пустые (значение должно быть "0")
        for (row in map) {
            for (cell in row) {
                assertEquals("0", cell)
            }
        }
    }

    @Test
    fun `test visualCatsMap with one cat and no barriers`() {
        val cat = createCat(width, height)
        val map = Map(width, height, mutableListOf(cat)).visualCatsMap()

        // Проверяем, что клетка, где находится кот, имеет статус "W" (walking)
        assertEquals("W", map[cat.x][cat.y])
    }

    @Test
    fun `test visualCatsMap with barriers and cats`() {
        val cats = createCats(amountCats, height, width)
        cats[0].status = Status.BREEDING
        cats[1].status = Status.FIGHT

        val map = Map(width, height, cats)

        // Добавляем барьер на карту
        map.addBarrier(1, 1, 4, 1, cats) // Пример добавления барьера

        val visualMap = map.visualCatsMap()
        Print(visualMap)
        // Проверяем, что статус котов отображается корректно
        val cat0 = cats[0]
        val cat1 = cats[1]
        assertEquals("B", visualMap[cat0.x][cat0.y])
        assertEquals("F", visualMap[cat1.x][cat1.y])

        // Проверяем, что барьер отображается корректно
        assertEquals("-", visualMap[1][4]) // Барьер по X
        assertEquals("|", visualMap[1][1]) // Барьер по Y
    }

    @Test
    fun `test visualCatsMap with dead cat`() {
        val cat = createCat(width, height)
        cat.status = Status.DEAD
        val map = Map(width, height, cats = mutableListOf(cat))
        val visualMap = map.visualCatsMap()

        // Проверяем, что мертвый кот отображается как "D"
        assertEquals("D", visualMap[width][height - 1])
    }
}
