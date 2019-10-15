@file:Suppress("unused")

package me.lambdatamer.kandroid.extensions

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.AnimatorRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.startActivityIfExist(intent: Intent) = (intent.resolveActivity(packageManager) != null)
    .also { isActivityExist -> if (isActivityExist) startActivity(intent) }

fun Context.getColorCompat(@ColorRes colorRes: Int) =
    ContextCompat.getColor(this, colorRes)

fun Number.dpToInt(context: Context) =
    (toFloat() * context.resources.displayMetrics.density).toInt()

fun Context.loadStateListAnimator(@AnimatorRes animatorRes: Int) =
    AnimatorInflater.loadStateListAnimator(this, animatorRes)