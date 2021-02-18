package com.github.mohamedwael.weather.setup

const val BASE_URL = "openweathermap.org"
const val END_POINT = "https://api.$BASE_URL/data/2.5/"

fun createIconUrl(icon: String) = "https://$BASE_URL/img/wn/$icon@4x.png"




