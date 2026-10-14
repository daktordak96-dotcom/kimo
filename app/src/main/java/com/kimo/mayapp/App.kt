package com.kimo.mayapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import java.util.Locale

/**
 * بخش مرکزی برنامه:
 * ۱) در اولین ورود، صفحهٔ انتخاب زبان را نشان می‌دهد.
 * ۲) روی همهٔ صفحه‌ها، تب‌ها را خودکار کپسولی و هم‌رنگ اپراتور می‌کند.
 * ۳) تقویم امروز را روی قسمت سیاه پرچم می‌نویسد.
 * ۴) صفحهٔ اول را جمع‌وجور می‌کند.
 * ۵) پشت صفحهٔ هر اپراتور، عکس تمام‌صفحهٔ یک جای تاریخی افغانستان را می‌گذارد.
 */
class App : Application() {

    private class Colors(val accent: Int, val bar: Int, val idle: Int, val selected: Int)

    private val etisalat = Colors(c("#9CFF3A"), c("#0A2112"), c("#8FBF96"), c("#0A1408"))
    private val afghan = Colors(c("#29B6FF"), c("#0C1A30"), c("#7F9CC0"), c("#06101E"))
    private val roshan = Colors(c("#FF3B4F"), c("#2A0812"), c("#E59AA2"), c("#FFFFFF"))
    private val atoma = Colors(c("#E8B923"), c("#1C170B"), c("#B8A77A"), c("#1A1306"))
    private val salaam = Colors(c("#26A69A"), c("#0A221C"), c("#7FB5AB"), c("#F2FFFC"))

    private fun c(hex: String) = Color.parseColor(hex)

    /**
     * جای تاریخی هر اپراتور
     * focusX: کدام قسمت عکس (افقی) روی صفحه بیاید: 0 = چپ عکس، 0.5 = وسط، 1 = راست عکس
     * (هر چه بیشتر، موضوع عکس بیشتر به سمت چپ صفحه می‌رود)
     */
    private class Place(val image: String, val fa: String, val en: String, val ps: String, val focusX: Float)

