package me.lambdatamer.kandroid

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

@Suppress("unused")
abstract class KApplication : Application(), KodeinAware {
    abstract val rootModule: Kodein.Module

    override val kodein = Kodein.lazy {
        import(androidXModule(this@KApplication))
        import(kAndroidModule)

        import(rootModule)
    }
}
