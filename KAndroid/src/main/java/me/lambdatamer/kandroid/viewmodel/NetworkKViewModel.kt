package me.lambdatamer.kandroid.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.lambdatamer.kandroid.atom.Atom
import me.lambdatamer.kandroid.system.NetworkConnectionReceiver
import org.kodein.di.generic.instance

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class NetworkKViewModel(application: Application) : KViewModel(application) {

    private val connection by instance<NetworkConnectionReceiver>()

    val isConnectedToNetwork by lazy { connection.observeNetworkAvailable().toLiveData() }

    @Suppress("ThrowableNotThrown")
    final override fun <T> launchRequestInternal(
        liveData: MutableLiveData<Atom<T>>,
        block: suspend () -> T
    ) = launch {
        val result = if (isConnectedToNetwork.value != true) {
            Atom.Error(NoInternetException())
        } else {
            withContext(Dispatchers.Main) { liveData.value = Atom.Loading() }
            try {
                Atom.Success(block())
            } catch (e: Throwable) {
                val isConnected = isConnectedToNetwork.value == true
                val actualException = if (isConnected) e else NoInternetException(e)
                L.e(actualException)
                Atom.Error<T>(actualException)
            }
        }
        liveData.postValue(result)
    }
}