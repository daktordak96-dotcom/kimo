package com.kimo.mayapp

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
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
import android.widget.TextView

/**
 * دکمهٔ شناور دستیار در صفحهٔ اصلی.
 * با اسکرول رو به پایین پنهان و با اسکرول رو به بالا ظاهر می‌شود.
 * بدون تغییر MainActivity و App.kt کار می‌کند (از طریق AssistantInit).
 */
object AssistantBubble : Application.ActivityLifecycleCallbacks {

    private const val TAG = "assistant_bubble"
    private val ui = Handler(Looper.getMainLooper())
    private var lastGreet = 0L

    override fun onActivityResumed(activity: Activity) {
        if (activity.javaClass.simpleName == "MainActivity") {
            activity.window.decorView.postDelayed({ attach(activity) }, 300)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun attach(a: Activity) {
        if (a.isFinishing) return
        val content = a.findViewById<FrameLayout>(android.R.id.content) ?: return
        if (content.findViewWithTag<View>(TAG) != null) return

        val d = a.resources.displayMetrics.density
        fun dp(v: Int) = (v * d).toInt()
        val lang = Strings.currentLang(a)
        fun t(fa: String, en: String, ps: String) = when (lang) { "en" -> en; "ps" -> ps; else -> fa }

        val box = LinearLayout(a).apply {
            tag = TAG
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            clipChildren = false
            clipToPadding = false
        }

        // دکمه با هاله
        val btnBox = FrameLayout(a).apply { clipChildren = false }
        val ring = View(a).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.argb(0x66, 124, 92, 255))
            }
        }
        btnBox.addView(ring, FrameLayout.LayoutParams(dp(56), dp(56), Gravity.CENTER))
        val btn = TextView(a).apply {
            text = "🤖"
            textSize = 25f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.parseColor("#7C5CFF"), Color.parseColor("#2EC9E6"))
            ).apply {
                shape = GradientDrawable.OVAL
                setStroke(dp(2), Color.argb(0x66, 255, 255, 255))
            }
            elevation = 10 * d
        }
        btnBox.addView(btn, FrameLayout.LayoutParams(dp(56), dp(56), Gravity.CENTER))
        box.addView(btnBox, LinearLayout.LayoutParams(dp(76), dp(76)))

        ring.animate().cancel()
        val pulse = android.animation.ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            repeatCount = android.animation.ValueAnimator.INFINITE
            addUpdateListener {
                val f = it.animatedValue as Float
                ring.scaleX = 1f + f * 0.4f
                ring.scaleY = 1f + f * 0.4f
                ring.alpha = 1f - f
            }
            start()
        }
        box.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) { pulse.cancel() }
        })

        // حباب گفتگو
        val speech = TextView(a).apply {
            text = t("سلام 👋 چطور می‌توانم راهنمایی‌تان کنم؟",
                "Hi 👋 How can I help you?",
                "سلام 👋 څنګه مو لارښوونه وکړم؟")
            textSize = 13.5f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#1B1F3A"))
            setPadding(dp(14), dp(10), dp(14), dp(10))
            maxWidth = dp(220)
            background = GradientDrawable().apply {
                cornerRadius = 18 * d
                setColor(Color.WHITE)
            }
            elevation = 8 * d
            visibility = View.GONE
            textDirection = if (lang == "en") View.TEXT_DIRECTION_LTR else View.TEXT_DIRECTION_RTL
        }
        box.addView(speech, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .apply { leftMargin = dp(2) })

        val open = View.OnClickListener {
            a.startActivity(Intent(a, AssistantActivity::class.java))
            a.overridePendingTransition(0, 0)
            speech.visibility = View.GONE
        }
        btn.setOnClickListener(open)
        speech.setOnClickListener(open)

        content.addView(box, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM or Gravity.LEFT
        ).apply { setMargins(dp(8), 0, dp(8), dp(18)) })

        // ورود اولیه
        box.translationY = dp(140).toFloat()
        box.animate().translationY(0f).setDuration(500).setInterpolator(OvershootInterpolator(1.2f)).start()

        var hidden = false
        fun showSpeech() {
            val now = SystemClock.elapsedRealtime()
            if (lastGreet != 0L && now - lastGreet < 60_000) return
            lastGreet = now
            speech.visibility = View.VISIBLE
            speech.alpha = 0f
            speech.scaleX = 0.6f; speech.scaleY = 0.6f
            speech.pivotX = 0f
            speech.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(320)
                .setInterpolator(OvershootInterpolator(1.6f)).start()
            ui.postDelayed({
                speech.animate().alpha(0f).setDuration(250).withEndAction { speech.visibility = View.GONE }.start()
            }, 4500)
        }
        fun hide() {
            if (hidden) return
            hidden = true
            speech.visibility = View.GONE
            box.animate().translationY(dp(140).toFloat()).setDuration(260)
                .setInterpolator(DecelerateInterpolator()).start()
        }
        fun show() {
            if (!hidden) return
            hidden = false
            box.animate().translationY(0f).setDuration(380)
                .setInterpolator(OvershootInterpolator(1.3f)).start()
            ui.postDelayed({ showSpeech() }, 250)
        }

        // بار اول: سلام
        val prefs = a.getSharedPreferences("Assistant", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("greeted", false)) {
            prefs.edit().putBoolean("greeted", true).apply()
            ui.postDelayed({ showSpeech() }, 1300)
        }

        // تشخیص جهت اسکرول
        var scroller: View? = null
        var lastY = 0
        content.viewTreeObserver.addOnScrollChangedListener {
            val s = scroller ?: findScroll(content, box).also { scroller = it } ?: return@addOnScrollChangedListener
            val y = s.scrollY
            val dy = y - lastY
            lastY = y
            if (dy > dp(6)) hide()
            else if (dy < -dp(6)) show()
        }
    }

    private fun findScroll(v: View, skip: View): View? {
        if (v === skip) return null
        val n = v.javaClass.simpleName
        if (n.endsWith("ScrollView") && !n.contains("Horizontal")) return v
        if (v is ViewGroup) {
            for (i in 0 until v.childCount) {
                val r = findScroll(v.getChildAt(i), skip)
                if (r != null) return r
            }
        }
        return null
    }
}

/** هنگام شروع برنامه خودکار اجرا می‌شود و دستیار را فعال می‌کند */
class AssistantInit : ContentProvider() {
    override fun onCreate(): Boolean {
        (context?.applicationContext as? Application)?.registerActivityLifecycleCallbacks(AssistantBubble)
        return true
    }
    override fun query(uri: Uri, p: Array<String>?, s: String?, a: Array<String>?, o: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, s: String?, a: Array<String>?): Int = 0
    override fun update(uri: Uri, v: ContentValues?, s: String?, a: Array<String>?): Int = 0
}