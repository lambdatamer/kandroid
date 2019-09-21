package me.lambdatamer.kandroid.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.lambdatamer.kandroid.viewmodel.KViewModelOwner
import org.kodein.di.Kodein
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.kodein
import org.kodein.di.android.subKodein

abstract class KFragment : Fragment(), CoroutineScope by MainScope(), KViewModelOwner {
    open val dependencies: Set<Kodein.Module> = emptySet()

    final override val kodein by lazy {
        subKodein(kodein(requireActivity().applicationContext)) {
            importAll(dependencies)
        }
    }

    final override val kodeinTrigger = KodeinTrigger()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*
        * We retrieve all our dependencies in onAttach because this way
        * we can be sure that all our dependencies have correctly been retrieved,
        * meaning that there were no non-declared dependency or dependency loop.
        * */
        kodeinTrigger.trigger()
    }
}