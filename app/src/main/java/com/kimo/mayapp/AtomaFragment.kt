package com.kimo.mayapp

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AtomaFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // ---------- رنگ‌های زرد طلایی اتوما (ملایم) ----------
    private val pageGrad = intArrayOf(
        Color.parseColor("#0E0B04"),
        Color.parseColor("#16120A"),
        Color.parseColor("#0E0B04")
    )
    private val barGrad = intArrayOf(
        Color.parseColor("#7A5E00"),
        Color.parseColor("#A07A00"),
        Color.parseColor("#C49A12")
    )
    private val cardGrad = intArrayOf(
        Color.parseColor("#2A220E"),
        Color.parseColor("#1E180A"),
        Color.parseColor("#161208")
    )
    private val titleText = Color.parseColor("#FFF8E1")
    private val accent = Color.parseColor("#FFD54F")
    private val accentDeep = Color.parseColor("#D9A400")
    private val pill = Color.parseColor("#1A1508")
    private val introText = Color.parseColor("#E8DDBF")
    private val stroke = Color.parseColor("#8A7020")
    private val glowColor = Color.parseColor("#C9A227")
    private val tabText = Color.parseColor("#B8A77A")
    private val neonOff = Color.parseColor("#E0559A")
    private val neonCheck = Color.parseColor("#4DB6C4")
    private val btnBg = Color.parseColor("#15110A")
    private val waveText = Color.parseColor("#1A1306")
    private val wave = intArrayOf(
        Color.parseColor("#B38900"),
        Color.parseColor("#D4A514"),
        Color.parseColor("#E8B923"),
        Color.parseColor("#C99A10"),
        Color.parseColor("#E8B923"),
        Color.parseColor("#D4A514"),
        Color.parseColor("#B38900")
    )

    private val animators = mutableListOf<ValueAnimator>()
    private val glows = mutableListOf<Drawable>()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val kind = arguments?.getString("kind") ?: "internet"
        val tab = arguments?.getInt("tab") ?: 0
        val items = AtomaData.forTab(kind, tab)
        val pageBg = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, pageGrad)

        if (items.isEmpty()) {
            return FrameLayout(ctx).apply {
                background = pageBg
                addView(TextView(ctx).apply {
                    text = AtomaData.soon.get(ctx)
                    textSize = 18f
                    setTextColor(tabText)
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
        items.forEachIndexed { index, p -> list.addView(card(p, index)) }
        startPulse()

        return ScrollView(ctx).apply {
            background = pageBg
            clipChildren = false
            clipToPadding = false
            addView(list)
        }
    }

    // هالهٔ نور ملایم
    private fun glowDrawable(cardRadius: Float, spread: Int): LayerDrawable {
        val steps = 6
        val layers = Array(steps) { idx ->
            val remaining = steps - idx
            GradientDrawable().apply {
                setColor(Color.argb(5 + idx * 2, Color.red(glowColor), Color.green(glowColor), Color.blue(glowColor)))
                cornerRadius = cardRadius + remaining * (spread / steps.toFloat())
            }
        }
        val ld = LayerDrawable(layers)
        for (idx in 0 until steps) {
            val inset = (idx * (spread / steps.toFloat())).toInt()
            ld.setLayerInset(idx, inset, inset, inset, inset)
        }
        return ld
    }

    private fun card(p: AtomaPackage, index: Int): View {
        val ctx = requireContext()
        val box = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }

        // عنوان
        box.addView(label(p.title.get(ctx), 20f, titleText).apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, barGrad)
        })
        // قیمت
        box.addView(label(p.price.get(ctx), 19f, accent).apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(18), dp(6), dp(18), dp(6))
            background = GradientDrawable().apply {
                setColor(pill)
                cornerRadius = dp(20).toFloat()
                setStroke(dp(2), accentDeep)
            }
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dp(14)
            }
        })
        // توضیحات (تمام عرض کارت، بدون لوگو)
        box.addView(label(p.description(ctx), 16f, introText).apply {
            setLineSpacing(dp(4).toFloat(), 1f)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply {
                setMargins(dp(18), dp(14), dp(18), dp(14))
            }
        })

        // دکمه‌ها: غیرفعال‌سازی، چک بیلانس، فعال‌سازی
        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(12), dp(4), dp(12), dp(14))
            clipChildren = false
            clipToPadding = false
        }
        row.addView(neonButton(AtomaData.btnOff.get(ctx), neonOff) {
            if (p.offBySms) sendSms(AtomaData.OFF_SMS_NUMBER, p.off) else dial(p.off)
        })
        row.addView(neonButton(AtomaData.btnCheck.get(ctx), neonCheck) {
            sendSms(AtomaData.CHECK_NUMBER, AtomaData.CHECK_BODY)
        })
        row.addView(waveButton(AtomaData.btnOn.get(ctx)) { dial(p.on) })
        box.addView(row)

        // بدنه
        val radius = dp(20).toFloat()
        val inner = FrameLayout(ctx).apply {
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR, cardGrad).apply {
                cornerRadius = radius
                setStroke(dp(1), stroke)
            }
            setPadding(dp(1), dp(1), dp(1), dp(1))
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
            elevation = dp(4).toFloat()
            addView(box)
        }

        // لایهٔ بیرونی با هالهٔ نور + انیمیشن ورود
        val spread = dp(10)
        val glow = glowDrawable(radius, spread)
        glows.add(glow)
        return FrameLayout(ctx).apply {
            clipChildren = false
            clipToPadding = false
            background = glow
            setPadding(spread, spread, spread, spread)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(6), dp(4), dp(6), dp(4)) }
            addView(inner, FrameLayout.LayoutParams(MATCH, WRAP))
            pressEffect(this)

            alpha = 0f
            translationY = dp(30).toFloat()
            post {
                animate().alpha(1f).translationY(0f)
                    .setStartDelay((index * 80L).coerceAtMost(500L))
                    .setDuration(450).start()
            }
        }
    }

    // نفس‌کشیدن آرام هاله
    private fun startPulse() {
        val a = ValueAnimator.ofFloat(0.6f, 1f).apply {
            duration = 2600
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val al = ((it.animatedValue as Float) * 255).toInt()
                glows.forEach { d -> d.alpha = al }
            }
        }
        animators.add(a)
        a.start()
    }

    private fun neonButton(txt: String, color: Int, click: () -> Unit) = Button(requireContext()).apply {
        text = txt
        textSize = 14f
        isAllCaps = false
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(color)
        background = GradientDrawable().apply {
            setColor(btnBg)
            cornerRadius = 200f
            setStroke(dp(1), color)
        }
        stateListAnimator = null
        elevation = dp(2).toFloat()
        layoutParams = LinearLayout.LayoutParams(0, dp(50), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
        setOnClickListener { click() }
    }

    private fun waveButton(txt: String, click: () -> Unit): Button {
        val btn = Button(requireContext()).apply {
            text = txt
            textSize = 15f
            isAllCaps = false
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(waveText)
            stateListAnimator = null
            elevation = dp(2).toFloat()
            layoutParams = LinearLayout.LayoutParams(0, dp(50), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
            setOnClickListener { click() }
        }
        val wd = WaveDrawable(wave)
        btn.background = wd
        val anim = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 16000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                wd.offset = it.animatedValue as Float
                wd.invalidateSelf()
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
        glows.clear()
        super.onDestroyView()
    }

    private fun label(txt: String, size: Float, color: Int) = TextView(requireContext()).apply {
        text = txt
        textSize = size
        setTextColor(color)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
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
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$number")).putExtra("sms_body", body))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "SMS error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance(kind: String, tab: Int) = AtomaFragment().apply {
            arguments = Bundle().apply {
                putString("kind", kind)
                putInt("tab", tab)
            }
        }
    }
}