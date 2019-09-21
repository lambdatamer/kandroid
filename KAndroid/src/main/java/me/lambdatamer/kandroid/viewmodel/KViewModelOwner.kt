package me.lambdatamer.kandroid.viewmodel

import org.kodein.di.KodeinAware
import org.kodein.di.KodeinProperty
import org.kodein.di.generic.instance

interface KViewModelOwner : KodeinAware

@Suppress("unused")
inline fun <reified T : KViewModel> KViewModelOwner.viewModel(
    noinline ownerProvider: () -> KViewModelOwner = { this }
): KodeinProperty<T> = instance(arg = ownerProvider)