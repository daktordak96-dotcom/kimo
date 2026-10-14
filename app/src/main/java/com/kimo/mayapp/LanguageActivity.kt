package com.kimo.mayapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

/**
 * صفحهٔ انتخاب زبان در اولین ورود — طراحی مدرن با پس‌زمینهٔ زنده
 */
class LanguageActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val BG = Color.parseColor("#070B1A")
    private val TEXT = Color.parseColor("#F4F7FF")
    private val SUB = Color.parseColor("#A9B6D8")
    private val BLUE = Color.parseColor("#2F8CFF")
    private val PURPLE = Color.parseColor("#B46CFF")

    private var selected: String? = null
    private var done = false
    private val cards = HashMap<String, LinearLayout>()
    private val checks = HashMap<String, TextView>()
    private lateinit var startBtn: TextView
    private lateinit var greet: TextView
    private var pulse: ValueAnimator? = null
    private val handler = Handler(Looper.getMainLooper())
    private var greetIndex = 0
    private val greetings = listOf("خوش آمدید", "ښه راغلاست", "Welcome")

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = BG
        window.navigationBarColor = BG

        val frame = FrameLayout(this)
        frame.setBackgroundColor(BG)
        frame.addView(AuroraView(this), FrameLayout.LayoutParams(MATCH, MATCH))

        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            setPadding(dp(22), dp(36), dp(22), dp(28))
        }

        // لوگوی چرخان
        val logo = RingLogo(this)
        col.addView(logo, LinearLayout.LayoutParams(dp(130), dp(130)))
        logo.scaleX = 0.3f; logo.scaleY = 0.3f; logo.alpha = 0f
        logo.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(700)
            .setInterpolator(OvershootInterpolator(1.6f)).start()

        // اسم برنامه با رنگ گرادیانی
        val title = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 30f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        col.addView(title, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(18) })
        title.post {
            title.paint.shader = LinearGradient(
                0f, 0f, title.width.toFloat(), 0f,
                intArrayOf(Color.parseColor("#8FD3FF"), Color.WHITE, Color.parseColor("#D7B4FF")),
                null, Shader.TileMode.CLAMP
            )
            title.invalidate()
        }

        // پیام خوش‌آمد به سه زبان (چرخشی)
        greet = TextView(this).apply {
            text = greetings[0]
            textSize = 17f
            setTextColor(SUB)
            gravity = Gravity.CENTER
        }
        col.addView(greet, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6) })

        // عنوان انتخاب زبان
        col.addView(TextView(this).apply {
            text = "زبان  •  ژبه  •  Language"
            textSize = 14f
            letterSpacing = 0.05f
            setTextColor(Color.parseColor("#7F8CB0"))
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply {
            topMargin = dp(28)
            bottomMargin = dp(6)
        })

        // کارت‌های زبان
        val langs = listOf(
            arrayOf("fa", "فارسی (دری)", "Persian · Dari", "فا"),
            arrayOf("ps", "پښتو", "Pashto", "پښ"),
            arrayOf("en", "English", "انگلیسی", "En")
        )
        val badgeColors = listOf(
            intArrayOf(Color.parseColor("#2F8CFF"), Color.parseColor("#1A4FD6")),
            intArrayOf(Color.parseColor("#1DE9B6"), Color.parseColor("#00897B")),
            intArrayOf(Color.parseColor("#B46CFF"), Color.parseColor("#6A2FD6"))
        )
        langs.forEachIndexed { i, l ->
            val card = langCard(l[0], l[1], l[2], l[3], badgeColors[i])
            col.addView(card, LinearLayout.LayoutParams(MATCH, dp(82)).apply { topMargin = dp(12) })
            card.alpha = 0f
            card.translationY = dp(40).toFloat()
            card.animate().alpha(1f).translationY(0f)
                .setStartDelay(450L + i * 130L).setDuration(520)
                .setInterpolator(DecelerateInterpolator()).start()
        }

        // دکمهٔ شروع
        startBtn = TextView(this).apply {
            text = "شروع  •  پیل  •  Start"
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(BLUE, PURPLE)
            ).apply { cornerRadius = dp(30).toFloat() }
            alpha = 0.35f
            elevation = dp(8).toFloat()
            if (Build.VERSION.SDK_INT >= 28) {
                outlineSpotShadowColor = PURPLE
                outlineAmbientShadowColor = BLUE
            }
            setOnClickListener { finishChoice() }
        }
        col.addView(startBtn, LinearLayout.LayoutParams(MATCH, dp(58)).apply { topMargin = dp(30) })

        col.addView(TextView(this).apply {
            text = "بعداً از «تنظیمات» قابل تغییر است"
            textSize = 12f
            setTextColor(Color.parseColor("#6E7A9C"))
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(14) })

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            isVerticalScrollBarEnabled = false
            addView(col, ViewGroup.LayoutParams(MATCH, WRAP))
        }
        frame.addView(scroll, FrameLayout.LayoutParams(MATCH, MATCH))
        setContentView(frame)

        startGreetingCycle()
    }

    // ---------------- کارت شیشه‌ای زبان ----------------
    private fun langCard(code: String, name: String, sub: String, badge: String, colors: IntArray): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), 0, dp(16), 0)
            background = glass(false)
            isClickable = true
            isFocusable = true
        }

        val b = TextView(this).apply {
            text = badge
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, colors).apply {
                shape = GradientDrawable.OVAL
            }
            elevation = dp(4).toFloat()
            if (Build.VERSION.SDK_INT >= 28) {
                outlineSpotShadowColor = colors[0]
                outlineAmbientShadowColor = colors[0]
            }
        }
        card.addView(b, LinearLayout.LayoutParams(dp(50), dp(50)))

        val texts = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), 0, dp(14), 0)
        }
        texts.addView(TextView(this).apply {
            text = name
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(TEXT)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        })
        texts.addView(TextView(this).apply {
            text = sub
            textSize = 13f
            setTextColor(SUB)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        })
        card.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f))

        val check = TextView(this).apply {
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = checkBg(false)
        }
        card.addView(check, LinearLayout.LayoutParams(dp(28), dp(28)))

        cards[code] = card
        checks[code] = check
        card.setOnClickListener { select(code) }
        return card
    }

    private fun glass(sel: Boolean) = GradientDrawable(
        GradientDrawable.Orientation.TL_BR,
        if (sel) intArrayOf(Color.argb(0x55, 47, 140, 255), Color.argb(0x40, 180, 108, 255))
        else intArrayOf(Color.argb(0x1E, 255, 255, 255), Color.argb(0x0C, 255, 255, 255))
    ).apply {
        cornerRadius = dp(24).toFloat()
        if (sel) setStroke(dp(2), Color.parseColor("#7DB8FF"))
        else setStroke(dp(1), Color.argb(0x33, 255, 255, 255))
    }

    private fun checkBg(sel: Boolean) = if (sel) {
        GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(BLUE, PURPLE)).apply {
            shape = GradientDrawable.OVAL
        }
    } else {
        GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.TRANSPARENT)
            setStroke(dp(2), Color.argb(0x66, 255, 255, 255))
        }
    }

    // ---------------- انتخاب ----------------
    private fun select(code: String) {
        if (done) return
        selected = code
        for ((k, card) in cards) {
            val on = k == code
            card.background = glass(on)
            checks[k]?.background = checkBg(on)
            checks[k]?.text = if (on) "✓" else ""
            card.elevation = if (on) dp(10).toFloat() else 0f
            if (Build.VERSION.SDK_INT >= 28 && on) {
                card.outlineSpotShadowColor = BLUE
                card.outlineAmbientShadowColor = PURPLE
            }
        }
        cards[code]?.let { c ->
            c.animate().scaleX(1.04f).scaleY(1.04f).setDuration(120).withEndAction {
                c.animate().scaleX(1f).scaleY(1f).setDuration(180)
                    .setInterpolator(OvershootInterpolator(2f)).start()
            }.start()
        }

        // متن‌ها به زبان انتخاب‌شده
        handler.removeCallbacksAndMessages(null)
        greet.text = when (code) { "ps" -> "ښه راغلاست"; "en" -> "Welcome"; else -> "خوش آمدید" }
        startBtn.text = when (code) { "ps" -> "پیل کړئ"; "en" -> "Get started"; else -> "شروع" }
        startBtn.animate().alpha(1f).setDuration(250).start()
        startPulse()
    }

    private fun startPulse() {
        if (pulse != null) return
        pulse = ValueAnimator.ofFloat(1f, 1.04f).apply {
            duration = 900
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val s = it.animatedValue as Float
                startBtn.scaleX = s
                startBtn.scaleY = s
            }
            start()
        }
    }

    private fun startGreetingCycle() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (selected != null) return
                greetIndex = (greetIndex + 1) % greetings.size
                greet.animate().alpha(0f).setDuration(250).withEndAction {
                    greet.text = greetings[greetIndex]
                    greet.animate().alpha(1f).setDuration(250).start()
                }.start()
                handler.postDelayed(this, 2200)
            }
        }, 2200)
    }

    private fun finishChoice() {
        val code = selected ?: return
        if (done) return
        done = true
        getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
            .putString(SELECTED_LANGUAGE, code).apply()
        getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_CHOSEN, true).apply()
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    // تا زبان انتخاب نشده، دکمهٔ برگشت کاری نمی‌کند
    override fun onBackPressed() {
        if (done) super.onBackPressed()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        pulse?.cancel()
        super.onDestroy()
    }

    // ================= پس‌زمینهٔ زنده (هاله‌های نور + ذره‌ها) =================
    private class AuroraView(ctx: Context) : View(ctx) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private val rnd = Random(27)
        private val n = 45
        private val px = FloatArray(n) { rnd.nextFloat() }
        private val py = FloatArray(n) { rnd.nextFloat() }
        private val sp = FloatArray(n) { 0.01f + rnd.nextFloat() * 0.03f }
        private val sz = FloatArray(n) { 1f + rnd.nextFloat() * 2.2f }
        private val ph = FloatArray(n) { rnd.nextFloat() * 6.28f }

        private val blobs = arrayOf(
            floatArrayOf(0.15f, 0.12f, 0.35f, 0f),
            floatArrayOf(0.90f, 0.35f, 0.28f, 1.7f),
            floatArrayOf(0.25f, 0.78f, 0.30f, 3.1f),
            floatArrayOf(0.85f, 0.95f, 0.25f, 4.4f)
        )
        private val blobColors = intArrayOf(
            Color.parseColor("#2F8CFF"),
            Color.parseColor("#B46CFF"),
            Color.parseColor("#1DE9B6"),
            Color.parseColor("#FF3B4F")
        )

        override fun onDraw(canvas: Canvas) {
            val w = width.toFloat(); val h = height.toFloat()
            if (w <= 0f || h <= 0f) return
            val t = SystemClock.uptimeMillis() / 1000f
            canvas.drawColor(Color.parseColor("#070B1A"))

            // هاله‌های نور
            for (i in blobs.indices) {
                val b = blobs[i]
                val cx = w * (b[0] + 0.10f * Math.sin((t * b[2] + b[3]).toDouble()).toFloat())
                val cy = h * (b[1] + 0.06f * Math.cos((t * b[2] * 0.8f + b[3]).toDouble()).toFloat())
                val r = w * 0.75f
                val c = blobColors[i]
                p.shader = RadialGradient(
                    cx, cy, r,
                    Color.argb(0x50, Color.red(c), Color.green(c), Color.blue(c)),
                    Color.argb(0, Color.red(c), Color.green(c), Color.blue(c)),
                    Shader.TileMode.CLAMP
                )
                canvas.drawCircle(cx, cy, r, p)
            }
            p.shader = null

            // ذره‌های نورانی
            for (i in 0 until n) {
                var y = (py[i] - t * sp[i]) % 1f
                if (y < 0f) y += 1f
                val x = px[i] + 0.015f * Math.sin((t + ph[i]).toDouble()).toFloat()
                val tw = 0.35f + 0.65f * (0.5f + 0.5f * Math.sin((t * 2f + ph[i]).toDouble()).toFloat())
                p.color = Color.argb((tw * 200).toInt(), 255, 255, 255)
                canvas.drawCircle(x * w, y * h, sz[i] * resources.displayMetrics.density * 0.8f, p)
            }
            postInvalidateOnAnimation()
        }
    }

    // ================= لوگوی چرخان =================
    private class RingLogo(ctx: Context) : View(ctx) {
        private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
        private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
        private val txt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
            val cx = w / 2f; val cy = h / 2f
            ring.shader = SweepGradient(
                cx, cy,
                intArrayOf(
                    Color.parseColor("#2F8CFF"), Color.parseColor("#B46CFF"),
                    Color.parseColor("#FF3B4F"), Color.parseColor("#FFD54F"),
                    Color.parseColor("#1DE9B6"), Color.parseColor("#2F8CFF")
                ), null
            )
        }

        override fun onDraw(canvas: Canvas) {
            val w = width.toFloat(); val h = height.toFloat()
            val cx = w / 2f; val cy = h / 2f
            val r = Math.min(w, h) / 2f
            val t = SystemClock.uptimeMillis() / 1000f

            // هالهٔ نورانی نفس‌کش
            val glow = 0.75f + 0.25f * Math.sin((t * 2f).toDouble()).toFloat()
            fill.shader = RadialGradient(
                cx, cy, r,
                Color.argb((0x70 * glow).toInt(), 47, 140, 255),
                Color.argb(0, 47, 140, 255), Shader.TileMode.CLAMP
            )
            canvas.drawCircle(cx, cy, r, fill)
            fill.shader = null

            // حلقهٔ رنگین‌کمانی چرخان
            ring.strokeWidth = r * 0.09f
            canvas.save()
            canvas.rotate((t * 60f) % 360f, cx, cy)
            canvas.drawCircle(cx, cy, r * 0.74f, ring)
            canvas.restore()

            // دایرهٔ داخلی
            fill.color = Color.parseColor("#0E1630")
            canvas.drawCircle(cx, cy, r * 0.66f, fill)

            // متن SIM و 27
            txt.textSize = r * 0.22f
            txt.color = Color.parseColor("#9DB4E8")
            txt.letterSpacing = 0.2f
            canvas.drawText("SIM", cx, cy - r * 0.18f, txt)
            txt.letterSpacing = 0f
            txt.textSize = r * 0.62f
            txt.shader = LinearGradient(
                cx - r * 0.4f, 0f, cx + r * 0.4f, 0f,
                Color.parseColor("#8FD3FF"), Color.parseColor("#D7B4FF"), Shader.TileMode.CLAMP
            )
            canvas.drawText("27", cx, cy + r * 0.38f, txt)
            txt.shader = null

            postInvalidateOnAnimation()
        }
    }

    companion object {
        const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
        const val KEY_CHOSEN = "language_chosen"
    }
}