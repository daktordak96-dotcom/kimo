package com.kimo.mayapp

import android.content.Context

class AtomaPackage(
    val title: Tr,
    val price: Tr,
    val intro: Tr,
    val on: String,
    val off: String,
    val offBySms: Boolean = false,
    val split: Boolean = false,
    val onAns: String = "",
    val offAns: String = ""
) {
    fun description(ctx: Context): String {
        val sb = StringBuilder(intro.get(ctx))

        if (split) {
            val onLine = (if (onAns.isEmpty()) ON_DIRECT else ON_ANS).get(ctx)
                .replace("{code}", on).replace("{ans}", onAns)
            val offLine = (if (offAns.isEmpty()) OFF_DIRECT else OFF_ANS).get(ctx)
                .replace("{code}", off).replace("{ans}", offAns)
            sb.append("\n").append(onLine)
            sb.append("\n").append(offLine)
        } else {
            val line = if (offBySms) DIAL_SMS else DIAL_BOTH
            sb.append("\n").append(line.get(ctx).replace("{on}", on).replace("{off}", off))
        }

        sb.append("\n").append(CHECK.get(ctx))
        return sb.toString()
    }

    companion object {
        val PRESS = Tr(
            "برای فعال سازی روی دکمه سبز و برای غیرفعال سازی روی دکمه سرخ فشار دهید.",
            "To activate press the green button and to deactivate press the red button.",
            "د فعالولو لپاره پر شنه تڼۍ او د غیر فعالولو لپاره پر سره تڼۍ کلیک وکړئ."
        )
        val DIAL_BOTH = Tr(
            "برای فعال سازی {on} و برای غیر فعال سازی {off} شماره گیری کنید.",
            "To activate dial {on} and to deactivate dial {off}.",
            "د فعالولو لپاره {on} او د غیر فعالولو لپاره {off} ډایل کړئ."
        )
        val DIAL_SMS = Tr(
            "برای فعال سازی {on} شماره گیری کنید و برای غیر فعال سازی {off} را به 344 بفرستید.",
            "To activate dial {on} and to deactivate send {off} to 344.",
            "د فعالولو لپاره {on} ډایل کړئ او د غیر فعالولو لپاره {off} 344 ته ولیږئ."
        )
        val ON_DIRECT = Tr(
            "برای فعال سازی {code} شماره گیری کنید.",
            "To activate dial {code}.",
            "د فعالولو لپاره {code} ډایل کړئ."
        )
        val ON_ANS = Tr(
            "برای فعال سازی {code} شماره گیری کرده و با {ans} جواب دهید.",
            "To activate dial {code} and answer with {ans}.",
            "د فعالولو لپاره {code} ډایل کړئ او په {ans} ځواب ورکړئ."
        )
        val OFF_DIRECT = Tr(
            "برای غیر فعال سازی {code} شماره گیری کنید.",
            "To deactivate dial {code}.",
            "د غیر فعالولو لپاره {code} ډایل کړئ."
        )
        val OFF_ANS = Tr(
            "برای غیر فعال سازی {code} شماره گیری کرده و با {ans} جواب دهید.",
            "To deactivate dial {code} and answer with {ans}.",
            "د غیر فعالولو لپاره {code} ډایل کړئ او په {ans} ځواب ورکړئ."
        )
        val CHECK = Tr(
            "برای دیدن بالانس باقیمانده 7 را به 344 بفرستید.",
            "To see your remaining balance send 7 to 344.",
            "د پاتې بیلانس د کتلو لپاره 7 شمېرې 344 ته ولیږئ."
        )
    }
}

object AtomaData {

    const val CHECK_NUMBER = "344"
    const val CHECK_BODY = "7"
    const val OFF_SMS_NUMBER = "344"
    private const val OFF_DIAL = "*344*8#"

    val soon = Tr("به‌زودی", "Coming soon", "ژر راځي")
    val btnOff = Tr("غیرفعال‌سازی", "Deactivate", "غیرفعالول")
    val btnCheck = Tr("چک بیلانس", "Check Balance", "بیلانس کتل")
    val btnOn = Tr("فعال‌سازی", "Activate", "فعالول")

