package com.kimo.mayapp

import android.graphics.Color

class NeonTheme(
    val pageGrad: IntArray,   // پس‌زمینه (3 رنگ)
    val headGrad: IntArray,   // هدر (4 رنگ)
    val barGrad: IntArray,    // عنوان کارت و نوار نام منو (3 رنگ)
    val cardGrad: IntArray,   // بدنه کارت (3 رنگ)
    val menuCard: Int,
    val stroke: Int,
    val glow: Int,
    val accent: Int,
    val accentDeep: Int,
    val pill: Int,
    val btnBg: Int,
    val introText: Int,
    val tabText: Int,
    val neonOff: Int,
    val neonCheck: Int,
    val wave: IntArray
)

object NeonThemes {
    private fun c(vararg hex: String) = IntArray(hex.size) { Color.parseColor(hex[it]) }
    private fun c1(hex: String) = Color.parseColor(hex)

    val ROSHAN = NeonTheme(
        pageGrad = c("#1A0509", "#2A0812", "#1F0610"),
        headGrad = c("#8A000F", "#E5102D", "#FF2D55", "#FF5A7A"),
        barGrad = c("#B30012", "#FF1F3D", "#FF5A5F"),
        cardGrad = c("#5A0A18", "#3A0812", "#2A0610"),
        menuCard = c1("#7A0A1C"),
        stroke = c1("#FF5A5F"),
        glow = c1("#FF1F3D"),
        accent = c1("#FFD54F"),
        accentDeep = c1("#FFB300"),
        pill = c1("#2A0610"),
        btnBg = c1("#1A0610"),
        introText = c1("#FFE3E6"),
        tabText = c1("#D9A9B0"),
        neonOff = c1("#FF2D95"),
        neonCheck = c1("#00E5FF"),
        wave = c("#FF1F3D", "#FF5A2D", "#FF9800", "#FFC107", "#FF9800", "#FF3D3D", "#FF1F3D")
    )

    val ATOMA = NeonTheme(
        pageGrad = c("#030B1F", "#071A3D", "#04122B"),
        headGrad = c("#0A3D91", "#1565FF", "#1E90FF", "#4DC3FF"),
        barGrad = c("#0A3FA8", "#1E6BFF", "#3FA9FF"),
        cardGrad = c("#0F3C8F", "#0A2A66", "#071C47"),
        menuCard = c1("#0B3A8C"),
        stroke = c1("#4DA3FF"),
        glow = c1("#1E90FF"),
        accent = c1("#FF8A3D"),
        accentDeep = c1("#FF5E3A"),
        pill = c1("#06183D"),
        btnBg = c1("#04122B"),
        introText = c1("#DCEBFF"),
        tabText = c1("#9FC3F0"),
        neonOff = c1("#FF4F8B"),
        neonCheck = c1("#00E5FF"),
        wave = c("#1565FF", "#1E90FF", "#00B8FF", "#4DD6FF", "#00B8FF", "#3D7BFF", "#1565FF")
    )
}