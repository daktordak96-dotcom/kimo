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

class SalaamFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌های سلام (ملایم‌تر و کم‌نورتر تا چشم را اذیت نکند)
    private val TEAL_DARK = "#004D43"
    private val TEAL_MAIN = "#00796B"
    private val TEAL_LIGHT = "#26A69A"
    private val GLOW_COLOR = "#00897B"
    private val CARD_STROKE = "#2E7D72"

    private val GOLD = "#FFD54F"
    private val GOLD_DEEP = "#E6A200"

    private val SOFT_CYAN = "#4DB6C4"
    private val SOFT_PINK = "#E0559A"

    // رنگ‌های موج دکمه (تیره‌تر و آرام‌تر)
    private val waveColors = intArrayOf(
        Color.parseColor("#00695C"),
        Color.parseColor("#00897B"),
        Color.parseColor("#26A69A"),
        Color.parseColor("#00838F"),
        Color.parseColor("#26A69A"),
        Color.parseColor("#00897B"),
        Color.parseColor("#00695C")
    )

    private val animators = mutableListOf<ValueAnimator>()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val tab = arguments?.getInt("tab") ?: 0
        val kind = arguments?.getString("kind") ?: "internet"
        val items = when (kind) {
            "sms" -> SalaamSmsData.forTab(tab)
            "call" -> SalaamCallData.forTab(tab)
            else -> SalaamInternetData.forTab(tab)
        }

        val pageBg = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor("#04100D"),
                Color.parseColor("#061915"),
                Color.parseColor("#04100D")
            )
        )

        if (items.isEmpty()) {
            return FrameLayout(ctx).apply {
                background = pageBg
                addView(TextView(ctx).apply {
                    text = SalaamInternetData.soon.get(ctx)
                    textSize = 18f
                    setTextColor(Color.parseColor("#9CCFC5"))
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

    // هالهٔ نور خیلی ملایم دور کارت
    private fun glowDrawable(color: String, cardRadius: Float, spread: Int): LayerDrawable {
        val base = Color.parseColor(color)
        val steps = 6
        val layers = Array(steps) { idx ->
            val remaining = steps - idx
            GradientDrawable().apply {
                setColor(Color.argb(5 + idx * 2, Color.red(base), Color.green(base), Color.blue(base)))
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

        box.addView(label(p.title.get(ctx), 20f, "#F2FFFC").apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(TEAL_DARK), Color.parseColor(TEAL_MAIN), Color.parseColor(TEAL_LIGHT))
            )
        })
        box.addView(label(p.price.get(ctx), 19f, GOLD).apply {
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor(GOLD))
            setPadding(dp(18), dp(6), dp(18), dp(6))
            background = GradientDrawable().apply {
                setColor(Color.parseColor("#061915"))
                cornerRadius = dp(20).toFloat()
                setStroke(dp(2), Color.parseColor(GOLD_DEEP))
            }
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dp(14)
            }
        })
        box.addView(label(p.intro.get(ctx), 16f, "#C4E3DC").apply {
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
            row.addView(neonButton(SalaamInternetData.btnOff.get(ctx), SOFT_PINK) {
                actCode(offCode)
            })
        }
        if (SalaamInternetData.CHECK_CODE.isNotEmpty()) {
            row.addView(neonButton(SalaamInternetData.btnCheck.get(ctx), SOFT_CYAN) {
                dial(SalaamInternetData.CHECK_CODE)
            })
        }
        row.addView(waveButton(SalaamInternetData.btnOn.get(ctx)) {
            actCode(p.on)
        })
        box.addView(row)

        val radius = dp(20).toFloat()
        val cardBg = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#0A2A24"),
                Color.parseColor("#07201B"),
                Color.parseColor("#051915")
            )
        ).apply {
            cornerRadius = radius
            setStroke(dp(1), Color.parseColor(CARD_STROKE))
        }

        val inner = FrameLayout(ctx).apply {
            background = cardBg
            setPadding(dp(1), dp(1), dp(1), dp(1))
            clipToOutline = true
            outlineProvider = android.view.ViewOutlineProvider.BACKGROUND
            elevation = dp(4).toFloat()
            addView(box)
        }

        val spread = dp(10)
        return FrameLayout(ctx).apply {
            clipChildren = false
            clipToPadding = false
            background = glowDrawable(GLOW_COLOR, radius, spread)
            setPadding(spread, spread, spread, spread)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(dp(6), dp(4), dp(6), dp(4))
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
        val c = Color.parseColor(color)
        setTextColor(c)
        background = GradientDrawable().apply {
            setColor(Color.parseColor("#051915"))
            cornerRadius = 200f
            setStroke(dp(1), c)
        }
        stateListAnimator = null
        elevation = dp(2).toFloat()
        layoutParams = LinearLayout.LayoutParams(0, dp(50), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
        setOnClickListener { click() }
    }

    private fun waveButton(t: String, click: () -> Unit): Button {
        val btn = Button(requireContext()).apply {
            text = t
            textSize = 15f
            isAllCaps = false
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#F2FFFC"))
            stateListAnimator = null
            elevation = dp(2).toFloat()
            layoutParams = LinearLayout.LayoutParams(0, dp(50), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
            setOnClickListener { click() }
        }
        val wave = WaveDrawable(waveColors)
        btn.background = wave

        val anim = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 16000
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

    // اگر کد با * شروع شود شماره‌گیری، وگرنه پیامک به 740
    private fun actCode(code: String) {
        if (code.trim().startsWith("*")) {
            dial(code)
        } else {
            sendSms(SalaamCallData.SMS_NUMBER, code)
        }
    }

    private fun dial(code: String) {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Dialer error", Toast.LENGTH_SHORT).show()
        }
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

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance(tab: Int, kind: String = "internet") = SalaamFragment().apply {
            arguments = Bundle().apply {
                putInt("tab", tab)
                putString("kind", kind)
            }
        }
    }
}