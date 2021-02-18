package com.github.mohamedwael.weather.setup

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("MMMM dd, hh:mm a", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}