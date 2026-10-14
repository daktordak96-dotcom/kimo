package com.kimo.mayapp

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AfghanCallActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_packages) // از همان لایوت مشترک استفاده می‌کند

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = AfghanCallData.screenTitle.get(this)
        toolbar.setNavigationOnClickListener { finish() }

        val tabLayout = findViewById<TabLayout>(R.id.callTabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.callViewPager)

        val tabTitles = listOf(
            Strings.get(this, "tab_daily"),
            Strings.get(this, "tab_weekly"),
            Strings.get(this, "tab_monthly"),
            Strings.get(this, "tab_combo"),
            Strings.get(this, "tab_night"),
            Strings.get(this, "tab_misc")
        )

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabTitles.size
            override fun createFragment(position: Int): Fragment {
                // ارسال نوع "call" به فرگمنت برای لود کردن بسته‌های تماس افغان بیسیم
                return AfghanPackagesFragment.newInstance("call", position)
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
