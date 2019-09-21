package me.lambdatamer.kandroid

import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

internal val kAndroidModule = Kodein.Module("KAndroid") {
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelProvider.AndroidViewModelFactory(instance())
    }
}