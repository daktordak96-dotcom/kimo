package com.kimo.mayapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class AfghanInternetFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val items = AfghanInternetData.forTab(arguments?.getInt("tab") ?: 0)

        // تب خالی
        if (items.isEmpty()) {
            return FrameLayout(ctx).apply {
                setBackgroundColor(Color.parseColor("#F1F1F1"))
                addView(TextView(ctx).apply {
                    text = AfghanInternetData.soon.get(ctx)
                    textSize = 18f
                    setTextColor(Color.parseColor("#6B7280"))
                    gravity = Gravity.CENTER
                }, FrameLayout.LayoutParams(MATCH, MATCH))
            }
        }

        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = if (Strings.currentLang(ctx) == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
            setPadding(dp(12), dp(8), dp(12), dp(16))
        }
        items.forEach { list.addView(card(it)) }
        return ScrollView(ctx).apply {
            setBackgroundColor(Color.parseColor("#F1F1F1"))
            addView(list)
        }
    }

    private fun card(p: AfghanPackage): CardView {
        val ctx = requireContext()
        val box = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL }

        // عنوان
        box.addView(label(p.title.get(ctx), 20f, "#F97316").apply {
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(16), dp(16), dp(16), dp(16))
        })
        // قیمت
        box.addView(label(p.price.get(ctx), 19f, "#DBEAFE").apply {
            setPadding(dp(14), dp(6), dp(14), dp(6))
            layoutParams = LinearLayout.LayoutParams(WRAP, WRAP).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = dp(14)
            }
        })
        // توضیحات
        box.addView(label(p.description(ctx), 16f, "#FFFFFF").apply {
            setLineSpacing(dp(4).toFloat(), 1f)
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(dp(16), dp(16), dp(16), dp(16)) }
        })
        // دکمه‌ها (غیرفعال [اگر دارد]، چک بیلانس، فعال)
        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        p.off?.let { off ->
            row.addView(button(AfghanInternetData.btnOff.get(ctx), "#E91E63") { dial(off) })
        }
        row.addView(button(AfghanInternetData.btnCheck.get(ctx), "#009688") { dial(AfghanInternetData.CHECK_CODE) })
        row.addView(button(AfghanInternetData.btnOn.get(ctx), "#4CAF50") { dial(p.on) })
        box.addView(row)

        return CardView(ctx).apply {
            radius = dp(14).toFloat()
            cardElevation = dp(3).toFloat()
            layoutParams = LinearLayout.LayoutParams(MATCH, WRAP).apply { setMargins(0, dp(8), 0, dp(8)) }
            addView(box)
        }
    }

    private fun label(t: String, size: Float, bg: String) = TextView(requireContext()).apply {
        text = t
        textSize = size
        setTextColor(Color.parseColor("#1F2937"))
        setBackgroundColor(Color.parseColor(bg))
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    // دکمه‌ی کپسولی
    private fun button(t: String, color: String, click: () -> Unit) = Button(requireContext()).apply {
        text = t
        textSize = 13f
        isAllCaps = false
        setTextColor(Color.WHITE)
        background = GradientDrawable().apply { setColor(Color.parseColor(color)); cornerRadius = 200f }
        layoutParams = LinearLayout.LayoutParams(0, dp(52), 1f).apply { setMargins(dp(4), 0, dp(4), 0) }
        setOnClickListener { click() }
    }

    // باز کردن شماره‌گیر با کد USSD (کاربر خودش دکمه تماس را می‌زند)
    private fun dial(code: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(code))))
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    companion object {
        fun newInstance(tab: Int) = AfghanInternetFragment().apply {
            arguments = Bundle().apply { putInt("tab", tab) }
        }
    }
}