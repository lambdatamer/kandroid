package me.lambdatamer.kandroid.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.lambdatamer.kandroid.viewmodel.KViewModelOwner
import org.kodein.di.Kodein
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.kodein
import org.kodein.di.android.subKodein

abstract class KActivity : AppCompatActivity(), CoroutineScope by MainScope(), KViewModelOwner {
    open val dependencies: Set<Kodein.Module> = emptySet()

    final override val kodein by subKodein(kodein()) {
        importAll(dependencies)
    }

    final override val kodeinTrigger = KodeinTrigger()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        * We retrieve all our dependencies in onCreate because this way
        * we can be sure that all our dependencies have correctly been retrieved,
        * meaning that there were no non-declared dependency or dependency loop.
        * */
        kodeinTrigger.trigger()
    }
}