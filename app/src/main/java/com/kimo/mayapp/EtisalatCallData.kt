package com.kimo.mayapp

object EtisalatCallData {

    const val SMS_NUMBER = "3378"
    const val CHECK_NUMBER = "606"
    const val CHECK_BODY = "505"

    val screenTitle = Tr("بسته‌های تماس اتصالات", "Etisalat Call Packages", "د اتصالاتو زنګ بستې")

    // ترتیب باید با forTab یکی باشد
    val tabs = listOf(
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفته‌ای", "Weekly", "اونیز"),
        Tr("ماهانه", "Monthly", "میاشتنی"),
        Tr("ترکیبی", "Combo", "ګډ"),
        Tr("شبانه", "Night", "شپنی"),
        Tr("متفرقه", "Misc", "نور")
    )

    fun forTab(index: Int): List<EtiPackage> = when (index) {
        0 -> daily
        1 -> weekly
        2 -> monthly
        3 -> combo
        4 -> night
        5 -> misc
        else -> emptyList()
    }

    private val daily = listOf(
        EtiPackage("100 دقیقه روزانه", "100 Min Daily", 25, "900", EtiPeriod.DAY, check888 = true),
        EtiPackage("50 دقیقه روزانه", "50 Min Daily", 15, "801", EtiPeriod.DAY, check888 = true)
    )

    private val weekly = listOf(
        EtiPackage("200 دقیقه هفتگی", "200 Min Weekly", 45, "W45", EtiPeriod.WEEK, check888 = true),
        EtiPackage("300 دقیقه هفتگی", "300 Min Weekly", 55, "E55", EtiPeriod.WEEK, check888 = true),
        EtiPackage("450 دقیقه هفتگی", "450 Min Weekly", 80, "W75", EtiPeriod.WEEK, check888 = true),
        EtiPackage("پیشنهاد شگفت انگیز (250 تا 500 دقیقه)", "Amazing Offer (250-500 min)", 55, "W55", EtiPeriod.WEEK, check888 = true),
        EtiPackage("نرخ 1 افغانی در دقیقه (هفتگی)", "1 AFN/min (Weekly)", 10, "R10", EtiPeriod.WEEK, check888 = true)
    )

    private val monthly = listOf(
        EtiPackage("1000 دقیقه ماهانه", "1000 Min Monthly", 180, "M180", EtiPeriod.MONTH, check888 = true),
        EtiPackage("1750 دقیقه ماهانه", "1750 Min Monthly", 220, "M220", EtiPeriod.MONTH, check888 = true),
        EtiPackage("650 دقیقه ماهانه", "650 Min Monthly", 135, "M135", EtiPeriod.MONTH, check888 = true),
        EtiPackage("5000 دقیقه تقسیم شده", "5000 Min Split", 599, "5000", EtiPeriod.MONTH, check888 = true),
        EtiPackage("تماس نامحدود اتصالات", "Unlimited Etisalat Calls", 300, "U300", EtiPeriod.MONTH, check888 = true),
        EtiPackage("نرخ 1 افغانی در دقیقه (ماهانه)", "1 AFN/min (Monthly)", 30, "R30", EtiPeriod.MONTH, check888 = true)
    )

    private val combo = listOf(
        EtiPackage("1000 دقیقه پریمیوم", "1000 Min Premium", 200, "M200", EtiPeriod.MONTH, check888 = true),
        EtiPackage("125 دقیقه + 50 پیامک", "125 Min + 50 SMS", 40, "700", EtiPeriod.DAY, check888 = true)
    )

    private val night = listOf(
        EtiPackage("250 دقیقه شبانه", "250 Min Night", 10, "N10", EtiPeriod.DAY, check888 = true),
        EtiPackage("750 دقیقه شبانه", "750 Min Night", 35, "N35", EtiPeriod.WEEK, check888 = true),
        EtiPackage("4000 دقیقه شبانه", "4000 Min Night", 199, "199", EtiPeriod.MONTH, check888 = true)
    )

    private val misc = listOf(
        EtiPackage("60 دقیقه ساعتی", "60 Min Hourly", 10, "H10", EtiPeriod.DAY, check888 = true),
        EtiPackage("400 دقیقه دو هفته‌ای", "400 Min Two Weeks", 80, "80", EtiPeriod.WEEK, check888 = true),
        EtiPackage("جمعه پریمیوم", "Friday Premium", 25, "F20", EtiPeriod.DAY, check888 = true),
        EtiPackage("جمعه استاندارد", "Friday Standard", 15, "F15", EtiPeriod.DAY, check888 = true)
    )
}