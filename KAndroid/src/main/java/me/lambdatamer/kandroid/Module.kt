package me.lambdatamer.kandroid

import androidx.lifecycle.ViewModelProvider
import me.lambdatamer.kandroid.system.NetworkConnectionReceiver
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal val kAndroidModule = Kodein.Module("KAndroid") {
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelProvider.AndroidViewModelFactory(instance())
    }

    bind() from singleton { NetworkConnectionReceiver(instance()) }
}