import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock

class SynchronizedQueue<T> {
    private val queue = LinkedList<T>()
    private val lock = ReentrantLock()
    private val channel = Channel<T>(Channel.BUFFERED)


    suspend fun enqueue(item: T) {
        lock.lock()
        try {
            queue.addLast(item)
            channel.send(item)
        } finally {
            lock.unlock()
        }
    }

    fun dequeue(): T? {
        lock.lock()
        try {
            return if (queue.isEmpty()) null else queue.removeFirst()
        } finally {
            lock.unlock()
        }
    }

    fun isEmpty(): Boolean {
        lock.lock()
        try {
            return queue.isEmpty()
        } finally {
            lock.unlock()
        }
    }

    fun size(): Int {
        lock.lock()
        try {
            return queue.size
        } finally {
            lock.unlock()
        }
    }

    fun asFlow(): Flow<T> = channel.receiveAsFlow()
}


