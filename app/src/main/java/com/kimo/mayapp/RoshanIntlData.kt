package com.kimo.mayapp

object RoshanIntlData {

    val screenTitle = Tr(
        "بسته‌های بین‌المللی روشن",
        "Roshan International Packages",
        "د روشن نړیوالې بستې"
    )

    private const val CLOSE_NOTE = ""

    private fun pkg(
        titleFa: String, titleEn: String, titlePs: String,
        price: String,
        descFa: String, descEn: String, descPs: String,
        on: String, off: String,
        countries: String = ""
    ): AfghanPackage {
        val extra = if (countries.isEmpty()) "" else "\n$countries"
        return AfghanPackage(
            Tr(titleFa, titleEn, titlePs),
            Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
            Tr(
                "$descFa\nبرای فعال‌سازی $on و برای غیرفعال‌سازی $off را به شماره 555 ارسال نمائید.$extra",
                "$descEn\nTo activate send $on, to deactivate send $off to 555.$extra",
                "$descPs\nد فعالولو لپاره $on او د غیرفعالولو لپاره $off 555 ته ولیږئ.$extra"
            ),
            on = on,
            off = off
        )
    }

    private const val COUNTRIES_CALL_21 =
        "USA, Canada, China, Bangladesh, India, Malaysia, Germany, UK, Denmark, Mongolia, New Zealand, Sweden, Bermuda, Thailand, Columbia, Ireland, Romania, Brazil, Hong Kong, Iceland, Indonesia"

    private const val COUNTRIES_ASIA =
        "Nepal, Philippine, Saudi Arabia, Tajikistan, Turkey, UAE and Uzbekistan"

    private const val COUNTRIES_SMS_50 =
        "USA, Iran, Pakistan, Nepal, India, Saudi Arabia, Germany, UK, Philippines, Turkey, Tajikistan, Italy, Sri Lanka, Australia, Russia, France, Canada, Netherlands, Kenya, China, Uzbekistan, Kuwait, Kyrgyzstan, Thailand, Kazakhstan, Uganda, South Africa, Belgium, Jordan, Sweden, Greece, Norway, Austria, Indonesia, Romania, Poland, Denmark, Puerto Rico, Egypt, Turkmenistan, Colombia, South Korea, Switzerland, Spain, Malaysia, Nigeria, New Zealand, Japan, Hong Kong, Finland"

