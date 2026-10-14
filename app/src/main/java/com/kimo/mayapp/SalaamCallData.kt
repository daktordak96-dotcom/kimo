package com.kimo.mayapp

object SalaamCallData {

    const val CHECK_CODE = ""
    const val SMS_NUMBER = "740"

    val screenTitle = Tr(
        "بسته‌های تماس سلام",
        "Salaam Call Packages",
        "د سلام د زنګ بستې"
    )

    val soon = Tr("به‌زودی", "Coming soon", "ژر راځي")
    val btnOff = Tr("غیرفعال‌سازی", "Deactivate", "غیرفعالول")
    val btnCheck = Tr("چک بیلانس", "Check Balance", "بیلانس کتل")
    val btnOn = Tr("فعال‌سازی", "Activate", "فعالول")

    // ترتیب باید با forTab یکی باشد
    val tabs = listOf(
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفته‌ای", "Weekly", "اونیز"),
        Tr("ماهانه", "Monthly", "میاشتنی"),
        Tr("ترکیبی", "Combo", "ګډ"),
        Tr("شبانه", "Night", "شپنی"),
        Tr("متفرقه", "Misc", "نور")
    )

    fun forTab(index: Int): List<AfghanPackage> {
        return when (index) {
            0 -> daily
            1 -> weekly
            2 -> monthly
            3 -> combo
            4 -> night
            5 -> misc
            else -> emptyList()
        }
    }

