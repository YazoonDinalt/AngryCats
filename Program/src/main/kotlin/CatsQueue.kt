import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock

class ChannelQueue<T>(private val channel: Channel<T> = Channel(Channel.BUFFERED)) {

    suspend fun enqueue(item: T) = channel.send(item)

    suspend fun dequeue(): T? =
        channel.receiveCatching().getOrNull() // receiveOrNull() возвращает null, если канал пустой и закрыт


    fun asFlow(): Flow<T> = channel.receiveAsFlow()

    fun close() = channel.close() // Закрывает канал
}


