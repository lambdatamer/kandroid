@file:Suppress("unused")

package me.lambdatamer.kandroid.viewmodel

import androidx.lifecycle.ViewModelProviders
import me.lambdatamer.kandroid.activity.KActivity
import me.lambdatamer.kandroid.fragment.KFragment
import me.lambdatamer.kandroid.extensions.className
import org.kodein.di.Kodein
import org.kodein.di.generic.contexted
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

inline fun <reified T : KViewModel> Kodein.Builder.viewModel() =
    contexted<KViewModelOwner>().factory { ownerProvider: () -> KViewModelOwner ->
        when (val owner = ownerProvider()) {
            is KActivity -> ViewModelProviders.of(owner, instance())
            is KFragment -> ViewModelProviders.of(owner.requireActivity(), instance())
            else -> throw IllegalArgumentException(
                """
                    Tried to receive ${T::class.java.simpleName} of ${owner.className}.
                    KViewModel can only be bound with KActivity or KFragment lifecycle.
                """.trimIndent()
            )
        }.get(T::class.java)
    }