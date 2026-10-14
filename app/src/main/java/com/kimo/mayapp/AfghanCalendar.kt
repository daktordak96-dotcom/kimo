package com.kimo.mayapp

import android.content.Context
import java.util.Calendar

/**
 * تقویم افغانستان: هجری شمسی (با نام ماه‌های افغانی)، هجری قمری و میلادی
 * همه بدون اینترنت حساب می‌شوند.
 */
object AfghanCalendar {

    class Day(
        val jy: Int, val jm: Int, val jd: Int,      // شمسی
        val hy: Int, val hm: Int, val hd: Int,      // قمری
        val gy: Int, val gm: Int, val gd: Int,      // میلادی
        val weekDay: Int                             // 1=یکشنبه ... 7=شنبه
    )

    // ---------------- نام ماه‌ها ----------------
    private val shamsiFa = arrayOf("حمل", "ثور", "جوزا", "سرطان", "اسد", "سنبله",
        "میزان", "عقرب", "قوس", "جدی", "دلو", "حوت")
    private val shamsiPs = arrayOf("وری", "غویی", "غبرګولی", "چنګاښ", "زمری", "وږی",
        "تله", "لړم", "لیندۍ", "مرغومی", "سلواغه", "کب")
    private val shamsiEn = arrayOf("Hamal", "Sawr", "Jawza", "Saratan", "Asad", "Sunbula",
        "Mizan", "Aqrab", "Qaws", "Jadi", "Dalw", "Hut")

    private val hijriFa = arrayOf("محرم", "صفر", "ربیع‌الاول", "ربیع‌الثانی", "جمادی‌الاول",
        "جمادی‌الثانی", "رجب", "شعبان", "رمضان", "شوال", "ذی‌القعده", "ذی‌الحجه")
    private val hijriEn = arrayOf("Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Thani",
        "Jumada al-Ula", "Jumada al-Akhirah", "Rajab", "Shaban", "Ramadan", "Shawwal",
        "Dhu al-Qadah", "Dhu al-Hijjah")

    private val gregFa = arrayOf("جنوری", "فبروری", "مارچ", "اپریل", "می", "جون",
        "جولای", "اگست", "سپتمبر", "اکتوبر", "نومبر", "دسمبر")
    private val gregPs = arrayOf("جنوري", "فبروري", "مارچ", "اپرېل", "می", "جون",
        "جولای", "اګست", "سپتمبر", "اکتوبر", "نومبر", "ډسمبر")
    private val gregEn = arrayOf("January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December")

