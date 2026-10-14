package com.kimo.mayapp

import android.content.Context

enum class EtiPeriod(val fa: String, val en: String, val ps: String) {
    HOUR1("1 ساعت", "1 hour", "1 ساعت"),
    HOUR2("2 ساعت", "2 hours", "2 ساعته"),
    HOUR4("4 ساعت", "4 hours", "4 ساعته"),
    DAY("یک روز", "1 day", "یوه ورځ"),
    WEEK("یک هفته", "1 week", "یوه اونۍ"),
    MONTH("30 روز", "30 days", "30 ورځې"),
    NIGHT_DAY("یک شب (11 شب تا 7 صبح)", "1 night (11PM-7AM)", "یوه شپه (له 11 تر 7 سهار)"),
    NIGHT_MONTH("30 شب (11 شب تا 7 صبح)", "30 nights (11PM-7AM)", "30 شپې (له 11 تر 7 سهار)")
}

class EtiPackage(
    val titleFa: String,
    val titleEn: String,
    val price: Int,
    val code: String,
    val period: EtiPeriod,
    val check888: Boolean = false
) {
    val onCode: String get() = code
    val offCode: String get() = "PU $code"

    fun titleText(ctx: Context): String = Tr(titleFa, titleEn, titleFa).get(ctx)

    fun priceText(ctx: Context): String =
        Tr("$price افغانی", "$price AFN", "$price افغانۍ").get(ctx)

    fun validityText(ctx: Context): String =
        Tr("اعتبار: ${period.fa}", "Validity: ${period.en}", "اعتبار: ${period.ps}").get(ctx)

    fun activateText(ctx: Context): String = Tr(
        "برای فعال‌سازی $onCode و برای غیرفعال‌سازی $offCode را به ${EtisalatData.SMS_NUMBER} ارسال نمائید.",
        "To activate send $onCode and to deactivate send $offCode to ${EtisalatData.SMS_NUMBER}.",
        "د فعالولو لپاره $onCode او د غیرفعالولو لپاره $offCode ${EtisalatData.SMS_NUMBER} ته ولیږئ."
    ).get(ctx)

    fun checkText(ctx: Context): String = if (check888) Tr(
        "برای بررسی موجودی 505 را به 606 ارسال نمائید یا به 888 زنگ بزنید.",
        "To check balance send 505 to 606 or call 888.",
        "د بیلانس د کتلو لپاره 505 ته 606 ولیږئ یا 888 ته زنګ ووهئ."
    ).get(ctx) else Tr(
        "برای بررسی موجودی 505 را به 606 ارسال نمائید.",
        "To check balance send 505 to 606.",
        "د بیلانس د کتلو لپاره 505 ته 606 ولیږئ."
    ).get(ctx)
}

class EtiTab(val title: Tr, val items: List<EtiPackage>)

object EtisalatData {

    const val SMS_NUMBER = "3378"
    const val CHECK_NUMBER = "606"
    const val CHECK_BODY = "505"

    val screenTitle = Tr("بسته‌های اینترنتی اتصالات", "Etisalat Internet Packages", "د اتصالاتو انټرنیټ بستې")
    val btnOn = Tr("فعال‌سازی", "Activate", "فعالول")
    val btnOff = Tr("غیرفعال‌سازی", "Deactivate", "غیرفعالول")
    val btnCheck = Tr("چک بیلانس", "Check Balance", "بیلانس کتل")