    fun title(kind: String): Tr = when (kind) {
        "sms" -> Tr("بسته‌های پیام اتوما", "ATOMA SMS Packages", "د اتوما د پیغام بستې")
        "call" -> Tr("بسته‌های تماس اتوما", "ATOMA Call Packages", "د اتوما د زنګ بستې")
        "services" -> Tr("خدمات اتوما", "ATOMA Services", "د اتوما خدمتونه")
        "intl" -> Tr("بسته‌های بین‌المللی اتوما", "ATOMA International Packages", "د اتوما نړیوالې بستې")
        else -> Tr("بسته‌های اینترنتی اتوما", "ATOMA Internet Packages", "د اتوما د انټرنټ بستې")
    }

    // =====================================================
    //                      اینترنت
    // =====================================================

    private class Per(
        val titleFa: String, val titleEn: String, val titlePs: String,
        val whenFa: String, val whenEn: String, val whenPs: String,
        val midFa: String = "", val midEn: String = "", val midPs: String = ""
    )

    private val HOUR = Per("ساعتی", "Hourly", "ساعتي",
        "برای یک ساعت", "for one hour", "د یوې ساعت لپاره")
    private val HOUR2 = Per("دو ساعته", "Two Hours", "دوه ساعتي",
        "برای دو ساعت", "for two hours", "د دوو ساعتونو لپاره")
    private val DAY = Per("روزانه", "Daily", "ورځنی",
        "برای یک روز", "for one day", "د یوې ورځې لپاره")
    private val WEEK = Per("هفتگی", "Weekly", "اونیز",
        "برای 7 روز", "for 7 days", "د 7 ورځو لپاره")
    private val MONTH = Per("ماهانه", "Monthly", "میاشتنی",
        "برای 30 روز", "for 30 days", "د 30 ورځو لپاره")
    private val NIGHT = Per("شبانه", "Night", "شپنی",
        "برای یک شب", "for one night", "د یوې شپې لپاره",
        " شبانه", " night", " شپنی")
    private val NIGHT7 = Per("هفت شب", "Seven Nights", "اوه شپې",
        "برای هفت شب", "for seven nights", "د اوو شپو لپاره",
        " شبانه", " night", " شپنی")

    private fun comma(p: String) =
        if (p.length > 3) p.substring(0, p.length - 3) + "," + p.substring(p.length - 3) else p