    // بسته با شماره‌گیری کد
    private fun pkg(
        titleFa: String, titleEn: String, titlePs: String,
        price: String,
        descFa: String, descEn: String, descPs: String,
        code: String
    ): AfghanPackage {
        val on = "*740*$code*1#"
        val off = "*740*$code*9#"
        return AfghanPackage(
            Tr(titleFa, titleEn, titlePs),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "$descFa\nبرای فعال‌سازی $on و برای غیرفعال‌سازی $off را شماره‌گیری کنید.",
                "$descEn\nTo activate dial $on, to deactivate dial $off.",
                "$descPs\nد فعالولو لپاره $on او د غیرفعالولو لپاره $off ډایل کړئ."
            ),
            on = on,
            off = off
        )
    }

    // ---------------- روزانه ----------------
    private val daily = listOf(
        pkg("60 دقیقه به سلام روزانه", "60 Min to Salaam Daily", "60 دقیقې سلام ته ورځنی", "20",
            "60 دقیقه تماس به سلام برای یک روز.", "60 minutes to Salaam for one day.", "د یوې ورځې لپاره سلام ته 60 دقیقې.", "14"),
        pkg("200 دقیقه به سلام روزانه", "200 Min to Salaam Daily", "200 دقیقې سلام ته ورځنی", "30",
            "200 دقیقه تماس به سلام برای یک روز.", "200 minutes to Salaam for one day.", "د یوې ورځې لپاره سلام ته 200 دقیقې.", "15"),
        pkg("300 دقیقه به سلام روزانه", "300 Min to Salaam Daily", "300 دقیقې سلام ته ورځنی", "45",
            "300 دقیقه تماس به سلام برای یک روز.", "300 minutes to Salaam for one day.", "د یوې ورځې لپاره سلام ته 300 دقیقې.", "16"),
        pkg("30 دقیقه به دیگر شبکه‌ها روزانه", "30 Min to Other Daily", "30 دقیقې نورو شبکو ته ورځنی", "25",
            "30 دقیقه تماس به شبکه‌های دیگر برای یک روز.", "30 minutes to other networks for one day.", "د یوې ورځې لپاره نورو شبکو ته 30 دقیقې.", "205"),
        pkg("نامحدود به سلام روزانه", "Unlimited to Salaam Daily", "نامحدود سلام ته ورځنی", "10",
            "تماس نامحدود به سلام برای یک روز.", "Unlimited calls to Salaam for one day.", "د یوې ورځې لپاره سلام ته نامحدود زنګونه.", "9")
    )

    // ---------------- هفته‌ای ----------------
    private val weekly = listOf(
        pkg("500 دقیقه به سلام هفتگی", "500 Min to Salaam Weekly", "500 دقیقې سلام ته اونیز", "89",
            "500 دقیقه تماس به سلام برای یک هفته.", "500 minutes to Salaam for one week.", "د یوې اونۍ لپاره سلام ته 500 دقیقې.", "89"),
        pkg("60 دقیقه به دیگر شبکه‌ها هفتگی", "60 Min to Other Weekly", "60 دقیقې نورو شبکو ته اونیز", "40",
            "60 دقیقه تماس به شبکه‌های دیگر برای یک هفته.", "60 minutes to other networks for one week.", "د یوې اونۍ لپاره نورو شبکو ته 60 دقیقې.", "300")
    )

    // ---------------- ماهانه ----------------
    private val monthly = listOf(
        pkg("1000 دقیقه به سلام ماهانه", "1000 Min to Salaam Monthly", "1000 دقیقې سلام ته میاشتنی", "90",
            "1000 دقیقه تماس به سلام برای یک ماه.", "1000 minutes to Salaam for one month.", "د یوې میاشتې لپاره سلام ته 1000 دقیقې.", "90"),
        // این بسته با پیامک A 1500 / D 1500 به 740 کار می‌کند
        AfghanPackage(
            Tr("3000 دقیقه به سلام ماهانه", "3000 Min to Salaam Monthly", "3000 دقیقې سلام ته میاشتنی"),
            Tr("299 افغانی", "299 AFN", "299 افغانۍ"),
            Tr(
                "3000 دقیقه تماس به سلام برای یک ماه.\nبرای فعال‌سازی A 1500 و برای غیرفعال‌سازی D 1500 را به 740 ارسال نمائید.",
                "3000 minutes to Salaam for one month.\nTo activate send A 1500, to deactivate send D 1500 to 740.",
                "د یوې میاشتې لپاره سلام ته 3000 دقیقې.\nد فعالولو لپاره A 1500 او د غیرفعالولو لپاره D 1500 740 ته ولیږئ."
            ),
            on = "A 1500",
            off = "D 1500"
        ),
        pkg("140 دقیقه به دیگر شبکه‌ها ماهانه", "140 Min to Other Monthly", "140 دقیقې نورو شبکو ته میاشتنی", "90",
            "140 دقیقه تماس به شبکه‌های دیگر برای یک ماه.", "140 minutes to other networks for one month.", "د یوې میاشتې لپاره نورو شبکو ته 140 دقیقې.", "900"),
        pkg("نامحدود به سلام ماهانه", "Unlimited to Salaam Monthly", "نامحدود سلام ته میاشتنی", "100",
            "تماس نامحدود به سلام برای یک ماه.", "Unlimited calls to Salaam for one month.", "د یوې میاشتې لپاره سلام ته نامحدود زنګونه.", "100")
    )

    // ---------------- ترکیبی (مشترک با پیام) ----------------
    private val combo = listOf(
        pkg("120 دقیقه + 120 پیامک + 200 ام بی", "120 Min + 120 SMS + 200 MB", "120 دقیقې + 120 پیغامونه + 200 ام بي", "13",
            "120 دقیقه تماس + 120 پیامک + 200 ام بی اینترنت برای یک هفته. همه به سلام.",
            "120 minutes + 120 SMS + 200 MB internet for one week. All to Salaam.",
            "د یوې اونۍ لپاره 120 دقیقې + 120 پیغامونه + 200 ام بي انټرنیټ. ټول سلام ته.", "13"),
        pkg("200 دقیقه + 200 پیامک + 100 به دیگران", "200 Min + 200 SMS + 100 Other", "200 دقیقې + 200 پیغامونه + 100 نور", "85",
            "200 دقیقه به سلام + 200 پیامک + 100 دقیقه به شبکه‌های دیگر برای یک هفته.",
            "200 minutes to Salaam + 200 SMS + 100 minutes to other networks for one week.",
            "د یوې اونۍ لپاره سلام ته 200 دقیقې + 200 پیغامونه + نورو شبکو ته 100 دقیقې.", "85"),
        pkg("1000 به سلام + 300 به دیگران", "1000 Salaam + 300 Other", "1000 سلام + 300 نور", "270",
            "1000 دقیقه به سلام + 300 دقیقه به شبکه‌های دیگر برای یک ماه.",
            "1000 minutes to Salaam + 300 minutes to other networks for one month.",
            "د یوې میاشتې لپاره سلام ته 1000 دقیقې + نورو شبکو ته 300 دقیقې.", "270"),
        pkg("نامحدود به سلام + 300 دیگر + 10 جی بی", "Unlimited Salaam + 300 + 10 GB", "نامحدود سلام + 300 + 10 جي بي", "700",
            "تماس نامحدود به سلام + 300 دقیقه به شبکه‌های دیگر + 10 جی بی اینترنت برای یک ماه.",
            "Unlimited calls to Salaam + 300 minutes to others + 10 GB internet for one month.",
            "د یوې میاشتې لپاره سلام ته نامحدود زنګونه + نورو شبکو ته 300 دقیقې + 10 جي بي انټرنیټ.", "700")
    )

    // ---------------- شبانه ----------------
    private val night = emptyList<AfghanPackage>()

    // ---------------- متفرقه ----------------
    private val misc = emptyList<AfghanPackage>()
}