package com.kimo.mayapp

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private var lang: String = "fa"
    private var dark = true

    private lateinit var resultsBox: LinearLayout
    private lateinit var hintText: TextView
    private var lastQuery: String = ""

    // ---------------- رنگ‌های پوسته ----------------
    private var BG = 0
    private var BAR = 0
    private var FIELD = 0
    private var FIELD_STROKE = 0
    private var FOCUS = 0
    private var TEXT = 0
    private var HINT = 0
    private var CARD = 0
    private var PRICE = 0
    private var ICON = 0

    private fun setupColors() {
        if (dark) {
            BG = c("#0A1020")
            BAR = Color.argb(0xCC, 14, 22, 40)
            FIELD = c("#111A2E")
            FIELD_STROKE = alpha("#FFFFFF", 0x1F)
            FOCUS = c("#7C5CFF")
            TEXT = Color.WHITE
            HINT = c("#7F8BA6")
            CARD = Color.argb(0xE6, 17, 26, 46)
            PRICE = c("#C9D3EA")
            ICON = Color.WHITE
        } else {
            BG = c("#FFF8F1")
            BAR = Color.argb(0xB3, 255, 255, 255)
            FIELD = Color.WHITE
            FIELD_STROKE = c("#F3D9C4")
            FOCUS = c("#FF7A2F")
            TEXT = c("#2A1E17")
            HINT = c("#9C8577")
            CARD = Color.argb(0xF2, 255, 255, 255)
            PRICE = c("#5C4636")
            ICON = c("#3A2A20")
        }
    }

    private class Row(
        val opName: String,
        val opColor: String,
        val kind: String,
        val title: String,
        val price: String,
        val desc: String,
        val haystack: String
    ) {
        fun key() = "$opName|$kind|$title"
    }

    private val allRows = ArrayList<Row>()

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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lang = Strings.currentLang(this)
        dark = try { ThemeMode.palette(this).dark } catch (e: Exception) { true }
        setupColors()

        window.statusBarColor = BG
        window.navigationBarColor = BG
        if (!dark) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val frame = FrameLayout(this)
        frame.addView(GlowBackground(this, BG, dark), FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL

        root.addView(buildTopBar())

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        scroll.isVerticalScrollBarEnabled = false

        resultsBox = LinearLayout(this)
        resultsBox.orientation = LinearLayout.VERTICAL
        resultsBox.setPadding(dpi(12f), dpi(8f), dpi(12f), dpi(24f))

        hintText = TextView(this).apply {
            text = pick(
                "نام بسته، حجم یا قیمت را بنویسید.\nمثلاً: 1 جی‌بی ، ساعتی ، 100 دقیقه ، روشن",
                "Type a package name, size or price.\nE.g.: 1 GB , hourly , 100 minutes , Roshan",
                "د بستې نوم، حجم یا بیه ولیکئ.\nلکه: 1 جي بي ، ساعتي ، 100 دقیقې ، روشن"
            )
            setTextColor(HINT)
            textSize = 14f
            gravity = Gravity.CENTER
            setLineSpacing(dpf(4f), 1f)
            setPadding(dpi(16f), dpi(40f), dpi(16f), dpi(16f))
        }
        resultsBox.addView(hintText)

        scroll.addView(resultsBox, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        frame.addView(root, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        setContentView(frame)

        collectData()
    }

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
    private fun opOn(key: String) =
        getSharedPreferences("AppSettings", MODE_PRIVATE).getBoolean(key, true)

    /** رنگ اپراتور؛ در حالت روز کمی تیره‌تر تا روی سفید خوانا باشد */
    private fun opCol(hex: String): Int {
        val col = c(hex)
        if (dark) return col
        val hsv = FloatArray(3)
        Color.colorToHSV(col, hsv)
        hsv[2] = minOf(hsv[2], 0.72f)
        hsv[1] = maxOf(hsv[1], 0.75f)
        return Color.HSVToColor(hsv)
    }

    private fun buildTopBar(): View {
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER_VERTICAL
        bar.setBackgroundColor(BAR)
        bar.setPadding(dpi(8f), dpi(10f), dpi(12f), dpi(10f))
        bar.elevation = dpf(if (dark) 6f else 3f)

        val back = TextView(this)
        back.text = "←"
        back.textSize = 22f
        back.setTextColor(ICON)
        back.gravity = Gravity.CENTER
        val bp = dpi(8f); back.setPadding(bp, bp, bp, bp)
        back.isClickable = true
        back.setOnClickListener { finish() }
        bar.addView(back, LinearLayout.LayoutParams(dpi(42f), dpi(42f)))

        val field = EditText(this)
        field.background = rounded(FIELD, 24f, FIELD_STROKE)
        field.hint = pick(
            "بسته دلخواه خود را جستجو کنید…",
            "Search for your package…",
            "خپله خوښه بسته ولټوئ…"
        )
        field.setHintTextColor(HINT)
        field.setTextColor(TEXT)
        field.textSize = 15f
        field.setSingleLine(true)
        field.elevation = dpf(if (dark) 0f else 2f)
        field.setPadding(dpi(18f), dpi(10f), dpi(18f), dpi(10f))
        field.setOnFocusChangeListener { _, has ->
            field.background = GradientDrawable().apply {
                cornerRadius = dpf(24f)
                setColor(FIELD)
                setStroke(dpi(if (has) 2f else 1f), if (has) FOCUS else FIELD_STROKE)
            }
        }
        field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                lastQuery = s?.toString() ?: ""
                runSearch(lastQuery)
            }
        })
        val flp = LinearLayout.LayoutParams(0, dpi(46f), 1f)
        flp.marginStart = dpi(6f)
        bar.addView(field, flp)

        field.requestFocus()
        return bar
    }

    private fun collectData() {
        if (opOn("op_afghan")) {
            val n = Strings.get(this, "afghan_wireless"); val col = "#29B6FF"
            for (i in AfghanInternetData.tabs.indices)
                AfghanInternetData.forTab(i).forEach { addAfghan(n, col, "internet", it) }
            for (i in 0..5)
                AfghanCallData.forTab(i).forEach { addAfghan(n, col, "call", it) }
        }
        if (opOn("op_roshan")) {
            val n = Strings.get(this, "roshan"); val col = "#FF3B4F"
            for (i in RoshanInternetData.tabs.indices)
                RoshanInternetData.forTab(i).forEach { addAfghan(n, col, "internet", it) }
            for (i in RoshanCallData.tabs.indices)
                RoshanCallData.forTab(i).forEach { addAfghan(n, col, "call", it) }
        }
        if (opOn("op_atoma")) {
            val n = Strings.get(this, "atoma"); val col = "#FFC928"
            for (i in AtomaData.tabs("internet").indices)
                AtomaData.forTab("internet", i).forEach { addAtoma(n, col, "internet", it) }
            for (i in AtomaData.tabs("call").indices)
                AtomaData.forTab("call", i).forEach { addAtoma(n, col, "call", it) }
        }
        if (opOn("op_etisalat")) {
            val n = Strings.get(this, "etisalat"); val col = "#9CFF3A"
            for (i in 0..5)
                CallPackagesData.forTab(i).forEach { addEtisalatCall(n, col, it) }
        }
    }

    private fun addAfghan(op: String, col: String, kind: String, p: AfghanPackage) {
        val title = p.title.get(this)
        val price = p.price.get(this)
        allRows.add(Row(op, col, kind, title, price, p.description(this),
            (title + " " + price).toLowerCase(Locale.ROOT)))
    }

    private fun addAtoma(op: String, col: String, kind: String, p: AtomaPackage) {
        val title = p.title.get(this)
        val price = p.price.get(this)
        allRows.add(Row(op, col, kind, title, price, p.description(this),
            (title + " " + price).toLowerCase(Locale.ROOT)))
    }

    private fun addEtisalatCall(op: String, col: String, p: CallPackage) {
        allRows.add(Row(op, col, "call", p.title, p.price, p.description(),
            (p.title + " " + p.price).toLowerCase(Locale.ROOT)))
    }

    private fun runSearch(query: String) {
        val q = query.trim().toLowerCase(Locale.ROOT)
        resultsBox.removeAllViews()

        if (q.isEmpty()) {
            resultsBox.addView(hintText)
            return
        }

        val matched = allRows.filter { it.haystack.contains(q) }

        if (matched.isEmpty()) {
            val none = TextView(this).apply {
                text = pick("بسته‌ای پیدا نشد", "No package found", "بسته ونه موندل شوه")
                setTextColor(HINT)
                textSize = 14f
                gravity = Gravity.CENTER
                setPadding(dpi(16f), dpi(40f), dpi(16f), dpi(16f))
            }
            resultsBox.addView(none)
            return
        }

        val count = TextView(this).apply {
            text = pick("${matched.size} نتیجه", "${matched.size} results", "${matched.size} پایلې")
            setTextColor(HINT)
            textSize = 12f
            setPadding(dpi(6f), dpi(6f), dpi(6f), dpi(8f))
        }
        resultsBox.addView(count)

        matched.forEachIndexed { i, row ->
            val card = buildResultCard(row)
            resultsBox.addView(card)
            if (i < 12) {
                card.alpha = 0f
                card.translationY = dpf(14f)
                card.animate().alpha(1f).translationY(0f)
                    .setStartDelay(i * 30L).setDuration(260)
                    .setInterpolator(DecelerateInterpolator()).start()
            }
        }
    }

    private fun buildResultCard(row: Row): View {
        val opc = opCol(row.opColor)
        val card = LinearLayout(this)
        card.orientation = LinearLayout.HORIZONTAL
        card.gravity = Gravity.CENTER_VERTICAL
        card.background = rounded(CARD, 14f,
            Color.argb(if (dark) 0x44 else 0x33, Color.red(opc), Color.green(opc), Color.blue(opc)))
        card.setPadding(dpi(12f), dpi(12f), dpi(12f), dpi(12f))
        card.isClickable = true
        if (!dark) card.elevation = dpf(2f)
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.bottomMargin = dpi(8f)
        lp.leftMargin = dpi(2f)
        lp.rightMargin = dpi(2f)
        card.layoutParams = lp

        val stripe = View(this)
        stripe.background = rounded(opc, 2f)
        card.addView(stripe, LinearLayout.LayoutParams(dpi(4f), dpi(44f)))

        val texts = LinearLayout(this)
        texts.orientation = LinearLayout.VERTICAL
        val tlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        tlp.marginStart = dpi(10f)
        card.addView(texts, tlp)

        val title = TextView(this).apply {
            text = row.title
            setTextColor(TEXT)
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
        }
        texts.addView(title)

        val meta = LinearLayout(this)
        meta.orientation = LinearLayout.HORIZONTAL
        meta.gravity = Gravity.CENTER_VERTICAL
        val mlp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mlp.topMargin = dpi(5f)
        texts.addView(meta, mlp)

        val badge = TextView(this).apply {
            text = row.opName + " · " + kindLabel(row.kind)
            setTextColor(opc)
            textSize = 11f
            background = rounded(Color.argb(0x22, Color.red(opc), Color.green(opc), Color.blue(opc)), 8f)
            setPadding(dpi(7f), dpi(2f), dpi(7f), dpi(2f))
        }
        meta.addView(badge)

        val price = TextView(this).apply {
            text = row.price
            setTextColor(PRICE)
            textSize = 12f
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.END
        }
        val plp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        plp.marginStart = dpi(8f)
        meta.addView(price, plp)

        val heart = TextView(this).apply {
            text = if (Favorites.contains(this@SearchActivity, row.key())) "♥" else "♡"
            setTextColor(c("#FF3B4F"))
            textSize = 20f
            gravity = Gravity.CENTER
        }
        heart.setPadding(dpi(8f), dpi(2f), dpi(2f), dpi(2f))
        heart.setOnClickListener {
            toggleFav(row)
            heart.text = if (Favorites.contains(this@SearchActivity, row.key())) "♥" else "♡"
            heart.animate().scaleX(1.35f).scaleY(1.35f).setDuration(110).withEndAction {
                heart.animate().scaleX(1f).scaleY(1f).setDuration(140).start()
            }.start()
        }
        card.addView(heart)

        card.setOnClickListener { showPackage(row) }
        return card
    }

    private fun kindLabel(kind: String) = when (kind) {
        "call" -> pick("تماس", "Call", "زنګ")
        else -> pick("اینترنت", "Internet", "انټرنټ")
    }

    private fun toggleFav(row: Row) {
        if (Favorites.contains(this, row.key())) {
            Favorites.remove(this, row.key())
        } else {
            Favorites.add(this, row.key(), row.opName, row.opColor,
                row.kind, row.title, row.price, row.desc)
        }
    }

    private fun showPackage(row: Row) {
        val inFav = Favorites.contains(this, row.key())
        val favBtn = if (inFav)
            pick("♥ حذف از علاقه‌مندی‌ها", "♥ Remove from favorites", "♥ له خوښو لرې کول")
        else
            pick("♡ افزودن به علاقه‌مندی‌ها", "♡ Add to favorites", "♡ خوښو ته زیاتول")

        AlertDialog.Builder(this)
            .setTitle(row.title + "  —  " + row.price)
            .setMessage(row.opName + " · " + kindLabel(row.kind) + "\n\n" + row.desc)
            .setPositiveButton(pick("باشه", "OK", "سمه ده"), null)
            .setNeutralButton(favBtn) { _, _ ->
                toggleFav(row)
                runSearch(lastQuery)
            }
            .show()
    }

    // =====================================================================
    // پس‌زمینهٔ نورانی متحرک (روز: نارنجی/قرمز/طلایی — شب: بنفش/آبی)
    // =====================================================================
    private class GlowBackground(ctx: Context, private val base: Int, dark: Boolean) : View(ctx) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private var t = 0f
        private val anim = ValueAnimator.ofFloat(0f, (Math.PI * 2).toFloat()).apply {
            duration = 18000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { t = it.animatedValue as Float; invalidate() }
        }
        // x, y, شعاع (نسبت به عرض), فاز
        private val blobs = arrayOf(
            floatArrayOf(0.85f, 0.10f, 0.75f, 0f),
            floatArrayOf(0.10f, 0.45f, 0.70f, 2.1f),
            floatArrayOf(0.70f, 0.90f, 0.80f, 4.2f)
        )
        private val colors = if (dark) intArrayOf(
            Color.argb(0x33, 124, 92, 255),
            Color.argb(0x26, 41, 182, 255),
            Color.argb(0x22, 192, 108, 255)
        ) else intArrayOf(
            Color.argb(0x55, 255, 154, 60),   // نارنجی
            Color.argb(0x3D, 255, 90, 95),    // قرمز ملایم
            Color.argb(0x4D, 255, 200, 87)    // طلایی
        )

        override fun onAttachedToWindow() { super.onAttachedToWindow(); anim.start() }
        override fun onDetachedFromWindow() { anim.cancel(); super.onDetachedFromWindow() }

        override fun onDraw(canvas: Canvas) {
            canvas.drawColor(base)
            val w = width.toFloat()
            val h = height.toFloat()
            if (w <= 0f || h <= 0f) return
            for (i in blobs.indices) {
                val b = blobs[i]
                val cx = w * (b[0] + 0.10f * Math.sin((t + b[3]).toDouble()).toFloat())
                val cy = h * (b[1] + 0.06f * Math.cos((t * 1.2f + b[3]).toDouble()).toFloat())
                val r = w * b[2]
                p.shader = RadialGradient(cx, cy, r, colors[i], Color.TRANSPARENT, Shader.TileMode.CLAMP)
                canvas.drawCircle(cx, cy, r, p)
            }
            p.shader = null
        }
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}