    // 1=یکشنبه ... 7=شنبه (مثل Calendar.DAY_OF_WEEK)
    private val weekFa = arrayOf("یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه")
    private val weekPs = arrayOf("یکشنبه", "دوشنبه", "درېشنبه", "څلورشنبه", "پنځشنبه", "جمعه", "شنبه")
    private val weekEn = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    // ---------------- امروز ----------------
    fun today(ctx: Context): Day {
        val c = Calendar.getInstance()
        return of(ctx, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
    }

    fun of(ctx: Context, gy: Int, gm: Int, gd: Int): Day {
        val jdn = g2d(gy, gm, gd)
        val j = d2j(jdn)
        val h = jdToHijri(jdn + hijriOffset(ctx))
        val c = Calendar.getInstance()
        c.set(gy, gm - 1, gd)
        return Day(j[0], j[1], j[2], h[0], h[1], h[2], gy, gm, gd, c.get(Calendar.DAY_OF_WEEK))
    }

    // تصحیح تاریخ قمری (بر اساس رؤیت هلال): -1، 0 یا +1 روز
    fun hijriOffset(ctx: Context): Int =
        ctx.getSharedPreferences("AppSettings", Context.MODE_PRIVATE).getInt("hijri_offset", 0)

    fun setHijriOffset(ctx: Context, v: Int) {
        ctx.getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit()
            .putInt("hijri_offset", v.coerceIn(-2, 2)).apply()
    }

    // ---------------- متن‌های آماده ----------------
    fun weekName(lang: String, d: Day): String = when (lang) {
        "en" -> weekEn[d.weekDay - 1]; "ps" -> weekPs[d.weekDay - 1]; else -> weekFa[d.weekDay - 1]
    }

    fun shamsiText(lang: String, d: Day): String = when (lang) {
        "en" -> "${d.jd} ${shamsiEn[d.jm - 1]} ${d.jy}"
        "ps" -> num("${d.jd} ${shamsiPs[d.jm - 1]} ${d.jy}", lang)
        else -> num("${d.jd} ${shamsiFa[d.jm - 1]} ${d.jy}", lang)
    }

    fun hijriText(lang: String, d: Day): String = when (lang) {
        "en" -> "${d.hd} ${hijriEn[d.hm - 1]} ${d.hy}"
        else -> num("${d.hd} ${hijriFa[d.hm - 1]} ${d.hy}", lang)
    }

    fun gregText(lang: String, d: Day): String = when (lang) {
        "en" -> "${d.gd} ${gregEn[d.gm - 1]} ${d.gy}"
        "ps" -> num("${d.gd} ${gregPs[d.gm - 1]} ${d.gy}", lang)
        else -> num("${d.gd} ${gregFa[d.gm - 1]} ${d.gy}", lang)
    }

    fun shamsiMonthName(lang: String, m: Int): String = when (lang) {
        "en" -> shamsiEn[m - 1]; "ps" -> shamsiPs[m - 1]; else -> shamsiFa[m - 1]
    }

    // اعداد فارسی برای دری و پښتو
    fun num(s: String, lang: String): String {
        if (lang == "en") return s
        val sb = StringBuilder()
        for (ch in s) sb.append(if (ch in '0'..'9') ('۰' + (ch - '0')) else ch)
        return sb.toString()
    }

    // ================= حساب‌ها =================
    // میلادی → شمارهٔ روز ژولینی
    private fun g2d(gy: Int, gm: Int, gd: Int): Long {
        var d = ((gy + (gm - 8) / 6 + 100100).toLong() * 1461) / 4 +
                (153 * ((gm + 9) % 12) + 2) / 5 + gd - 34840408
        d = d - ((gy + 100100 + (gm - 8) / 6) / 100) * 3 / 4 + 752
        return d
    }

    // شمارهٔ روز → میلادی
    private fun d2g(jdn: Long): IntArray {
        var j = 4 * jdn + 139361631
        j = j + ((4 * jdn + 183187720) / 146097) * 3 / 4 * 4 - 3908
        val i = ((j % 1461) / 4) * 5 + 308
        val gd = ((i % 153) / 5 + 1).toInt()
        val gm = (((i / 153) % 12) + 1).toInt()
        val gy = (j / 1461 - 100100 + (8 - gm) / 6).toInt()
        return intArrayOf(gy, gm, gd)
    }

    // اطلاعات سال شمسی (الگوریتم دقیق جلالی)
    private fun jalCal(jy: Int): IntArray {
        val breaks = intArrayOf(-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
            1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178)
        val gy = jy + 621
        var leapJ = -14
        var jp = breaks[0]
        var jump = 0
        for (k in 1 until breaks.size) {
            val jm = breaks[k]
            jump = jm - jp
            if (jy < jm) break
            leapJ += jump / 33 * 8 + (jump % 33) / 4
            jp = jm
        }
        var n = jy - jp
        leapJ += n / 33 * 8 + ((n % 33) + 3) / 4
        if (jump % 33 == 4 && jump - n == 4) leapJ += 1
        val leapG = gy / 4 - (gy / 100 + 1) * 3 / 4 - 150
        val march = 20 + leapJ - leapG
        if (jump - n < 6) n = n - jump + (jump + 4) / 33 * 33
        var leap = (((n + 1) % 33) - 1) % 4
        if (leap == -1) leap = 4
        return intArrayOf(leap, gy, march)
    }

    // شمارهٔ روز → شمسی
    private fun d2j(jdn: Long): IntArray {
        val gy = d2g(jdn)[0]
        var jy = gy - 621
        val r = jalCal(jy)
        val jdn1f = g2d(gy, 3, r[2])
        var k = (jdn - jdn1f).toInt()
        if (k >= 0) {
            if (k <= 185) return intArrayOf(jy, 1 + k / 31, k % 31 + 1)
            k -= 186
        } else {
            jy -= 1
            k += 179
            if (r[0] == 1) k += 1
        }
        return intArrayOf(jy, 7 + k / 30, k % 30 + 1)
    }

    // شمارهٔ روز → قمری (تقویم قمری حسابی)
    private fun jdToHijri(jd: Long): IntArray {
        var l = jd - 1948440 + 10632
        val n = (l - 1) / 10631
        l = l - 10631 * n + 354
        val j = ((10985 - l) / 5316) * ((50 * l) / 17719) + (l / 5670) * ((43 * l) / 15238)
        l = l - ((30 - j) / 15) * ((17719 * j) / 50) - (j / 16) * ((15238 * j) / 43) + 29
        val m = (24 * l) / 709
        val d = l - (709 * m) / 24
        val y = 30 * n + j - 30
        return intArrayOf(y.toInt(), m.toInt(), d.toInt())
    }
}