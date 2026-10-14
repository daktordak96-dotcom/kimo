package com.kimo.mayapp

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale

class AfghanSmsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val p = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val l = p?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        val locale = Locale(l)
        Locale.setDefault(locale)
        val config = Configuration(newBase!!.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afghan_internet)

        // رنگ‌های افغان بیسیم
        val neon = Color.parseColor("#29B6FF")
        val page = Color.parseColor("#0A1020")
        val tabText = Color.parseColor("#7F9CC0")

        window.statusBarColor = page
        window.navigationBarColor = page
        window.decorView.setBackgroundColor(page)
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)?.setBackgroundColor(page)

        // نوار بالا
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = AfghanSmsData.screenTitle.get(this)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor("#0D47A1"), Color.parseColor("#1E88E5"), neon)
        ).apply {
            cornerRadius = dp(20).toFloat()
            setStroke(dp(1), Color.parseColor("#80D8FF"))
        }
        (toolbar.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            it.setMargins(dp(10), dp(8), dp(10), dp(8))
            toolbar.layoutParams = it
        }
        toolbar.clipToOutline = true
        toolbar.outlineProvider = ViewOutlineProvider.BACKGROUND
        toolbar.elevation = dp(8).toFloat()
        if (Build.VERSION.SDK_INT >= 28) {
            toolbar.outlineAmbientShadowColor = neon
            toolbar.outlineSpotShadowColor = neon
        }
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.navigationIcon?.setTint(Color.WHITE)
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is TextView) {
                child.textSize = 19f
                child.typeface = Typeface.DEFAULT_BOLD
            }
        }

        // صفحه‌ها با کارت‌های آبی
        val pager = findViewById<ViewPager2>(R.id.afghanViewPager)
        pager.setBackgroundColor(page)
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = AfghanSmsData.tabs.size
            override fun createFragment(position: Int): Fragment =
                AfghanPackagesFragment.newInstance("sms", position)
        }

        // تب‌ها
        val tabLayout = findViewById<TabLayout>(R.id.afghanTabLayout)
        tabLayout.setBackgroundColor(page)
        tabLayout.setTabTextColors(tabText, neon)
        tabLayout.setSelectedTabIndicatorColor(neon)
        tabLayout.elevation = 0f
        tabLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#3329B6FF"))

        TabLayoutMediator(tabLayout, pager) { tab, pos ->
            tab.text = AfghanSmsData.tabs[pos].get(this)
        }.attach()
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}