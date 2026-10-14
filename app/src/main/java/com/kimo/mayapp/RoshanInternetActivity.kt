package com.kimo.mayapp

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
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

class RoshanInternetActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val preferences = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = preferences?.getString(SELECTED_LANGUAGE, "fa") ?: "fa"
        super.attachBaseContext(updateBaseContextLocale(newBase!!, lang))
    }

    private fun updateBaseContextLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roshan_internet)

        // نوع صفحه: internet, sms, call, services, intl
        val kind = intent.getStringExtra("KIND") ?: "internet"
        val noTabs = kind == "services" || kind == "intl"
        val tabsList = when (kind) {
            "sms" -> RoshanSmsData.tabs
            "call" -> RoshanCallData.tabs
            else -> RoshanInternetData.tabs
        }
        val titleText = when (kind) {
            "sms" -> RoshanSmsData.screenTitle.get(this)
            "call" -> RoshanCallData.screenTitle.get(this)
            "services" -> RoshanServicesData.screenTitle.get(this)
            "intl" -> RoshanIntlData.screenTitle.get(this)
            else -> RoshanInternetData.screenTitle.get(this)
        }

        val gold = Color.parseColor("#FFD54F")
        val pageDark = Color.parseColor("#1A0509")
        val navDark = Color.parseColor("#1F0610")
        val tabText = Color.parseColor("#D9A9B0")

        window.statusBarColor = pageDark
        window.navigationBarColor = navDark

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = titleText
        toolbar.setNavigationOnClickListener { finish() }

        val barRadius = dp(20).toFloat()
        toolbar.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#8A000F"),
                Color.parseColor("#E5102D"),
                Color.parseColor("#FF2D55"),
                Color.parseColor("#FF5A7A")
            )
        ).apply {
            cornerRadius = barRadius
            setStroke(dp(1), Color.parseColor("#FFD54F"))
        }

        (toolbar.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            it.setMargins(dp(10), dp(8), dp(10), dp(8))
            toolbar.layoutParams = it
        }

        toolbar.clipToOutline = true
        toolbar.outlineProvider = ViewOutlineProvider.BACKGROUND

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.navigationIcon?.setTint(Color.WHITE)
        toolbar.elevation = dp(8).toFloat()
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            toolbar.outlineAmbientShadowColor = Color.parseColor("#FF1F3D")
            toolbar.outlineSpotShadowColor = Color.parseColor("#FF1F3D")
        }

        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is TextView) {
                child.textSize = 20f
                child.typeface = Typeface.DEFAULT_BOLD
            }
        }

        val pager = findViewById<ViewPager2>(R.id.afghanViewPager)
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = if (noTabs) 1 else tabsList.size
            override fun createFragment(position: Int): Fragment = when (kind) {
                "services" -> RoshanServicesFragment.newInstance()
                "intl" -> RoshanIntlFragment.newInstance()
                else -> RoshanInternetFragment.newInstance(position, kind)
            }
        }

        val tabLayout = findViewById<TabLayout>(R.id.afghanTabLayout)
        tabLayout.setBackgroundColor(pageDark)
        tabLayout.setTabTextColors(tabText, gold)
        tabLayout.setSelectedTabIndicatorColor(gold)
        tabLayout.elevation = 0f
        tabLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#33FFD54F"))

        if (noTabs) {
            tabLayout.visibility = View.GONE
        } else {
            TabLayoutMediator(tabLayout, pager) { tab, pos ->
                tab.text = tabsList[pos].get(this)
            }.attach()
        }
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}