package me.lambdatamer.kandroid.components

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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

    fun <T> LiveData<T>.observe(
        lifecycleOwner: LifecycleOwner,
        observer: (item: T?) -> Unit
    ) = observe(lifecycleOwner, Observer(observer))

    fun <T> LiveData<T>.observeNonNull(
        lifecycleOwner: LifecycleOwner,
        observer: (item: T) -> Unit
    ) = observe(lifecycleOwner, Observer { it?.let(observer) })

    fun <T> LiveData<T>.observe(observer: (item: T?) -> Unit) =
        observe(this@KActivity, Observer(observer))

    fun <T> LiveData<T>.observeNonNull(observer: (item: T) -> Unit) =
        observeNonNull(this@KActivity, observer)
}