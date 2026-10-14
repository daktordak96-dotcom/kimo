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

class AtomaActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_atoma)

        val kind = intent.getStringExtra("KIND") ?: "internet"
        val tabs = AtomaData.tabs(kind)
        val pages = if (tabs.size <= 1) 1 else tabs.size

        // رنگ‌های زرد طلایی اتوما (ملایم)
        val pageDark = Color.parseColor("#0E0B04")
        val navDark = Color.parseColor("#16120A")
        val accent = Color.parseColor("#F2C94C")
        val tabText = Color.parseColor("#B8A77A")
        val titleColor = Color.parseColor("#FFF8E1")

        window.statusBarColor = pageDark
        window.navigationBarColor = navDark

        // هدر
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = AtomaData.title(kind).get(this)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                Color.parseColor("#6B5200"),
                Color.parseColor("#8F6E00"),
                Color.parseColor("#B38900"),
                Color.parseColor("#C49A12")
            )
        ).apply {
            cornerRadius = dp(20).toFloat()
            setStroke(dp(1), Color.parseColor("#8A7020"))
        }
        (toolbar.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
            it.setMargins(dp(10), dp(8), dp(10), dp(8))
            toolbar.layoutParams = it
        }
        toolbar.clipToOutline = true
        toolbar.outlineProvider = ViewOutlineProvider.BACKGROUND
        toolbar.setTitleTextColor(titleColor)
        toolbar.navigationIcon?.setTint(titleColor)
        toolbar.elevation = dp(3).toFloat()
        for (i in 0 until toolbar.childCount) {
            val child = toolbar.getChildAt(i)
            if (child is TextView) {
                child.textSize = 20f
                child.typeface = Typeface.DEFAULT_BOLD
            }
        }

        // صفحه‌ها
        val pager = findViewById<ViewPager2>(R.id.atomaPager)
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = pages
            override fun createFragment(position: Int): Fragment = when (kind) {
                "services" -> AtomaServicesFragment.newInstance()
                "intl" -> AtomaIntlFragment.newInstance()
                else -> AtomaFragment.newInstance(kind, position)
            }
        }

        // تب‌ها
        val tabLayout = findViewById<TabLayout>(R.id.atomaTabLayout)
        tabLayout.setBackgroundColor(pageDark)
        tabLayout.setTabTextColors(tabText, accent)
        tabLayout.setSelectedTabIndicatorColor(accent)
        tabLayout.elevation = 0f
        tabLayout.tabRippleColor = ColorStateList.valueOf(Color.parseColor("#22C9A227"))

        if (tabs.size <= 1) {
            tabLayout.visibility = View.GONE
        } else {
            TabLayoutMediator(tabLayout, pager) { tab, pos ->
                tab.text = tabs[pos].get(this)
            }.attach()
        }
    }

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }
}