    private fun pkg(
        size: String, isMb: Boolean, per: Per, price: String,
        code: String, offBySms: Boolean = false
    ): AtomaPackage {
        val uFa = if (isMb) "ام بی" else "جی بی"
        val uEn = if (isMb) "MB" else "GB"
        val uPs = if (isMb) "ام بي" else "جي بي"
        return AtomaPackage(
            Tr(
                "$size $uFa ${per.titleFa}",
                "$size $uEn ${per.titleEn}",
                "$size $uPs ${per.titlePs}"
            ),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "بسته $size $uFa انترنت${per.midFa} ${per.whenFa} به قیمت ${comma(price)} افغانی.",
                "$size $uEn${per.midEn} internet ${per.whenEn}, price $price AFN.",
                "${per.whenPs} د $size $uPs${per.midPs} انټرنټ بسته، بیه $price افغانۍ."
            ),
            on = "*344*$code#",
            off = if (offBySms) "D" else OFF_DIAL,
            offBySms = offBySms
        )
    }

    private val hourly = listOf(
        pkg("130", true, HOUR, "5", "58"),
        pkg("400", true, HOUR, "10", "59"),
        pkg("650", true, HOUR, "15", "60"),
        pkg("1", false, HOUR, "20", "61"),
        pkg("2", false, HOUR2, "30", "62", offBySms = true)
    )

    private val daily = listOf(
        pkg("170", true, DAY, "15", "12"),
        pkg("400", true, DAY, "30", "13"),
        pkg("1", false, DAY, "50", "14"),
        pkg("2", false, DAY, "80", "15"),
        pkg("3", false, DAY, "95", "16")
    )

    private val weekly = listOf(
        pkg("1", false, WEEK, "89", "22"),
        pkg("3", false, WEEK, "199", "24")
    )

    private val monthly = listOf(
        pkg("1", false, MONTH, "95", "30"),
        pkg("2", false, MONTH, "185", "31"),
        pkg("3", false, MONTH, "275", "52"),
        pkg("4", false, MONTH, "365", "32"),
        pkg("5", false, MONTH, "450", "53"),
        pkg("10", false, MONTH, "650", "33"),
        pkg("13", false, MONTH, "699", "37"),
        pkg("18", false, MONTH, "899", "34"),
        pkg("25", false, MONTH, "1099", "37"),
        pkg("35", false, MONTH, "1499", "36"),
        pkg("55", false, MONTH, "1999", "55"),
        pkg("100", false, MONTH, "3599", "100")
    )

    private val night = listOf(
        pkg("1", false, NIGHT, "25", "41"),
        pkg("10", false, NIGHT, "200", "42"),
        pkg("3", false, NIGHT7, "75", "43")
    )

    // =====================================================
    //                        پیام
    // =====================================================

    private class SmsPer(
        val titleFa: String, val titleEn: String, val titlePs: String,
        val days: Int
    )

    private val S_DAY = SmsPer("روزانه", "Daily", "ورځنی", 1)
    private val S_WEEK = SmsPer("هفتگی", "Weekly", "اونیز", 7)
    private val S_MONTH = SmsPer("ماهانه", "Monthly", "میاشتنی", 30)

    private fun smsCount(
        count: String, per: SmsPer, price: String, onAns: String, mini: Boolean = false
    ): AtomaPackage {
        val miniFa = if (mini) " مینی" else ""
        val miniEn = if (mini) " Mini" else ""
        return AtomaPackage(
            Tr(
                "$count پیام ${per.titleFa}$miniFa",
                "$count SMS ${per.titleEn}$miniEn",
                "$count پیغامونه ${per.titlePs}$miniFa"
            ),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "بسته $count پیام برای ${per.days} روز به قیمت $price افغانی.",
                "$count SMS for ${per.days} day(s), price $price AFN.",
                "د ${per.days} ورځو لپاره $count پیغامونه، بیه $price افغانۍ."
            ),
            on = "*247#", off = "*247#",
            split = true, onAns = onAns, offAns = "5,5"
        )
    }

    private fun smsUnlimited(
        per: SmsPer, price: String, on: String, off: String, offAns: String = ""
    ): AtomaPackage {
        return AtomaPackage(
            Tr(
                "مکالمه و پیام نامحدود ${per.titleFa}",
                "Unlimited Calls and SMS ${per.titleEn}",
                "نامحدود زنګونه او پیغامونه ${per.titlePs}"
            ),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "بسته مکالمه و پیام نامحدود برای ${per.days} روز به قیمت $price افغانی.",
                "Unlimited calls and SMS for ${per.days} days, price $price AFN.",
                "د ${per.days} ورځو لپاره نامحدود زنګونه او پیغامونه، بیه $price افغانۍ."
            ),
            on = on, off = off,
            split = true, offAns = offAns
        )
    }

    private val smsDaily = listOf(
        smsCount("1,000", S_DAY, "15", "5,1,2"),
        smsCount("150", S_DAY, "2", "5,1,1", mini = true)
    )

    private val smsWeekly = listOf(
        smsCount("5,000", S_WEEK, "35", "5,2,1"),
        smsUnlimited(S_WEEK, "75", "*247*231#", "*247#", offAns = "2,8")
    )

    private val smsMonthly = listOf(
        smsCount("10,000", S_MONTH, "70", "5,3,1"),
        smsUnlimited(S_MONTH, "250", "*247*252#", "*344*8#")
    )

    // =====================================================
    //                        تماس
    // =====================================================

    private fun callPkg(
        titleFa: String, titleEn: String, titlePs: String,
        price: String, days: Int, minutes: String,
        on: String, onAns: String,
        off: String = "*344*8#", offAns: String = ""
    ): AtomaPackage {
        val dFa = if (days == 1) "یک روز" else "$days روز"
        val dEn = if (days == 1) "one day" else "$days days"
        val dPs = if (days == 1) "یوې ورځې" else "$days ورځو"
        return AtomaPackage(
            Tr(titleFa, titleEn, titlePs),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "بسته $minutes برای $dFa به قیمت $price افغانی.",
                "$minutes for $dEn, price $price AFN.",
                "د $dPs لپاره $minutes، بیه $price افغانۍ."
            ),
            on = on, off = off,
            split = true, onAns = onAns, offAns = offAns
        )
    }

    private val callDaily = listOf(
        callPkg("85 دقیقه روزانه", "85 Minutes Daily", "85 دقیقې ورځنی",
            "25", 1, "85 دقیقه مکالمه", "*247#", "2,2,2"),
        callPkg("8 دقیقه روزانه", "8 Minutes Daily", "8 دقیقې ورځنی",
            "5", 1, "8 دقیقه مکالمه", "*247#", "2,2,1")
    )

    private val callWeekly = listOf(
        callPkg("مکالمه و پیام نامحدود هفتگی", "Unlimited Calls and SMS Weekly", "نامحدود زنګونه او پیغامونه اونیز",
            "75", 7, "مکالمه و پیام نامحدود", "*247*231#", "",
            off = "*247#", offAns = "2,8"),
        callPkg("850 دقیقه دو هفته‌ای", "850 Minutes Biweekly", "850 دقیقې دوه اونیز",
            "150", 14, "850 دقیقه مکالمه", "*247#", "2,4,1")
    )

    private val callMonthly = listOf(
        callPkg("1100 دقیقه ماهانه", "1100 Minutes Monthly", "1100 دقیقې میاشتنی",
            "200", 30, "1100 دقیقه مکالمه", "*247#", "2,5,1"),
        callPkg("مکالمه و پیام نامحدود ماهانه", "Unlimited Calls and SMS Monthly", "نامحدود زنګونه او پیغامونه میاشتنی",
            "250", 30, "مکالمه و پیام نامحدود", "*247*252#", "")
    )

    // =====================================================
    //                        خدمات
    // =====================================================

    class AtomaService(
        val title: Tr,
        val body: Tr,
        val rows: List<Pair<String, String>> = emptyList()
    )

    val services = listOf(
        AtomaService(
            title = Tr(
                "ارسال کردیت (مبلغ 10-100)",
                "Send Credit (Amount 10-100)",
                "کریډیټ لیږل (مقدار 10-100)"
            ),
            body = Tr(
                "*776*2*10*07xxxxxxxx#",
                "*776*2*10*07xxxxxxxx#",
                "*776*2*10*07xxxxxxxx#"
            )
        ),
        AtomaService(
            title = Tr("تنظیمات APN", "APN Settings", "د APN تنظیمات"),
            body = Tr("", "", ""),
            rows = listOf(
                "Name" to "ATOMA",
                "APN" to "INTERNET.ATOMA.COM.AF"
            )
        )
    )

    // =====================================================
    //                   تب‌ها و داده‌ها
    // =====================================================

    fun tabs(kind: String): List<Tr> = when (kind) {
        "internet" -> listOf(
            Tr("ساعتی", "Hourly", "ساعتي"),
            Tr("روزانه", "Daily", "ورځنی"),
            Tr("هفته ای", "Weekly", "اونیز"),
            Tr("ماهانه", "Monthly", "میاشتنی"),
            Tr("شبانه", "Night", "شپنی"),
            Tr("ترکیبی", "Combo", "ګډ")
        )
        "sms" -> listOf(
            Tr("روزانه", "Daily", "ورځنی"),
            Tr("هفته ای", "Weekly", "اونیز"),
            Tr("ماهانه", "Monthly", "میاشتنی"),
            Tr("ترکیبی", "Combo", "ګډ"),
            Tr("متفرقه", "Misc", "نور")
        )
        "call" -> listOf(
            Tr("روزانه", "Daily", "ورځنی"),
            Tr("هفته ای", "Weekly", "اونیز"),
            Tr("ماهانه", "Monthly", "میاشتنی"),
            Tr("ترکیبی", "Combo", "ګډ"),
            Tr("شبانه", "Night", "شپنی"),
            Tr("متفرقه", "Misc", "نور")
        )
        else -> emptyList()
    }

    fun forTab(kind: String, index: Int): List<AtomaPackage> = when (kind) {
        "internet" -> when (index) {
            0 -> hourly
            1 -> daily
            2 -> weekly
            3 -> monthly
            4 -> night
            else -> emptyList()
        }
        "sms" -> when (index) {
            0 -> smsDaily
            1 -> smsWeekly
            2 -> smsMonthly
            else -> emptyList()
        }
        "call" -> when (index) {
            0 -> callDaily
            1 -> callWeekly
            2 -> callMonthly
            else -> emptyList()
        }
        else -> emptyList()
    }
}