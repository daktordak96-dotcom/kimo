package com.kimo.mayapp

object RoshanInternetData {

    // شمارهٔ ارسال پیامک فعال‌سازی روشن
    const val SMS_NUMBER = "555"

    // کد چک بیلانس روشن را اینجا بگذارید. تا خالی است دکمهٔ چک نمایش داده نمی‌شود.
    const val CHECK_CODE = ""

    // بسته‌هایی که کدشان با این شروع شود، با زنگ زدن فعال می‌شوند (نه پیامک)
    const val DIAL_PREFIX = "tel:"

    val screenTitle = Tr(
        "بسته‌های اینترنتی روشن",
        "Roshan Internet Packages",
        "د روشن انټرنیټ بستې"
    )

    val soon = Tr("به‌زودی", "Coming soon", "ژر راځي")
    val btnOff = Tr("غیرفعال‌سازی", "Deactivate", "غیرفعالول")
    val btnCheck = Tr("چک بیلانس", "Check Balance", "بیلانس کتل")
    val btnOn = Tr("فعال‌سازی", "Activate", "فعالول")

    // ترتیب باید با forTab یکی باشد
    val tabs = listOf(
        Tr("ساعتی", "Hourly", "ساعتي"),
        Tr("شبانه", "Night", "شپنی"),
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفتگی", "Weekly", "اونیز"),
        Tr("ماهانه", "Monthly", "میاشتنی"),
        Tr("ترکیبی", "Combo", "ګډ"),
        Tr("متفرقه", "Misc", "نور")
    )

    fun forTab(index: Int): List<AfghanPackage> {
        return when (index) {
            0 -> hourly
            1 -> night
            2 -> daily
            3 -> weekly
            4 -> monthly
            5 -> combo
            6 -> misc
            else -> emptyList()
        }
    }

    // ---------------- ساعتی ----------------
    private val hourly = listOf(
        AfghanPackage(
            Tr("512 ام بی ساعتی", "512 MB Hourly", "512 ميګابایټ ساعتي"),
            Tr("15 افغانی", "15 AFN", "15 افغانۍ"),
            Tr(
                "512 ام بی برای یک ساعت به قیمت 15 افغانی.\nبرای فعال‌سازی H15 را به شماره 555 ارسال نمائید.",
                "512 MB for one hour, price 15 AFN.\nTo activate, send H15 to 555.",
                "د یوې ساعت لپاره 512 ميګابایټ، بیه 15 افغانۍ.\nد فعالولو لپاره H15 555 ته ولیږئ."
            ),
            on = "H15"
        ),
        AfghanPackage(
            Tr("1 جی بی ساعتی", "1 GB Hourly", "1 جي بي ساعتي"),
            Tr("20 افغانی", "20 AFN", "20 افغانۍ"),
            Tr(
                "1 جی بی برای یک ساعت به قیمت 20 افغانی.\nبرای فعال‌سازی H20 را به شماره 555 ارسال نمائید.",
                "1 GB for one hour, price 20 AFN.\nTo activate, send H20 to 555.",
                "د یوې ساعت لپاره 1 جي بي، بیه 20 افغانۍ.\nد فعالولو لپاره H20 555 ته ولیږئ."
            ),
            on = "H20"
        ),
        AfghanPackage(
            Tr("1.5 جی بی ساعتی", "1.5 GB Hourly", "1.5 جي بي ساعتي"),
            Tr("30 افغانی", "30 AFN", "30 افغانۍ"),
            Tr(
                "1.5 جی بی برای یک ساعت به قیمت 30 افغانی.\nبرای فعال‌سازی H30 را به شماره 555 ارسال نمائید.",
                "1.5 GB for one hour, price 30 AFN.\nTo activate, send H30 to 555.",
                "د یوې ساعت لپاره 1.5 جي بي، بیه 30 افغانۍ.\nد فعالولو لپاره H30 555 ته ولیږئ."
            ),
            on = "H30"
        )
    )

