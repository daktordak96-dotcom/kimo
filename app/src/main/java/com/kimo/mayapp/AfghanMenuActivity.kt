package com.kimo.mayapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale

class AfghanMenuActivity : AppCompatActivity() {

    private var currentLang: String = "fa"

    override fun attachBaseContext(newBase: Context?) {
        val preferences = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = preferences?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateBaseContextLocale(newBase!!, lang))
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afghan_menu)

        val preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        currentLang = preferences.getString(SELECTED_LANGUAGE, "fa") ?: "fa"

        findViewById<TextView>(R.id.txtHeaderTitle)?.text = pick(currentLang, "افغان بیسیم", "Afghan Wireless", "افغان بېسېم")
        updateTexts(currentLang)

        findViewById<CardView>(R.id.cardInternet)?.setOnClickListener {
            startActivity(Intent(this, AfghanInternetActivity::class.java))
        }
        findViewById<CardView>(R.id.cardSmsPackages)?.setOnClickListener {
            startActivity(Intent(this, AfghanSmsActivity::class.java))
        }
        findViewById<CardView>(R.id.cardCallPackages)?.setOnClickListener {
            startActivity(Intent(this, CallPackagesActivity::class.java).putExtra("OPERATOR", "AFGHAN"))
        }
        findViewById<CardView>(R.id.cardServices)?.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java).putExtra("OPERATOR", "AFGHAN"))
        }
        findViewById<CardView>(R.id.cardInternationalPackages)?.setOnClickListener {
            startActivity(Intent(this, InternationalPackagesActivity::class.java).putExtra("OPERATOR", "AFGHAN"))
        }
    }

    private fun pick(lang: String, fa: String, en: String, ps: String): String = when (lang) {
        "en" -> en
        "ps" -> ps
        else -> fa
    }

    private fun updateTexts(lang: String) {
        findViewById<TextView>(R.id.txtInternetPackages)?.text = pick(lang, "بسته‌های اینترنتی", "Internet Packages", "د انټرنټ بستې")
        findViewById<TextView>(R.id.txtSmsPackages)?.text = pick(lang, "بسته‌های پیام", "SMS Packages", "د پیغام بستې")
        findViewById<TextView>(R.id.txtCallPackages)?.text = pick(lang, "بسته‌های تماس", "Call Packages", "د زنګ بستې")
        findViewById<TextView>(R.id.txtServices)?.text = pick(lang, "خدمات", "Services", "خدمتونه")
        findViewById<TextView>(R.id.txtInternationalPackages)?.text = pick(lang, "بسته‌های بین‌المللی", "International Packages", "نړیوال بستې")
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}
