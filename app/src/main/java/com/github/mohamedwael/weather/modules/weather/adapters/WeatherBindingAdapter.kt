package com.github.mohamedwael.weather.modules.weather.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.mohamedwael.weather.R
import com.github.mohamedwael.weather.setup.createIconUrl

@BindingAdapter("app:query", "app:submitQuery", requireAll = true)
fun bindQuery(searchView: SearchView, query: String?, submit: Boolean?) {
    query?.also {
        searchView.setQuery(query, submit ?: false)
    }
}

@BindingAdapter("app:queryTextListener")
fun setOnQueryTextListener(
    searchView: SearchView,
    listener: SearchView.OnQueryTextListener?
) {
    searchView.setOnQueryTextListener(listener)
}


@BindingAdapter("app:bindMaxMinTemperature")
fun bindMaxMinTemperature(tv: TextView, temperature: String?) {
    temperature?.also {
        val max = temperature.split(",")[0]
        val min = temperature.split(",")[1]
        tv.text = tv.context.getString(R.string.min_max_temp, max, min)
    }
}


@BindingAdapter("app:bindFeelsLike")
fun bindFeelsLike(tv: TextView, feelsLike: String?) {
    feelsLike?.also {
        tv.text = tv.context.getString(R.string.feels_like, feelsLike)
    }
}

@BindingAdapter("app:bindWeatherIcon")
fun bindFeelsLike(iv: ImageView, icon: String?) {
    icon?.also {
        Glide.with(iv).load(createIconUrl(it)).into(iv)
    }
}