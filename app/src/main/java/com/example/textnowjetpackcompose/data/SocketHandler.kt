package com.example.textnowjetpackcompose.data

import android.util.Log
import io.socket.client.Socket
import io.socket.client.IO
import java.net.URISyntaxException

object SocketHandler {
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(userId: String) {
        try {
            val options = IO.Options()
            options.query = "userId=$userId"
            mSocket = IO.socket("http://10.0.2.2:5001", options)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getSocket(): Socket{
        return mSocket
    }

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