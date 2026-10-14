package com.kimo.mayapp

object RoshanCallData {

    val screenTitle = Tr(
        "بسته‌های تماس روشن",
        "Roshan Call Packages",
        "د روشن د زنګ بستې"
    )

    // ترتیب باید با forTab یکی باشد
    val tabs = listOf(
        Tr("ساعتی", "Hourly", "ساعتي"),
        Tr("شبانه", "Night", "شپنی"),
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفتگی", "Weekly", "اونیز"),
        Tr("ماهانه", "Monthly", "میاشتنی"),
        Tr("ترکیبی", "Combo", "ګډ")
    )

    fun forTab(index: Int): List<AfghanPackage> {
        return when (index) {
            0 -> hourly
            1 -> night
            2 -> daily
            3 -> weekly
            4 -> monthly
            5 -> combo
            else -> emptyList()
        }
    }

    // ساخت یک بسته با کد فعال‌سازی و غیرفعال‌سازی
    private fun pkg(
        titleFa: String, titleEn: String, titlePs: String,
        price: String,
        descFa: String, descEn: String, descPs: String,
        on: String, off: String
    ): AfghanPackage {
        return AfghanPackage(
            Tr(titleFa, titleEn, titlePs),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "$descFa\nبرای فعال‌سازی $on و برای غیرفعال‌سازی $off را به شماره 555 ارسال نمائید.",
                "$descEn\nTo activate send $on, to deactivate send $off to 555.",
                "$descPs\nد فعالولو لپاره $on او د غیرفعالولو لپاره $off 555 ته ولیږئ."
            ),
            on = on,
            off = off
        )
    }

    // ---------------- ساعتی ----------------
    private val hourly = listOf(
        pkg(
            "یک ساعت نامحدود", "Unlimited One Hour", "یوه ساعت نامحدود",
            "10",
            "تماس نامحدود به روشن برای یک ساعت به قیمت 10 افغانی.",
            "Unlimited calls to Roshan for one hour, price 10 AFN.",
            "د یوې ساعت لپاره روشن ته نامحدود زنګونه، بیه 10 افغانۍ.",
            "60", "D60"
        )
    )

    // ---------------- شبانه (از کم به زیاد) ----------------
    private val night = listOf(
        pkg(
            "تماس نامحدود به روشن شبانه", "Unlimited Calls to Roshan Night", "روشن ته نامحدود شپنی زنګونه",
            "15",
            "تماس نامحدود به روشن برای یک شب به قیمت 15 افغانی. از ده شب تا شش صبح.",
            "Unlimited calls to Roshan for one night, price 15 AFN. From 10 PM to 6 AM.",
            "د یوې شپې لپاره روشن ته نامحدود زنګونه، بیه 15 افغانۍ. له 10 شپې تر 6 سهار.",
            "15", "15d"
        ),
        pkg(
            "تماس نامحدود به روشن هفتگی", "Unlimited Calls to Roshan Weekly", "روشن ته نامحدود اونیز زنګونه",
            "45",
            "تماس نامحدود به روشن برای یک هفته به قیمت 45 افغانی. از ده شب تا شش صبح.",
            "Unlimited calls to Roshan for one week, price 45 AFN. From 10 PM to 6 AM.",
            "د یوې اونۍ لپاره روشن ته نامحدود زنګونه، بیه 45 افغانۍ. له 10 شپې تر 6 سهار.",
            "W45", "DW45"
        ),
        pkg(
            "تماس نامحدود به روشن ماهانه", "Unlimited Calls to Roshan Monthly", "روشن ته نامحدود میاشتني زنګونه",
            "135",
            "تماس نامحدود به روشن برای یک ماه شبانه به قیمت 135 افغانی. از ده شب تا شش صبح.",
            "Unlimited calls to Roshan for one month at night, price 135 AFN. From 10 PM to 6 AM.",
            "د یوې میاشتې لپاره روشن ته نامحدود شپنی زنګونه، بیه 135 افغانۍ. له 10 شپې تر 6 سهار.",
            "N135", "DN135"
        )
    )

    // ---------------- روزانه ----------------
    private val daily = listOf(
        pkg(
            "40 دقیقه روزانه", "40 Minutes Daily", "40 دقیقې ورځنی",
            "15",
            "40 دقیقه تماس به روشن برای یک روز به قیمت 15 افغانی.",
            "40 minutes of calls to Roshan for one day, price 15 AFN.",
            "د یوې ورځې لپاره روشن ته 40 دقیقې زنګونه، بیه 15 افغانۍ.",
            "40", "D40"
        )
    )

    // ---------------- هفتگی (از کم به زیاد) ----------------
    private val weekly = listOf(
        pkg(
            "200 دقیقه", "200 Minutes", "200 دقیقې",
            "45",
            "200 دقیقه تماس به روشن برای یک هفته به قیمت 45 افغانی.",
            "200 minutes of calls to Roshan for one week, price 45 AFN.",
            "د یوې اونۍ لپاره روشن ته 200 دقیقې زنګونه، بیه 45 افغانۍ.",
            "45", "D45"
        ),
        pkg(
            "500 دقیقه + 500 پیام", "500 Minutes + 500 SMS", "500 دقیقې + 500 پیغامونه",
            "75",
            "500 دقیقه تماس + 500 پیام به روشن برای یک هفته به قیمت 75 افغانی.",
            "500 minutes of calls + 500 SMS to Roshan for one week, price 75 AFN.",
            "د یوې اونۍ لپاره روشن ته 500 دقیقې زنګونه + 500 پیغامونه، بیه 75 افغانۍ.",
            "W1", "UW1"
        )
    )

    // ---------------- ماهانه (از کم به زیاد) ----------------
    private val monthly = listOf(
        pkg(
            "250 دقیقه", "250 Minutes", "250 دقیقې",
            "90",
            "250 دقیقه به روشن برای یک ماه به قیمت 90 افغانی.",
            "250 minutes to Roshan for one month, price 90 AFN.",
            "د یوې میاشتې لپاره روشن ته 250 دقیقې، بیه 90 افغانۍ.",
            "250", "D250"
        ),
        pkg(
            "600 دقیقه", "600 Minutes", "600 دقیقې",
            "135",
            "600 دقیقه تماس به روشن برای یک ماه به قیمت 135 افغانی.",
            "600 minutes of calls to Roshan for one month, price 135 AFN.",
            "د یوې میاشتې لپاره روشن ته 600 دقیقې زنګونه، بیه 135 افغانۍ.",
            "120", "D120"
        ),
        pkg(
            "بسته 500 دقیقه ای به روشن و سایر شبکه ها", "500 Minutes to Roshan and Other Networks", "روشن او نورو شبکو ته 500 دقیقې",
            "225",
            "300 به روشن و 200 دقیقه به سایر شبکه ها برای یک ماه به قیمت 225 افغانی.",
            "300 minutes to Roshan and 200 minutes to other networks for one month, price 225 AFN.",
            "د یوې میاشتې لپاره روشن ته 300 او نورو شبکو ته 200 دقیقې، بیه 225 افغانۍ.",
            "25", "D25"
        ),
        pkg(
            "1500 دقیقه و پیام نامحدود", "1500 Minutes and Unlimited SMS", "1500 دقیقې او نامحدود پیغامونه",
            "225",
            "1500 دقیقه تماس و پیام نامحدود فقط به روشن برای یک ماه به قیمت 225 افغانی.",
            "1500 minutes of calls and unlimited SMS to Roshan only for one month, price 225 AFN.",
            "د یوې میاشتې لپاره یوازې روشن ته 1500 دقیقې زنګونه او نامحدود پیغامونه، بیه 225 افغانۍ.",
            "220", "D220"
        ),
        pkg(
            "بسته 500 دقیقه ای سایر شبکه ها", "500 Minutes to Other Networks", "نورو شبکو ته 500 دقیقې",
            "450",
            "500 دقیقه به سایر شبکه ها برای یک ماه به قیمت 450 افغانی.",
            "500 minutes to other networks for one month, price 450 AFN.",
            "د یوې میاشتې لپاره نورو شبکو ته 500 دقیقې، بیه 450 افغانۍ.",
            "SN", "DSN"
        )
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
        pkg(
            "بسته کامبو", "Combo Package", "کمبو بسته",
            "450",
            "1.5 جی بی انترنت، 300 دقیقه به همه شبکه ها، 3000 پیام به روشن برای یک ماه به قیمت 450 افغانی.",
            "1.5 GB internet, 300 minutes to all networks, 3000 SMS to Roshan for one month, price 450 AFN.",
            "د یوې میاشتې لپاره 1.5 جي بي انټرنیټ، ټولو شبکو ته 300 دقیقې، روشن ته 3000 پیغامونه، بیه 450 افغانۍ.",
            "SP1", "DSP1"
        )
    )
}