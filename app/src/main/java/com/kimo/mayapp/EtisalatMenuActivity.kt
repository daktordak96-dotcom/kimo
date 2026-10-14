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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale

class EtisalatMenuActivity : AppCompatActivity() {

    private var operatorKey: String = "etisalat"
    private var operatorDisplayName: String = "اتصالات"
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

        operatorKey = intent.getStringExtra("OPERATOR_KEY")
            ?: intent.getStringExtra("OPERATOR_NAME")
            ?: "etisalat"
        operatorDisplayName = getOperatorNameByKey(operatorKey, currentLang)

        // اتصالات: طرح سبز تمام‌صفحه
        if (isEtisalat()) {
            buildEtisalatScreen()
            return
        }

        setContentView(R.layout.activity_etisalat_menu)
        findViewById<TextView>(R.id.txtHeaderTitle)?.text = operatorDisplayName
        updateTexts(currentLang)

        if (isRoshan()) applyRoshanTheme()

        findViewById<CardView>(R.id.cardInternet)?.setOnClickListener { openInternet() }
        findViewById<CardView>(R.id.cardSmsPackages)?.setOnClickListener { openSms() }
        findViewById<CardView>(R.id.cardCallPackages)?.setOnClickListener { openCall() }
        findViewById<CardView>(R.id.cardServices)?.setOnClickListener { openServices() }
        findViewById<CardView>(R.id.cardInternationalPackages)?.setOnClickListener { openIntl() }
    }

    // ---------------- اکشن‌ها ----------------

    private fun openInternet() {
        when {
            isAfghan() -> go(AfghanInternetActivity::class.java)
            isRoshan() -> go(RoshanInternetActivity::class.java)
            else -> open(PackagesContainerActivity::class.java)
        }
    }

    private fun openSms() {
        when {
            isAfghan() -> go(AfghanSmsActivity::class.java)
            isRoshan() -> go(RoshanInternetActivity::class.java, "sms")
            else -> open(SmsPackagesActivity::class.java)
        }
    }

    private fun openCall() {
        if (isRoshan()) go(RoshanInternetActivity::class.java, "call")
        else open(CallPackagesActivity::class.java)
    }

    private fun openServices() {
        if (isRoshan()) go(RoshanInternetActivity::class.java, "services")
        else open(ServicesActivity::class.java)
    }

    private fun openIntl() {
        if (isRoshan()) go(RoshanInternetActivity::class.java, "intl")
        else open(InternationalPackagesActivity::class.java)
    }

    // ---------------- صفحهٔ سبز اتصالات (تمام‌صفحه) ----------------

    private fun buildEtisalatScreen() {
        val greenMain = Color.parseColor("#4CD137")
        val greenLight = Color.parseColor("#9CFF57")
        val pageTop = Color.parseColor("#031208")
        val pageBottom = Color.parseColor("#06260F")

        window.statusBarColor = pageTop
        window.navigationBarColor = pageBottom

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(pageTop, Color.parseColor("#041A0B"), pageBottom)
            )
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setPadding(dp(24), dp(16), dp(24), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#0B5A1F"), Color.parseColor("#1FA02F"), Color.parseColor("#4CD137"))
            ).apply {
                cornerRadius = dp(20).toFloat()
                setStroke(dp(1), greenLight)
            }
        }
        header.addView(TextView(this).apply {
            text = operatorDisplayName
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        })
        root.addView(header, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(dp(10), dp(10), dp(10), dp(4))
        })

        val area = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            clipChildren = false
            clipToPadding = false
            setPadding(dp(8), dp(4), dp(8), dp(12))
        }

        val cInternet = darkCard(android.R.drawable.ic_menu_mapmode, pick(currentLang, "بسته‌های اینترنتی", "Internet Packages", "د انټرنټ بستې"), greenMain, greenLight, "#0B3A18", "#0E5A24", false) { openInternetE() }
        val cSms = darkCard(android.R.drawable.ic_dialog_email, pick(currentLang, "بسته‌های پیام", "SMS Packages", "د پیغام بستې"), greenMain, greenLight, "#0B3A18", "#0E5A24", false) { openSmsE() }
        val cCall = darkCard(android.R.drawable.ic_menu_call, pick(currentLang, "بسته‌های تماس", "Call Packages", "د زنګ بستې"), greenMain, greenLight, "#0B3A18", "#0E5A24", false) { openCallE() }
        val cSrv = darkCard(android.R.drawable.ic_menu_manage, pick(currentLang, "خدمات", "Services", "خدمتونه"), greenMain, greenLight, "#0B3A18", "#0E5A24", false) { openServicesE() }
        val cIntl = darkCard(android.R.drawable.ic_menu_mapmode, pick(currentLang, "بسته‌های بین‌المللی", "International Packages", "نړیوال بستې"), greenMain, greenLight, "#0B3A18", "#0E5A24", true) { openIntlE() }

        fun row(a: View, b: View): LinearLayout {
            val r = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                clipChildren = false
                clipToPadding = false
            }
            r.addView(a, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            r.addView(b, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            return r
        }

        area.addView(row(cInternet, cSms), LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        area.addView(row(cCall, cSrv), LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        area.addView(cIntl, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        root.addView(area, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }

    private fun openInternetE() = open(PackagesContainerActivity::class.java)
    private fun openSmsE() = open(SmsPackagesActivity::class.java)
    private fun openCallE() = open(CallPackagesActivity::class.java)
    private fun openServicesE() = open(ServicesActivity::class.java)
    private fun openIntlE() = open(InternationalPackagesActivity::class.java)

    private fun darkCard(
        iconRes: Int,
        title: String,
        accent: Int,
        accentLight: Int,
        colorA: String,
        colorB: String,
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
                intArrayOf(Color.parseColor("#0B5A1F"), Color.parseColor("#1FA02F"))
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
            setTextColor(accentLight)
        })

        val icon = ImageView(this).apply {
            setImageResource(iconRes)
            setColorFilter(Color.WHITE)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(dp(6), dp(6), dp(6), dp(6))
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
                intArrayOf(Color.parseColor(colorB), Color.parseColor(colorA), Color.parseColor("#041A0B"))
            ).apply {
                cornerRadius = radius
                setStroke(dp(2), accentLight)
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
                setColor(Color.argb(14 + idx * 4, Color.red(accent), Color.green(accent), Color.blue(accent)))
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

    // ---------------- طرح روشن (قبلی) ----------------

    private fun applyRoshanTheme() {
        val pageDark = Color.parseColor("#1A0509")
        val navDark = Color.parseColor("#1F0610")
        val redGlow = Color.parseColor("#FF1F3D")
        val redLight = Color.parseColor("#FF5A5F")
        val gold = Color.parseColor("#FFD54F")

        window.statusBarColor = pageDark
        window.navigationBarColor = navDark

        val content = window.decorView.findViewById<ViewGroup>(android.R.id.content)
        val root = content.getChildAt(0) as? ViewGroup ?: return
        root.background = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor("#1A0509"),
                Color.parseColor("#2A0812"),
                Color.parseColor("#1F0610")
            )
        )

        val header = root.getChildAt(0) as? LinearLayout
        if (header != null) {
            header.background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    Color.parseColor("#8A000F"),
                    Color.parseColor("#E5102D"),
                    Color.parseColor("#FF2D55"),
                    Color.parseColor("#FF5A7A")
                )
            ).apply {
                cornerRadius = dp(20).toFloat()
                setStroke(dp(1), gold)
            }
            (header.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                it.setMargins(dp(10), dp(8), dp(10), dp(8))
                header.layoutParams = it
            }
            header.clipToOutline = true
            header.outlineProvider = ViewOutlineProvider.BACKGROUND
            header.elevation = dp(8).toFloat()
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                header.outlineAmbientShadowColor = redGlow
                header.outlineSpotShadowColor = redGlow
            }
        }
        findViewById<TextView>(R.id.txtHeaderTitle)?.apply {
            setTextColor(Color.WHITE)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
        }

        val scroll = root.getChildAt(1) as? ViewGroup
        scroll?.clipToPadding = false
        scroll?.clipChildren = false
        (scroll?.getChildAt(0) as? ViewGroup)?.clipChildren = false
        ((scroll?.getChildAt(0) as? ViewGroup)?.getChildAt(0) as? ViewGroup)?.clipChildren = false

        val cardIds = listOf(
            R.id.cardInternet,
            R.id.cardSmsPackages,
            R.id.cardCallPackages,
            R.id.cardServices,
            R.id.cardInternationalPackages
        )
        val textIds = listOf(
            R.id.txtInternetPackages,
            R.id.txtSmsPackages,
            R.id.txtCallPackages,
            R.id.txtServices,
            R.id.txtInternationalPackages
        )

        cardIds.forEach { id ->
            val card = findViewById<CardView>(id) ?: return@forEach
            card.setCardBackgroundColor(Color.parseColor("#7A0A1C"))
            card.radius = dp(20).toFloat()
            card.cardElevation = dp(14).toFloat()
            card.clipChildren = false
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                card.outlineAmbientShadowColor = redGlow
                card.outlineSpotShadowColor = redGlow
            }
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                card.foreground = GradientDrawable().apply {
                    setColor(Color.TRANSPARENT)
                    cornerRadius = dp(20).toFloat()
                    setStroke(dp(3), redLight)
                }
            }
        }

        textIds.forEach { id ->
            findViewById<TextView>(id)?.apply {
                setTextColor(Color.WHITE)
                textSize = 15f
                typeface = Typeface.DEFAULT_BOLD
                setPadding(dp(8), dp(6), dp(8), dp(6))
                background = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(
                        Color.parseColor("#B30012"),
                        Color.parseColor("#FF1F3D"),
                        Color.parseColor("#FF5A5F")
                    )
                )
                (layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                    it.width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.topMargin = 0
                    layoutParams = it
                }
                gravity = Gravity.CENTER
            }
        }
    }

    // ---------------- کمکی‌ها ----------------

    private fun isEtisalat(): Boolean =
        operatorKey.toLowerCase(Locale.ROOT) in setOf("etisalat", "اتصالات")

    private fun isAfghan(): Boolean =
        operatorKey.toLowerCase(Locale.ROOT) in setOf("afghan", "afghan wireless", "افغان بیسیم")

    private fun isRoshan(): Boolean =
        operatorKey.toLowerCase(Locale.ROOT) in setOf("roshan", "روشن")

    private fun isReady(key: String): Boolean = key.toLowerCase(Locale.ROOT) in READY_OPERATORS

    private fun go(target: Class<*>, kind: String? = null) {
        startActivity(Intent(this, target).apply {
            putExtra("OPERATOR_NAME", operatorDisplayName)
            putExtra("OPERATOR_KEY", operatorKey)
            if (kind != null) putExtra("KIND", kind)
        })
    }

    private fun open(target: Class<*>) {
        if (!isReady(operatorKey)) {
            val msg = when (currentLang) {
                "en" -> "Coming soon"
                "ps" -> "ژر راځي"
                else -> "به‌زودی"
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            return
        }
        go(target)
    }

    private fun pick(lang: String, fa: String, en: String, ps: String): String = when (lang) {
        "en" -> en
        "ps" -> ps
        else -> fa
    }

    private fun getOperatorNameByKey(key: String, lang: String): String {
        return when (key.toLowerCase(Locale.ROOT)) {
            "afghan", "afghan wireless", "افغان بیسیم" -> pick(lang, "افغان بیسیم", "Afghan Wireless", "افغان بېسېم")
            "etisalat", "اتصالات" -> pick(lang, "اتصالات", "Etisalat", "اتصالات")
            "roshan", "روشن" -> pick(lang, "روشن", "Roshan", "روښان")
            "atoma", "اتوما" -> pick(lang, "اتوما", "ATOMA", "اتوما")
            "salaam", "سلام" -> pick(lang, "سلام", "Salaam", "سلام")
            "fal", "فال حافظ" -> pick(lang, "فال حافظ", "Hafez Faal", "د حافظ فال")
            else -> key
        }
    }

    private fun updateTexts(lang: String) {
        findViewById<TextView>(R.id.txtInternetPackages)?.text =
            pick(lang, "بسته‌های اینترنتی", "Internet Packages", "د انټرنټ بستې")
        findViewById<TextView>(R.id.txtSmsPackages)?.text =
            pick(lang, "بسته‌های پیام", "SMS Packages", "د پیغام بستې")
        findViewById<TextView>(R.id.txtCallPackages)?.text =
            pick(lang, "بسته‌های تماس", "Call Packages", "د زنګ بستې")
        findViewById<TextView>(R.id.txtServices)?.text =
            pick(lang, "خدمات", "Services", "خدمتونه")
        findViewById<TextView>(R.id.txtInternationalPackages)?.text =
            pick(lang, "بسته‌های بین‌المللی", "International Packages", "نړیوال بستې")
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

        private val READY_OPERATORS = setOf(
            "etisalat", "اتصالات"
        )
    }
}