    // ---------------- شبانه ----------------
    private val night = listOf(
        AfghanPackage(
            Tr("3 جی بی شبانه", "3 GB Night", "3 جي بي شپنی"),
            Tr("45 افغانی", "45 AFN", "45 افغانۍ"),
            Tr(
                "3 جی بی اینترنت برای یک شب به قیمت 45 افغانی.\nبرای فعال‌سازی N45 و برای غیرفعال‌سازی UN45 را به شماره 555 ارسال نمائید.\nاز ساعت یازده شب تا هفت صبح.",
                "3 GB internet for one night, price 45 AFN.\nTo activate send N45, to deactivate send UN45 to 555.\nFrom 11 PM to 7 AM.",
                "د یوې شپې لپاره 3 جي بي انټرنیټ، بیه 45 افغانۍ.\nد فعالولو لپاره N45 او د غیرفعالولو لپاره UN45 555 ته ولیږئ.\nله 11 شپې تر 7 سهار."
            ),
            on = "N45",
            off = "UN45"
        ),
        AfghanPackage(
            Tr("10 جی بی شبانه", "10 GB Night", "10 جي بي شپنی"),
            Tr("225 افغانی", "225 AFN", "225 افغانۍ"),
            Tr(
                "10 جی بی اینترنت برای یک شب به قیمت 225 افغانی.\nبرای فعال‌سازی N225 و برای غیرفعال‌سازی UN225 را به شماره 555 ارسال نمائید.\nاز ساعت یازده شب تا هفت صبح.",
                "10 GB internet for one night, price 225 AFN.\nTo activate send N225, to deactivate send UN225 to 555.\nFrom 11 PM to 7 AM.",
                "د یوې شپې لپاره 10 جي بي انټرنیټ، بیه 225 افغانۍ.\nد فعالولو لپاره N225 او د غیرفعالولو لپاره UN225 555 ته ولیږئ.\nله 11 شپې تر 7 سهار."
            ),
            on = "N225",
            off = "UN225"
        ),
        AfghanPackage(
            Tr("30 جی بی شبانه ماهانه", "30 GB Night Monthly", "30 جي بي شپنی میاشتنی"),
            Tr("450 افغانی", "450 AFN", "450 افغانۍ"),
            Tr(
                "30 جی بی اینترنت برای یک ماه به قیمت 450 افغانی.\nبرای فعال‌سازی N450 و برای غیرفعال‌سازی UN450 را به شماره 555 ارسال نمائید.\nاز ساعت یازده شب تا هفت صبح.",
                "30 GB internet for one month, price 450 AFN.\nTo activate send N450, to deactivate send UN450 to 555.\nFrom 11 PM to 7 AM.",
                "د یوې میاشتې لپاره 30 جي بي انټرنیټ، بیه 450 افغانۍ.\nد فعالولو لپاره N450 او د غیرفعالولو لپاره UN450 555 ته ولیږئ.\nله 11 شپې تر 7 سهار."
            ),
            on = "N450",
            off = "UN450"
        ),
        AfghanPackage(
            Tr("6 جی بی شبانه هفته ای", "6 GB Night Weekly", "6 جي بي شپنی اونیز"),
            Tr("135 افغانی", "135 AFN", "135 افغانۍ"),
            Tr(
                "6 جی بی اینترنت برای یک هفته به قیمت 135 افغانی.\nبرای فعال‌سازی N135 و برای غیرفعال‌سازی UN135 را به شماره 555 ارسال نمائید.\nاز ساعت یازده شب تا هفت صبح.",
                "6 GB internet for one week, price 135 AFN.\nTo activate send N135, to deactivate send UN135 to 555.\nFrom 11 PM to 7 AM.",
                "د یوې اونۍ لپاره 6 جي بي انټرنیټ، بیه 135 افغانۍ.\nد فعالولو لپاره N135 او د غیرفعالولو لپاره UN135 555 ته ولیږئ.\nله 11 شپې تر 7 سهار."
            ),
            on = "N135",
            off = "UN135"
        )
    )

