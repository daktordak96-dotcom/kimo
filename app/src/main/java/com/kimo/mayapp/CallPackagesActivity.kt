package com.kimo.mayapp

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale

class CallPackagesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_packages)

        val operatorKey = (intent.getStringExtra("OPERATOR_KEY")
            ?: intent.getStringExtra("OPERATOR_NAME")
            ?: "afghan").toLowerCase(Locale.ROOT)

        val isAfghan = operatorKey in setOf("afghan", "afghan wireless", "افغان بیسیم")
        val isEtisalat = operatorKey in setOf("etisalat", "اتصالات")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = when {
            isEtisalat -> EtisalatCallData.screenTitle.get(this)
            isAfghan -> AfghanCallData.screenTitle.get(this)
            else -> Strings.get(this, "call_packages_title")
        }
        toolbar.setNavigationOnClickListener { finish() }

        val tabLayout = findViewById<TabLayout>(R.id.callTabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.callViewPager)

        val tabTitles: List<String> = if (isEtisalat) {
            EtisalatCallData.tabs.map { it.get(this) }
        } else {
            listOf(
                Strings.get(this, "tab_daily"),
                Strings.get(this, "tab_weekly"),
                Strings.get(this, "tab_monthly"),
                Strings.get(this, "tab_combo"),
                Strings.get(this, "tab_night"),
                Strings.get(this, "tab_misc")
            )
        }

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabTitles.size
            override fun createFragment(position: Int): Fragment =
                if (isEtisalat) {
                    EtisalatCallFragment.newInstance(position)
                } else {
                    AfghanPackagesFragment.newInstance("call", position)
                }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}