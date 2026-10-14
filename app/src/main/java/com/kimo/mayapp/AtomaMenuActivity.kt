package com.kimo.mayapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class AtomaMenuActivity : AppCompatActivity() {

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

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        currentLang = preferences.getString(SELECTED_LANGUAGE, "fa") ?: "fa"

        buildAtomaScreen()
    }

    private fun buildAtomaScreen() {
        val blueMain = Color.parseColor("#1E6BFF")
        val blueLight = Color.parseColor("#4DB8FF")
        val pageTop = Color.parseColor("#030B22")
        val pageBottom = Color.parseColor("#061A4A")

        window.statusBarColor = pageTop
        window.navigationBarColor = pageBottom

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(pageTop, Color.parseColor("#051338"), pageBottom)
            )
        }

        // هدر
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setPadding(dp(20), dp(14), dp(20), dp(6))
        }
        header.addView(ImageView(this).apply {
            setImageResource(android.R.drawable.ic_menu_compass)
            setColorFilter(Color.WHITE)
            scaleType = ImageView.ScaleType.FIT_CENTER
            layoutParams = LinearLayout.LayoutParams(dp(52), dp(52))
        })
        header.addView(TextView(this).apply {
            text = pick(currentLang, "اتوما", "ATOMA", "اتوما")
            textSize = 26f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            setShadowLayer(16f, 0f, 0f, blueLight)
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = dp(12)
            }
        })
        root.addView(header)

        // منطقه کارت‌ها
        val area = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            clipChildren = false
            clipToPadding = false
            setPadding(dp(8), dp(4), dp(8), dp(12))
        }

        val cInternet = atomaCard(android.R.drawable.ic_menu_mapmode, pick(currentLang, "بسته‌های اینترنتی", "Internet Packages", "د انټرنټ بستې"), blueMain, blueLight, false) { openAtomaKind("internet") }
        val cSms = atomaCard(android.R.drawable.ic_dialog_email, pick(currentLang, "بسته‌های پیام", "SMS Packages", "د پیغام بستې"), blueMain, blueLight, false) { openAtomaKind("sms") }
        val cCall = atomaCard(android.R.drawable.ic_menu_call, pick(currentLang, "بسته‌های تماس", "Call Packages", "د زنګ بستې"), blueMain, blueLight, false) { openAtomaKind("call") }
        val cSrv = atomaCard(android.R.drawable.ic_menu_manage, pick(currentLang, "خدمات", "Services", "خدمتونه"), blueMain, blueLight, false) { openAtomaKind("services") }
        val cIntl = atomaCard(android.R.drawable.ic_menu_mapmode, pick(currentLang, "بسته‌های بین‌المللی", "International Packages", "نړیوال بستې"), blueMain, blueLight, true) { openAtomaKind("intl") }

        fun weightedRow(a: View, b: View): LinearLayout {
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                clipChildren = false
                clipToPadding = false
            }
            row.addView(a, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            row.addView(b, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            return row
        }

        area.addView(weightedRow(cInternet, cSms), LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        area.addView(weightedRow(cCall, cSrv), LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        area.addView(cIntl, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        root.addView(area, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }

    private fun atomaCard(
        iconRes: Int,
        title: String,
        blueMain: Int,
        blueLight: Int,
        wide: Boolean,
        click: () -> Unit
    ): View {
        val radius = dp(22).toFloat()

        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#0B3FB8"), Color.parseColor("#1E6BFF"))
            )
        }
        bar.addView(TextView(this).apply {
            text = title
            textSize = if (wide) 17f else 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        })
        bar.addView(TextView(this).apply {
            text = "›"
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(blueLight)
        })

        val icon = ImageView(this).apply {
            setImageResource(iconRes)
            setColorFilter(Color.WHITE)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(dp(8), dp(8), dp(8), dp(8))
        }

        val content: View = if (wide) {
            LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                gravity = Gravity.CENTER_VERTICAL
                addView(bar, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
                addView(icon, LinearLayout.LayoutParams(dp(130), ViewGroup.LayoutParams.MATCH_PARENT))
            }
        } else {
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                addView(icon, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
                addView(bar, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            }
        }

        val inner = FrameLayout(this).apply {
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.parseColor("#0A2A78"), Color.parseColor("#06194D"), Color.parseColor("#041033"))
            ).apply {
                cornerRadius = radius
                setStroke(dp(2), blueLight)
            }
            setPadding(dp(2), dp(2), dp(2), dp(2))
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
            elevation = dp(10).toFloat()
            addView(content, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }

        val spread = dp(10)
        val steps = 8
        val layers = Array(steps) { idx ->
            val remaining = steps - idx
            GradientDrawable().apply {
                setColor(Color.argb(14 + idx * 4, Color.red(blueMain), Color.green(blueMain), Color.blue(blueMain)))
                cornerRadius = radius + remaining * (spread / steps.toFloat())
            }
        }
        val glow = LayerDrawable(layers)
        for (idx in 0 until steps) {
            val inset = (idx * (spread / steps.toFloat())).toInt()
            glow.setLayerInset(idx, inset, inset, inset, inset)
        }

        return FrameLayout(this).apply {
            clipChildren = false
            clipToPadding = false
            background = glow
            setPadding(spread, spread, spread, spread)
            addView(inner, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            isClickable = true
            isFocusable = true
            setOnClickListener { click() }
        }
    }

    private fun openAtomaKind(kind: String) {
        startActivity(Intent(this, AtomaActivity::class.java).apply {
            putExtra("OPERATOR", "ATOMA")
            putExtra("KIND", kind)
        })
    }

    private fun pick(lang: String, fa: String, en: String, ps: String): String = when (lang) {
        "en" -> en
        "ps" -> ps
        else -> fa
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}