    // ---------------- روزانه (از کم به زیاد) ----------------
    private val daily = listOf(
        AfghanPackage(
            Tr("200 ام بی روزانه", "200 MB Daily", "200 ميګابایټ ورځنی"),
            Tr("15 افغانی", "15 AFN", "15 افغانۍ"),
            Tr(
                "200 ام بی اینترنت برای یک روز به قیمت 15 افغانی.\nبرای فعال‌سازی S6 و برای غیرفعال‌سازی US6 را به شماره 555 ارسال نمائید.",
                "200 MB internet for one day, price 15 AFN.\nTo activate send S6, to deactivate send US6 to 555.",
                "د یوې ورځې لپاره 200 ميګابایټ انټرنیټ، بیه 15 افغانۍ.\nد فعالولو لپاره S6 او د غیرفعالولو لپاره US6 555 ته ولیږئ."
            ),
            on = "S6",
            off = "US6"
        ),
        AfghanPackage(
            Tr("350 ام بی روزانه", "350 MB Daily", "350 ميګابایټ ورځنی"),
            Tr("25 افغانی", "25 AFN", "25 افغانۍ"),
            Tr(
                "350 ام بی اینترنت برای یک روز به قیمت 25 افغانی.\nبرای فعال‌سازی S25 و برای غیرفعال‌سازی US25 را به شماره 555 ارسال نمائید.",
                "350 MB internet for one day, price 25 AFN.\nTo activate send S25, to deactivate send US25 to 555.",
                "د یوې ورځې لپاره 350 ميګابایټ انټرنیټ، بیه 25 افغانۍ.\nد فعالولو لپاره S25 او د غیرفعالولو لپاره US25 555 ته ولیږئ."
            ),
            on = "S25",
            off = "US25"
        ),
        AfghanPackage(
            Tr("450 ام بی روزانه", "450 MB Daily", "450 ميګابایټ ورځنی"),
            Tr("30 افغانی", "30 AFN", "30 افغانۍ"),
            Tr(
                "450 ام بی اینترنت برای یک روز به قیمت 30 افغانی.\nبرای فعال‌سازی D200 و برای غیرفعال‌سازی UD200 را به شماره 555 ارسال نمائید.",
                "450 MB internet for one day, price 30 AFN.\nTo activate send D200, to deactivate send UD200 to 555.",
                "د یوې ورځې لپاره 450 ميګابایټ انټرنیټ، بیه 30 افغانۍ.\nد فعالولو لپاره D200 او د غیرفعالولو لپاره UD200 555 ته ولیږئ."
            ),
            on = "D200",
            off = "UD200"
        ),
        AfghanPackage(
            Tr("1 جی بی روزانه", "1 GB Daily", "1 جي بي ورځنی"),
            Tr("50 افغانی", "50 AFN", "50 افغانۍ"),
            Tr(
                "1 جی بی اینترنت برای یک روز به قیمت 50 افغانی.\nبرای فعال‌سازی D1 و برای غیرفعال‌سازی UD1 را به شماره 555 ارسال نمائید.",
                "1 GB internet for one day, price 50 AFN.\nTo activate send D1, to deactivate send UD1 to 555.",
                "د یوې ورځې لپاره 1 جي بي انټرنیټ، بیه 50 افغانۍ.\nد فعالولو لپاره D1 او د غیرفعالولو لپاره UD1 555 ته ولیږئ."
            ),
            on = "D1",
            off = "UD1"
        ),
        AfghanPackage(
            Tr("2.5 جی بی روزانه", "2.5 GB Daily", "2.5 جي بي ورځنی"),
            Tr("90 افغانی", "90 AFN", "90 افغانۍ"),
            Tr(
                "2.5 جی بی اینترنت برای یک روز به قیمت 90 افغانی.\nبرای فعال‌سازی 3GB و برای غیرفعال‌سازی U3GB را به شماره 555 ارسال نمائید.",
                "2.5 GB internet for one day, price 90 AFN.\nTo activate send 3GB, to deactivate send U3GB to 555.",
                "د یوې ورځې لپاره 2.5 جي بي انټرنیټ، بیه 90 افغانۍ.\nد فعالولو لپاره 3GB او د غیرفعالولو لپاره U3GB 555 ته ولیږئ."
            ),
            on = "3GB",
            off = "U3GB"
        )
    )

