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
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class InternationalPackagesFragment : Fragment() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        val ctx = requireContext()
        val list = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            layoutDirection = if (Strings.currentLang(ctx) == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
            setPadding(dp(12), dp(8), dp(12), dp(16))
        }
        InternationalPackagesData.all.forEach { list.addView(card(it)) }
        return ScrollView(ctx).apply {
            setBackgroundColor(Color.parseColor("#F1F1F1"))
            addView(list)
        }
    }

    private fun card(p: IntlPackage): CardView {
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
        // دکمه‌ها
        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        row.addView(button(Tr("غیر فعال سازی", "Deactivate", "غیر فعالول").get(ctx), "#E11D48") { sms("3378", p.off) })
        if (p.checkButton) row.addView(button(Tr("چک بیلانس", "Check Balance", "بیلانس کتل").get(ctx), "#0891B2") { sms("606", "505") })
        row.addView(button(Tr("فعال سازی", "Activate", "فعالول").get(ctx), "#16A34A") { sms("3378", p.on) })
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

    private fun sms(number: String, body: String) {
        startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$number")).putExtra("sms_body", body))
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}