package com.kimo.mayapp

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale

class PackagesContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val operatorKey = intent.getStringExtra("OPERATOR_KEY") ?: "etisalat"
        val isEtisalat = operatorKey.toLowerCase(Locale.ROOT) == "etisalat" ||
            operatorKey == "اتصالات"
        if (!isEtisalat) {
            Toast.makeText(this, Strings.get(this, "soon_internet"), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        window.statusBarColor = Color.parseColor("#031208")
        window.navigationBarColor = Color.parseColor("#06260F")

        val title = EtisalatData.screenTitle.get(this)
        val tabs = EtisalatData.tabs

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.parseColor("#031208"), Color.parseColor("#041A0B"), Color.parseColor("#06260F"))
            )
        }

        // هدر سبز مثل صفحه خدمات
        val header = TextView(this).apply {
            text = title
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(18), dp(14), dp(18), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#0B5A1F"), Color.parseColor("#1FA02F"), Color.parseColor("#4CD137"))
            ).apply {
                cornerRadius = dp(18).toFloat()
                setStroke(dp(1), Color.parseColor("#FFD54F"))
            }
            setOnClickListener { finish() }
        }
        root.addView(header, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(dp(10), dp(10), dp(10), dp(6))
        })

        // نوار تب‌ها
        val tabLayout = TabLayout(this).apply {
            setBackgroundColor(Color.parseColor("#031208"))
            setTabTextColors(Color.parseColor("#9FE8DC"), Color.parseColor("#FFD54F"))
            setSelectedTabIndicatorColor(Color.parseColor("#FFD54F"))
            tabMode = TabLayout.MODE_SCROLLABLE
        }
        root.addView(tabLayout, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        // صفحه تب‌ها
        val viewPager = ViewPager2(this)
        viewPager.adapter = PackagesPagerAdapter(this)
        root.addView(viewPager, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position].title.get(this)
        }.attach()

        setContentView(root)
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}