    // ---------------- هفتگی (از کم به زیاد) ----------------
    private val weekly = listOf(
        AfghanPackage(
            Tr("500 ام بی هفته وار", "500 MB Weekly", "500 ميګابایټ اونیز"),
            Tr("45 افغانی", "45 AFN", "45 افغانۍ"),
            Tr(
                "500 ام بی اینترنت برای یک هفته به قیمت 45 افغانی.\nبرای فعال‌سازی S45 و برای غیرفعال‌سازی US45 را به شماره 555 ارسال نمائید.",
                "500 MB internet for one week, price 45 AFN.\nTo activate send S45, to deactivate send US45 to 555.",
                "د یوې اونۍ لپاره 500 ميګابایټ انټرنیټ، بیه 45 افغانۍ.\nد فعالولو لپاره S45 او د غیرفعالولو لپاره US45 555 ته ولیږئ."
            ),
            on = "S45",
            off = "US45"
        ),
        AfghanPackage(
            Tr("1 جی بی هفته وار", "1 GB Weekly", "1 جي بي اونیز"),
            Tr("80 افغانی", "80 AFN", "80 افغانۍ"),
            Tr(
                "1 جی بی اینترنت برای یک هفته به قیمت 80 افغانی.\nبرای فعال‌سازی w512 و برای غیرفعال‌سازی uw512 را به شماره 555 ارسال نمائید.",
                "1 GB internet for one week, price 80 AFN.\nTo activate send w512, to deactivate send uw512 to 555.",
                "د یوې اونۍ لپاره 1 جي بي انټرنیټ، بیه 80 افغانۍ.\nد فعالولو لپاره w512 او د غیرفعالولو لپاره uw512 555 ته ولیږئ."
            ),
            on = "w512",
            off = "uw512"
        ),
        AfghanPackage(
            Tr("2 جی بی هفته وار", "2 GB Weekly", "2 جي بي اونیز"),
            Tr("135 افغانی", "135 AFN", "135 افغانۍ"),
            Tr(
                "2 جی بی اینترنت برای یک هفته به قیمت 135 افغانی.\nبرای فعال‌سازی R2 و برای غیرفعال‌سازی DR2 را به شماره 555 ارسال نمائید.",
                "2 GB internet for one week, price 135 AFN.\nTo activate send R2, to deactivate send DR2 to 555.",
                "د یوې اونۍ لپاره 2 جي بي انټرنیټ، بیه 135 افغانۍ.\nد فعالولو لپاره R2 او د غیرفعالولو لپاره DR2 555 ته ولیږئ."
            ),
            on = "R2",
            off = "DR2"
        )
    )

