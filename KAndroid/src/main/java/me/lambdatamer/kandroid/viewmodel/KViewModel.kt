package me.lambdatamer.kandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class KViewModel(application: Application) : AndroidViewModel(application), KodeinAware {
    final override val kodein by kodein()
}

