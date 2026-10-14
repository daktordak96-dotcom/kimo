package com.kimo.mayapp

object SalaamInternetData {

    const val CHECK_CODE = ""

    val screenTitle = Tr(
        "بسته‌های اینترنتی سلام",
        "Salaam Internet Packages",
        "د سلام انټرنیټ بستې"
    )

    val soon = Tr("به‌زودی", "Coming soon", "ژر راځي")
    val btnOff = Tr("غیرفعال‌سازی", "Deactivate", "غیرفعالول")
    val btnCheck = Tr("چک بیلانس", "Check Balance", "بیلانس کتل")
    val btnOn = Tr("فعال‌سازی", "Activate", "فعالول")

    val tabs = listOf(
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفته‌ای", "Weekly", "اونیز"),
        Tr("ماهانه", "Monthly", "میاشتنی"),
        Tr("شبانه", "Night", "شپنی"),
        Tr("ترکیبی", "Combo", "ګډ"),
        Tr("متفرقه", "Misc", "نور")
    )

    fun forTab(index: Int): List<AfghanPackage> {
        return when (index) {
            0 -> daily
            1 -> weekly
            2 -> monthly
            3 -> night
            4 -> combo
            5 -> misc
            else -> emptyList()
        }
    }

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
        pkg("100 ام بی روزانه", "100 MB Daily", "100 ام بي ورځنی", "10",
            "100 ام بی اینترنت برای یک روز.", "100 MB internet for one day.", "د یوې ورځې لپاره 100 ام بي انټرنیټ.", "11"),
        pkg("1 جی بی روزانه", "1 GB Daily", "1 جي بي ورځنی", "45",
            "1 جی بی اینترنت برای یک روز.", "1 GB internet for one day.", "د یوې ورځې لپاره 1 جي بي انټرنیټ.", "12")
    )

    // ---------------- هفته‌ای ----------------
    private val weekly = listOf(
        pkg("250 ام بی دو هفته‌ای", "250 MB Biweekly", "250 ام بي دوه اونیز", "28",
            "250 ام بی اینترنت برای 2 هفته.", "250 MB internet for 2 weeks.", "د 2 اونیو لپاره 250 ام بي انټرنیټ.", "250"),
        pkg("2 جی بی هفتگی", "2 GB Weekly", "2 جي بي اونیز", "90",
            "2 جی بی اینترنت برای یک هفته.", "2 GB internet for one week.", "د یوې اونۍ لپاره 2 جي بي انټرنیټ.", "91")
    )

    // ---------------- ماهانه ----------------
    private val monthly = listOf(
        pkg("1 جی بی ماهانه", "1 GB Monthly", "1 جي بي میاشتنی", "90",
            "1 جی بی اینترنت برای یک ماه.", "1 GB internet for one month.", "د یوې میاشتې لپاره 1 جي بي انټرنیټ.", "1"),
        pkg("2 جی بی ماهانه", "2 GB Monthly", "2 جي بي میاشتنی", "140",
            "2 جی بی اینترنت برای یک ماه.", "2 GB internet for one month.", "د یوې میاشتې لپاره 2 جي بي انټرنیټ.", "2"),
        pkg("3 جی بی ماهانه", "3 GB Monthly", "3 جي بي میاشتنی", "184",
            "3 جی بی اینترنت برای یک ماه.", "3 GB internet for one month.", "د یوې میاشتې لپاره 3 جي بي انټرنیټ.", "3"),
        pkg("4 جی بی ماهانه", "4 GB Monthly", "4 جي بي میاشتنی", "230",
            "4 جی بی اینترنت برای یک ماه.", "4 GB internet for one month.", "د یوې میاشتې لپاره 4 جي بي انټرنیټ.", "4"),
        pkg("5 جی بی ماهانه", "5 GB Monthly", "5 جي بي میاشتنی", "276",
            "5 جی بی اینترنت برای یک ماه.", "5 GB internet for one month.", "د یوې میاشتې لپاره 5 جي بي انټرنیټ.", "5"),
        pkg("10 جی بی ماهانه", "10 GB Monthly", "10 جي بي میاشتنی", "415",
            "10 جی بی اینترنت برای یک ماه.", "10 GB internet for one month.", "د یوې میاشتې لپاره 10 جي بي انټرنیټ.", "10"),
        pkg("20 جی بی ماهانه", "20 GB Monthly", "20 جي بي میاشتنی", "800",
            "20 جی بی اینترنت برای یک ماه.", "20 GB internet for one month.", "د یوې میاشتې لپاره 20 جي بي انټرنیټ.", "20"),
        pkg("30 جی بی ماهانه", "30 GB Monthly", "30 جي بي میاشتنی", "999",
            "30 جی بی اینترنت برای یک ماه.", "30 GB internet for one month.", "د یوې میاشتې لپاره 30 جي بي انټرنیټ.", "30"),
        pkg("50 جی بی دو ماهه", "50 GB Two Months", "50 جي بي دوه میاشتنی", "1499",
            "50 جی بی اینترنت برای 2 ماه.", "50 GB internet for 2 months.", "د 2 میاشتو لپاره 50 جي بي انټرنیټ.", "50"),
        pkg("60 جی بی شش ماهه", "60 GB Six Months", "60 جي بي شپږ میاشتنی", "2250",
            "60 جی بی اینترنت برای 6 ماه.", "60 GB internet for 6 months.", "د 6 میاشتو لپاره 60 جي بي انټرنیټ.", "60")
    )

    // ---------------- شبانه ----------------
    private val night = listOf(
        pkg("1.5 جی بی شبانه", "1.5 GB Night", "1.5 جي بي شپنی", "50",
            "1.5 جی بی اینترنت برای یک هفته شبانه. از 11 شب تا 7 صبح.",
            "1.5 GB internet for one week at night. From 11 PM to 7 AM.",
            "د یوې اونۍ لپاره 1.5 جي بي شپنی انټرنیټ. له 11 شپې تر 7 سهار.", "95"),
        pkg("3 جی بی شبانه", "3 GB Night", "3 جي بي شپنی", "90",
            "3 جی بی اینترنت برای یک ماه شبانه. از 11 شب تا 7 صبح.",
            "3 GB internet for one month at night. From 11 PM to 7 AM.",
            "د یوې میاشتې لپاره 3 جي بي شپنی انټرنیټ. له 11 شپې تر 7 سهار.", "93"),
        pkg("7 جی بی شبانه", "7 GB Night", "7 جي بي شپنی", "200",
            "7 جی بی اینترنت برای یک ماه شبانه. از 11 شب تا 7 صبح.",
            "7 GB internet for one month at night. From 11 PM to 7 AM.",
            "د یوې میاشتې لپاره 7 جي بي شپنی انټرنیټ. له 11 شپې تر 7 سهار.", "97"),
        pkg("13 جی بی شبانه + 200 دقیقه", "13 GB Night + 200 Min", "13 جي بي شپنی + 200 دقیقې", "350",
            "13 جی بی اینترنت شبانه + 200 دقیقه به سلام برای یک ماه. از 11 شب تا 7 صبح.",
            "13 GB night internet + 200 minutes to Salaam for one month. From 11 PM to 7 AM.",
            "د یوې میاشتې لپاره 13 جي بي شپنی انټرنیټ + سلام ته 200 دقیقې. له 11 شپې تر 7 سهار.", "913")
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