    private val places = mapOf(
        "etisalat" to Place("bg_etisalat", "بند امیر، بامیان", "Band-e Amir, Bamyan", "بند امیر، بامیان", 0.5f),
        "afghan" to Place("bg_afghan", "منار جام، غور", "Minaret of Jam, Ghor", "د جام منار، غور", 1.0f),
        "roshan" to Place("bg_roshan", "روضهٔ شریف، مزار", "Blue Mosque, Mazar", "روضه شریف، مزار", 0.5f),
        "atoma" to Place("bg_atoma", "قلعهٔ اختیارالدین، هرات", "Herat Citadel", "د اختیارالدین کلا، هرات", 0.5f),
        "salaam" to Place("bg_salaam", "باغ بابر، کابل", "Babur Garden, Kabul", "د بابر باغ، کابل", 0.5f)
    )

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // اولین ورود: انتخاب زبان
                if (activity is MainActivity && savedInstanceState == null) {
                    val prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                    if (!prefs.getBoolean(LanguageActivity.KEY_CHOSEN, false)) {
                        activity.startActivity(Intent(activity, LanguageActivity::class.java))
                    }
                }
            }
            override fun onActivityStarted(activity: Activity) {
                val root = activity.window?.decorView ?: return
                root.post {
                    styleTabs(activity, root)
                    if (activity is MainActivity) {
                        showCalendarOnFlag(activity)
                        hideBottomBar(activity)
                        tightenHome(activity)
                    }
                    if (activity is OperatorMenuActivity) {
                        historicBackground(activity)
                    }
                }
            }
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    // =====================================================================
    // پس‌زمینهٔ تمام‌صفحهٔ جای تاریخی در صفحهٔ هر اپراتور
    // =====================================================================
    private fun historicBackground(activity: Activity) {
        try {
            val key = operatorKey(activity) ?: return
            val place = places[key] ?: return
            val resId = activity.resources.getIdentifier(place.image, "drawable", activity.packageName)
            if (resId == 0) return   // عکس هنوز گذاشته نشده؛ صفحه مثل قبل می‌ماند

            val content = activity.findViewById<ViewGroup>(android.R.id.content) ?: return
            val root = content.getChildAt(0) ?: return

            var pageColor = Color.parseColor("#0A0F1E")
            (root.background as? ColorDrawable)?.let { pageColor = it.color }
            clearSolidBackgrounds(root, root.width)

            val dm = activity.resources.displayMetrics
            val bmp = decodeScaled(activity, resId, dm.widthPixels) ?: return
            val lang = Strings.currentLang(activity)
            val label = "📍 " + when (lang) { "en" -> place.en; "ps" -> place.ps; else -> place.fa }

            root.background = HistoricDrawable(bmp, pageColor, label, dm.density, place.focusX)
            activity.window.decorView.setBackgroundColor(pageColor)
            activity.window.statusBarColor = pageColor
        } catch (e: Exception) { }
    }

    private fun operatorKey(activity: Activity): String? {
        val i = activity.intent ?: return null
        val s = ((i.getStringExtra("OPERATOR_KEY") ?: "") + " " +
                (i.getStringExtra("OPERATOR") ?: "") + " " +
                (i.getStringExtra("OPERATOR_NAME") ?: "")).toLowerCase(Locale.ROOT)
        return when {
            s.contains("etisalat") || s.contains("اتصالات") -> "etisalat"
            s.contains("afghan") || s.contains("افغان") -> "afghan"
            s.contains("roshan") || s.contains("روشن") -> "roshan"
            s.contains("atoma") || s.contains("اتوما") -> "atoma"
            s.contains("salaam") || s.contains("سلام") -> "salaam"
            else -> null
        }
    }

    private fun clearSolidBackgrounds(v: View, rootW: Int) {
        if (v.background is ColorDrawable && v.width >= rootW * 0.9f) {
            v.setBackgroundColor(Color.TRANSPARENT)
        }
        if (v is ViewGroup) {
            for (i in 0 until v.childCount) clearSolidBackgrounds(v.getChildAt(i), rootW)
        }
    }

    private fun decodeScaled(ctx: Context, resId: Int, targetW: Int): Bitmap? {
        val o = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeResource(ctx.resources, resId, o)
        var sample = 1
        while (o.outWidth / (sample * 2) >= targetW) sample *= 2
        val o2 = BitmapFactory.Options().apply {
            inSampleSize = sample
            inScaled = false
        }
        return BitmapFactory.decodeResource(ctx.resources, resId, o2)
    }

    /** عکس تمام‌صفحه: بالا تیره‌تر (برای خوانایی)، وسط روشن‌تر، پایین با نام مکان */
    private class HistoricDrawable(
        private val bmp: Bitmap,
        private val pageColor: Int,
        private val label: String,
        private val dp: Float,
        private val focusX: Float
    ) : Drawable() {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        private val src = Rect()
        private val dst = RectF()

        private fun shade(a: Int) =
            Color.argb(a, Color.red(pageColor), Color.green(pageColor), Color.blue(pageColor))

        override fun draw(canvas: Canvas) {
            val b = bounds
            if (b.isEmpty) return
            val w = b.width().toFloat()
            val h = b.height().toFloat()

            canvas.drawColor(pageColor)

            // عکس کل صفحه را پر می‌کند (برش با نقطهٔ تمرکز)
            dst.set(b.left.toFloat(), b.top.toFloat(), b.right.toFloat(), b.bottom.toFloat())
            val dstRatio = w / h
            val bw = bmp.width.toFloat()
            val bh = bmp.height.toFloat()
            if (bw / bh > dstRatio) {
                val cw = bh * dstRatio
                val l = ((bw - cw) * focusX.coerceIn(0f, 1f)).toInt()
                src.set(l, 0, (l + cw).toInt(), bh.toInt())
            } else {
                val ch = bw / dstRatio
                val t = ((bh - ch) / 2f).toInt()
                src.set(0, t, bw.toInt(), (t + ch).toInt())
            }
            p.shader = null
            p.alpha = 255
            canvas.drawBitmap(bmp, src, dst, p)

            // سایه‌روشن: بالا تیره (اسم اپراتور و کارت‌ها)، وسط روشن‌تر، پایین کمی تیره
            p.shader = LinearGradient(
                0f, b.top.toFloat(), 0f, b.bottom.toFloat(),
                intArrayOf(shade(0xEE), shade(0xB4), shade(0x8C), shade(0x70), shade(0xA0)),
                floatArrayOf(0f, 0.18f, 0.45f, 0.75f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(dst, p)
            p.shader = null

            // نام مکان در پایین
            val tp = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.argb(0xEE, 255, 255, 255)
                textSize = 13f * dp
                typeface = Typeface.DEFAULT_BOLD
                setShadowLayer(6f, 0f, 2f, Color.BLACK)
            }
            @Suppress("DEPRECATION")
            val sl = StaticLayout(label, tp, (w - 32 * dp).toInt(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
            canvas.save()
            canvas.translate(b.left + 16 * dp, b.bottom - sl.height - 22 * dp)
            sl.draw(canvas)
            canvas.restore()
        }

        override fun setAlpha(alpha: Int) { p.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { p.colorFilter = colorFilter }
        @Suppress("DEPRECATION")
        override fun getOpacity(): Int = PixelFormat.OPAQUE
    }

    // =====================================================================
    // جمع‌وجور کردن صفحهٔ اول
    // =====================================================================
    private fun tightenHome(activity: Activity) {
        try {
            val dp = activity.resources.displayMetrics.density
            fun px(v: Int) = (v * dp).toInt()

            activity.findViewById<View>(R.id.titleOperators)?.visibility = View.GONE

            activity.findViewById<View>(R.id.heroBanner)?.let { v ->
                (v.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                    it.bottomMargin = 0
                    v.layoutParams = it
                }
            }
            activity.findViewById<View>(R.id.searchBar)?.let { v ->
                (v.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                    it.topMargin = px(12)
                    it.bottomMargin = px(6)
                    v.layoutParams = it
                }
            }
            activity.findViewById<View>(R.id.opGrid)?.let { v ->
                (v.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                    it.topMargin = px(4)
                    v.layoutParams = it
                }
                v.setPadding(v.paddingLeft, 0, v.paddingRight, v.paddingBottom)
            }
        } catch (e: Exception) { }
    }

    // =====================================================================
    // تقویم روی قسمت سیاه پرچم
    // =====================================================================
    private fun showCalendarOnFlag(activity: Activity) {
        try {
            activity.findViewById<View>(R.id.heroSim)?.visibility = View.GONE
            activity.findViewById<View>(R.id.btnHero)?.visibility = View.GONE
            activity.findViewById<View>(R.id.heroDots)?.visibility = View.GONE
            activity.findViewById<View>(R.id.txtHeroAccent)?.visibility = View.INVISIBLE
            activity.findViewById<View>(R.id.txtHeroTitle)?.visibility = View.INVISIBLE
            activity.findViewById<View>(R.id.txtHeroSub)?.visibility = View.INVISIBLE

            val hero = activity.findViewById<View>(R.id.heroBanner) ?: return
            val lang = Strings.currentLang(activity)
            val day = AfghanCalendar.today(activity)
            val sp = activity.resources.displayMetrics.scaledDensity
            val dp = activity.resources.displayMetrics.density

            val shamsi = AfghanCalendar.shamsiText(lang, day)
            val lastSpace = shamsi.lastIndexOf(' ')
            val dayMonth = if (lastSpace > 0) shamsi.substring(0, lastSpace) else shamsi
            val year = if (lastSpace > 0) shamsi.substring(lastSpace + 1) else ""

            val lines = listOf(
                Line(when (lang) { "en" -> "Today"; "ps" -> "نن ورځ"; else -> "امروز" } +
                        " • " + AfghanCalendar.weekName(lang, day),
                    13f * sp, Color.parseColor("#FFD54F"), true),
                Line(dayMonth, 27f * sp, Color.WHITE, true),
                Line(year, 18f * sp, Color.parseColor("#E8ECF7"), true),
                Line("—", 0f, 0, false),
                Line(AfghanCalendar.hijriText(lang, day), 12.5f * sp, Color.parseColor("#C8F7EA"), false),
                Line(AfghanCalendar.gregText(lang, day), 12.5f * sp, Color.parseColor("#E3D6FF"), false)
            )

            hero.post {
                hero.overlay.clear()
                val d = BlackStripCalendar(lines, dp)
                d.setBounds(0, 0, hero.width, hero.height)
                hero.overlay.add(d)
            }
        } catch (e: Exception) { }
    }

    private class Line(val text: String, val size: Float, val color: Int, val bold: Boolean)

    private class BlackStripCalendar(private val lines: List<Line>, private val dp: Float) : Drawable() {
        private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun draw(canvas: Canvas) {
            val b = bounds
            if (b.isEmpty) return
            val stripW = (b.width() * 0.335f).toInt()
            val pad = (8 * dp).toInt()
            val textW = stripW - pad * 2
            if (textW <= 0) return

            val layouts = ArrayList<StaticLayout?>()
            var total = 0f
            val gaps = floatArrayOf(0f, 4f, 0f, 8f, 8f, 3f)
            for ((i, ln) in lines.withIndex()) {
                if (ln.size == 0f) {
                    layouts.add(null)
                    total += gaps[i] * dp + 2 * dp
                    continue
                }
                val p = TextPaint(paint)
                p.textSize = ln.size
                p.color = ln.color
                p.typeface = if (ln.bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                p.setShadowLayer(6f, 0f, 2f, Color.argb(0xCC, 0, 0, 0))
                while (p.measureText(ln.text) > textW && p.textSize > 8f * dp) {
                    p.textSize = p.textSize * 0.92f
                }
                @Suppress("DEPRECATION")
                val sl = StaticLayout(ln.text, p, textW, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
                layouts.add(sl)
                total += sl.height + gaps[i] * dp
            }

            var y = b.top + (b.height() - total) / 2f
            for ((i, sl) in layouts.withIndex()) {
                y += gaps[i] * dp
                if (sl == null) {
                    val cx = b.left + stripW / 2f
                    val half = textW * 0.28f
                    linePaint.color = Color.parseColor("#FFD54F")
                    linePaint.alpha = 0xB0
                    linePaint.strokeWidth = 1.5f * dp
                    canvas.drawLine(cx - half, y + dp, cx + half, y + dp, linePaint)
                    y += 2 * dp
                    continue
                }
                canvas.save()
                canvas.translate((b.left + pad).toFloat(), y)
                sl.draw(canvas)
                canvas.restore()
                y += sl.height
            }
        }

        override fun setAlpha(alpha: Int) { paint.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { paint.colorFilter = colorFilter }
        @Suppress("DEPRECATION")
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }

    // ---------------- حذف نوار چهار دکمهٔ پایین ----------------
    private fun hideBottomBar(activity: Activity) {
        try {
            activity.findViewById<View>(R.id.navBar)?.visibility = View.GONE
        } catch (e: Exception) { }
    }

    // =====================================================================
    // تب‌های کپسولی
    // =====================================================================
    private fun styleTabs(activity: Activity, root: View) {
        val tabs = ArrayList<TabLayout>()
        findTabs(root, tabs)
        if (tabs.isEmpty()) return
        val col = colorsFor(activity)
        for (tl in tabs) {
            PillTabs.style(tl, col.accent, col.bar, col.idle, col.selected)
        }
    }

    private fun findTabs(v: View, out: ArrayList<TabLayout>) {
        if (v is TabLayout) {
            out.add(v)
            return
        }
        if (v is ViewGroup) {
            for (i in 0 until v.childCount) findTabs(v.getChildAt(i), out)
        }
    }

    private fun colorsFor(activity: Activity): Colors {
        val i = activity.intent
        val hint = (
            (i?.getStringExtra("OPERATOR") ?: "") + " " +
            (i?.getStringExtra("OPERATOR_KEY") ?: "") + " " +
            (i?.getStringExtra("OPERATOR_NAME") ?: "")
        ).toLowerCase(Locale.ROOT)

        when {
            hint.contains("roshan") || hint.contains("روشن") -> return roshan
            hint.contains("atoma") || hint.contains("اتوما") -> return atoma
            hint.contains("salaam") || hint.contains("سلام") -> return salaam
            hint.contains("afghan") || hint.contains("افغان") -> return afghan
            hint.contains("etisalat") || hint.contains("اتصالات") -> return etisalat
        }

        val name = activity.javaClass.simpleName.toLowerCase(Locale.ROOT)
        return when {
            name.contains("roshan") -> roshan
            name.contains("atoma") -> atoma
            name.contains("salaam") -> salaam
            name.contains("afghan") -> afghan
            else -> etisalat
        }
    }
}