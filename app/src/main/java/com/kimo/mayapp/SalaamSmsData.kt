package com.kimo.mayapp

object SalaamSmsData {

    const val CHECK_CODE = ""

    val screenTitle = Tr(
        "بسته‌های پیام سلام",
        "Salaam SMS Packages",
        "د سلام د پیغام بستې"
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
        Tr("متفرقه", "Misc", "نور")
    )

    fun forTab(index: Int): List<AfghanPackage> {
        return when (index) {
            0 -> daily
            1 -> weekly
            2 -> monthly
            3 -> combo
            4 -> misc
            else -> emptyList()
        }
    }

    // کمک‌کننده: ساخت بسته با کد (شماره‌گیری)
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
    private val daily = emptyList<AfghanPackage>()

    // ---------------- هفته‌ای ----------------
    private val weekly = emptyList<AfghanPackage>()

    // ---------------- ماهانه ----------------
    private val monthly = listOf(
        pkg("پیامک نامحدود ماهانه", "Unlimited SMS Monthly", "نامحدود پیغامونه میاشتنی", "50",
            "پیامک نامحدود به تمام مخاطبین سلام برای یک ماه.",
            "Unlimited SMS to all Salaam contacts for one month.",
            "د یوې میاشتې لپاره ټولو سلام اړیکو ته نامحدود پیغامونه.", "45")
    )

    // ---------------- ترکیبی ----------------
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

    // ---------------- متفرقه ----------------
    private val misc = emptyList<AfghanPackage>()
}