    // ---------------- ماهانه (از کم به زیاد) ----------------
    private fun monthlyPkg(
        size: String,
        isMb: Boolean,
        price: String,
        on: String,
        off: String,
        tagFa: String = "",
        tagEn: String = "",
        tagPs: String = "",
        extraFa: String = "",
        extraEn: String = "",
        extraPs: String = ""
    ): AfghanPackage {
        val unitFa = if (isMb) "ام بی" else "جی بی"
        val unitEn = if (isMb) "MB" else "GB"
        val unitPs = if (isMb) "ميګابایټ" else "جي بي"
        val tFa = if (tagFa.isEmpty()) "" else " $tagFa"
        val tEn = if (tagEn.isEmpty()) "" else " $tagEn"
        val tPs = if (tagPs.isEmpty()) "" else " $tagPs"
        val eFa = if (extraFa.isEmpty()) "" else " $extraFa"
        val eEn = if (extraEn.isEmpty()) "" else " $extraEn"
        val ePs = if (extraPs.isEmpty()) "" else " $extraPs"
        return AfghanPackage(
            Tr(
                "$size $unitFa ماهانه$tFa",
                "$size $unitEn Monthly$tEn",
                "$size $unitPs میاشتنی$tPs"
            ),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "$size $unitFa اینترنت برای یک ماه به قیمت $price افغانی.$eFa\nبرای فعال‌سازی $on و برای غیرفعال‌سازی $off را به شماره 555 ارسال نمائید.",
                "$size $unitEn internet for one month, price $price AFN.$eEn\nTo activate send $on, to deactivate send $off to 555.",
                "د یوې میاشتې لپاره $size $unitPs انټرنیټ، بیه $price افغانۍ.$ePs\nد فعالولو لپاره $on او د غیرفعالولو لپاره $off 555 ته ولیږئ."
            ),
            on = on,
            off = off
        )
    }

    private val monthly = listOf(
        monthlyPkg("900", true, "90", "S100", "U100"),
        monthlyPkg("1", false, "95", "AM1", "UAM1"),
        monthlyPkg("1.2", false, "110", "1GB", "U1GB"),
        monthlyPkg("1.5", false, "135", "S135", "US135"),
        monthlyPkg("2", false, "190", "AM2", "UAM2"),
        monthlyPkg("2.4", false, "220", "M1", "UM1"),
        monthlyPkg("3", false, "285", "AM3", "UAM3"),
        monthlyPkg("3.6", false, "330", "M2", "UM2"),
        monthlyPkg("4", false, "380", "AM4", "UAM4"),
        monthlyPkg("4.9", false, "440", "M3", "UM3"),
        monthlyPkg(
            "6", false, "550", "M5", "UM5",
            extraFa = "500 ام بی فیسبوک اضافی.",
            extraEn = "Extra 500 MB for Facebook.",
            extraPs = "500 ميګابایټ اضافي فیسبوک."
        ),
        monthlyPkg("8", false, "500", "S7", "DS7"),
        monthlyPkg("10.5", false, "600", "R10", "DR10"),
        monthlyPkg(
            "15", false, "450", "T9", "UT9",
            tagFa = "برونز",
            tagEn = "Bronze",
            tagPs = "برونز",
            extraFa = "10 جی بی با سرعت پایین.",
            extraEn = "10 GB at low speed.",
            extraPs = "10 جي بي په ټیټه چټکتیا."
        ),
        monthlyPkg(
            "20", false, "700", "T10", "UT10",
            tagFa = "نقره ای",
            tagEn = "Silver",
            tagPs = "سپین",
            extraFa = "10 جی بی با سرعت پایین.",
            extraEn = "10 GB at low speed.",
            extraPs = "10 جي بي په ټیټه چټکتیا."
        ),
        monthlyPkg("20", false, "999", "R20", "DR20"),
        monthlyPkg("25", false, "1099", "R25", "DR25"),
        monthlyPkg("50", false, "1799", "R50", "DR50")
    )

    // ---------------- ترکیبی ----------------
    private val combo = listOf(
        AfghanPackage(
            Tr("بسته کامبو کوچک", "Small Combo Package", "کوچنۍ کمبو بسته"),
            Tr("450 افغانی", "450 AFN", "450 افغانۍ"),
            Tr(
                "4 جی بی انترنت، 1000 دقیقه به روشن، 1000 پیام به روشن برای یک ماه به قیمت 450 افغانی.\nبرای فعال‌سازی D16 را به شماره 555 ارسال نمائید.",
                "4 GB internet, 1000 minutes to Roshan, 1000 SMS to Roshan for one month, price 450 AFN.\nTo activate, send D16 to 555.",
                "د یوې میاشتې لپاره 4 جي بي انټرنیټ، روشن ته 1000 دقیقې، روشن ته 1000 پیغامونه، بیه 450 افغانۍ.\nد فعالولو لپاره D16 555 ته ولیږئ."
            ),
            on = "D16"
        ),
        AfghanPackage(
            Tr("بسته کامبو", "Combo Package", "کمبو بسته"),
            Tr("450 افغانی", "450 AFN", "450 افغانۍ"),
            Tr(
                "1.5 جی بی انترنت، 300 دقیقه به همه شبکه ها، 3000 پیام به روشن برای یک ماه به قیمت 450 افغانی.\nبرای فعال‌سازی SP1 و برای غیرفعال‌سازی DSP1 را به شماره 555 ارسال نمائید.",
                "1.5 GB internet, 300 minutes to all networks, 3000 SMS to Roshan for one month, price 450 AFN.\nTo activate send SP1, to deactivate send DSP1 to 555.",
                "د یوې میاشتې لپاره 1.5 جي بي انټرنیټ، ټولو شبکو ته 300 دقیقې، روشن ته 3000 پیغامونه، بیه 450 افغانۍ.\nد فعالولو لپاره SP1 او د غیرفعالولو لپاره DSP1 555 ته ولیږئ."
            ),
            on = "SP1",
            off = "DSP1"
        )
    )

    // ---------------- متفرقه (بسته‌های اجتماعی و فیسبوک) ----------------

    // فعال‌سازی با پیامک به 555
    private fun socialSms(
        titleFa: String, titleEn: String, titlePs: String,
        price: String,
        daily: Boolean,
        appsFa: String, appsEn: String, appsPs: String,
        on: String, off: String
    ): AfghanPackage {
        val perFa = if (daily) "یک روز" else "یک ماه"
        val perEn = if (daily) "one day" else "one month"
        val perPs = if (daily) "یوې ورځې" else "یوې میاشتې"
        return AfghanPackage(
            Tr(titleFa, titleEn, titlePs),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "$titleFa برای $perFa به قیمت $price افغانی. فقط برای استفاده از $appsFa.\nبرای فعال‌سازی $on و برای غیرفعال‌سازی $off را به شماره 555 ارسال نمائید.",
                "$titleEn for $perEn, price $price AFN. Only for $appsEn.\nTo activate send $on, to deactivate send $off to 555.",
                "د $perPs لپاره $titlePs، بیه $price افغانۍ. یوازې د $appsPs لپاره.\nد فعالولو لپاره $on او د غیرفعالولو لپاره $off 555 ته ولیږئ."
            ),
            on = on,
            off = off
        )
    }

    // فعال‌سازی با زنگ به 242
    private fun socialCall(
        size: String,
        price: String
    ): AfghanPackage {
        return AfghanPackage(
            Tr("$size جی بی اجتماعی", "$size GB Social", "$size جي بي ټولنیز"),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "$size جی بی اینترنت برای یک ماه به قیمت $price افغانی. فقط برای استفاده شبکه‌های اجتماعی.\nبرای فعال‌سازی به شماره 242 زنگ بزنید.",
                "$size GB internet for one month, price $price AFN. Only for social networks.\nTo activate, call 242.",
                "د یوې میاشتې لپاره $size جي بي انټرنیټ، بیه $price افغانۍ. یوازې د ټولنیزو شبکو لپاره.\nد فعالولو لپاره 242 ته زنګ ووهئ."
            ),
            on = DIAL_PREFIX + "242"
        )
    }

    private val misc = listOf(
        socialSms(
            "50 ام بی اجتماعی", "50 MB Social", "50 ميګابایټ ټولنیز",
            "5", true,
            "اپلیکیشن‌های اجتماعی", "social apps", "ټولنیزو اپلیکیشنونو",
            "S5", "US5"
        ),
        socialSms(
            "450 ام بی فیسبوک", "450 MB Facebook", "450 ميګابایټ فیسبوک",
            "45", false,
            "فیسبوک", "Facebook", "فیسبوک",
            "FB50", "UYFB"
        ),
        socialSms(
            "650 ام بی فیسبوک", "650 MB Facebook", "650 ميګابایټ فیسبوک",
            "65", false,
            "فیسبوک", "Facebook", "فیسبوک",
            "FB A", "FB D"
        ),
        socialSms(
            "1.9 جی بی اجتماعی", "1.9 GB Social", "1.9 جي بي ټولنیز",
            "199", false,
            "شبکه‌های اجتماعی", "social networks", "ټولنیزو شبکو",
            "S199", "U199"
        ),
        socialSms(
            "3 جی بی اجتماعی", "3 GB Social", "3 جي بي ټولنیز",
            "299", false,
            "شبکه‌های اجتماعی", "social networks", "ټولنیزو شبکو",
            "S299", "U299"
        ),
        socialCall("2", "135"),
        socialCall("4", "299"),
        socialCall("3.5", "375"),
        socialCall("5.5", "475")
    )
}