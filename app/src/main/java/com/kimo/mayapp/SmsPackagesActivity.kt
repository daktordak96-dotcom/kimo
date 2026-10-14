package com.kimo.mayapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SmsPackagesActivity : AppCompatActivity() {

    // رنگ‌های اتصالات (مثل صفحهٔ بین‌المللی)
    private val PAGE_DARK = Color.parseColor("#04120A")
    private val NAV_DARK = Color.parseColor("#06180D")
    private val GREEN_NEON = Color.parseColor("#9CFF3A")
    private val TAB_TEXT = Color.parseColor("#8FBF96")
    private val GOLD = Color.parseColor("#FFD54F")

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages_container)
        supportActionBar?.hide()

        // نوار وضعیت و پس‌زمینهٔ تیره
        window.statusBarColor = PAGE_DARK
        window.navigationBarColor = NAV_DARK
        if (Build.VERSION.SDK_INT >= 23) {
            window.decorView.systemUiVisibility = 0
        }
        window.decorView.setBackgroundColor(PAGE_DARK)
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)?.setBackgroundColor(PAGE_DARK)

        // ---------------- نوار بالا ----------------
        val title = findViewById<TextView>(R.id.txtHeaderTitle)
        title?.apply {
            text = EtisalatSmsData.screenTitle.get(this@SmsPackagesActivity)
            setTextColor(Color.WHITE)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setBackgroundColor(Color.TRANSPARENT)
        }
        val header = title?.parent as? ViewGroup
        if (header != null) {
            header.background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    Color.parseColor("#0B5D1E"),
                    Color.parseColor("#1E9E2F"),
                    Color.parseColor("#4CD137")
                )
            ).apply {
                cornerRadius = dp(20).toFloat()
                setStroke(dp(1), GOLD)
            }
            header.clipToOutline = true
            header.outlineProvider = ViewOutlineProvider.BACKGROUND
            header.elevation = dp(4).toFloat()
            (header.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                it.setMargins(dp(10), dp(8), dp(10), dp(8))
                header.layoutParams = it
            }
            addBackArrow(header)
        }

        // ---------------- صفحه‌ها ----------------
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val ctx = this

        viewPager.setBackgroundColor(PAGE_DARK)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = EtisalatSmsData.tabs.size
            override fun createFragment(position: Int) = EtisalatSmsFragment.newInstance(position)
        }

        // ---------------- تب‌ها (تیره، مثل بخش بین‌المللی) ----------------
        tabLayout.setBackgroundColor(PAGE_DARK)
        (tabLayout.parent as? View)?.let { p ->
            if (p.id != android.R.id.content) p.setBackgroundColor(PAGE_DARK)
        }
        tabLayout.setTabTextColors(TAB_TEXT, GREEN_NEON)
        tabLayout.setSelectedTabIndicatorColor(GREEN_NEON)
        tabLayout.elevation = 0f
        tabLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#229CFF3A"))

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = EtisalatSmsData.tabs[position].get(ctx)
        }.attach()
    }

    // فلش برگشت در ابتدای نوار بالا (سمت راست در فارسی)
    private fun addBackArrow(header: ViewGroup) {
        val isEn = Strings.currentLang(this) == "en"
        val arrow = TextView(this).apply {
            text = if (isEn) "←" else "→"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(dp(14), 0, dp(14), 0)
            setOnClickListener { finish() }
        }
        when (header) {
            is LinearLayout -> {
                if (header.orientation == LinearLayout.HORIZONTAL) {
                    header.addView(arrow, 0, LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT))
                }
            }
            is FrameLayout -> {
                header.addView(arrow, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.START or Gravity.CENTER_VERTICAL))
            }
        }
    }
}