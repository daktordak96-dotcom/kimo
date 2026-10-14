package com.kimo.mayapp

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.LruCache
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * گالری خصوصی مدرن با رمز ۴ رقمی
 * عکس‌ها در حافظهٔ خصوصی برنامه نگهداری می‌شوند (filesDir/vault)
 */
class VaultActivity : AppCompatActivity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌ها
    private val BG = Color.parseColor("#070A16")
    private val ACCENT = Color.parseColor("#7C5CFF")
    private val ACCENT2 = Color.parseColor("#C06CFF")
    private val TEAL = Color.parseColor("#2EE6D6")
    private val GOLD = Color.parseColor("#FFC857")
    private val TEXT = Color.parseColor("#F4F6FF")
    private val SUB = Color.parseColor("#9AA5CC")
    private val RED = Color.parseColor("#FF4D6A")

    private val REQ_PICK = 501
    private val REQ_EXPORT = 502

    private lateinit var root: FrameLayout
    private lateinit var content: FrameLayout
    private lateinit var prefs: android.content.SharedPreferences
    private lateinit var vaultDir: File
    private val ui = Handler(Looper.getMainLooper())
    private val pool: ExecutorService = Executors.newFixedThreadPool(3)
    private val cache = object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()) {
        override fun sizeOf(key: String, value: Bitmap) = value.byteCount / 1024
    }

    private var locked = true
    private var busyOutside = false
    private var pendingExport: File? = null

    // صفحهٔ رمز
    private enum class Mode { SETUP, CONFIRM, UNLOCK }
    private var mode = Mode.UNLOCK
    private var typed = ""
    private var firstPin = ""
    private var dots: LinearLayout? = null
    private var pinTitle: TextView? = null
    private var pinHint: TextView? = null

    private lateinit var lang: String
    private fun t(fa: String, en: String, ps: String) = when (lang) { "en" -> en; "ps" -> ps; else -> fa }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun dpf(v: Float) = v * resources.displayMetrics.density

    private fun num(s: String): String {
        if (lang == "en") return s
        val sb = StringBuilder()
        for (c in s) sb.append(if (c in '0'..'9') ('۰' + (c - '0')) else c)
        return sb.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        window.statusBarColor = BG
        window.navigationBarColor = BG

        lang = Strings.currentLang(this)
        prefs = getSharedPreferences("Vault", Context.MODE_PRIVATE)
        vaultDir = File(filesDir, "vault").apply { mkdirs() }

        root = FrameLayout(this).apply {
            setBackgroundColor(BG)
            layoutDirection = if (lang == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
        }
        root.addView(AuroraView(this, BG), FrameLayout.LayoutParams(MATCH, MATCH))
        content = FrameLayout(this)
        root.addView(content, FrameLayout.LayoutParams(MATCH, MATCH))
        setContentView(root)
        showPin()
    }

    override fun onStop() {
        super.onStop()
        if (!busyOutside && !isChangingConfigurations) locked = true
    }

    override fun onRestart() {
        super.onRestart()
        if (locked) showPin()
    }

    override fun onDestroy() {
        super.onDestroy()
        pool.shutdownNow()
    }

    // ---------------- ابزار ظاهری ----------------

    private fun glass(radius: Float, alpha: Int = 0x1C): GradientDrawable = GradientDrawable().apply {
        cornerRadius = radius
        setColor(Color.argb(alpha, 255, 255, 255))
        setStroke(dp(1), Color.argb(0x30, 255, 255, 255))
    }

    private fun gradientText(tv: TextView, c1: Int, c2: Int) {
        tv.post {
            tv.paint.shader = LinearGradient(0f, 0f, tv.width.toFloat(), 0f, c1, c2, Shader.TileMode.CLAMP)
            tv.invalidate()
        }
    }

    private fun pressEffect(v: View) {
        v.setOnTouchListener { view, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> view.animate().scaleX(0.93f).scaleY(0.93f).setDuration(90).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    view.animate().scaleX(1f).scaleY(1f).setDuration(160).setInterpolator(OvershootInterpolator()).start()
            }
            false
        }
    }

    private fun dialogStyle() = android.R.style.Theme_DeviceDefault_Dialog_Alert

    // =====================================================================
    // صفحهٔ رمز
    // =====================================================================
    private fun hasPin() = prefs.getString("pin_hash", null) != null

    private fun hash(pin: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val b = md.digest(("kimo-vault-27:" + pin).toByteArray())
        return b.joinToString("") { "%02x".format(it) }
    }

    private fun showPin() {
        locked = true
        typed = ""
        mode = if (hasPin()) Mode.UNLOCK else Mode.SETUP
        content.removeAllViews()

        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(24), dp(36), dp(24), dp(24))
        }

        col.addView(TextView(this).apply {
            text = if (lang == "en") "←" else "→"
            textSize = 24f
            setTextColor(TEXT)
            setOnClickListener { finish() }
        }, LinearLayout.LayoutParams(MATCH, WRAP))

        val lockBox = FrameLayout(this)
        val halo = View(this).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                gradientType = GradientDrawable.RADIAL_GRADIENT
                gradientRadius = dpf(60f)
                colors = intArrayOf(Color.argb(0x88, 124, 92, 255), Color.TRANSPARENT)
            }
        }
        lockBox.addView(halo, FrameLayout.LayoutParams(dp(120), dp(120), Gravity.CENTER))
        lockBox.addView(TextView(this).apply {
            text = "🔒"
            textSize = 46f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }, FrameLayout.LayoutParams(WRAP, WRAP, Gravity.CENTER))
        col.addView(lockBox, LinearLayout.LayoutParams(dp(120), dp(120)))
        ValueAnimator.ofFloat(0.85f, 1.15f).apply {
            duration = 1600
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val s = it.animatedValue as Float
                halo.scaleX = s; halo.scaleY = s
            }
            start()
        }

        pinTitle = TextView(this).apply {
            textSize = 23f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(TEXT)
            gravity = Gravity.CENTER
        }
        col.addView(pinTitle, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6) })

        pinHint = TextView(this).apply {
            textSize = 14f
            setTextColor(SUB)
            gravity = Gravity.CENTER
        }
        col.addView(pinHint, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(6) })

        dots = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        col.addView(dots, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(26) })

        col.addView(keypad(), LinearLayout.LayoutParams(WRAP, WRAP).apply {
            topMargin = dp(30)
            gravity = Gravity.CENTER_HORIZONTAL
        })

        content.addView(ScrollView(this).apply {
            isFillViewport = true
            addView(col)
        }, FrameLayout.LayoutParams(MATCH, MATCH))
        updatePinTexts()
        drawDots()

        col.alpha = 0f
        col.translationY = dpf(30f)
        col.animate().alpha(1f).translationY(0f).setDuration(450).setInterpolator(DecelerateInterpolator()).start()
    }

    private fun updatePinTexts() {
        when (mode) {
            Mode.SETUP -> {
                pinTitle?.text = t("رمز گالری را بسازید", "Create a PIN", "د ګالرۍ رمز جوړ کړئ")
                pinHint?.text = t("یک رمز ۴ رقمی انتخاب کنید", "Choose a 4-digit PIN", "یو ۴ رقمي رمز وټاکئ")
            }
            Mode.CONFIRM -> {
                pinTitle?.text = t("رمز را تکرار کنید", "Confirm your PIN", "رمز بیا ولیکئ")
                pinHint?.text = t("همان رمز را دوباره وارد کنید", "Enter the same PIN again", "هماغه رمز بیا ولیکئ")
            }
            Mode.UNLOCK -> {
                pinTitle?.text = t("گالری خصوصی", "Private Gallery", "شخصي ګالري")
                pinHint?.text = t("رمز خود را وارد کنید", "Enter your PIN", "خپل رمز ولیکئ")
            }
        }
    }

    private fun drawDots() {
        val d = dots ?: return
        d.removeAllViews()
        for (i in 0 until 4) {
            val on = i < typed.length
            val v = View(this).apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    if (on) {
                        orientation = GradientDrawable.Orientation.TL_BR
                        colors = intArrayOf(ACCENT, ACCENT2)
                    } else {
                        setColor(Color.TRANSPARENT)
                        setStroke(dp(2), SUB)
                    }
                }
            }
            d.addView(v, LinearLayout.LayoutParams(dp(16), dp(16)).apply { setMargins(dp(11), 0, dp(11), 0) })
            if (on && i == typed.length - 1) {
                v.scaleX = 0.4f; v.scaleY = 0.4f
                v.animate().scaleX(1f).scaleY(1f).setDuration(220).setInterpolator(OvershootInterpolator(3f)).start()
            }
        }
    }

    private fun keypad(): View {
        val grid = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "⌫")
        )
        for (row in keys) {
            val r = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            for (k in row) {
                val b = TextView(this).apply {
                    text = if (k == "⌫" || k.isEmpty()) k else num(k)
                    textSize = 25f
                    typeface = Typeface.DEFAULT_BOLD
                    setTextColor(TEXT)
                    gravity = Gravity.CENTER
                    if (k.isNotEmpty()) {
                        background = GradientDrawable().apply {
                            shape = GradientDrawable.OVAL
                            setColor(Color.argb(0x1C, 255, 255, 255))
                            setStroke(dp(1), Color.argb(0x30, 255, 255, 255))
                        }
                        pressEffect(this)
                        setOnClickListener { onKey(k) }
                    }
                }
                r.addView(b, LinearLayout.LayoutParams(dp(72), dp(72)).apply { setMargins(dp(10), dp(8), dp(10), dp(8)) })
            }
            grid.addView(r)
        }
        return grid
    }

    private fun onKey(k: String) {
        val lockUntil = prefs.getLong("lock_until", 0L)
        val now = SystemClock.elapsedRealtime()
        if (now < lockUntil && lockUntil - now < 60_000) {
            val s = ((lockUntil - now) / 1000) + 1
            toast(t("لطفاً ${num(s.toString())} ثانیه صبر کنید", "Please wait $s seconds", "مهرباني وکړئ ${num(s.toString())} ثانیې صبر وکړئ"))
            return
        }
        if (k == "⌫") {
            if (typed.isNotEmpty()) typed = typed.dropLast(1)
            drawDots()
            return
        }
        if (typed.length >= 4) return
        typed += k
        drawDots()
        if (typed.length == 4) ui.postDelayed({ submitPin() }, 180)
    }

    private fun submitPin() {
        when (mode) {
            Mode.SETUP -> {
                firstPin = typed
                typed = ""
                mode = Mode.CONFIRM
                updatePinTexts()
                drawDots()
            }
            Mode.CONFIRM -> {
                if (typed == firstPin) {
                    prefs.edit().putString("pin_hash", hash(typed)).apply()
                    toast(t("رمز ذخیره شد", "PIN saved", "رمز خوندي شو"))
                    unlock()
                } else {
                    wrong(t("رمزها یکی نیستند، دوباره بسازید", "PINs don't match, try again", "رمزونه یو شان نه دي، بیا هڅه وکړئ"), 0)
                    mode = Mode.SETUP
                    pinTitle?.text = t("رمز گالری را بسازید", "Create a PIN", "د ګالرۍ رمز جوړ کړئ")
                }
            }
            Mode.UNLOCK -> {
                if (hash(typed) == prefs.getString("pin_hash", "")) {
                    prefs.edit().putInt("fails", 0).apply()
                    unlock()
                } else {
                    val fails = prefs.getInt("fails", 0) + 1
                    if (fails >= 5) {
                        prefs.edit().putInt("fails", 0)
                            .putLong("lock_until", SystemClock.elapsedRealtime() + 30_000).apply()
                        wrong(t("۵ بار اشتباه؛ ۳۰ ثانیه صبر کنید", "5 wrong tries; wait 30 seconds", "۵ ځله غلط؛ ۳۰ ثانیې صبر وکړئ"), 5)
                    } else {
                        prefs.edit().putInt("fails", fails).apply()
                        wrong(t("رمز اشتباه است", "Wrong PIN", "رمز غلط دی"), fails)
                    }
                }
            }
        }
    }

    /** angryLevel = 0 یعنی بدون ایموجی (مثلاً هنگام ساخت رمز) */
    private fun wrong(msg: String, angryLevel: Int) {
        typed = ""
        drawDots()
        pinHint?.text = msg
        pinHint?.setTextColor(RED)
        if (angryLevel > 0) AngryFace.show(root, lang, angryLevel)
        val d = dots ?: return
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 420
            addUpdateListener {
                val f = it.animatedFraction
                d.translationX = (Math.sin(f * Math.PI * 6) * dpf(14f) * (1 - f)).toFloat()
            }
            start()
        }
        ui.postDelayed({ pinHint?.setTextColor(SUB) }, 1600)
    }

    private fun unlock() {
        locked = false
        showGallery(true)
    }

    // =====================================================================
    // صفحهٔ گالری
    // =====================================================================
    private fun files(): MutableList<File> =
        (vaultDir.listFiles()?.toList() ?: emptyList()).sortedByDescending { it.lastModified() }.toMutableList()

    private fun showGallery(animateIn: Boolean = false) {
        content.removeAllViews()
        val list = files()
        val col = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        col.addView(header(list.size), LinearLayout.LayoutParams(MATCH, WRAP))

        if (!prefs.getBoolean("warn_hidden", false)) {
            col.addView(warningCard(), LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(dp(16), dp(4), dp(16), dp(10))
            })
        }

        if (list.isEmpty()) {
            col.addView(emptyState(), LinearLayout.LayoutParams(MATCH, 0, 1f))
            content.addView(col, FrameLayout.LayoutParams(MATCH, MATCH))
        } else {
            col.addView(grid(list), LinearLayout.LayoutParams(MATCH, 0, 1f))
            content.addView(col, FrameLayout.LayoutParams(MATCH, MATCH))
            content.addView(fab(), FrameLayout.LayoutParams(dp(84), dp(84), Gravity.BOTTOM or Gravity.END).apply {
                setMargins(dp(14), dp(14), dp(14), dp(20))
            })
        }

        if (animateIn) {
            col.alpha = 0f
            col.animate().alpha(1f).setDuration(350).start()
        }
    }

    private fun header(count: Int): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(14), dp(18), dp(10))
        }
        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        fun roundBtn(label: String, click: () -> Unit) = TextView(this).apply {
            text = label
            textSize = 17f
            setTextColor(TEXT)
            gravity = Gravity.CENTER
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.argb(0x1C, 255, 255, 255))
                setStroke(dp(1), Color.argb(0x30, 255, 255, 255))
            }
            pressEffect(this)
            setOnClickListener { click() }
        }
        top.addView(roundBtn(if (lang == "en") "←" else "→") { finish() }, LinearLayout.LayoutParams(dp(42), dp(42)))
        top.addView(View(this), LinearLayout.LayoutParams(0, 1, 1f))
        if (prefs.getBoolean("warn_hidden", false)) {
            top.addView(roundBtn("ⓘ") {
                prefs.edit().putBoolean("warn_hidden", false).apply()
                showGallery()
            }, LinearLayout.LayoutParams(dp(42), dp(42)).apply { marginEnd = dp(8) })
        }
        top.addView(roundBtn("🔒") {
            locked = true
            showPin()
        }, LinearLayout.LayoutParams(dp(42), dp(42)))
        box.addView(top)

        val title = TextView(this).apply {
            text = t("گالری خصوصی", "Private Gallery", "شخصي ګالري")
            textSize = 30f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        }
        box.addView(title, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(14) })
        gradientText(title, Color.WHITE, Color.parseColor("#C9B4FF"))

        val chip = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(12), dp(5), dp(12), dp(5))
            background = glass(dpf(20f))
        }
        chip.addView(View(this).apply {
            background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(TEAL) }
        }, LinearLayout.LayoutParams(dp(8), dp(8)).apply { marginEnd = dp(8) })
        chip.addView(TextView(this).apply {
            text = num(count.toString()) + " " + t("عکس", "photos", "انځورونه") + "  •  " +
                    t("محافظت با رمز", "PIN protected", "د رمز سره خوندي")
            textSize = 12.5f
            setTextColor(SUB)
        })
        box.addView(chip, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(8) })
        return box
    }

    // ---------------- کارت هشدار پویا ----------------
    private fun warningCard(): View {
        val glow = GlowCard(dpf(20f), dpf(1.6f), GOLD, Color.parseColor("#FF8A5C"))
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14), dp(12), dp(10), dp(12))
            background = glow
        }
        val spin = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 3600
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { glow.angle = it.animatedValue as Float; glow.invalidateSelf() }
        }

        val icon = TextView(this).apply {
            text = "🛡️"
            textSize = 20f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.argb(0x33, 255, 200, 87))
            }
        }
        card.addView(icon, LinearLayout.LayoutParams(dp(42), dp(42)))
        val pulse = ValueAnimator.ofFloat(1f, 1.12f).apply {
            duration = 900
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val s = it.animatedValue as Float
                icon.scaleX = s; icon.scaleY = s
            }
        }

        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = t("نکتهٔ مهم", "Important", "مهمه خبره")
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(GOLD)
        })
        texts.addView(TextView(this).apply {
            text = t(
                "عکس‌ها فقط داخل همین برنامه هستند؛ با حذف برنامه پاک می‌شوند. قبل از حذف، «ذخیره در گوشی» بزنید.",
                "Photos live only inside this app and are erased if you uninstall it. Save them to your phone first.",
                "انځورونه یوازې په دې اپ کې دي؛ د اپ په ړنګولو ړنګېږي. مخکې «په ګوشي کې خوندي» کړئ."
            )
            textSize = 12.5f
            setTextColor(Color.parseColor("#E9E2CF"))
            setLineSpacing(dpf(2f), 1f)
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { topMargin = dp(2) })
        card.addView(texts, LinearLayout.LayoutParams(0, WRAP, 1f).apply { setMargins(dp(12), 0, dp(6), 0) })

        val close = TextView(this).apply {
            text = "✕"
            textSize = 14f
            setTextColor(SUB)
            gravity = Gravity.CENTER
        }
        card.addView(close, LinearLayout.LayoutParams(dp(32), dp(32)))
        close.setOnClickListener {
            val h = card.height
            ValueAnimator.ofFloat(1f, 0f).apply {
                duration = 320
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val f = it.animatedValue as Float
                    card.alpha = f
                    card.layoutParams = card.layoutParams.apply { height = (h * f).toInt() }
                }
                addListener(object : android.animation.AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: android.animation.Animator?) {
                        prefs.edit().putBoolean("warn_hidden", true).apply()
                        showGallery()
                    }
                })
                start()
            }
        }

        card.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) { spin.start(); pulse.start() }
            override fun onViewDetachedFromWindow(v: View?) { spin.cancel(); pulse.cancel() }
        })

        card.alpha = 0f
        card.translationY = -dpf(12f)
        card.animate().alpha(1f).translationY(0f).setStartDelay(150).setDuration(400).start()
        return card
    }

    // ---------------- گالری خالی ----------------
    private fun emptyState(): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(30), 0, dp(30), dp(60))
        }
        val icon = TextView(this).apply {
            text = "🖼️"
            textSize = 64f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        box.addView(icon)
        ValueAnimator.ofFloat(-1f, 1f).apply {
            duration = 1800
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { icon.translationY = (it.animatedValue as Float) * dpf(8f) }
            start()
        }
        box.addView(TextView(this).apply {
            text = t("گالری شما خالی است", "Your gallery is empty", "ستاسو ګالري تشه ده")
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(TEXT)
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(14) })
        box.addView(TextView(this).apply {
            text = t("عکس‌های شخصی‌تان را اینجا امن نگه دارید", "Keep your private photos safe here", "خپل شخصي انځورونه دلته خوندي وساتئ")
            textSize = 14f
            setTextColor(SUB)
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(6) })
        box.addView(TextView(this).apply {
            text = "＋  " + t("افزودن عکس", "Add photos", "انځور زیاتول")
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(dp(28), dp(14), dp(28), dp(14))
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(ACCENT, ACCENT2)).apply {
                cornerRadius = dpf(28f)
            }
            elevation = dpf(8f)
            pressEffect(this)
            setOnClickListener { pickImages() }
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply { topMargin = dp(26) })
        return box
    }

    // ---------------- شبکهٔ عکس‌ها ----------------
    private fun grid(list: List<File>): View {
        val g = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(10), dp(2), dp(10), dp(120))
        }
        val gap = dp(4)
        val cell = (resources.displayMetrics.widthPixels - dp(20) - gap * 6) / 3
        var row: LinearLayout? = null
        list.forEachIndexed { i, f ->
            if (i % 3 == 0) {
                row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
                g.addView(row)
            }
            val iv = ImageView(this).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                background = GradientDrawable().apply {
                    cornerRadius = dpf(16f)
                    setColor(Color.argb(0x22, 255, 255, 255))
                }
                outlineProvider = ViewOutlineProvider.BACKGROUND
                clipToOutline = true
                pressEffect(this)
                setOnClickListener { openViewer(i) }
                setOnLongClickListener { confirmDelete(f) { showGallery() }; true }
            }
            row?.addView(iv, LinearLayout.LayoutParams(cell, cell).apply { setMargins(gap, gap, gap, gap) })

            iv.alpha = 0f
            iv.scaleX = 0.8f; iv.scaleY = 0.8f
            iv.animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setStartDelay(minOf(i, 18) * 40L).setDuration(350)
                .setInterpolator(DecelerateInterpolator()).start()
            loadThumb(f, iv, cell)
        }
        return ScrollView(this).apply {
            isVerticalScrollBarEnabled = false
            addView(g)
        }
    }

    private fun fab(): View {
        val box = FrameLayout(this)
        val ring = View(this).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.argb(0x66, 160, 110, 255))
            }
        }
        box.addView(ring, FrameLayout.LayoutParams(dp(62), dp(62), Gravity.CENTER))
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1800
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val f = it.animatedValue as Float
                ring.scaleX = 1f + f * 0.45f
                ring.scaleY = 1f + f * 0.45f
                ring.alpha = 1f - f
            }
            start()
        }
        val btn = TextView(this).apply {
            text = "＋"
            textSize = 28f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(ACCENT, ACCENT2)).apply {
                shape = GradientDrawable.OVAL
            }
            elevation = dpf(10f)
            pressEffect(this)
            setOnClickListener { pickImages() }
        }
        box.addView(btn, FrameLayout.LayoutParams(dp(62), dp(62), Gravity.CENTER))
        btn.scaleX = 0f; btn.scaleY = 0f
        btn.animate().scaleX(1f).scaleY(1f).setStartDelay(300).setDuration(450)
            .setInterpolator(OvershootInterpolator(2.5f)).start()
        return box
    }

    private fun loadThumb(f: File, iv: ImageView, size: Int) {
        val key = f.absolutePath + "#t"
        cache.get(key)?.let { iv.setImageBitmap(it); return }
        pool.execute {
            val bmp = decode(f, size) ?: return@execute
            cache.put(key, bmp)
            ui.post { iv.setImageBitmap(bmp) }
        }
    }

    private fun decode(f: File, target: Int): Bitmap? = try {
        val o = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(f.absolutePath, o)
        var s = 1
        while (o.outWidth / (s * 2) >= target && o.outHeight / (s * 2) >= target) s *= 2
        BitmapFactory.decodeFile(f.absolutePath, BitmapFactory.Options().apply { inSampleSize = s })
    } catch (e: Throwable) { null }

    private fun confirmDelete(f: File, after: () -> Unit) {
        AlertDialog.Builder(this, dialogStyle())
            .setMessage(t("این عکس برای همیشه حذف شود؟", "Delete this photo permanently?", "دا انځور د تل لپاره ړنګ شي؟"))
            .setPositiveButton(t("حذف", "Delete", "ړنګول")) { _, _ ->
                cache.remove(f.absolutePath + "#t")
                f.delete()
                after()
            }
            .setNegativeButton(t("لغو", "Cancel", "لغوه"), null)
            .show()
    }

    // =====================================================================
    // افزودن عکس
    // =====================================================================
    private fun pickImages() {
        busyOutside = true
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        try {
            startActivityForResult(i, REQ_PICK)
        } catch (e: Exception) {
            busyOutside = false
            toast("Error")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        busyOutside = false
        if (resultCode != Activity.RESULT_OK || data == null) return

        if (requestCode == REQ_PICK) {
            val uris = ArrayList<Uri>()
            data.clipData?.let { cd -> for (k in 0 until cd.itemCount) uris.add(cd.getItemAt(k).uri) }
            if (uris.isEmpty()) data.data?.let { uris.add(it) }
            importImages(uris)
        } else if (requestCode == REQ_EXPORT) {
            val src = pendingExport ?: return
            val dest = data.data ?: return
            pool.execute {
                val ok = try {
                    contentResolver.openOutputStream(dest)?.use { out ->
                        src.inputStream().use { it.copyTo(out) }
                    }
                    true
                } catch (e: Exception) { false }
                ui.post {
                    toast(if (ok) t("در گوشی ذخیره شد ✓", "Saved to phone ✓", "په ګوشي کې خوندي شو ✓") else "Error")
                }
            }
            pendingExport = null
        }
    }

    private fun importImages(uris: List<Uri>) {
        if (uris.isEmpty()) return
        toast(t("در حال افزودن…", "Adding…", "زیاتېږي…"))
        pool.execute {
            var n = 0
            for ((i, u) in uris.withIndex()) {
                try {
                    val out = File(vaultDir, "img_${System.currentTimeMillis()}_$i.jpg")
                    contentResolver.openInputStream(u)?.use { input ->
                        out.outputStream().use { input.copyTo(it) }
                    }
                    n++
                } catch (e: Exception) { }
            }
            ui.post {
                toast(t("${num(n.toString())} عکس اضافه شد. اگر خواستید، اصل عکس‌ها را از گالری گوشی پاک کنید.",
                    "$n photos added. You may now delete the originals from your phone gallery.",
                    "${num(n.toString())} انځورونه زیات شول. که غواړئ، اصلي انځورونه د ګوشي له ګالرۍ ړنګ کړئ."))
                showGallery()
            }
        }
    }

    // =====================================================================
    // نمایش تمام‌صفحه با کشیدن بین عکس‌ها
    // =====================================================================
    private fun openViewer(start: Int) {
        val list = files()
        if (list.isEmpty()) return
        var idx = start.coerceIn(0, list.size - 1)
        val screenW = resources.displayMetrics.widthPixels

        val d = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        d.window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        val box = FrameLayout(this).apply {
            setBackgroundColor(Color.parseColor("#04050B"))
            layoutDirection = root.layoutDirection
        }
        val iv = ImageView(this).apply { scaleType = ImageView.ScaleType.FIT_CENTER }
        box.addView(iv, FrameLayout.LayoutParams(MATCH, MATCH))

        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(28), dp(16), dp(16))
            background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.argb(0xCC, 0, 0, 0), Color.TRANSPARENT))
        }
        val counter = TextView(this).apply {
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            setPadding(dp(14), dp(6), dp(14), dp(6))
            background = glass(dpf(16f), 0x26)
        }
        topBar.addView(counter)
        topBar.addView(View(this), LinearLayout.LayoutParams(0, 1, 1f))
        topBar.addView(TextView(this).apply {
            text = "✕"
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.argb(0x26, 255, 255, 255))
            }
            setOnClickListener { d.dismiss() }
        }, LinearLayout.LayoutParams(dp(40), dp(40)))
        box.addView(topBar, FrameLayout.LayoutParams(MATCH, WRAP, Gravity.TOP))

        fun load(dir: Int) {
            val f = list[idx]
            counter.text = num("${idx + 1} / ${list.size}")
            iv.scaleX = 1f; iv.scaleY = 1f
            val put = {
                val key = f.absolutePath + "#full"
                val c = cache.get(key)
                if (c != null) iv.setImageBitmap(c) else {
                    iv.setImageBitmap(null)
                    pool.execute {
                        val bmp = decode(f, screenW)
                        if (bmp != null) cache.put(key, bmp)
                        ui.post { if (list.getOrNull(idx) == f) iv.setImageBitmap(bmp) }
                    }
                }
            }
            if (dir == 0) {
                put()
                iv.alpha = 0f
                iv.animate().alpha(1f).setDuration(250).start()
            } else {
                iv.animate().translationX(-dir * screenW * 0.35f).alpha(0f).setDuration(130).withEndAction {
                    put()
                    iv.translationX = dir * screenW * 0.35f
                    iv.animate().translationX(0f).alpha(1f).setDuration(220)
                        .setInterpolator(DecelerateInterpolator()).start()
                }.start()
            }
        }

        val bottom = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(10), dp(10), dp(10), dp(10))
            background = glass(dpf(26f), 0x22)
        }
        fun action(icon: String, label: String, color: Int, click: () -> Unit) = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(0, dp(4), 0, dp(4))
            addView(TextView(context).apply {
                text = icon
                textSize = 20f
                setTextColor(color)
                gravity = Gravity.CENTER
            })
            addView(TextView(context).apply {
                text = label
                textSize = 11.5f
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
            })
            pressEffect(this)
            setOnClickListener { click() }
        }
        bottom.addView(action("⬇", t("ذخیره در گوشی", "Save to phone", "په ګوشي کې خوندي"), TEAL) {
            exportFile(list[idx])
        }, LinearLayout.LayoutParams(0, WRAP, 1f))
        bottom.addView(action("🗑", t("حذف", "Delete", "ړنګول"), RED) {
            confirmDelete(list[idx]) {
                list.removeAt(idx)
                if (list.isEmpty()) d.dismiss() else {
                    if (idx >= list.size) idx = list.size - 1
                    load(1)
                }
            }
        }, LinearLayout.LayoutParams(0, WRAP, 1f))
        box.addView(bottom, FrameLayout.LayoutParams(MATCH, WRAP, Gravity.BOTTOM).apply {
            setMargins(dp(24), 0, dp(24), dp(28))
        })

        var barsVisible = true
        val gd = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?) = true
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, vx: Float, vy: Float): Boolean {
                if (Math.abs(vx) > Math.abs(vy) && Math.abs(vx) > 600) {
                    if (vx < 0 && idx < list.size - 1) { idx++; load(1) }
                    else if (vx > 0 && idx > 0) { idx--; load(-1) }
                    return true
                }
                return false
            }
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                val target = if (iv.scaleX > 1.1f) 1f else 2.2f
                iv.animate().scaleX(target).scaleY(target).setDuration(220).start()
                return true
            }
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                barsVisible = !barsVisible
                val a = if (barsVisible) 1f else 0f
                topBar.animate().alpha(a).setDuration(200).start()
                bottom.animate().alpha(a).setDuration(200).start()
                return true
            }
        })
        box.setOnTouchListener { _, e -> gd.onTouchEvent(e) }

        d.setContentView(box)
        d.setOnDismissListener { showGallery() }
        d.show()
        load(0)
        bottom.translationY = dpf(80f)
        bottom.animate().translationY(0f).setDuration(380).setInterpolator(DecelerateInterpolator()).start()
    }

    private fun exportFile(f: File) {
        pendingExport = f
        busyOutside = true
        val i = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/jpeg"
            putExtra(Intent.EXTRA_TITLE, "photo_${System.currentTimeMillis()}.jpg")
        }
        try {
            startActivityForResult(i, REQ_EXPORT)
        } catch (e: Exception) {
            busyOutside = false
            toast("Error")
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()

    // =====================================================================
    // پس‌زمینهٔ نورهای متحرک
    // =====================================================================
    private class AuroraView(ctx: Context, private val base: Int) : View(ctx) {
        private val p = Paint(Paint.ANTI_ALIAS_FLAG)
        private var t = 0f
        private val anim = ValueAnimator.ofFloat(0f, (Math.PI * 2).toFloat()).apply {
            duration = 16000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { t = it.animatedValue as Float; invalidate() }
        }
        private val blobs = arrayOf(
            floatArrayOf(0.15f, 0.12f, 0.80f, 0f),
            floatArrayOf(0.90f, 0.40f, 0.70f, 2f),
            floatArrayOf(0.30f, 0.85f, 0.75f, 4f)
        )
        private val colors = intArrayOf(
            Color.argb(0x66, 124, 92, 255),
            Color.argb(0x44, 46, 230, 214),
            Color.argb(0x4D, 192, 108, 255)
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
                val cx = w * (b[0] + 0.12f * Math.sin((t + b[3]).toDouble()).toFloat())
                val cy = h * (b[1] + 0.07f * Math.cos((t * 1.3f + b[3]).toDouble()).toFloat())
                val r = w * b[2]
                p.shader = RadialGradient(cx, cy, r, colors[i], Color.TRANSPARENT, Shader.TileMode.CLAMP)
                canvas.drawCircle(cx, cy, r, p)
            }
            p.shader = null
        }
    }

    // =====================================================================
    // کارت با نور چرخان دور لبه
    // =====================================================================
    private class GlowCard(
        private val radius: Float,
        private val sw: Float,
        private val c1: Int,
        private val c2: Int
    ) : Drawable() {
        var angle = 0f
        private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(0x24, 255, 190, 80) }
        private val base = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = sw
            color = Color.argb(0x33, 255, 200, 87)
        }
        private val st = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = sw * 1.4f
        }
        private val m = Matrix()
        private val rect = RectF()

        override fun draw(canvas: Canvas) {
            rect.set(bounds)
            rect.inset(sw, sw)
            canvas.drawRoundRect(rect, radius, radius, fill)
            canvas.drawRoundRect(rect, radius, radius, base)
            val cx = rect.centerX()
            val cy = rect.centerY()
            val sh = SweepGradient(cx, cy,
                intArrayOf(Color.TRANSPARENT, c1, c2, Color.TRANSPARENT, Color.TRANSPARENT),
                floatArrayOf(0f, 0.12f, 0.22f, 0.34f, 1f))
            m.setRotate(angle, cx, cy)
            sh.setLocalMatrix(m)
            st.shader = sh
            canvas.drawRoundRect(rect, radius, radius, st)
        }

        override fun setAlpha(alpha: Int) { fill.alpha = alpha }
        override fun setColorFilter(colorFilter: ColorFilter?) { st.colorFilter = colorFilter }
        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }
}