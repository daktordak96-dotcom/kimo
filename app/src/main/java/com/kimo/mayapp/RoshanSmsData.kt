package com.kimo.mayapp

object RoshanSmsData {

    val screenTitle = Tr(
        "بسته‌های پیام روشن",
        "Roshan SMS Packages",
        "د روشن د پیغام بستې"
    )

    // ترتیب باید با forTab یکی باشد
    val tabs = listOf(
        Tr("روزانه", "Daily", "ورځنی"),
        Tr("هفتگی", "Weekly", "اونیز"),
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

    private val daily = emptyList<AfghanPackage>()

    private val weekly = listOf(
        AfghanPackage(
            Tr("3000 پیام هفتگی", "3000 SMS Weekly", "3000 پیغامونه اونیز"),
            Tr("25 افغانی", "25 AFN", "25 افغانۍ"),
            Tr(
                "3000 پیام فقط به روشن برای یک هفته به قیمت 25 افغانی.\nبرای فعال‌سازی ONW و برای غیرفعال‌سازی DONW را به شماره 555 ارسال نمائید.",
                "3000 SMS to Roshan only for one week, price 25 AFN.\nTo activate send ONW, to deactivate send DONW to 555.",
                "د یوې اونۍ لپاره یوازې روشن ته 3000 پیغامونه، بیه 25 افغانۍ.\nد فعالولو لپاره ONW او د غیرفعالولو لپاره DONW 555 ته ولیږئ."
            ),
            on = "ONW",
            off = "DONW"
        )
    )

    private val monthly = listOf(
        AfghanPackage(
            Tr("30000 پیام ماهانه", "30000 SMS Monthly", "30000 پیغامونه میاشتنی"),
            Tr("70 افغانی", "70 AFN", "70 افغانۍ"),
            Tr(
                "30000 پیام فقط به روشن برای یک ماه به قیمت 70 افغانی.\nبرای فعال‌سازی on1 و برای غیرفعال‌سازی DON1 را به شماره 555 ارسال نمائید.",
                "30000 SMS to Roshan only for one month, price 70 AFN.\nTo activate send on1, to deactivate send DON1 to 555.",
                "د یوې میاشتې لپاره یوازې روشن ته 30000 پیغامونه، بیه 70 افغانۍ.\nد فعالولو لپاره on1 او د غیرفعالولو لپاره DON1 555 ته ولیږئ."
            ),
            on = "on1",
            off = "DON1"
        )
    )

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

    private val misc = emptyList<AfghanPackage>()
}