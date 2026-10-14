package com.kimo.mayapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PackagesPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = EtisalatData.tabs.size

    override fun createFragment(position: Int): Fragment =
        EtisalatPackagesFragment.newInstance(position)
}