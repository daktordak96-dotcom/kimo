package com.kimo.mayapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayout

/**
 * تب‌های کپسولی: به جای خط باریک، یک کپسول رنگی پشت تب انتخاب‌شده
 * که با رفتن به تب دیگر با انیمیشن جابه‌جا می‌شود.
 */
object PillTabs {

    private const val TAG_DONE = "pill_tabs_done"

    fun style(
        tl: TabLayout,
        accent: Int,
        barBg: Int,
        idleText: Int,
        selectedText: Int = Color.parseColor("#0A1408"),
        stroke: Int = Color.argb(0x40, 255, 255, 255)
    ) {
        if (tl.getTag(R.id.toolbar) == TAG_DONE) return
        tl.setTag(R.id.toolbar, TAG_DONE)

        val d = tl.resources.displayMetrics.density
        fun dp(v: Int) = (v * d).toInt()

        // کپسول انتخاب
        val pill = GradientDrawable().apply {
            cornerRadius = dp(100).toFloat()
            setColor(accent)
        }
        val inset = InsetDrawable(pill, dp(3), dp(5), dp(3), dp(5))
        tl.setSelectedTabIndicator(inset)
        tl.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_STRETCH)
        tl.isTabIndicatorFullWidth = true
        tl.setSelectedTabIndicatorColor(accent)

        // رنگ نوشته‌ها
        tl.setTabTextColors(idleText, selectedText)
        tl.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)

        // نوار گرد تیره دور تب‌ها
        tl.background = GradientDrawable().apply {
            cornerRadius = dp(100).toFloat()
            setColor(barBg)
            setStroke(dp(1), stroke)
        }
        tl.setPadding(dp(4), 0, dp(4), 0)
        tl.elevation = 0f
        (tl.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            it.setMargins(dp(10), dp(4), dp(10), dp(8))
            tl.layoutParams = it
        }

        // پررنگ کردن نوشتهٔ تب انتخاب‌شده
        tl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) = bold(tab, true)
            override fun onTabUnselected(tab: TabLayout.Tab) = bold(tab, false)
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tl.post {
            for (i in 0 until tl.tabCount) {
                tl.getTabAt(i)?.let { bold(it, it.isSelected) }
            }
        }
    }

    private fun bold(tab: TabLayout.Tab, on: Boolean) {
        val view = tab.view
        for (i in 0 until view.childCount) {
            val c = view.getChildAt(i)
            if (c is TextView) c.setTypeface(null, if (on) Typeface.BOLD else Typeface.NORMAL)
        }
    }
}