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

class SalaamInternetActivity : AppCompatActivity() {

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

        val kind = intent.getStringExtra("KIND") ?: "internet"
        val noTabs = kind == "services" || kind == "intl"
        val tabsList = when (kind) {
            "sms" -> SalaamSmsData.tabs
            "call" -> SalaamCallData.tabs
            else -> SalaamInternetData.tabs
        }
        val titleText = when (kind) {
            "sms" -> SalaamSmsData.screenTitle.get(this)
            "call" -> SalaamCallData.screenTitle.get(this)
            "services" -> SalaamServicesData.screenTitle.get(this)
            "intl" -> SalaamIntlData.screenTitle.get(this)
            else -> SalaamInternetData.screenTitle.get(this)
        }

        // رنگ‌های ملایم‌تر
        val gold = Color.parseColor("#F2C94C")
        val pageDark = Color.parseColor("#04100D")
        val navDark = Color.parseColor("#061915")
        val tabText = Color.parseColor("#7FB5AB")

        window.statusBarColor = pageDark
        window.navigationBarColor = navDark

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = titleText
        toolbar.setNavigationOnClickListener { finish() }

        val barRadius = dp(20).toFloat()
        toolbar.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#004D43"),
                Color.parseColor("#00695C"),
                Color.parseColor("#00796B"),
                Color.parseColor("#26A69A")
            )
        ).apply {
            cornerRadius = barRadius
            setStroke(dp(1), Color.parseColor("#2E7D72"))
        }

        (toolbar.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            it.setMargins(dp(10), dp(8), dp(10), dp(8))
            toolbar.layoutParams = it
        }

        toolbar.clipToOutline = true
        toolbar.outlineProvider = ViewOutlineProvider.BACKGROUND

        toolbar.setTitleTextColor(Color.parseColor("#F2FFFC"))
        toolbar.navigationIcon?.setTint(Color.parseColor("#F2FFFC"))
        toolbar.elevation = dp(3).toFloat()

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
                "services" -> SalaamServicesFragment.newInstance("services")
                "intl" -> SalaamServicesFragment.newInstance("intl")
                else -> SalaamFragment.newInstance(position, kind)
            }
        }

        val tabLayout = findViewById<TabLayout>(R.id.afghanTabLayout)
        tabLayout.setBackgroundColor(pageDark)
        tabLayout.setTabTextColors(tabText, gold)
        tabLayout.setSelectedTabIndicatorColor(gold)
        tabLayout.elevation = 0f
        tabLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#2200897B"))

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