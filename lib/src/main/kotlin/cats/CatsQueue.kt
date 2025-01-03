package org.example.lib.cats

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**

 A class that implements the channel that is needed to send data to the presenter

 */

class ChannelQueue<T>(private val channel: Channel<T> = Channel(Channel.BUFFERED)) {
    suspend fun enqueue(item: T) = channel.send(item)

    fun asFlow(): Flow<T> = channel.receiveAsFlow()
}
