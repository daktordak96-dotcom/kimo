package com.kimo.mayapp

import android.content.Context

data class CallText(val title: String, val price: String, val intro: String)

object CallStrings {

    // جمله‌های مشترک همه‌ی کارت‌ها
    private val common = mapOf(
        "renew" to mapOf(
            "fa" to "تمدید خودکار موجود است.",
            "en" to "Auto-renewal is available.",
            "ps" to "اتوماتیک تمدید شته."
        ),
        "activate_line" to mapOf(
            "fa" to "برای فعال سازی {on} و برای غیرفعال سازی {off} را به 3378 ارسال نمائید.",
            "en" to "To activate send {on} and to deactivate send {off} to 3378.",
            "ps" to "د فعالولو لپاره {on} او د غیر فعالولو لپاره {off} ته 3378 ولیږئ."
        ),
        "check_line" to mapOf(
            "fa" to "برای بررسی موجودی 505 را به 606 ارسال نمائید یا به 888 زنگ بزنید.",
            "en" to "To check your balance send 505 to 606 or call 888.",
            "ps" to "د بیلانس د کتلو لپاره 505 ته 606 ولیږئ یا 888 ته زنګ ووهئ."
        )
    )

    // ترجمه‌ی هر بسته، با کلید «متن فعال‌سازی»
    private val packages = mapOf(

        // ---------- روزانه ----------
        "900" to mapOf(
            "fa" to CallText("100 دقیقه روزانه", "25 افغانی",
                "بسته 100 دقیقه تماس اتصالات برای یک روز به قیمت 25 افغانی."),
            "en" to CallText("100 Minutes Daily", "25 AFN",
                "100 Etisalat call minutes for one day, price 25 AFN."),
            "ps" to CallText("100 ورځنۍ دقیقې", "25 افغانۍ",
                "د اتصالاتو 100 د زنګ دقیقې د یوې ورځې لپاره، بیه 25 افغانۍ.")
        ),
        "801" to mapOf(
            "fa" to CallText("50 دقیقه روزانه", "15 افغانی",
                "بسته 50 دقیقه تماس اتصالات برای یک روز به قیمت 15 افغانی."),
            "en" to CallText("50 Minutes Daily", "15 AFN",
                "50 Etisalat call minutes for one day, price 15 AFN."),
            "ps" to CallText("50 ورځنۍ دقیقې", "15 افغانۍ",
                "د اتصالاتو 50 د زنګ دقیقې د یوې ورځې لپاره، بیه 15 افغانۍ.")
        ),

        // ---------- هفتگی ----------
        "W75" to mapOf(
            "fa" to CallText("450 دقیقه هفتگی", "80 افغانی",
                "بسته 450 دقیقه تماس اتصالات برای 7 روز به قیمت 80 افغانی."),
            "en" to CallText("450 Minutes Weekly", "80 AFN",
                "450 Etisalat call minutes for 7 days, price 80 AFN."),
            "ps" to CallText("450 اونیزې دقیقې", "80 افغانۍ",
                "د اتصالاتو 450 د زنګ دقیقې د 7 ورځو لپاره، بیه 80 افغانۍ.")
        ),
        "E55" to mapOf(
            "fa" to CallText("300 دقیقه هفتگی", "55 افغانی",
                "بسته 300 دقیقه تماس اتصالات برای 7 روز به قیمت 55 افغانی."),
            "en" to CallText("300 Minutes Weekly", "55 AFN",
                "300 Etisalat call minutes for 7 days, price 55 AFN."),
            "ps" to CallText("300 اونیزې دقیقې", "55 افغانۍ",
                "د اتصالاتو 300 د زنګ دقیقې د 7 ورځو لپاره، بیه 55 افغانۍ.")
        ),

        // ---------- ماهانه ----------
        "U300" to mapOf(
            "fa" to CallText("تماس نامحدود اتصالات", "300 افغانی",
                "بسته تماس نامحدود اتصالات + 500 ام بی انترنت برای 30 روز به قیمت 300 افغانی."),
            "en" to CallText("Unlimited Etisalat Calls", "300 AFN",
                "Unlimited Etisalat calls + 500 MB internet for 30 days, price 300 AFN."),
            "ps" to CallText("د اتصالاتو نامحدود زنګونه", "300 افغانۍ",
                "د اتصالاتو نامحدود زنګونه + 500 MB انټرنټ د 30 ورځو لپاره، بیه 300 افغانۍ.")
        ),
        "5000" to mapOf(
            "fa" to CallText("5000 دقیقه تقسیم شده", "599 افغانی",
                "بسته 5000 دقیقه تماس اتصالات (4000 دقیقه از ساعت 10 شب تا 6 صبح + 1000 دقیقه از 6 صبح تا 10 شب) برای 30 روز به قیمت 599 افغانی."),
            "en" to CallText("5000 Split Minutes", "599 AFN",
                "5000 Etisalat call minutes (4000 minutes from 10 PM to 6 AM + 1000 minutes from 6 AM to 10 PM) for 30 days, price 599 AFN."),
            "ps" to CallText("5000 وېشل شوې دقیقې", "599 افغانۍ",
                "د اتصالاتو 5000 د زنګ دقیقې (4000 دقیقې له شپې 10 بجو تر سهار 6 بجو + 1000 دقیقې له سهار 6 بجو تر شپې 10 بجو) د 30 ورځو لپاره، بیه 599 افغانۍ.")
        ),
        "M220" to mapOf(
            "fa" to CallText("1750 دقیقه ماهانه", "220 افغانی",
                "بسته 1750 دقیقه تماس اتصالات برای 30 روز به قیمت 220 افغانی."),
            "en" to CallText("1750 Minutes Monthly", "220 AFN",
                "1750 Etisalat call minutes for 30 days, price 220 AFN."),
            "ps" to CallText("1750 میاشتنۍ دقیقې", "220 افغانۍ",
                "د اتصالاتو 1750 د زنګ دقیقې د 30 ورځو لپاره، بیه 220 افغانۍ.")
        ),
        "M180" to mapOf(
            "fa" to CallText("1000 دقیقه استاندارد", "180 افغانی",
                "بسته 1000 دقیقه تماس اتصالات برای 30 روز به قیمت 180 افغانی."),
            "en" to CallText("1000 Standard Minutes", "180 AFN",
                "1000 Etisalat call minutes for 30 days, price 180 AFN."),
            "ps" to CallText("1000 معیاري دقیقې", "180 افغانۍ",
                "د اتصالاتو 1000 د زنګ دقیقې د 30 ورځو لپاره، بیه 180 افغانۍ.")
        ),
        "M135" to mapOf(
            "fa" to CallText("650 دقیقه ماهانه", "135 افغانی",
                "بسته 650 دقیقه تماس اتصالات برای 30 روز به قیمت 135 افغانی."),
            "en" to CallText("650 Minutes Monthly", "135 AFN",
                "650 Etisalat call minutes for 30 days, price 135 AFN."),
            "ps" to CallText("650 میاشتنۍ دقیقې", "135 افغانۍ",
                "د اتصالاتو 650 د زنګ دقیقې د 30 ورځو لپاره، بیه 135 افغانۍ.")
        ),
        "R30" to mapOf(
            "fa" to CallText("نرخ 1 افغانی در دقیقه", "30 افغانی",
                "بسته نرخ 1 افغانی در دقیقه برای تماس با اتصالات (صورتحساب 30 ثانیه‌ای) برای 30 روز به قیمت 30 افغانی."),
            "en" to CallText("1 AFN per Minute Rate", "30 AFN",
                "1 AFN per minute rate for calls to Etisalat (billed per 30 seconds) for 30 days, price 30 AFN."),
            "ps" to CallText("په یوه دقیقه کې 1 افغانۍ", "30 افغانۍ",
                "د اتصالاتو سره د زنګ لپاره په یوه دقیقه کې 1 افغانۍ (د 30 ثانیو بل) د 30 ورځو لپاره، بیه 30 افغانۍ.")
        ),

        // ---------- ترکیبی ----------
        "M200" to mapOf(
            "fa" to CallText("1000 دقیقه پریمیوم", "200 افغانی",
                "بسته 1000 دقیقه تماس اتصالات + 200 ام بی انترنت برای 30 روز به قیمت 200 افغانی."),
            "en" to CallText("1000 Premium Minutes", "200 AFN",
                "1000 Etisalat call minutes + 200 MB internet for 30 days, price 200 AFN."),
            "ps" to CallText("1000 پریمیم دقیقې", "200 افغانۍ",
                "د اتصالاتو 1000 د زنګ دقیقې + 200 MB انټرنټ د 30 ورځو لپاره، بیه 200 افغانۍ.")
        ),
        "700" to mapOf(
            "fa" to CallText("125 دقیقه + 50 پیامک", "40 افغانی",
                "بسته 125 دقیقه تماس اتصالات + 50 پیامک برای یک روز به قیمت 40 افغانی."),
            "en" to CallText("125 Minutes + 50 SMS", "40 AFN",
                "125 Etisalat call minutes + 50 SMS for one day, price 40 AFN."),
            "ps" to CallText("125 دقیقې + 50 پیغامونه", "40 افغانۍ",
                "د اتصالاتو 125 د زنګ دقیقې + 50 پیغامونه د یوې ورځې لپاره، بیه 40 افغانۍ.")
        ),

        // ---------- شبانه ----------
        "N10" to mapOf(
            "fa" to CallText("250 دقیقه شبانه", "10 افغانی",
                "بسته 250 دقیقه تماس اتصالات (از ساعت 10 شب تا 5:59 صبح) برای یک روز به قیمت 10 افغانی."),
            "en" to CallText("250 Night Minutes", "10 AFN",
                "250 Etisalat call minutes (from 10 PM to 5:59 AM) for one day, price 10 AFN."),
            "ps" to CallText("250 شپنۍ دقیقې", "10 افغانۍ",
                "د اتصالاتو 250 د زنګ دقیقې (له شپې 10 بجو تر سهار 5:59 بجو) د یوې ورځې لپاره، بیه 10 افغانۍ.")
        ),
        "199" to mapOf(
            "fa" to CallText("4000 دقیقه شبانه", "199 افغانی",
                "بسته 4000 دقیقه تماس اتصالات (از ساعت 10 شب تا 5:59 صبح) برای 30 روز به قیمت 199 افغانی."),
            "en" to CallText("4000 Night Minutes", "199 AFN",
                "4000 Etisalat call minutes (from 10 PM to 5:59 AM) for 30 days, price 199 AFN."),
            "ps" to CallText("4000 شپنۍ دقیقې", "199 افغانۍ",
                "د اتصالاتو 4000 د زنګ دقیقې (له شپې 10 بجو تر سهار 5:59 بجو) د 30 ورځو لپاره، بیه 199 افغانۍ.")
        ),
        "N35" to mapOf(
            "fa" to CallText("750 دقیقه شبانه", "35 افغانی",
                "بسته 750 دقیقه از ساعت 10 شب تا 6 صبح برای 7 روز به قیمت 35 افغانی."),
            "en" to CallText("750 Night Minutes", "35 AFN",
                "750 minutes from 10 PM to 6 AM for 7 days, price 35 AFN."),
            "ps" to CallText("750 شپنۍ دقیقې", "35 افغانۍ",
                "له شپې 10 بجو تر سهار 6 بجو 750 دقیقې د 7 ورځو لپاره، بیه 35 افغانۍ.")
        ),

        // ---------- متفرقه ----------
        "H10" to mapOf(
            "fa" to CallText("60 دقیقه ساعتی", "10 افغانی",
                "بسته 60 دقیقه تماس اتصالات ساعتی برای یک روز به قیمت 10 افغانی."),
            "en" to CallText("60 Minutes Hourly", "10 AFN",
                "60 Etisalat call minutes (hourly package) for one day, price 10 AFN."),
            "ps" to CallText("60 ساعتي دقیقې", "10 افغانۍ",
                "د اتصالاتو 60 د زنګ دقیقې (ساعتي بسته) د یوې ورځې لپاره، بیه 10 افغانۍ.")
        ),
        "80" to mapOf(
            "fa" to CallText("400 دقیقه دو هفته‌ای", "80 افغانی",
                "بسته 400 دقیقه تماس اتصالات برای 14 روز به قیمت 80 افغانی."),
            "en" to CallText("400 Minutes Two Weeks", "80 AFN",
                "400 Etisalat call minutes for 14 days, price 80 AFN."),
            "ps" to CallText("400 دوه اونیزې دقیقې", "80 افغانۍ",
                "د اتصالاتو 400 د زنګ دقیقې د 14 ورځو لپاره، بیه 80 افغانۍ.")
        ),
        "F20" to mapOf(
            "fa" to CallText("جمعه پریمیوم", "25 افغانی",
                "بسته 100 دقیقه تماس اتصالات در روزهای جمعه + تماس رایگان از 12 شب تا 6 صبح برای روزهای جمعه به قیمت 25 افغانی."),
            "en" to CallText("Friday Premium", "25 AFN",
                "100 Etisalat call minutes on Fridays + free calls from 12 AM to 6 AM on Fridays, price 25 AFN."),
            "ps" to CallText("د جمعې پریمیم", "25 افغانۍ",
                "د جمعې په ورځو کې د اتصالاتو 100 د زنګ دقیقې + د جمعې په ورځو کې له شپې 12 بجو تر سهار 6 بجو وړیا زنګونه، بیه 25 افغانۍ.")
        ),
        "F15" to mapOf(
            "fa" to CallText("جمعه استاندارد", "15 افغانی",
                "بسته 90 دقیقه تماس اتصالات در روزهای جمعه برای روزهای جمعه به قیمت 15 افغانی."),
            "en" to CallText("Friday Standard", "15 AFN",
                "90 Etisalat call minutes on Fridays, price 15 AFN."),
            "ps" to CallText("د جمعې معیاري", "15 افغانۍ",
                "د جمعې په ورځو کې د اتصالاتو 90 د زنګ دقیقې، بیه 15 افغانۍ.")
        )
    )

    fun find(context: Context, activateText: String): CallText? =
        packages[activateText]?.get(Strings.currentLang(context))

    fun description(
        context: Context,
        text: CallText,
        activate: String,
        deactivate: String,
        autoRenew: Boolean
    ): String {
        val lang = Strings.currentLang(context)
        fun c(key: String) = common[key]?.get(lang) ?: common[key]?.get("fa") ?: key

        val sb = StringBuilder(text.intro)
        if (autoRenew) sb.append("\n").append(c("renew"))
        sb.append("\n").append(
            c("activate_line").replace("{on}", activate).replace("{off}", deactivate)
        )
        sb.append("\n").append(c("check_line"))
        return sb.toString()
    }
}