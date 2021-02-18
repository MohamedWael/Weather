package com.github.mohamedwael.weather.modules.weather.dto

import com.google.gson.annotations.SerializedName

data class Clouds(

	@field:SerializedName("all")
	val all: Int? = null
)