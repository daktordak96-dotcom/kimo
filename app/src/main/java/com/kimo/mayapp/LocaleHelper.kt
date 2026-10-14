package com.kimo.mayapp

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }

    fun getLanguage(context: Context): String =
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString(SELECTED_LANGUAGE, "fa") ?: "fa"

    fun wrap(context: Context): Context =
        updateResources(context, getLanguage(context))

    private fun persist(context: Context, language: String) {
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .edit().putString(SELECTED_LANGUAGE, language).apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}