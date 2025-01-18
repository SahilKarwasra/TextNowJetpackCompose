package com.example.textnowjetpackcompose.data

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(userId: String) {
        try {
            val options = IO.Options().apply {
                query = "userId=$userId"
                reconnection = true
            }
            mSocket = IO.socket("https://textnowbackend.onrender.com", options)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getSocket(): Socket = mSocket

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
        Log.d("WebSocket", "Socket URL: ${mSocket.id()}, Connected: ${mSocket.connected()}")
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}
