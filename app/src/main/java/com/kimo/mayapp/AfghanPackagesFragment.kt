package com.kimo.mayapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AfghanPackagesFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    // رنگ‌های افغان بیسیم
    private val NEON = "#29B6FF"
    private val DEEP = "#0D47A1"
    private val MID = "#1E88E5"
    private val PAGE = "#0A1020"
    private val CARD = "#0F1626"
    private val GOLD = "#FFD54F"

    // بررسی نوع محتوا: sms، call یا internet
    private fun items(): List<AfghanPackage> {
        val tab = arguments?.getInt("tab") ?: 0
        return when (arguments?.getString("kind")) {
            "sms" -> AfghanSmsData.forTab(tab)
            "call" -> AfghanCallData.forTab(tab)
            else -> AfghanInternetData.forTab(tab)
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val items = items()

        if (items.isEmpty()) {
            return FrameLayout(ctx).apply {
                setBackgroundColor(c(PAGE))
                addView(TextView(ctx).apply {
                    text = AfghanInternetData.soon.get(ctx)
                    textSize = 18f
                    setTextColor(c("#9FB0D0"))
                    gravity = Gravity.CENTER
                }, FrameLayout.LayoutParams(MATCH, MATCH))
            }
        }

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = if (Strings.currentLang(ctx) == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
            setPadding(dp(12), dp(10), dp(12), dp(20))
            clipChildren = false
            clipToPadding = false
        }
        items.forEach { list.addView(card(it)) }
        return ScrollView(ctx).apply {
            setBackgroundColor(c(PAGE))
            clipToPadding = false
            addView(list)
        }
    }

    private fun card(p: AfghanPackage): View {
        val ctx = requireContext()
        val radius = dp(20).toFloat()

        val box = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(alpha(NEON, 0x22), c(CARD), c("#0B111F"))
            ).apply {
                cornerRadius = radius
                setStroke(dp(1), alpha(NEON, 0x88))
            }
            clipToOutline = true
            outlineProvider = ViewOutlineProvider.BACKGROUND
            elevation = dp(8).toFloat()
            glow(this)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(8), 0, dp(8)) }
        }

        // عنوان با گرادیان آبی
        box.addView(TextView(ctx).apply {
            text = p.title.get(ctx)
            textSize = 19f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.WHITE)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            setPadding(dp(18), dp(15), dp(18), dp(15))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(c(DEEP), c(MID), c(NEON))
            )
        }, LinearLayout.LayoutParams(MATCH, WRAP))

        // قیمت طلایی
        box.addView(TextView(ctx).apply {
            text = p.price.get(ctx)
            textSize = 19f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(c(GOLD))
            setPadding(dp(20), dp(6), dp(20), dp(6))
            background = GradientDrawable().apply {
                setColor(c("#14203A"))
                cornerRadius = dp(20).toFloat()
                setStroke(dp(2), c(GOLD))
            }
        }, LinearLayout.LayoutParams(WRAP, WRAP).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = dp(16)
        })

        // توضیحات
        box.addView(TextView(ctx).apply {
            text = p.description(ctx)
            textSize = 16f
            setTextColor(c("#E3F2FD"))
            setLineSpacing(dp(4).toFloat(), 1f)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }, LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(18), dp(14), dp(18), dp(10)) })

        // دکمه‌ها
        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(12), dp(6), dp(12), dp(16))
        }
        val off = p.off
        if (!off.isNullOrBlank()) {
            row.addView(button(AfghanInternetData.btnOff.get(ctx), "#E91E63") { dial(off) })
        }
        row.addView(button(AfghanInternetData.btnCheck.get(ctx), "#00ACC1") {
            dial(p.check ?: AfghanInternetData.CHECK_CODE)
        })
        row.addView(button(AfghanInternetData.btnOn.get(ctx), "#2EAD5B") { dial(p.on) })
        box.addView(row)

        pressEffect(box)
        return box
    }

    private fun button(t: String, color: String, click: () -> Unit) = Button(requireContext()).apply {
        text = t
        textSize = 13f
        isAllCaps = false
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(Color.WHITE)
        stateListAnimator = null
        background = GradientDrawable().apply { setColor(c(color)); cornerRadius = 200f }
        elevation = dp(4).toFloat()
        if (Build.VERSION.SDK_INT >= 28) {
            outlineAmbientShadowColor = c(color)
            outlineSpotShadowColor = c(color)
        }
        layoutParams = LinearLayout.LayoutParams(0, dp(50), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
        setOnClickListener { click() }
    }

    private fun pressEffect(v: View) {
        v.setOnTouchListener { view, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> view.animate().scaleX(0.985f).scaleY(0.985f).setDuration(90).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    view.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
            }
            false
        }
    }

    private fun glow(v: View) {
        if (Build.VERSION.SDK_INT >= 28) {
            v.outlineAmbientShadowColor = c(NEON)
            v.outlineSpotShadowColor = c(NEON)
        }
    }

    private fun dial(code: String) {
        if (code.isBlank() || code == "TODO") {
            Toast.makeText(requireContext(), "کد این بسته هنوز ثبت نشده است", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
    }

    private fun c(h: String) = Color.parseColor(h)
    private fun alpha(h: String, a: Int): Int {
        val x = c(h); return Color.argb(a, Color.red(x), Color.green(x), Color.blue(x))
    }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance(kind: String, tab: Int) = AfghanPackagesFragment().apply {
            arguments = Bundle().apply {
                putString("kind", kind)
                putInt("tab", tab)
            }
        }

        // سازگاری با متد قدیمی‌تر
        fun newInstance(tab: Int) = AfghanPackagesFragment().apply {
            arguments = Bundle().apply {
                putString("kind", "call")
                putInt("tab", tab)
            }
        }
    }
}