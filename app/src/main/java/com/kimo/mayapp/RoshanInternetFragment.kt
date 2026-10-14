package com.kimo.mayapp

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class RoshanInternetFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌های قرمز نئونی (برند روشن)
    private val RED_DARK = "#B30012"
    private val RED_MAIN = "#FF1F3D"
    private val RED_LIGHT = "#FF5A5F"
    private val GLOW_COLOR = "#FF2A3F"

    // رنگ‌های طلایی
    private val GOLD = "#FFD54F"
    private val GOLD_DEEP = "#FFB300"

    private val NEON_CYAN = "#00E5FF"
    private val NEON_PINK = "#FF2D95"

    // رنگ‌های موج دکمه (رنگ اول و آخر یکی است تا حرکت بدون پرش باشد)
    private val waveColors = intArrayOf(
        Color.parseColor("#FF1F3D"),
        Color.parseColor("#FF5A2D"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#FFC107"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#FF3D3D"),
        Color.parseColor("#FF1F3D")
    )

    private val animators = mutableListOf<ValueAnimator>()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val tab = arguments?.getInt("tab") ?: 0
        val kind = arguments?.getString("kind") ?: "internet"
        val items = when (kind) {
            "sms" -> RoshanSmsData.forTab(tab)
            "call" -> RoshanCallData.forTab(tab)
            else -> RoshanInternetData.forTab(tab)
        }

        val pageBg = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor("#1A0509"),
                Color.parseColor("#2A0812"),
                Color.parseColor("#1F0610")
            )
        )

        if (items.isEmpty()) {
            return FrameLayout(ctx).apply {
                background = pageBg
                addView(TextView(ctx).apply {
                    text = RoshanInternetData.soon.get(ctx)
                    textSize = 18f
                    setTextColor(Color.parseColor("#FFB3BA"))
                    gravity = Gravity.CENTER
                }, FrameLayout.LayoutParams(MATCH, MATCH))
            }
        }

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = if (Strings.currentLang(ctx) == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
            setPadding(dp(4), dp(10), dp(4), dp(20))
            clipChildren = false
            clipToPadding = false
        }
        items.forEach { pkg -> list.addView(card(pkg)) }
        return ScrollView(ctx).apply {
            background = pageBg
            clipChildren = false
            clipToPadding = false
            addView(list)
        }
    }

    // هالهٔ نور چندلایه دور تا دور کارت
    private fun glowDrawable(color: String, cardRadius: Float, spread: Int): LayerDrawable {
        val base = Color.parseColor(color)
        val steps = 8
        val layers = Array(steps) { idx ->
            val remaining = steps - idx
            GradientDrawable().apply {
                setColor(Color.argb(16 + idx * 4, Color.red(base), Color.green(base), Color.blue(base)))
                cornerRadius = cardRadius + remaining * (spread / steps.toFloat())
            }
        }
        val ld = LayerDrawable(layers)
        for (idx in 0 until steps) {
            val inset = ((idx) * (spread / steps.toFloat())).toInt()
            ld.setLayerInset(idx, inset, inset, inset, inset)
        }
        return ld
    }

    private fun card(p: AfghanPackage): View {
        val ctx = requireContext()
        val box = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }

        box.addView(label(p.title.get(ctx), 20f, "#FFFFFF").apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(RED_DARK), Color.parseColor(RED_MAIN), Color.parseColor(RED_LIGHT))
            )
        })
        box.addView(label(p.price.get(ctx), 19f, GOLD).apply {
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(GOLD))
            setPadding(dp(18), dp(6), dp(18), dp(6))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#2A0610"))
                cornerRadius = dp(20).toFloat()
                setStroke(dp(2), Color.parseColor(GOLD_DEEP))
            }
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dp(14)
            }
        })
        box.addView(label(p.intro.get(ctx), 16f, "#FFE3E6").apply {
            setLineSpacing(dp(4).toFloat(), 1f)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(18), dp(14), dp(18), dp(14)) }
        })

        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(12), dp(4), dp(12), dp(14))
            clipChildren = false
            clipToPadding = false
        }
        val offCode = p.off
        if (!offCode.isNullOrEmpty()) {
            row.addView(neonButton(RoshanInternetData.btnOff.get(ctx), NEON_PINK) {
                sendSms(RoshanInternetData.SMS_NUMBER, offCode)
            })
        }
        if (RoshanInternetData.CHECK_CODE.isNotEmpty()) {
            row.addView(neonButton(RoshanInternetData.btnCheck.get(ctx), NEON_CYAN) {
                dial(RoshanInternetData.CHECK_CODE)
            })
        }
        // فعال‌سازی: بسته‌های «tel:» با زنگ، بقیه با پیامک به 555
        row.addView(waveButton(RoshanInternetData.btnOn.get(ctx)) {
            if (p.on.startsWith(RoshanInternetData.DIAL_PREFIX)) {
                dial(p.on.removePrefix(RoshanInternetData.DIAL_PREFIX))
            } else {
                sendSms(RoshanInternetData.SMS_NUMBER, p.on)
            }
        })
        box.addView(row)

        val radius = dp(20).toFloat()
        val cardBg = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#5A0A18"),
                Color.parseColor("#3A0812"),
                Color.parseColor("#2A0610")
            )
        ).apply {
            cornerRadius = radius
            setStroke(dp(2), Color.parseColor(RED_LIGHT))
        }

        val inner = FrameLayout(ctx).apply {
            background = cardBg
            setPadding(dp(2), dp(2), dp(2), dp(2))
            clipToOutline = true
            outlineProvider = android.view.ViewOutlineProvider.BACKGROUND
            elevation = dp(12).toFloat()
            addView(box)
        }

        val spread = dp(16)
        return FrameLayout(ctx).apply {
            clipChildren = false
            clipToPadding = false
            background = glowDrawable(GLOW_COLOR, radius, spread)
            setPadding(spread, spread, spread, spread)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(dp(2), dp(4), dp(2), dp(4))
            }
            addView(inner, FrameLayout.LayoutParams(MATCH, WRAP))
            pressEffect(this)
        }
    }

    private fun neonButton(t: String, color: String, click: () -> Unit) = Button(requireContext()).apply {
        text = t
        textSize = 14f
        isAllCaps = false
        typeface = Typeface.DEFAULT_BOLD
        val neon = Color.parseColor(color)
        setTextColor(neon)
        background = GradientDrawable().apply {
            setColor(Color.parseColor("#1A0610"))
            cornerRadius = 200f
            setStroke(dp(2), neon)
        }
        stateListAnimator = null
        elevation = dp(6).toFloat()
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            outlineAmbientShadowColor = neon
            outlineSpotShadowColor = neon
        }
        layoutParams = LinearLayout.LayoutParams(0, dp(52), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
        setOnClickListener { click() }
    }

    private fun waveButton(t: String, click: () -> Unit): Button {
        val btn = Button(requireContext()).apply {
            text = t
            textSize = 15f
            isAllCaps = false
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            stateListAnimator = null
            elevation = dp(6).toFloat()
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                outlineAmbientShadowColor = Color.parseColor(GOLD_DEEP)
                outlineSpotShadowColor = Color.parseColor(GOLD_DEEP)
            }
            layoutParams = LinearLayout.LayoutParams(0, dp(52), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
            setOnClickListener { click() }
        }
        val wave = WaveDrawable(waveColors)
        btn.background = wave

        val anim = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 14000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                wave.offset = it.animatedValue as Float
                wave.invalidateSelf()
            }
        }
        animators.add(anim)
        anim.start()
        return btn
    }

    private fun pressEffect(v: View) {
        v.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> view.animate().scaleX(0.985f).scaleY(0.985f).setDuration(90).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    view.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
            }
            false
        }
    }

    override fun onDestroyView() {
        animators.forEach { it.cancel() }
        animators.clear()
        super.onDestroyView()
    }

    private fun label(t: String, size: Float, textColor: String) =
        TextView(requireContext()).apply {
            text = t
            textSize = size
            setTextColor(Color.parseColor(textColor))
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }

    private fun sendSms(number: String, body: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:" + Uri.encode(number) + "?body=" + Uri.encode(body))
                putExtra("sms_body", body)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "SMS error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dial(code: String) {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Dialer error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance(tab: Int, kind: String = "internet") = RoshanInternetFragment().apply {
            arguments = Bundle().apply {
                putInt("tab", tab)
                putString("kind", kind)
            }
        }
    }
}