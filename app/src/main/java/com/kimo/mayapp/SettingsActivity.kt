package com.kimo.mayapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: android.content.SharedPreferences
    private var lang: String = "fa"

    // اپراتورها: کلید ذخیره، متن سه‌زبانه، رنگ
    private val operators = listOf(
        Triple("op_afghan", Triple("افغان بیسیم", "Afghan Wireless", "افغان بېسیم"), "#29B6FF"),
        Triple("op_etisalat", Triple("اتصالات", "Etisalat", "اتصالات"), "#9CFF3A"),
        Triple("op_roshan", Triple("روشن", "Roshan", "روشن"), "#FF3B4F"),
        Triple("op_atoma", Triple("اتوما", "Atoma", "اتوما"), "#FFC928"),
        Triple("op_salaam", Triple("سلام", "Salaam", "سلام"), "#1DE9D0")
    )

    override fun attachBaseContext(newBase: Context?) {
        val p = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val l = p?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateBaseContextLocale(newBase!!, l))
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lang = Strings.currentLang(this)
        prefs = getSharedPreferences("AppSettings", MODE_PRIVATE)

        window.statusBarColor = c("#0A1020")
        window.navigationBarColor = c("#0A1020")

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(c("#0A1020"))

        root.addView(buildHeader())

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.setPadding(dpi(16f), dpi(16f), dpi(16f), dpi(24f))

        // بخش اپراتورها
        content.addView(sectionTitle(pick("اپراتورهای نمایش‌داده‌شده", "Visible operators", "ښکاره اپراتوران")))
        content.addView(sectionHint(pick(
            "اپراتورهایی را که استفاده نمی‌کنید خاموش کنید تا در صفحه اصلی نمایش داده نشوند.",
            "Turn off operators you don't use to hide them from the home screen.",
            "هغه اپراتوران چې نه یې کاروئ، ګلاس کړئ ترڅو په کوره پاڼه کې ښکاره نشي."
        )))
        content.addView(buildOperatorCard())

        // بخش زبان
        content.addView(sectionTitle(pick("زبان", "Language", "ژبه")))
        content.addView(buildLanguageCard())

        scroll.addView(content, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        setContentView(root)
    }

    // ---------- ابزارها ----------

    private fun dpf(v: Float) = v * resources.displayMetrics.density
    private fun dpi(v: Float) = (v * resources.displayMetrics.density).toInt()
    private fun c(hex: String) = Color.parseColor(hex)
    private fun alpha(hex: String, a: Int): Int {
        val col = c(hex); return Color.argb(a, Color.red(col), Color.green(col), Color.blue(col))
    }
    private fun pick(fa: String, en: String, ps: String) = when (lang) {
        "en" -> en; "ps" -> ps; else -> fa
    }
    private fun rounded(fill: Int, radiusDp: Float, stroke: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            cornerRadius = dpf(radiusDp); setColor(fill)
            if (stroke != null) setStroke(dpi(1f), stroke)
        }

    private fun setLanguage(code: String) {
        getSharedPreferences("Settings", MODE_PRIVATE)
            .edit().putString(SELECTED_LANGUAGE, code).apply()
    }

    // ---------- نوار بالا ----------

    private fun buildHeader(): View {
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER_VERTICAL
        bar.setBackgroundColor(c("#0E1628"))
        bar.setPadding(dpi(8f), dpi(12f), dpi(12f), dpi(12f))
        bar.elevation = dpf(6f)

        val back = TextView(this)
        back.text = "←"
        back.textSize = 22f
        back.setTextColor(Color.WHITE)
        back.gravity = Gravity.CENTER
        val bp = dpi(8f); back.setPadding(bp, bp, bp, bp)
        back.isClickable = true
        back.setOnClickListener { finish() }
        bar.addView(back, LinearLayout.LayoutParams(dpi(44f), dpi(44f)))

        val title = TextView(this)
        title.text = pick("تنظیمات", "Settings", "تنظیمات")
        title.textSize = 19f
        title.setTextColor(Color.WHITE)
        title.setTypeface(title.typeface, Typeface.BOLD)
        val tlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        tlp.marginStart = dpi(8f)
        bar.addView(title, tlp)

        return bar
    }

    private fun sectionTitle(text: String): View {
        val t = TextView(this)
        t.text = text
        t.textSize = 15f
        t.setTextColor(c("#7DF9FF"))
        t.setTypeface(t.typeface, Typeface.BOLD)
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = dpi(18f); lp.bottomMargin = dpi(8f)
        t.layoutParams = lp
        return t
    }

    private fun sectionHint(text: String): View {
        val t = TextView(this)
        t.text = text
        t.textSize = 12f
        t.setTextColor(c("#8A97B3"))
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.bottomMargin = dpi(10f)
        t.layoutParams = lp
        return t
    }

    // ---------- کارت اپراتورها ----------

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun buildOperatorCard(): View {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.background = rounded(c("#111A2E"), 16f, alpha("#FFFFFF", 0x14))
        card.setPadding(dpi(6f), dpi(6f), dpi(6f), dpi(6f))

        operators.forEachIndexed { index, op ->
            val (key, names, color) = op
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = Gravity.CENTER_VERTICAL
            row.setPadding(dpi(12f), dpi(14f), dpi(12f), dpi(14f))

            val dot = View(this)
            dot.background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL; setColor(c(color))
            }
            row.addView(dot, LinearLayout.LayoutParams(dpi(12f), dpi(12f)))

            val name = TextView(this)
            name.text = pick(names.first, names.second, names.third)
            name.textSize = 15f
            name.setTextColor(Color.WHITE)
            val nlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            nlp.marginStart = dpi(12f)
            row.addView(name, nlp)

            val sw = SwitchCompat(this)
            sw.isChecked = prefs.getBoolean(key, true)
            sw.setOnCheckedChangeListener { _, checked ->
                if (!checked && enabledCount() <= 1) {
                    sw.isChecked = true
                    Toast.makeText(this,
                        pick("حداقل یک اپراتور باید روشن بماند",
                             "At least one operator must stay on",
                             "لږ تر لږه یو اپراتور باید روښانه وي"),
                        Toast.LENGTH_SHORT).show()
                } else {
                    prefs.edit().putBoolean(key, checked).apply()
                }
            }
            row.addView(sw)

            card.addView(row)

            if (index < operators.size - 1) {
                val div = View(this)
                div.setBackgroundColor(alpha("#FFFFFF", 0x12))
                val dlp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dpi(0.6f).coerceAtLeast(1))
                dlp.marginStart = dpi(12f); dlp.marginEnd = dpi(12f)
                card.addView(div, dlp)
            }
        }

        return card
    }

    private fun enabledCount(): Int {
        var n = 0
        for (op in operators) if (prefs.getBoolean(op.first, true)) n++
        return n
    }

    // ---------- کارت زبان ----------

    private fun buildLanguageCard(): View {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.background = rounded(c("#111A2E"), 16f, alpha("#FFFFFF", 0x14))
        card.setPadding(dpi(6f), dpi(6f), dpi(6f), dpi(6f))

        val langs = listOf(
            Pair("fa", "فارسی"),
            Pair("ps", "پښتو"),
            Pair("en", "English")
        )

        langs.forEachIndexed { index, (code, label) ->
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = Gravity.CENTER_VERTICAL
            row.setPadding(dpi(12f), dpi(14f), dpi(12f), dpi(14f))
            row.isClickable = true

            val name = TextView(this)
            name.text = label
            name.textSize = 15f
            name.setTextColor(Color.WHITE)
            row.addView(name, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

            val check = TextView(this)
            check.text = if (code == lang) "✓" else ""
            check.textSize = 18f
            check.setTextColor(c("#7DF9FF"))
            check.setTypeface(check.typeface, Typeface.BOLD)
            row.addView(check)

            row.setOnClickListener {
                if (code != lang) {
                    setLanguage(code)
                    recreate()
                }
            }

            card.addView(row)

            if (index < langs.size - 1) {
                val div = View(this)
                div.setBackgroundColor(alpha("#FFFFFF", 0x12))
                val dlp = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dpi(0.6f).coerceAtLeast(1))
                dlp.marginStart = dpi(12f); dlp.marginEnd = dpi(12f)
                card.addView(div, dlp)
            }
        }

        return card
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}