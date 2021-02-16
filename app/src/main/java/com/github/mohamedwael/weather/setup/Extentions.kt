package com.github.mohamedwael.weather.setup

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * check and requests the required permissions
 * @return true if some or all permissions are needed otherwise false
 */
fun Fragment.needPermissions(vararg permissions: String, requestCode: Int = 105): Boolean? =
    context?.let {
        val deniedPermissions = checkPermissions(permissions)
        deniedPermissions?.also {
            if (it.isNotEmpty()) requestPermissions(it.toTypedArray(), requestCode)
        }
        return deniedPermissions?.isNotEmpty()
    }

/**
 * @return list of denied permissions
 */
fun Fragment.checkPermissions(permissions: Array<out String>) = context?.let {
    permissions.filter { permission ->
        ContextCompat.checkSelfPermission(it, permission) != PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

