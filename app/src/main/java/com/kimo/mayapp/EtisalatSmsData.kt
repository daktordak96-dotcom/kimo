package com.kimo.mayapp

object EtisalatSmsData {

    const val SMS_NUMBER = "3378"
    const val CHECK_NUMBER = "606"
    const val CHECK_BODY = "505"

    val screenTitle = Tr("بسته‌های پیام اتصالات", "Etisalat SMS Packages", "د اتصالاتو پیغام بستې")

    // ترتیب باید با forTab یکی باشد
    val tabs = listOf(
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفته‌ای", "Weekly", "اونیز"),
        Tr("ماهانه", "Monthly", "میاشتنی"),
        Tr("ترکیبی", "Combo", "ګډ"),
        Tr("متفرقه", "Misc", "نور")
    )

    fun forTab(index: Int): List<EtiPackage> = when (index) {
        0 -> daily
        1 -> weekly
        2 -> monthly
        3 -> combo
        else -> emptyList()
    }

    private val daily = listOf(
        EtiPackage("200 پیام روزانه", "200 SMS Daily", 10, "BDS", EtiPeriod.DAY)
    )

    private val weekly = listOf(
        EtiPackage("2000 پیام هفته‌ای", "2000 SMS Weekly", 30, "BWS", EtiPeriod.WEEK)
    )

    private val monthly = listOf(
        EtiPackage("20000 پیام ماهانه", "20000 SMS Monthly", 65, "BMS", EtiPeriod.MONTH)
    )

    // بسته‌های ترکیبی مشترک با تب ترکیبی اینترنت هستند
    private val combo = listOf(
        EtiPackage("1000 دقیقه پریمیوم", "1000 Min Premium", 200, "M200", EtiPeriod.MONTH, check888 = true),
        EtiPackage("125 دقیقه + 50 پیامک", "125 Min + 50 SMS", 40, "700", EtiPeriod.DAY, check888 = true)
    )
}