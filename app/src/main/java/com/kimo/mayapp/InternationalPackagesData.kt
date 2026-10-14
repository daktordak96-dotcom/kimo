package com.kimo.mayapp

import android.content.Context

// متن سه‌زبانه
class Tr(val fa: String, val en: String, val ps: String) {
    fun get(ctx: Context) = when (Strings.currentLang(ctx)) {
        "en" -> en
        "ps" -> ps
        else -> fa
    }
}

class IntlPackage(
    val title: Tr,
    val price: Tr,
    val intro: Tr,
    val on: String,
    val off: String,
    val checkLine: Boolean = false,   // جمله‌ی بررسی موجودی
    val checkButton: Boolean = false  // دکمه‌ی چک بیلانس
) {
    fun description(ctx: Context): String {
        val sb = StringBuilder(intro.get(ctx))
        sb.append("\n").append(PRESS.get(ctx))
        sb.append("\n").append(SEND.get(ctx).replace("{on}", on).replace("{off}", off))
        if (checkLine) sb.append("\n").append(if (checkButton) CHECK_FULL.get(ctx) else CHECK_SHORT.get(ctx))
        return sb.toString()
    }

    companion object {
        val PRESS = Tr(
            "برای فعال سازی روی دکمه سبز و برای غیرفعال سازی روی دکمه سرخ فشار دهید.",
            "To activate press the green button and to deactivate press the red button.",
            "د فعالولو لپاره پر شنه تڼۍ او د غیر فعالولو لپاره پر سره تڼۍ کلیک وکړئ."
        )
        val SEND = Tr(
            "برای فعال سازی {on} و برای غیر فعال سازی {off} را به شماره 3378 ارسال نمائید.",
            "To activate send {on} and to deactivate send {off} to 3378.",
            "د فعالولو لپاره {on} او د غیر فعالولو لپاره {off} شمېرې 3378 ته ولیږئ."
        )
        val CHECK_FULL = Tr(
            "برای بررسی موجودی 505 را به 606 ارسال نمائید یا به 888 زنگ بزنید.",
            "To check your balance send 505 to 606 or call 888.",
            "د بیلانس د کتلو لپاره 505 ته 606 ولیږئ یا 888 ته زنګ ووهئ."
        )
        val CHECK_SHORT = Tr(
            "برای دیدن باقیمانده بسته 505 را به 606 بفرستید.",
            "To see your remaining package send 505 to 606.",
            "د بستې د پاتې کتلو لپاره 505 ته 606 ولیږئ."
        )
    }
}

object InternationalPackagesData {

    val screenTitle = Tr("بسته های بین المللی اتصالات", "Etisalat International Packages", "د اتصالاتو نړیوال بستې")

    val all = listOf(
        IntlPackage(
            Tr("بسته تماس بین المللی روزانه", "Daily International Call Package", "نړیوال ورځنی زنګ بسته"),
            Tr("35 افغانی", "35 AFN", "35 افغانۍ"),
            Tr("10 دقیقه تماس به پاکستان، هند، امریکا و کانادا برای یک روز به قیمت 35 افغانی.",
                "10 minutes of calls to Pakistan, India, USA and Canada for one day, price 35 AFN.",
                "د پاکستان، هند، امریکا او کاناډا ته د 10 دقیقو زنګ د یوې ورځې لپاره، بیه 35 افغانۍ."),
            on = "35", off = "PU 35"
        ),
        IntlPackage(
            Tr("بسته تماس بین المللی هفته وار", "Weekly International Call Package", "نړیوال اونیز زنګ بسته"),
            Tr("75 افغانی", "75 AFN", "75 افغانۍ"),
            Tr("25 دقیقه تماس به پاکستان، هند، امریکا و کانادا برای یک هفته به قیمت 75 افغانی.",
                "25 minutes of calls to Pakistan, India, USA and Canada for one week, price 75 AFN.",
                "د پاکستان، هند، امریکا او کاناډا ته د 25 دقیقو زنګ د یوې اونۍ لپاره، بیه 75 افغانۍ."),
            on = "w25", off = "PU w25"
        ),
        IntlPackage(
            Tr("نرخ هفتگی پاکستان", "Pakistan Weekly Rate", "د پاکستان اونیز نرخ"),
            Tr("10 افغانی", "10 AFN", "10 افغانۍ"),
            Tr("بسته نرخ 3 افغانی در دقیقه برای تماس با پاکستان برای 7 روز به قیمت 10 افغانی.",
                "3 AFN per minute rate for calls to Pakistan for 7 days, price 10 AFN.",
                "د پاکستان سره د زنګ لپاره په یوه دقیقه کې 3 افغانۍ د 7 ورځو لپاره، بیه 10 افغانۍ."),
            on = "P7", off = "PU P7", checkLine = true, checkButton = true
        ),
        IntlPackage(
            Tr("بسته تماس بین المللی ماهانه", "Monthly International Call Package", "نړیوال میاشتنی زنګ بسته"),
            Tr("150 افغانی", "150 AFN", "150 افغانۍ"),
            Tr("50 دقیقه تماس به پاکستان، هند، امریکا و کانادا برای یک ماه به قیمت 150 افغانی.",
                "50 minutes of calls to Pakistan, India, USA and Canada for one month, price 150 AFN.",
                "د پاکستان، هند، امریکا او کاناډا ته د 50 دقیقو زنګ د یوې میاشتې لپاره، بیه 150 افغانۍ."),
            on = "50", off = "PU 50", checkLine = true
        ),
        IntlPackage(
            Tr("نرخ ماهانه پاکستان", "Pakistan Monthly Rate", "د پاکستان میاشتنی نرخ"),
            Tr("10 افغانی", "10 AFN", "10 افغانۍ"),
            Tr("بسته نرخ 3 افغانی در دقیقه برای تماس با پاکستان برای 30 روز به قیمت 10 افغانی.",
                "3 AFN per minute rate for calls to Pakistan for 30 days, price 10 AFN.",
                "د پاکستان سره د زنګ لپاره په یوه دقیقه کې 3 افغانۍ د 30 ورځو لپاره، بیه 10 افغانۍ."),
            on = "P30", off = "PU P30", checkLine = true, checkButton = true
        )
    )
}