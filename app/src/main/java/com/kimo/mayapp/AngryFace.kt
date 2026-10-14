package com.kimo.mayapp

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

/**
 * ایموجی عصبانی که هنگام رمز اشتباه سرش را تکان می‌دهد.
 * level: شمارهٔ اشتباه (۱ تا ۵)
 */
object AngryFace {

    private const val TAG = "angry_face"

    fun show(root: FrameLayout, lang: String, level: Int) {
        val ctx = root.context
        val d = ctx.resources.displayMetrics.density
        fun dp(v: Int) = (v * d).toInt()
        fun t(fa: String, en: String, ps: String) = when (lang) { "en" -> en; "ps" -> ps; else -> fa }

        root.findViewWithTag<View>(TAG)?.let { root.removeView(it) }

        val face: String
        val msg: String
        when (level) {
            1 -> { face = "😠"; msg = t("رمز اشتباه است!", "Wrong PIN!", "رمز غلط دی!") }
            2 -> { face = "😤"; msg = t("باز هم اشتباه!", "Wrong again!", "بیا هم غلط!") }
            3 -> { face = "😡"; msg = t("شما صاحب این گالری نیستید؟!", "Are you really the owner?!", "ته د دې ګالرۍ څښتن نه یې؟!") }
            4 -> { face = "🤬"; msg = t("آخرین فرصت! یک اشتباه دیگر = قفل", "Last chance! One more = locked", "وروستی چانس! بل غلط = قلف") }
            else -> { face = "👮"; msg = t("۳۰ ثانیه قفل شد!", "Locked for 30 seconds!", "۳۰ ثانیې قلف شو!") }
        }

        // لایهٔ کامل
        val layer = FrameLayout(ctx).apply {
            tag = TAG
            isClickable = false
            setBackgroundColor(Color.argb(0x55, 120, 0, 20))
            alpha = 0f
        }

        val col = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        // هالهٔ قرمز + ایموجی
        val faceBox = FrameLayout(ctx).apply { clipChildren = false }
        val glow = View(ctx).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                gradientType = GradientDrawable.RADIAL_GRADIENT
                gradientRadius = 95 * d
                colors = intArrayOf(Color.argb(0xAA, 255, 40, 60), Color.TRANSPARENT)
            }
        }
        faceBox.addView(glow, FrameLayout.LayoutParams(dp(190), dp(190), Gravity.CENTER))
        val emoji = TextView(ctx).apply {
            text = face
            textSize = 96f
            setTextColor(Color.BLACK)   // برای کدر بودن ایموجی
            gravity = Gravity.CENTER
        }
        faceBox.addView(emoji, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER))
        col.addView(faceBox, LinearLayout.LayoutParams(dp(190), dp(190)))

        // حباب پیام
        val bubble = TextView(ctx).apply {
            text = msg
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(dp(20), dp(12), dp(20), dp(12))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#E5193A"), Color.parseColor("#FF5A3C"))
            ).apply { cornerRadius = 24 * d }
            elevation = 10 * d
        }
        col.addView(bubble, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            .apply { topMargin = dp(4) })

        layer.addView(col, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER))
        root.addView(layer, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        // ورود
        layer.animate().alpha(1f).setDuration(150).start()
        emoji.scaleX = 0.2f; emoji.scaleY = 0.2f
        emoji.animate().scaleX(1f).scaleY(1f).setDuration(380).setInterpolator(OvershootInterpolator(3f)).start()
        bubble.alpha = 0f
        bubble.translationY = 30 * d
        bubble.animate().alpha(1f).translationY(0f).setStartDelay(200).setDuration(300)
            .setInterpolator(DecelerateInterpolator()).start()

        // تکان دادن سر با عصبانیت (شدیدتر با هر اشتباه)
        val strength = 14f + level * 4f
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1100
            startDelay = 250
            addUpdateListener {
                val f = it.animatedFraction
                val decay = 1f - f * 0.6f
                emoji.rotation = (Math.sin(f * Math.PI * 8) * strength * decay).toFloat()
                emoji.translationX = (Math.sin(f * Math.PI * 8) * 10 * d * decay).toFloat()
            }
            start()
        }

        // تپش هاله
        val pulse = ValueAnimator.ofFloat(0.8f, 1.15f).apply {
            duration = 350
            repeatCount = 5
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val s = it.animatedValue as Float
                glow.scaleX = s; glow.scaleY = s
            }
            start()
        }

        // خروج
        layer.postDelayed({
            layer.animate().alpha(0f).setDuration(300).withEndAction {
                pulse.cancel()
                root.removeView(layer)
            }.start()
        }, 2100)
    }
}