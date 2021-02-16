package com.github.mohamedwael.weather.modules.weather

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

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