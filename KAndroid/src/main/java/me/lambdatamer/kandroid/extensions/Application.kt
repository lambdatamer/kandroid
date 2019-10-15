package me.lambdatamer.kandroid.extensions

import android.app.Application

val Application.name: String get() = applicationInfo.loadLabel(packageManager).toString()