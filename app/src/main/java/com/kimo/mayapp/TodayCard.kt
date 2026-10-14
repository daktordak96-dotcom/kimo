package com.kimo.mayapp

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/**
 * کارت «امروز» در صفحهٔ اول: تاریخ شمسی، قمری و میلادی کنار هم
 */
object TodayCard {

    const val TAG = "today_card"

    fun build(ctx: Context): View {
        val d0 = ctx.resources.displayMetrics.density
        fun dp(v: Int) = (v * d0).toInt()

        val lang = Strings.currentLang(ctx)
        val dark = ThemeMode.isDark(ctx)
        val day = AfghanCalendar.today(ctx)

        val textCol = if (dark) Color.parseColor("#F2F6FF") else Color.parseColor("#14213D")
        val subCol = if (dark) Color.parseColor("#9DB0D6") else Color.parseColor("#5B6B8C")
        val goldCol = if (dark) Color.parseColor("#FFD54F") else Color.parseColor("#C98A00")
        val chipBg = if (dark) Color.argb(0x22, 255, 255, 255) else Color.parseColor("#F1F4FA")

        val card = LinearLayout(ctx).apply {
            tag = TAG
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            layoutDirection = if (lang == "en") View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                if (dark) intArrayOf(Color.parseColor("#1B2A4E"), Color.parseColor("#121A30"))
                else intArrayOf(Color.WHITE, Color.parseColor("#F5F8FF"))
            ).apply {
                cornerRadius = dp(22).toFloat()
                setStroke(dp(1), if (dark) Color.argb(0x40, 120, 170, 255) else Color.parseColor("#DDE5F3"))
            }
            elevation = dp(4).toFloat()
            if (Build.VERSION.SDK_INT >= 28) {
                outlineSpotShadowColor = Color.parseColor("#2F8CFF")
                outlineAmbientShadowColor = Color.parseColor("#2F8CFF")
            }
            isClickable = true
            isFocusable = true
        }

        // ---------- ردیف بالا: عدد روز + روز هفته + تاریخ شمسی ----------
        val top = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val dayBadge = TextView(ctx).apply {
            text = AfghanCalendar.num(day.jd.toString(), lang)
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.parseColor("#2F8CFF"), Color.parseColor("#7B3FD6"))
            ).apply { cornerRadius = dp(18).toFloat() }
            elevation = dp(3).toFloat()
        }
        top.addView(dayBadge, LinearLayout.LayoutParams(dp(58), dp(58)))

        val col = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), 0, dp(14), 0)
        }
        col.addView(TextView(ctx).apply {
            text = when (lang) { "en" -> "Today"; "ps" -> "نن ورځ"; else -> "امروز" } +
                    "  •  " + AfghanCalendar.weekName(lang, day)
            textSize = 14f
            setTextColor(subCol)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        })
        col.addView(TextView(ctx).apply {
            text = AfghanCalendar.shamsiText(lang, day)
            textSize = 21f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(textCol)
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        })
        top.addView(col, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        top.addView(TextView(ctx).apply {
            text = when (lang) { "en" -> "Solar"; "ps" -> "لمریز"; else -> "شمسی" }
            textSize = 11f
            setTextColor(goldCol)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(dp(10), dp(4), dp(10), dp(4))
            background = GradientDrawable().apply {
                cornerRadius = dp(20).toFloat()
                setStroke(dp(1), goldCol)
            }
        })
        card.addView(top)

        // ---------- ردیف پایین: قمری و میلادی ----------
        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        row.addView(chip(ctx,
            when (lang) { "en" -> "Lunar Hijri"; "ps" -> "سپوږمیز"; else -> "قمری" },
            AfghanCalendar.hijriText(lang, day),
            Color.parseColor("#1DE9B6"), textCol, subCol, chipBg),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply { marginEnd = dp(6) })
        row.addView(chip(ctx,
            when (lang) { "en" -> "Gregorian"; "ps" -> "میلادي"; else -> "میلادی" },
            AfghanCalendar.gregText(lang, day),
            Color.parseColor("#B46CFF"), textCol, subCol, chipBg),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply { marginStart = dp(6) })
        card.addView(row, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(12) })

        card.setOnClickListener {
            Toast.makeText(ctx,
                when (lang) { "en" -> "Full calendar coming soon"; "ps" -> "بشپړ جنتري ژر راځي"; else -> "تقویم کامل به‌زودی" },
                Toast.LENGTH_SHORT).show()
        }
        return card
    }

    private fun chip(ctx: Context, label: String, value: String, accent: Int,
                     textCol: Int, subCol: Int, bg: Int): View {
        val d0 = ctx.resources.displayMetrics.density
        fun dp(v: Int) = (v * d0).toInt()
        val box = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(8), dp(12), dp(8))
            background = GradientDrawable().apply {
                cornerRadius = dp(14).toFloat()
                setColor(bg)
            }
        }
        val head = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        head.addView(View(ctx).apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(accent)
            }
        }, LinearLayout.LayoutParams(dp(7), dp(7)))
        head.addView(TextView(ctx).apply {
            text = label
            textSize = 11f
            setTextColor(subCol)
            setPadding(dp(6), 0, dp(6), 0)
        })
        box.addView(head)
        box.addView(TextView(ctx).apply {
            text = value
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(textCol)
            maxLines = 1
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = dp(3)
        })
        return box
    }
}