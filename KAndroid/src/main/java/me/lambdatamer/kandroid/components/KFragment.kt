package me.lambdatamer.kandroid.components

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.lambdatamer.kandroid.extensions.getColorCompat
import me.lambdatamer.kandroid.viewmodel.KViewModelOwner
import org.kodein.di.Kodein
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.kodein
import org.kodein.di.android.subKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.on

@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class KFragment : Fragment(), CoroutineScope by MainScope(), KViewModelOwner {
    open val forceHideKeyboardOnDestroyView = true

    open val dependencies: Set<Kodein.Module> = emptySet()

    val kActivity get() = requireActivity() as KActivity

    final override val kodeinTrigger = KodeinTrigger()

    final override val kodein by subKodein(kodein { requireContext() }) {
        importAll(dependencies)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*
        * We retrieve all our dependencies in onAttach because this way
        * we can be sure that all our dependencies have correctly been retrieved,
        * meaning that there were no non-declared dependency or dependency loop.
        * */
        kodeinTrigger.trigger()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (forceHideKeyboardOnDestroyView) {
            val inputManager by kodein.on(requireContext()).instance<InputMethodManager>()
            inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    fun getColor(@ColorRes colorRes: Int) = requireContext().getColorCompat(colorRes)

    fun <T> LiveData<T>.observe(
        lifecycleOwner: LifecycleOwner,
        observer: (item: T?) -> Unit
    ) = observe(lifecycleOwner, Observer(observer))

    fun <T> LiveData<T>.observeNonNull(
        lifecycleOwner: LifecycleOwner,
        observer: (item: T) -> Unit
    ) = observe(lifecycleOwner, Observer { it?.let(observer) })

    fun <T> LiveData<T>.observe(observer: (item: T?) -> Unit) =
        observe(getSuitableLifecycleOwner(), Observer(observer))

    fun <T> LiveData<T>.observeNonNull(observer: (item: T) -> Unit) =
        observeNonNull(getSuitableLifecycleOwner(), observer)

    private fun getSuitableLifecycleOwner() = if (view != null) viewLifecycleOwner else this
}