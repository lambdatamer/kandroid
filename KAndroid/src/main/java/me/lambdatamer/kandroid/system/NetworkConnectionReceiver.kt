package me.lambdatamer.kandroid.system

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.Channel

@Suppress("unused")
class NetworkConnectionReceiver(private val connectivityManager: ConnectivityManager) {
    fun observeNetworkAvailable(): Channel<Boolean> {
        val channel = Channel<Boolean>(Channel.CONFLATED)

        channel.offer(connectivityManager.activeNetworkInfo?.isConnected ?: false)

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                channel.offer(true)
            }

            override fun onLost(network: Network?) {
                channel.offer(false)
            }
        }

        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)

        channel.invokeOnClose { connectivityManager.unregisterNetworkCallback(callback) }

        return channel
    }
}