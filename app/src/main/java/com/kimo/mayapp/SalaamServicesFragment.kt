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

class SalaamServicesFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val TEAL_DARK = "#00695C"
    private val TEAL_MAIN = "#1DE9B6"
    private val TEAL_LIGHT = "#64FFDA"
    private val GLOW_COLOR = "#1DE9B6"
    private val GOLD = "#FFD54F"
    private val GOLD_DEEP = "#FFB300"

    private val waveColors = intArrayOf(
        Color.parseColor("#00897B"),
        Color.parseColor("#1DE9B6"),
        Color.parseColor("#64FFDA"),
        Color.parseColor("#26C6DA"),
        Color.parseColor("#64FFDA"),
        Color.parseColor("#1DE9B6"),
        Color.parseColor("#00897B")
    )

    private val animators = mutableListOf<ValueAnimator>()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val kind = arguments?.getString("kind") ?: "services"
        val items = if (kind == "intl") SalaamIntlData.items else SalaamServicesData.items

        val pageBg = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor("#04120F"),
                Color.parseColor("#06201A"),
                Color.parseColor("#04120F")
            )
        )

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = if (Strings.currentLang(ctx) == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
            setPadding(dp(4), dp(10), dp(4), dp(20))
            clipChildren = false
            clipToPadding = false
        }
        items.forEach { list.addView(card(it)) }
        return ScrollView(ctx).apply {
            background = pageBg
            clipChildren = false
            clipToPadding = false
            addView(list)
        }
    }

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

    private fun card(sv: SalaamService): View {
        val ctx = requireContext()
        val box = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }

        box.addView(label(sv.title.get(ctx), 20f, "#FFFFFF").apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor(TEAL_DARK), Color.parseColor(TEAL_MAIN), Color.parseColor(TEAL_LIGHT))
            )
        })

        sv.code?.let { code ->
            box.addView(label(code.get(ctx), 20f, GOLD).apply {
                typeface = Typeface.DEFAULT_BOLD
                textDirection = View.TEXT_DIRECTION_LTR
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setPadding(dp(14), dp(8), dp(14), dp(8))
                background = GradientDrawable().apply {
                    setColor(Color.parseColor("#06201A"))
                    cornerRadius = dp(20).toFloat()
                    setStroke(dp(2), Color.parseColor(GOLD_DEEP))
                }
                layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                    topMargin = dp(14)
                }
            })
        }

        sv.note?.let { note ->
            box.addView(label(note.get(ctx), 16f, "#D6FFF4").apply {
                setLineSpacing(dp(4).toFloat(), 1f)
                layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(18), dp(14), dp(18), dp(4)) }
            })
        }

        sv.extras.forEach { e ->
            box.addView(label(e.get(ctx), 15f, "#D6FFF4").apply {
                layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(18), dp(10), dp(18), dp(0)) }
            })
        }

        sv.rows.forEach { (k, v) ->
            val r = LinearLayout(ctx).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutDirection = View.LAYOUT_DIRECTION_LTR
                setPadding(dp(18), dp(10), dp(18), dp(10))
            }
            r.addView(TextView(ctx).apply {
                text = k
                textSize = 16f
                setTextColor(Color.parseColor("#9FE8DC"))
                layoutParams = LinearLayout.LayoutParams(0, WRAP, 1f)
            })
            r.addView(TextView(ctx).apply {
                text = v
                textSize = 17f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor(GOLD))
                gravity = Gravity.END
            })
            box.addView(r)
        }

        val btnText = sv.button
        if (sv.action != SalaamServicesData.INFO && btnText != null) {
            val row = LinearLayout(ctx).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(dp(12), dp(14), dp(12), dp(14))
                clipChildren = false
                clipToPadding = false
            }
            row.addView(waveButton(btnText.get(ctx)) {
                if (sv.action == SalaamServicesData.DIAL) dial(sv.target) else sendSms(sv.target, sv.smsBody)
            })
            box.addView(row)
        } else {
            box.addView(View(ctx), LinearLayout.LayoutParams(MATCH, dp(16)))
        }

        val radius = dp(20).toFloat()
        val cardBg = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#0A3A30"),
                Color.parseColor("#06281F"),
                Color.parseColor("#04201A")
            )
        ).apply {
            cornerRadius = radius
            setStroke(dp(2), Color.parseColor(TEAL_LIGHT))
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
                outlineAmbientShadowColor = Color.parseColor(TEAL_MAIN)
                outlineSpotShadowColor = Color.parseColor(TEAL_MAIN)
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
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance(kind: String = "services") = SalaamServicesFragment().apply {
            arguments = Bundle().apply { putString("kind", kind) }
        }
    }
}