    val items: List<AfghanPackage> = listOf(
        // ---------- روزانه ----------
        pkg(
            "بسته روزانه ایران", "Iran Daily Package", "د ایران ورځنۍ بسته",
            "35",
            "5 دقیقه تماس به ایران، هر دقیقه 7 افغانی. قیمت این بسته روزانه 35 افغانی میباشد.",
            "5 minutes of calls to Iran, 7 AFN per minute. Daily price 35 AFN.",
            "ایران ته 5 دقیقې زنګونه، هره دقیقه 7 افغانۍ. ورځنۍ بیه 35 افغانۍ.",
            "981", "d981"
        ),
        pkg(
            "بسته روزانه پاکستان", "Pakistan Daily Package", "د پاکستان ورځنۍ بسته",
            "35",
            "15 دقیقه تماس به پاکستان، هر دقیقه 2.3 افغانی. قیمت این بسته روزانه 35 افغانی میباشد.",
            "15 minutes of calls to Pakistan, 2.3 AFN per minute. Daily price 35 AFN.",
            "پاکستان ته 15 دقیقې زنګونه، هره دقیقه 2.3 افغانۍ. ورځنۍ بیه 35 افغانۍ.",
            "921", "d921"
        ),
        pkg(
            "بسته روزانه هند", "India Daily Package", "د هند ورځنۍ بسته",
            "35",
            "15 دقیقه تماس به هند، هر دقیقه 2 افغانی. قیمت این بسته روزانه 35 افغانی میباشد.",
            "15 minutes of calls to India, 2 AFN per minute. Daily price 35 AFN.",
            "هند ته 15 دقیقې زنګونه، هره دقیقه 2 افغانۍ. ورځنۍ بیه 35 افغانۍ.",
            "911", "d911"
        ),
        // ---------- هفتگی ----------
        pkg(
            "بسته هفته ای ایران", "Iran Weekly Package", "د ایران اونیزه بسته",
            "150",
            "25 دقیقه تماس به ایران، هر دقیقه 6 افغانی. قیمت این بسته هفته ای 150 افغانی میباشد.",
            "25 minutes of calls to Iran, 6 AFN per minute. Weekly price 150 AFN.",
            "ایران ته 25 دقیقې زنګونه، هره دقیقه 6 افغانۍ. اونیزه بیه 150 افغانۍ.",
            "987", "d987"
        ),
        pkg(
            "بسته هفته ای پاکستان", "Pakistan Weekly Package", "د پاکستان اونیزه بسته",
            "150",
            "75 دقیقه تماس به پاکستان، هر دقیقه 2 افغانی. قیمت این بسته هفته ای 150 افغانی میباشد.",
            "75 minutes of calls to Pakistan, 2 AFN per minute. Weekly price 150 AFN.",
            "پاکستان ته 75 دقیقې زنګونه، هره دقیقه 2 افغانۍ. اونیزه بیه 150 افغانۍ.",
            "927", "d927"
        ),
        pkg(
            "بسته هفته ای هند", "India Weekly Package", "د هند اونیزه بسته",
            "150",
            "75 دقیقه تماس به هند، هر دقیقه 2 افغانی. قیمت این بسته هفته ای 150 افغانی میباشد.",
            "75 minutes of calls to India, 2 AFN per minute. Weekly price 150 AFN.",
            "هند ته 75 دقیقې زنګونه، هره دقیقه 2 افغانۍ. اونیزه بیه 150 افغانۍ.",
            "917", "d917"
        ),
        pkg(
            "بسته تماس بین المللی هفته وار", "International Call Weekly Package", "نړیوال زنګ اونیزه بسته",
            "399",
            "100 دقیقه تماس به 21 کشور، هر دقیقه 4 افغانی. قیمت این بسته هفته ای 399 افغانی میباشد.",
            "100 minutes of calls to 21 countries, 4 AFN per minute. Weekly price 399 AFN.",
            "21 هیوادونو ته 100 دقیقې زنګونه، هره دقیقه 4 افغانۍ. اونیزه بیه 399 افغانۍ.",
            "100", "UN100",
            COUNTRIES_CALL_21
        ),
        // ---------- ماهانه ----------
        pkg(
            "50 پیام به کشورهای خارجی", "50 SMS to Foreign Countries", "بهرنیو هیوادونو ته 50 پیغامونه",
            "250",
            "50 پیام بین المللی برای یک ماه به 50 کشور جهان به قیمت 250 افغانی.",
            "50 international SMS for one month to 50 countries, price 250 AFN.",
            "د یوې میاشتې لپاره 50 هیوادونو ته 50 نړیوال پیغامونه، بیه 250 افغانۍ.",
            "isms", "dsms",
            COUNTRIES_SMS_50
        ),
        pkg(
            "بسته ماهانه آسیایی", "Asian Monthly Package", "آسیایي میاشتنۍ بسته",
            "400",
            "50 دقیقه تماس به کشورهای ذیل، هر دقیقه 8 افغانی. قیمت این بسته ماهانه 400 افغانی میباشد.",
            "50 minutes of calls to the countries below, 8 AFN per minute. Monthly price 400 AFN.",
            "لاندې هیوادونو ته 50 دقیقې زنګونه، هره دقیقه 8 افغانۍ. میاشتنۍ بیه 400 افغانۍ.",
            "50", "UN50",
            COUNTRIES_ASIA
        ),
        pkg(
            "بسته تماس بین المللی ماهانه", "International Call Monthly Package", "نړیوال زنګ میاشتنۍ بسته",
            "999",
            "300 دقیقه تماس به 21 کشور، هر دقیقه 3.3 افغانی. قیمت این بسته ماهانه 999 افغانی میباشد.",
            "300 minutes of calls to 21 countries, 3.3 AFN per minute. Monthly price 999 AFN.",
            "21 هیوادونو ته 300 دقیقې زنګونه، هره دقیقه 3.3 افغانۍ. میاشتنۍ بیه 999 افغانۍ.",
            "300", "d300",
            COUNTRIES_CALL_21
        )
    )
}