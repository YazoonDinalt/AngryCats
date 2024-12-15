package cats

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantReadWriteLock

/**

 A class that implements the channel that is needed to send data to the presenter

 */

class BlockingQueue<T>(private val capacity: Int) {
    private val queue: MutableList<T> = mutableListOf()
    private val lock = ReentrantReadWriteLock()
    private val size = AtomicInteger(0)

    fun enqueue(item: T) {
        lock.writeLock().lock()
        try {
            while (size.get() == capacity) {
                lock.writeLock().unlock()
                // Ждем, пока не освободится место в очереди
                Thread.sleep(10) // Можно использовать более сложную стратегию ожидания
                lock.writeLock().lock()
            }
            queue.add(item)
            size.incrementAndGet()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun dequeue(): T? {
        lock.writeLock().lock()
        try {
            while (size.get() == 0) {
                lock.writeLock().unlock()
                // Ждем, пока не появится элемент в очереди
                Thread.sleep(10)
                lock.writeLock().lock()
            }
            val item = queue.removeAt(0)
            size.decrementAndGet()
            return item
        } finally {
            lock.writeLock().unlock()
        }
    }


    fun isEmpty(): Boolean {
        lock.readLock().lock()
        try {
            return size.get() == 0
        } finally {
            lock.readLock().unlock()
        }
    }

}