    val tabs: List<EtiTab> = listOf(
        EtiTab(Tr("شبانه", "Night", "شپنی"), listOf(
            EtiPackage("1 جی بی شبانه روزانه", "1 GB Night Daily", 25, "NBD", EtiPeriod.NIGHT_DAY),
            EtiPackage("3 جی بی شبانه ماهانه", "3 GB Night Monthly", 99, "NBS", EtiPeriod.NIGHT_MONTH)
        )),
        EtiTab(Tr("ترکیبی", "Combo", "ګډ"), listOf(
            EtiPackage("1000 دقیقه پریمیوم", "1000 Min Premium", 200, "M200", EtiPeriod.MONTH, check888 = true),
            EtiPackage("125 دقیقه + 50 پیامک", "125 Min + 50 SMS", 40, "700", EtiPeriod.DAY, check888 = true)
        )),
        EtiTab(Tr("ماهانه", "Monthly", "میاشتنی"), listOf(
            EtiPackage("8 جی بی ماهانه", "8 GB Monthly", 500, "8GB", EtiPeriod.MONTH),
            EtiPackage("12 جی بی ماهانه", "12 GB Monthly", 599, "599", EtiPeriod.MONTH),
            EtiPackage("25 جی بی ماهانه", "25 GB Monthly", 999, "10GB", EtiPeriod.MONTH),
            EtiPackage("30 جی بی ماهانه (استاندارد)", "30 GB Monthly (Standard)", 1199, "1199", EtiPeriod.MONTH),
            EtiPackage("30 جی بی ماهانه (پیشرفته)", "30 GB Monthly (Advanced)", 1399, "1399", EtiPeriod.MONTH),
            EtiPackage("1 جی بی ماهانه", "1 GB Monthly", 95, "M95", EtiPeriod.MONTH),
            EtiPackage("1.2 جی بی ماهانه", "1.2 GB Monthly", 110, "110", EtiPeriod.MONTH),
            EtiPackage("1.65 جی بی ماهانه", "1.65 GB Monthly", 150, "M150", EtiPeriod.MONTH),
            EtiPackage("2 جی بی ماهانه", "2 GB Monthly", 190, "190", EtiPeriod.MONTH),
            EtiPackage("3 جی بی ماهانه", "3 GB Monthly", 250, "2GB", EtiPeriod.MONTH),
            EtiPackage("4 جی بی ماهانه", "4 GB Monthly", 325, "M325", EtiPeriod.MONTH),
            EtiPackage("5 جی بی ماهانه", "5 GB Monthly", 325, "N325", EtiPeriod.MONTH),
            EtiPackage("5.5 جی بی ماهانه", "5.5 GB Monthly", 440, "440", EtiPeriod.MONTH),
            EtiPackage("7 جی بی ماهانه", "7 GB Monthly", 550, "550", EtiPeriod.MONTH),
            EtiPackage("13.5 جی بی ماهانه", "13.5 GB Monthly", 799, "7GB", EtiPeriod.MONTH),
            EtiPackage("14 جی بی ماهانه", "14 GB Monthly", 699, "N699", EtiPeriod.MONTH),
            EtiPackage("15 جی بی ماهانه (5 جی بی شبانه)", "15 GB Monthly (5 GB Night)", 699, "M15", EtiPeriod.MONTH),
            EtiPackage("40 جی بی ماهانه", "40 GB Monthly", 1699, "1699", EtiPeriod.MONTH),
            EtiPackage("50 جی بی ماهانه", "50 GB Monthly", 2099, "2099", EtiPeriod.MONTH)
        )),
        EtiTab(Tr("هفته‌ای", "Weekly", "اونیز"), listOf(
            EtiPackage("550 ام بی هفتگی", "550 MB Weekly", 50, "300", EtiPeriod.WEEK),
            EtiPackage("1.5 جی بی هفتگی", "1.5 GB Weekly", 99, "W99", EtiPeriod.WEEK),
            EtiPackage("1.8 جی بی هفتگی", "1.8 GB Weekly", 149, "149", EtiPeriod.WEEK),
            EtiPackage("2 جی بی هفتگی (1 جی بی شبانه)", "2 GB Weekly (1 GB Night)", 99, "ND2", EtiPeriod.WEEK),
            EtiPackage("3 جی بی هفتگی", "3 GB Weekly", 199, "W199", EtiPeriod.WEEK)
        )),
        EtiTab(Tr("روزانه", "Daily", "ورځنی"), listOf(
            EtiPackage("200 ام بی روزانه", "200 MB Daily", 15, "D200", EtiPeriod.DAY),
            EtiPackage("500-750 ام بی روزانه + 5 دقیقه", "500-750 MB Daily + 5 min", 30, "D400", EtiPeriod.DAY),
            EtiPackage("1.3 جی بی روزانه", "1.3 GB Daily", 50, "D50", EtiPeriod.DAY),
            EtiPackage("2 جی بی روزانه", "2 GB Daily", 77, "D2", EtiPeriod.DAY),
            EtiPackage("3 جی بی روزانه", "3 GB Daily", 90, "D3", EtiPeriod.DAY)
        )),
        EtiTab(Tr("ساعتی", "Hourly", "ساعتي"), listOf(
            EtiPackage("400 ام بی ساعتی", "400 MB Hourly", 10, "H400", EtiPeriod.HOUR1),
            EtiPackage("512 ام بی ساعتی", "512 MB Hourly", 12, "H512", EtiPeriod.HOUR1),
            EtiPackage("650 ام بی ساعتی", "650 MB Hourly", 15, "650MB", EtiPeriod.HOUR1),
            EtiPackage("1 جی بی ساعتی", "1 GB Hourly", 20, "1GB", EtiPeriod.HOUR1),
            EtiPackage("900 ام بی دو ساعته", "900 MB 2 Hours", 20, "2H", EtiPeriod.HOUR2),
            EtiPackage("1.5 جی بی دو ساعته", "1.5 GB 2 Hours", 30, "H2", EtiPeriod.HOUR2),
            EtiPackage("2 جی بی چهار ساعته", "2 GB 4 Hours", 40, "40", EtiPeriod.HOUR4)
        ))
    )
}