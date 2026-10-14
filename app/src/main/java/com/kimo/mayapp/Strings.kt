package com.kimo.mayapp

import android.content.Context

object Strings {

    private val data = mapOf(
        "app_title" to mapOf(
            "fa" to "سیم کارت 27",
            "en" to "SIM Card 27",
            "ps" to "سیم کارټ 27"
        ),
        "version" to mapOf(
            "fa" to "ورژن 1.0",
            "en" to "Version 1.0",
            "ps" to "نسخه 1.0"
        ),
        "rate" to mapOf(
            "fa" to "⭐  ما را درجه بندی کنید",
            "en" to "⭐  Rate us",
            "ps" to "⭐  موږ ته درجه ورکړئ"
        ),
        "facebook" to mapOf(
            "fa" to "📘  صفحه ما را لایک کنید",
            "en" to "📘  Like our page",
            "ps" to "📘  زموږ پاڼه خوښ کړئ"
        ),
        "telegram" to mapOf(
            "fa" to "✈️  به کانال تلگرام ما بپیوندید",
            "en" to "✈️  Join our Telegram channel",
            "ps" to "✈️  زموږ د تلګرام کانال سره یوځای شئ"
        ),
        "language" to mapOf(
            "fa" to "🌐  زبان",
            "en" to "🌐  Language",
            "ps" to "🌐  ژبه"
        ),
        "etisalat" to mapOf(
            "fa" to "اتصالات", "en" to "Etisalat", "ps" to "اتصالات"
        ),
        "afghan_wireless" to mapOf(
            "fa" to "افغان بیسیم", "en" to "Afghan Wireless", "ps" to "افغان بېسیم"
        ),
        "roshan" to mapOf(
            "fa" to "روشن", "en" to "Roshan", "ps" to "روشن"
        ),
        "atoma" to mapOf(
            "fa" to "اتوما", "en" to "Atoma", "ps" to "اتوما"
        ),
        "salaam" to mapOf(
            "fa" to "سلام", "en" to "Salaam", "ps" to "سلام"
        ),
        "fal_hafez" to mapOf(
            "fa" to "فال حافظ", "en" to "Fal-e Hafez", "ps" to "د حافظ فال"
        ),
        "internet_packages_title" to mapOf(
            "fa" to "بسته‌های اینترنتی",
            "en" to "Internet Packages",
            "ps" to "د انټرنټ بسته‌ګانې"
        ),
        "sms_packages_title" to mapOf(
            "fa" to "بسته‌های پیام",
            "en" to "SMS Packages",
            "ps" to "د پیغام بسته‌ګانې"
        ),
        "call_packages_title" to mapOf(
            "fa" to "بسته‌های تماس",
            "en" to "Call Packages",
            "ps" to "د زنګ بسته‌ګانې"
        ),
        "services_title" to mapOf(
            "fa" to "خدمات",
            "en" to "Services",
            "ps" to "خدمتونه"
        ),
        "international_packages_title" to mapOf(
            "fa" to "بسته‌های بین‌المللی",
            "en" to "International Packages",
            "ps" to "نړیوالې بسته‌ګانې"
        ),
        "tab_night" to mapOf("fa" to "شبانه", "en" to "Night", "ps" to "شپنۍ"),
        "tab_hourly" to mapOf("fa" to "ساعتی", "en" to "Hourly", "ps" to "ساعتي"),
        "tab_daily" to mapOf("fa" to "روزانه", "en" to "Daily", "ps" to "ورځنۍ"),
        "tab_weekly" to mapOf("fa" to "هفتگی", "en" to "Weekly", "ps" to "اونیزه"),
        "tab_monthly" to mapOf("fa" to "ماهانه", "en" to "Monthly", "ps" to "میاشتنۍ"),
        "tab_combo" to mapOf("fa" to "ترکیبی", "en" to "Combo", "ps" to "ګډې"),
        "tab_misc" to mapOf("fa" to "متفرقه", "en" to "Misc", "ps" to "نورې"),
        "soon_internet" to mapOf(
            "fa" to "بخش بسته‌های اینترنتی این اپراتور به زودی فعال خواهد شد",
            "en" to "Internet packages for this operator will be available soon",
            "ps" to "د دې اپراتور د انټرنټ بسته‌ګانې ژر فعالیږي"
        ),
        "btn_activate" to mapOf("fa" to "فعال سازی", "en" to "Activate", "ps" to "فعالول"),
        "btn_deactivate" to mapOf("fa" to "غیر فعال سازی", "en" to "Deactivate", "ps" to "غیر فعالول"),
        "btn_check" to mapOf("fa" to "چک بیلانس", "en" to "Check Balance", "ps" to "بیلانس وګورئ"),
        "sms_error" to mapOf(
            "fa" to "خطا در ارسال پیامک",
            "en" to "Error sending SMS",
            "ps" to "د SMS په لیږلو کې ستونزه"
        ),
        "sms_app_not_found" to mapOf(
            "fa" to "برنامه پیام‌ها پیدا نشد",
            "en" to "Messages app not found",
            "ps" to "د پیغامونو اپ ونه موندل شو"
        ),
        "soon_generic" to mapOf(
            "fa" to "این بخش به‌زودی فعال می‌شود",
            "en" to "This section will be available soon",
            "ps" to "دا برخه ژر فعالیږي"
        ),
        "svc_remaining_title" to mapOf(
            "fa" to "دقیقه باقیمانده",
            "en" to "Remaining Minutes",
            "ps" to "پاتې شوې دقیقې"
        ),
        "svc_loan_title" to mapOf(
            "fa" to "قرضه 20 افغانی",
            "en" to "20 AFN Loan",
            "ps" to "د 20 افغانیو پور"
        ),
        "svc_loan_body" to mapOf(
            "fa" to "حروف 'SIM' را به 3378 بفرستید",
            "en" to "Send the letters 'SIM' to 3378",
            "ps" to "'SIM' توري 3378 ته ولیږئ"
        ),
        "svc_loan_btn" to mapOf(
            "fa" to "قرضه",
            "en" to "Get Loan",
            "ps" to "پور واخلئ"
        ),
        "svc_credit_send_title" to mapOf(
            "fa" to "ارسال کردیت",
            "en" to "Send Credit",
            "ps" to "کریډیټ لیږل"
        ),
        "svc_credit_send_body" to mapOf(
            "fa" to "*125*078xxxxxxx*مبلغ#",
            "en" to "*125*078xxxxxxx*Amount#",
            "ps" to "*125*078xxxxxxx*مقدار#"
        ),
        "svc_apn_title" to mapOf(
            "fa" to "تنظیمات APN",
            "en" to "APN Settings",
            "ps" to "د APN تنظیمات"
        ),
        "svc_credit_add_title" to mapOf(
            "fa" to "اضافه کردن کردیت",
            "en" to "Add Credit",
            "ps" to "کریډیټ اضافه کول"
        ),
        "svc_3g_title" to mapOf(
            "fa" to "فعال سازی 3G",
            "en" to "Activate 3G",
            "ps" to "3G فعالول"
        ),
        "svc_3g_body" to mapOf(
            "fa" to "حروف '3G' را به 3378 بفرستید",
            "en" to "Send the letters '3G' to 3378",
            "ps" to "'3G' توري 3378 ته ولیږئ"
        ),
        "svc_4g_title" to mapOf(
            "fa" to "فعال سازی 4G",
            "en" to "Activate 4G",
            "ps" to "4G فعالول"
        ),
        "svc_4g_body" to mapOf(
            "fa" to "حروف '4G' را به 3378 بفرستید",
            "en" to "Send the letters '4G' to 3378",
            "ps" to "'4G' توري 3378 ته ولیږئ"
        ),
        "svc_dialer_not_found" to mapOf(
            "fa" to "شماره‌گیر پیدا نشد",
            "en" to "Dialer app not found",
            "ps" to "د ډیلر اپ ونه موندل شو"
        ),
        "night_1_title" to mapOf(
            "fa" to "1 جی بی شبانه روزانه",
            "en" to "1 GB Night Daily",
            "ps" to "1 جي بي شپنۍ ورځنۍ"
        ),
        "night_1_price" to mapOf(
            "fa" to "25 افغانی", "en" to "25 AFN", "ps" to "25 افغانۍ"
        ),
        "night_1_desc" to mapOf(
            "fa" to "بسته 1 جی بی اینترنت برای یک روز شبانه به قیمت 25 افغانی. از 11 شب تا 7 صبح. تمدید خودکار فعال.\nبرای فعال سازی NBD و برای غیرفعال سازی PU NBD را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
            "en" to "1 GB internet package for one night, price 25 AFN. From 11 PM to 7 AM. Auto-renewal enabled.\nTo activate send NBD and to deactivate send PU NBD to 3378.\nTo check your balance send 505 to 606.",
            "ps" to "د یوې شپې لپاره 1 جي بي انټرنټ بسته، بیه 25 افغانۍ. له شپې 11 بجو تر سهار 7 بجو. اتوماتیک تمدید فعال دی.\nد فعالولو لپاره NBD او د غیر فعالولو لپاره PU NBD ته 3378 ولیږئ.\nد بیلانس د کتلو لپاره 505 ته 606 ولیږئ."
        ),
        "night_2_title" to mapOf(
            "fa" to "3 جی بی شبانه ماهانه",
            "en" to "3 GB Night Monthly",
            "ps" to "3 جي بي شپنۍ میاشتنۍ"
        ),
        "night_2_price" to mapOf(
            "fa" to "99 افغانی", "en" to "99 AFN", "ps" to "99 افغانۍ"
        ),
        "night_2_desc" to mapOf(
            "fa" to "بسته 3 جی بی اینترنت برای 30 روز شبانه به قیمت 99 افغانی. از 11 شب تا 7 صبح. تمدید خودکار فعال.\nبرای فعال سازی NBS و برای غیرفعال سازی PU NBS را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید.",
            "en" to "3 GB internet package for 30 nights, price 99 AFN. From 11 PM to 7 AM. Auto-renewal enabled.\nTo activate send NBS and to deactivate send PU NBS to 3378.\nTo check your balance send 505 to 606.",
            "ps" to "د 30 شپو لپاره 3 جي بي انټرنټ بسته، بیه 99 افغانۍ. له شپې 11 بجو تر سهار 7 بجو. اتوماتیک تمدید فعال دی.\nد فعالولو لپاره NBS او د غیر فعالولو لپاره PU NBS ته 3378 ولیږئ.\nد بیلانس د کتلو لپاره 505 ته 606 ولیږئ."
        )
    )

    fun currentLang(context: Context): String =
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            .getString("Locale.Helper.Selected.Language", "fa") ?: "fa"

    fun get(context: Context, key: String): String =
        data[key]?.get(currentLang(context)) ?: data[key]?.get("fa") ?: key
}