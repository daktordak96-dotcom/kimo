package com.kimo.mayapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale

private const val IC_NET = 0
private const val IC_SMS = 1
private const val IC_CALL = 2
private const val IC_SRV = 3
private const val IC_INTL = 4
private const val IC_CHEV = 5

class OperatorMenuActivity : AppCompatActivity() {

    private var opKey: String = "etisalat"
    private var opName: String = "اتصالات"
    private var lang: String = "fa"
    private var isRtl: Boolean = true

    private class Item(val icon: Int, val title: String, val kind: String)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lang = Strings.currentLang(this)
        isRtl = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

        opKey = (intent.getStringExtra("OPERATOR_KEY") ?: "etisalat").toLowerCase(Locale.ROOT)
        opName = getName(opKey)

        val pageBg = c("#0A1020")
        window.statusBarColor = pageBg
        window.navigationBarColor = pageBg

        setContentView(buildScreen())
    }

    private fun brand(): Pair<String, String> = when {
        isAfghan() -> "#29B6FF" to "#0D47A1"
        isEtisalat() -> "#9CFF3A" to "#2E7D12"
        isRoshan() -> "#FF3B4F" to "#A0001C"
        isAtoma() -> "#FFC928" to "#A86B00"
        isSalaam() -> "#1DE9D0" to "#00796B"
        else -> "#2F8CFF" to "#1A5FD6"
    }

    private fun buildScreen(): View {
        val (neon, mid) = brand()

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(c("#0A1020"))

        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER_VERTICAL
        bar.setPadding(dpi(10f), dpi(14f), dpi(14f), dpi(10f))

        val back = TextView(this)
        back.text = if (isRtl) "→" else "←"
        back.textSize = 22f
        back.setTextColor(Color.WHITE)
        back.gravity = Gravity.CENTER
        val bp = dpi(8f); back.setPadding(bp, bp, bp, bp)
        back.isClickable = true
        back.setOnClickListener { finish() }
        bar.addView(back, LinearLayout.LayoutParams(dpi(44f), dpi(44f)))

        val badge = FrameLayout(this)
        badge.background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(c(neon), c(mid))
        ).apply {
            shape = GradientDrawable.OVAL
            setStroke(dpi(2f), alpha("#FFFFFF", 0x40))
        }
        badge.elevation = dpf(5f)
        applyGlow(badge, neon)
        val sym = ImageView(this)
        sym.setImageDrawable(MenuIcon(opIcon(), Color.WHITE))
        val sp = dpi(10f); sym.setPadding(sp, sp, sp, sp)
        badge.addView(sym, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        val blp = LinearLayout.LayoutParams(dpi(44f), dpi(44f))
        blp.marginStart = dpi(8f)
        bar.addView(badge, blp)

        val title = TextView(this)
        title.text = opName
        title.textSize = 22f
        title.setTextColor(Color.WHITE)
        title.setTypeface(title.typeface, Typeface.BOLD)
        val tlp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        tlp.marginStart = dpi(12f)
        bar.addView(title, tlp)

        root.addView(bar)

        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        scroll.isFillViewport = true

        val grid = LinearLayout(this)
        grid.orientation = LinearLayout.VERTICAL
        grid.setPadding(dpi(10f), dpi(6f), dpi(10f), dpi(16f))

        val info = pick("مشاهده و فعال‌سازی", "View & activate", "کتل او فعالول")
        val items = listOf(
            Item(IC_NET, pick("بسته‌های اینترنتی", "Internet Packages", "د انټرنټ بستې"), "internet"),
            Item(IC_SMS, pick("بسته‌های پیام", "SMS Packages", "د پیغام بستې"), "sms"),
            Item(IC_CALL, pick("بسته‌های تماس", "Call Packages", "د زنګ بستې"), "call"),
            Item(IC_SRV, pick("خدمات", "Services", "خدمتونه"), "services"),
            Item(IC_INTL, pick("بسته‌های بین‌المللی", "International Packages", "نړیوال بستې"), "intl")
        )

        val cards = items.map { buildCard(it, info, neon, mid) }

        var i = 0
        while (i < cards.size) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.isBaselineAligned = false
            for (j in 0..1) {
                val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                lp.setMargins(dpi(5f), dpi(5f), dpi(5f), dpi(5f))
                if (i + j < cards.size) row.addView(cards[i + j], lp)
                else row.addView(View(this), lp)
            }
            grid.addView(row)
            i += 2
        }

        scroll.addView(grid, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        cards.forEachIndexed { idx, card -> animateIn(card, idx) }
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buildCard(item: Item, info: String, neon: String, mid: String): CardView {
        val match = ViewGroup.LayoutParams.MATCH_PARENT
        val wrap = ViewGroup.LayoutParams.WRAP_CONTENT

        val card = CardView(this)
        card.radius = dpf(20f)
        card.cardElevation = dpf(8f)
        card.preventCornerOverlap = false
        card.setCardBackgroundColor(c("#0F1626"))
        card.isClickable = true
        card.isFocusable = true
        applyGlow(card, neon)

        val frame = FrameLayout(this)
        frame.background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(alpha(neon, 0x33), c("#0F1626"), c("#0B111F"))
        ).apply {
            cornerRadius = dpf(20f)
            setStroke(dpi(1f), alpha(neon, 0x66))
        }
        frame.setPadding(dpi(14f), dpi(16f), dpi(14f), dpi(14f))
        val fh = dpi(150f)
        card.addView(frame, FrameLayout.LayoutParams(match, fh))

        val emblem = FrameLayout(this)
        emblem.background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(c(neon), c(mid))
        ).apply {
            shape = GradientDrawable.OVAL
            setStroke(dpi(2f), alpha("#FFFFFF", 0x40))
        }
        emblem.elevation = dpf(5f)
        applyGlow(emblem, neon)
        val icon = ImageView(this)
        icon.setImageDrawable(MenuIcon(item.icon, Color.WHITE))
        val ip = dpi(13f); icon.setPadding(ip, ip, ip, ip)
        emblem.addView(icon, FrameLayout.LayoutParams(match, match))
        frame.addView(emblem, FrameLayout.LayoutParams(dpi(58f), dpi(58f), Gravity.TOP or Gravity.CENTER_HORIZONTAL))

        val chev = ImageView(this)
        chev.background = circle(alpha(neon, 0x33))
        chev.setImageDrawable(MenuIcon(IC_CHEV, c(neon)))
        val cp = dpi(6f); chev.setPadding(cp, cp, cp, cp)
        frame.addView(chev, FrameLayout.LayoutParams(dpi(28f), dpi(28f), Gravity.TOP or Gravity.START))

        val texts = LinearLayout(this)
        texts.orientation = LinearLayout.VERTICAL
        texts.gravity = Gravity.CENTER_HORIZONTAL
        frame.addView(texts, FrameLayout.LayoutParams(match, wrap, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL))

        val t = TextView(this).apply {
            text = item.title
            setTextColor(Color.WHITE)
            textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            gravity = Gravity.CENTER
            maxLines = 2
        }
        texts.addView(t)

        val sub = TextView(this).apply {
            text = info
            setTextColor(alpha(neon, 0xD0))
            textSize = 10.5f
            gravity = Gravity.CENTER
            maxLines = 1
        }
        val slp = LinearLayout.LayoutParams(wrap, wrap)
        slp.topMargin = dpi(3f)
        texts.addView(sub, slp)

        card.setOnClickListener { openKind(item.kind) }
        card.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN ->
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(90).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(140).start()
            }
            false
        }
        return card
    }

    private fun animateIn(view: View, index: Int) {
        view.alpha = 0f
        view.translationY = dpf(26f)
        view.animate().alpha(1f).translationY(0f)
            .setStartDelay(70L * index).setDuration(400)
            .setInterpolator(DecelerateInterpolator()).start()
    }

    // ---------- مقصد کلیک هر اپراتور ----------

    private fun openKind(kind: String) {
        when {
            isAfghan() -> openAfghan(kind)
            isEtisalat() -> openEtisalat(kind)
            isRoshan() -> go(RoshanInternetActivity::class.java, kind)
            isSalaam() -> go(SalaamInternetActivity::class.java, kind)
            isAtoma() -> openAtoma(kind)
        }
    }

    private fun openAfghan(kind: String) {
        when (kind) {
            "internet" -> go(AfghanInternetActivity::class.java)
            "sms" -> go(AfghanSmsActivity::class.java)
            "call" -> go(CallPackagesActivity::class.java)
            "services" -> go(AfghanServicesActivity::class.java)
            "intl" -> go(AfghanIntlActivity::class.java)
        }
    }

    private fun openEtisalat(kind: String) {
        when (kind) {
            "internet" -> go(PackagesContainerActivity::class.java)
            "sms" -> go(SmsPackagesActivity::class.java)
            "call" -> go(CallPackagesActivity::class.java)
            "services" -> go(EtisalatServicesActivity::class.java)
            "intl" -> go(InternationalPackagesActivity::class.java)
        }
    }

    private fun openAtoma(kind: String) {
        startActivity(Intent(this, AtomaActivity::class.java).apply {
            putExtra("OPERATOR", "ATOMA")
            putExtra("KIND", kind)
        })
    }

    private fun go(target: Class<*>, kind: String? = null) {
        startActivity(Intent(this, target).apply {
            putExtra("OPERATOR_NAME", opName)
            putExtra("OPERATOR_KEY", opKey)
            putExtra("OPERATOR", opKey.toUpperCase(Locale.ROOT))
            if (kind != null) putExtra("KIND", kind)
        })
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
    private fun circle(fill: Int): GradientDrawable =
        GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(fill) }
    private fun applyGlow(view: View, neon: String) {
        if (Build.VERSION.SDK_INT >= 28) {
            view.outlineAmbientShadowColor = c(neon)
            view.outlineSpotShadowColor = c(neon)
        }
    }

    private fun isAfghan() = opKey in setOf("afghan", "afghan wireless", "افغان بیسیم")
    private fun isEtisalat() = opKey in setOf("etisalat", "اتصالات")
    private fun isRoshan() = opKey in setOf("roshan", "روشن")
    private fun isAtoma() = opKey in setOf("atoma", "اتوما")
    private fun isSalaam() = opKey in setOf("salaam", "سلام")

    private fun opIcon(): Int = when {
        isAfghan() -> IC_NET
        isEtisalat() -> IC_CALL
        isRoshan() -> IC_CALL
        isAtoma() -> IC_NET
        isSalaam() -> IC_SMS
        else -> IC_NET
    }

    private fun getName(key: String): String = when (key) {
        "afghan", "افغان بیسیم" -> pick("افغان بیسیم", "Afghan Wireless", "افغان بېسیم")
        "etisalat", "اتصالات" -> pick("اتصالات", "Etisalat", "اتصالات")
        "roshan", "روشن" -> pick("روشن", "Roshan", "روښان")
        "atoma", "اتوما" -> pick("اتوما", "ATOMA", "اتوما")
        "salaam", "سلام" -> pick("سلام", "Salaam", "سلام")
        else -> intent.getStringExtra("OPERATOR_NAME") ?: key
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }

    private class MenuIcon(private val type: Int, color: Int) : Drawable() {
        private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND; this.color = color
        }
        private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL; this.color = color
        }
        private val path = Path()
        private val rect = RectF()

        override fun draw(canvas: Canvas) {
            val b = bounds
            val s = Math.min(b.width(), b.height()) / 24f
            canvas.save()
            canvas.translate(b.exactCenterX() - 12f * s, b.exactCenterY() - 12f * s)
            canvas.scale(s, s)
            stroke.strokeWidth = 2f

            when (type) {
                IC_NET -> {
                    stroke.strokeWidth = 2.2f
                    canvas.drawCircle(12f, 18f, 1.9f, fill)
                    val radii = floatArrayOf(5f, 9f, 13f)
                    for (r in radii) {
                        rect.set(12f - r, 18f - r, 12f + r, 18f + r)
                        canvas.drawArc(rect, 225f, 90f, false, stroke)
                    }
                }
                IC_SMS -> {
                    stroke.strokeWidth = 1.9f
                    rect.set(3f, 5.5f, 21f, 18.5f)
                    canvas.drawRoundRect(rect, 2.5f, 2.5f, stroke)
                    path.reset()
                    path.moveTo(4f, 7f); path.lineTo(12f, 13f); path.lineTo(20f, 7f)
                    canvas.drawPath(path, stroke)
                }
                IC_CALL -> {
                    path.reset()
                    path.moveTo(6.62f, 10.79f)
                    path.cubicTo(8.06f, 13.62f, 10.38f, 15.93f, 13.21f, 17.38f)
                    path.lineTo(15.41f, 15.18f)
                    path.cubicTo(15.68f, 14.91f, 16.08f, 14.82f, 16.43f, 14.94f)
                    path.cubicTo(17.55f, 15.31f, 18.76f, 15.51f, 20f, 15.51f)
                    path.cubicTo(20.55f, 15.51f, 21f, 15.96f, 21f, 16.51f)
                    path.lineTo(21f, 20f)
                    path.cubicTo(21f, 20.55f, 20.55f, 21f, 20f, 21f)
                    path.cubicTo(10.61f, 21f, 3f, 13.39f, 3f, 4f)
                    path.cubicTo(3f, 3.45f, 3.45f, 3f, 4f, 3f)
                    path.lineTo(7.5f, 3f)
                    path.cubicTo(8.05f, 3f, 8.5f, 3.45f, 8.5f, 4f)
                    path.cubicTo(8.5f, 5.25f, 8.7f, 6.45f, 9.07f, 7.57f)
                    path.cubicTo(9.18f, 7.92f, 9.1f, 8.31f, 8.82f, 8.59f)
                    path.close()
                    canvas.drawPath(path, fill)
                }
                IC_SRV -> {
                    stroke.strokeWidth = 2.2f
                    path.reset()
                    path.moveTo(14.7f, 6.3f)
                    path.cubicTo(13.9f, 7.1f, 13.9f, 8.4f, 14.7f, 9.3f)
                    path.cubicTo(15.6f, 10.1f, 16.9f, 10.1f, 17.7f, 9.3f)
                    path.lineTo(20f, 7f)
                    path.cubicTo(20f, 9.8f, 17.8f, 12f, 15f, 12f)
                    path.cubicTo(14.5f, 12f, 14f, 11.9f, 13.5f, 11.8f)
                    path.lineTo(7.5f, 17.8f)
                    path.cubicTo(6.7f, 18.6f, 5.4f, 18.6f, 4.6f, 17.8f)
                    path.cubicTo(3.8f, 17f, 3.8f, 15.7f, 4.6f, 14.9f)
                    path.lineTo(10.6f, 8.9f)
                    path.cubicTo(10.5f, 8.4f, 10.4f, 7.9f, 10.4f, 7.4f)
                    path.cubicTo(10.4f, 4.6f, 12.6f, 2.4f, 15.4f, 2.4f)
                    path.cubicTo(16f, 2.4f, 16.6f, 2.5f, 17.1f, 2.7f)
                    path.close()
                    canvas.drawPath(path, stroke)
                }
                IC_INTL -> {
                    stroke.strokeWidth = 1.7f
                    canvas.drawCircle(12f, 12f, 9f, stroke)
                    rect.set(7f, 3f, 17f, 21f)
                    canvas.drawOval(rect, stroke)
                    canvas.drawLine(3f, 12f, 21f, 12f, stroke)
                    canvas.drawLine(4.2f, 8f, 19.8f, 8f, stroke)
                    canvas.drawLine(4.2f, 16f, 19.8f, 16f, stroke)
                }
                IC_CHEV -> {
                    stroke.strokeWidth = 2.6f
                    canvas.drawLine(15f, 5f, 8f, 12f, stroke)
                    canvas.drawLine(8f, 12f, 15f, 19f, stroke)
                }
            }
            canvas.restore()
        }

        override fun setAlpha(alpha: Int) { stroke.alpha = alpha; fill.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) {
            stroke.colorFilter = colorFilter; fill.colorFilter = colorFilter
        }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}