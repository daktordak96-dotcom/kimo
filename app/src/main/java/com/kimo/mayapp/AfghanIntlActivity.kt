package com.kimo.mayapp

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class AfghanIntlActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌های افغان بیسیم
    private val NEON = "#29B6FF"
    private val DEEP = "#0D47A1"
    private val PAGE = "#0A1020"
    private val CARD = "#0F1626"
    private val GOLD = "#FFD54F"

    private var lang = "fa"
    private var isRtl = true

    private class Zone(val code: String, val rate: String, val fa: String, val en: String)

    private val zones = listOf(
        Zone("A", "5.99", "استرالیا، هند، هنگ‌کنگ، سنگاپور، آفریقای جنوبی", "Australia, India, Hong Kong, Singapore, South Africa"),
        Zone("B", "3.99", "پاکستان، کانادا، چین، امریکا", "Pakistan, Canada, China, USA"),
        Zone("C", "9.99", "ژاپن، تایلند، اسپانیا، ویتنام، رومانی", "Japan, Thailand, Spain, Vietnam, Romania"),
        Zone("D", "12", "مصر، اندونزی، نپال، نیجریه، لهستان، ترکیه", "Egypt, Indonesia, Nepal, Nigeria, Poland, Turkey"),
        Zone("E", "20", "بریتانیا، ایتالیا، فیلیپین، سریلانکا، آرژانتین", "UK, Italy, Philippines, Sri Lanka, Argentina"),
        Zone("F", "25", "اتریش، غنا، کنیا، مراکش، عمان", "Austria, Ghana, Kenya, Morocco, Oman"),
        Zone("G", "35", "آلبانی، الجزایر، اوکراین، زامبیا، زیمبابوه", "Albania, Algeria, Ukraine, Zambia, Zimbabwe"),
        Zone("H", "50", "بلاروس، شیلی، موریتانی، سنگال", "Belarus, Chile, Mauritania, Senegal"),
        Zone("I", "200", "کوبا، ماداگاسکار، مالدیو، تونس", "Cuba, Madagascar, Maldives, Tunisia"),
        Zone("J", "600", "خدمات ماهواره‌ای (اینمارست، ایریدیوم)", "Satellite services (Inmarsat, Iridium)"),
        Zone("K", "1000", "شبکه‌های ماهواره‌ای ویژه", "Premium satellite networks"),
        Zone("L", "11.99", "ایران", "Iran"),
        Zone("L", "30", "پرتغال", "Portugal")
    )

    override fun attachBaseContext(newBase: Context?) {
        val p = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val l = p?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        val locale = Locale(l)
        Locale.setDefault(locale)
        val config = Configuration(newBase!!.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        lang = Strings.currentLang(this)
        isRtl = lang != "en"
        window.statusBarColor = c(PAGE)
        window.navigationBarColor = c(PAGE)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(c(PAGE))
            layoutDirection = if (isRtl) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        }

        // سربرگ آبی
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(8), dp(12), dp(16), dp(12))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(c(DEEP), c("#1E88E5"), c(NEON))
            ).apply { cornerRadius = dp(20).toFloat(); setStroke(dp(1), c("#80D8FF")) }
            elevation = dp(8).toFloat()
            glow(this)
        }
        header.addView(TextView(this).apply {
            text = if (isRtl) "→" else "←"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(dp(44), dp(44)))
        header.addView(TextView(this).apply {
            text = pick("بسته‌های بین‌المللی افغان بیسیم", "Afghan Wireless International", "د افغان بېسیم نړیوالې بستې")
            textSize = 19f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(6) })
        root.addView(header, LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(10), dp(10), dp(10), dp(6)) })

        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(6), dp(12), dp(24))
        }

        list.addView(sectionTitle(pick("نرخ تماس بین‌المللی (بر اساس منطقه)", "International Call Rates by Zone", "نړیوال زنګ نرخونه (د سیمې له مخې)")))
        zones.forEach { list.addView(zoneCard(it)) }

        list.addView(sectionTitle(pick("بسته‌های ویژه", "Special Promotional Bundles", "ځانګړې بستې")))
        list.addView(promoCard(
            pick("۱۳ کشور پرطرفدار", "ILD Top 13 Countries", "۱۳ مشهور هېوادونه"),
            pick("پاکستان، هند، امریکا، کانادا، سوئد، استرالیا، بلغارستان، بنگلادش، دانمارک، نروژ، بلژیک، چین، مالزی",
                "Pakistan, India, USA, Canada, Sweden, Australia, Bulgaria, Bangladesh, Denmark, Norway, Belgium, China, Malaysia",
                "پاکستان، هند، امریکا، کاناډا، سویډن، استرالیا، بلغاریا، بنګله‌دېش، ډنمارک، ناروې، بلجیم، چین، مالیزیا")
        ))
        list.addView(promoCard(
            pick("۵ کشور پرطرفدار", "ILD Top 5 Countries", "۵ مشهور هېوادونه"),
            pick("امریکا، کانادا، هند، مالزی، چین", "USA, Canada, India, Malaysia, China",
                "امریکا، کاناډا، هند، مالیزیا، چین")
        ))

        list.addView(TextView(this).apply {
            text = pick(
                "توجه: همه نرخ‌ها به افغانی است و ممکن است تغییر کند. برای نرخ دقیق با خدمات مشتریان افغان بیسیم تماس بگیرید.",
                "Note: All rates are in AFN and subject to change. Contact customer service for current rates.",
                "یادونه: ټول نرخونه په افغانیو دي او بدلېدای شي. د دقیق نرخ لپاره له پېرېدونکو خدمتونو سره اړیکه ونیسئ."
            )
            textSize = 13f
            setTextColor(c("#9FB0D0"))
            setLineSpacing(dp(3).toFloat(), 1f)
            setPadding(dp(14), dp(12), dp(14), dp(12))
            background = rounded(c("#101A30"), 14, c("#1E3A5F"))
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(16) })

        val scroll = ScrollView(this).apply { isVerticalScrollBarEnabled = false; addView(list) }
        root.addView(scroll, LinearLayout.LayoutParams(MATCH, 0, 1f))
        setContentView(root)
    }

    private fun sectionTitle(t: String): View = TextView(this).apply {
        text = t
        textSize = 17f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(c(NEON))
        setPadding(dp(4), dp(16), dp(4), dp(8))
    }

    private fun zoneCard(z: Zone): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR, intArrayOf(alpha(NEON, 0x26), c(CARD), c("#0B111F"))
            ).apply { cornerRadius = dp(18).toFloat(); setStroke(dp(1), alpha(NEON, 0x66)) }
            elevation = dp(4).toFloat()
            glow(this)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(5), 0, dp(5)) }
        }

        // دایرهٔ حرف منطقه
        card.addView(TextView(this).apply {
            text = z.code
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR, intArrayOf(c(NEON), c(DEEP))
            ).apply { shape = GradientDrawable.OVAL; setStroke(dp(2), alpha("#FFFFFF", 0x40)) }
        }, LinearLayout.LayoutParams(dp(48), dp(48)))

        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = pick("منطقه ${z.code}", "Zone ${z.code}", "سیمه ${z.code}")
            textSize = 13f
            setTextColor(c("#9FB0D0"))
        })
        texts.addView(TextView(this).apply {
            text = pick(z.fa, z.en, z.fa)
            textSize = 15f
            setTextColor(Color.WHITE)
            setLineSpacing(dp(2).toFloat(), 1f)
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(2) })
        card.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { marginStart = dp(12); marginEnd = dp(8) })

        card.addView(ratePill(z.rate))
        return card
    }

    private fun promoCard(title: String, countries: String): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR, intArrayOf(c("#0D3B73"), c("#0A2550"), c(CARD))
            ).apply { cornerRadius = dp(20).toFloat(); setStroke(dp(2), c(NEON)) }
            elevation = dp(8).toFloat()
            glow(this)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(6), 0, dp(6)) }
        }
        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        top.addView(TextView(this).apply {
            text = "⭐ $title"
            textSize = 17f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }, LinearLayout.LayoutParams(0, WRAP, 1f))
        top.addView(ratePill("3.75"))
        card.addView(top)
        card.addView(TextView(this).apply {
            text = countries
            textSize = 14f
            setTextColor(c("#D6E4FF"))
            setLineSpacing(dp(3).toFloat(), 1f)
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(10) })
        return card
    }

    private fun ratePill(rate: String): View = TextView(this).apply {
        text = pick("$rate افغانی\nهر دقیقه", "$rate AFN\nper min", "$rate افغانۍ\nهره دقیقه")
        textSize = 12f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(c(GOLD))
        gravity = Gravity.CENTER
        setPadding(dp(10), dp(6), dp(10), dp(6))
        background = rounded(c("#14203A"), 14, c(GOLD))
    }

    private fun glow(v: View) {
        if (Build.VERSION.SDK_INT >= 28) {
            v.outlineAmbientShadowColor = c(NEON)
            v.outlineSpotShadowColor = c(NEON)
        }
    }

    private fun rounded(fill: Int, r: Int, stroke: Int?) = GradientDrawable().apply {
        cornerRadius = dp(r).toFloat(); setColor(fill); if (stroke != null) setStroke(dp(1), stroke)
    }

    private fun c(h: String) = Color.parseColor(h)
    private fun alpha(h: String, a: Int): Int {
        val x = c(h); return Color.argb(a, Color.red(x), Color.green(x), Color.blue(x))
    }
    private fun pick(fa: String, en: String, ps: String) = when (lang) { "en" -> en; "ps" -> ps; else -> fa }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}