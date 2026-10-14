package com.kimo.mayapp

object SalaamIntlData {

    val screenTitle = Tr(
        "بسته‌های بین‌المللی سلام",
        "Salaam International Packages",
        "د سلام نړیوالې بستې"
    )

    val items: List<SalaamService> = listOf(
        // ===== نرخ تماس بین‌المللی =====
        SalaamService(
            title = Tr("نرخ تماس بین‌المللی", "International Call Rates", "د نړیوال زنګ نرخونه")
        ),
        SalaamService(
            title = Tr("۲ تا ۳ افغانی در دقیقه", "AFN 2-3 per minute", "۲ تر ۳ افغانۍ په دقیقه کې"),
            note = Tr(
                "India (2.5), Mexico (2), Bangladesh (3), Canada (3), USA (3), Hong Kong (3), Malaysia (3), New Zealand (3), Norway (3), Romania (3), Singapore (3), South Korea (3), Brazil (3.5), Chile (3.5), Indonesia (3.5)",
                "India (2.5), Mexico (2), Bangladesh (3), Canada (3), USA (3), Hong Kong (3), Malaysia (3), New Zealand (3), Norway (3), Romania (3), Singapore (3), South Korea (3), Brazil (3.5), Chile (3.5), Indonesia (3.5)",
                "India (2.5), Mexico (2), Bangladesh (3), Canada (3), USA (3), Hong Kong (3), Malaysia (3), New Zealand (3), Norway (3), Romania (3), Singapore (3), South Korea (3), Brazil (3.5), Chile (3.5), Indonesia (3.5)"
            )
        ),
        SalaamService(
            title = Tr("۴ تا ۸ افغانی در دقیقه", "AFN 4-8 per minute", "۴ تر ۸ افغانۍ په دقیقه کې"),
            note = Tr(
                "Australia (4), Japan (4), Mongolia (4.5), Iceland (4.5), China (6), Greece (6), Hungary (6), Paraguay (6), Thailand (4), Vietnam (5), Bhutan (7), Cambodia (7), Laos (7.5), Uzbekistan (7.5), Czech Republic (8), Liechtenstein (8), Switzerland (8), Venezuela (8), Taiwan (8)",
                "Australia (4), Japan (4), Mongolia (4.5), Iceland (4.5), China (6), Greece (6), Hungary (6), Paraguay (6), Thailand (4), Vietnam (5), Bhutan (7), Cambodia (7), Laos (7.5), Uzbekistan (7.5), Czech Republic (8), Liechtenstein (8), Switzerland (8), Venezuela (8), Taiwan (8)",
                "Australia (4), Japan (4), Mongolia (4.5), Iceland (4.5), China (6), Greece (6), Hungary (6), Paraguay (6), Thailand (4), Vietnam (5), Bhutan (7), Cambodia (7), Laos (7.5), Uzbekistan (7.5), Czech Republic (8), Liechtenstein (8), Switzerland (8), Venezuela (8), Taiwan (8)"
            )
        ),
        SalaamService(
            title = Tr("۹ تا ۱۵ افغانی در دقیقه", "AFN 9-15 per minute", "۹ تر ۱۵ افغانۍ په دقیقه کې"),
            note = Tr(
                "Egypt (9), Germany (10), France (10), Slovakia (10), UAE (10), Yemen (10), Oman (11), Namibia (11), Portugal (12), Iran (12), Jordan (13), Sudan (12), Lebanon (12), Nepal (14), Turkey (14), Argentina (14), Cyprus (14), Kazakhstan (13), Poland (9)",
                "Egypt (9), Germany (10), France (10), Slovakia (10), UAE (10), Yemen (10), Oman (11), Namibia (11), Portugal (12), Iran (12), Jordan (13), Sudan (12), Lebanon (12), Nepal (14), Turkey (14), Argentina (14), Cyprus (14), Kazakhstan (13), Poland (9)",
                "Egypt (9), Germany (10), France (10), Slovakia (10), UAE (10), Yemen (10), Oman (11), Namibia (11), Portugal (12), Iran (12), Jordan (13), Sudan (12), Lebanon (12), Nepal (14), Turkey (14), Argentina (14), Cyprus (14), Kazakhstan (13), Poland (9)"
            )
        ),
        SalaamService(
            title = Tr("۱۶ تا ۳۱ افغانی در دقیقه", "AFN 16-31 per minute", "۱۶ تر ۳۱ افغانۍ په دقیقه کې"),
            note = Tr(
                "Saudi Arabia (16), Iraq (15), Palestine (16), Kyrgyzstan (16), Ireland (31), UK (22), Sweden (20), Israel (20), Myanmar (20), Ukraine (23), Syria (27)",
                "Saudi Arabia (16), Iraq (15), Palestine (16), Kyrgyzstan (16), Ireland (31), UK (22), Sweden (20), Israel (20), Myanmar (20), Ukraine (23), Syria (27)",
                "Saudi Arabia (16), Iraq (15), Palestine (16), Kyrgyzstan (16), Ireland (31), UK (22), Sweden (20), Israel (20), Myanmar (20), Ukraine (23), Syria (27)"
            )
        ),
        SalaamService(
            title = Tr("۹۰ افغانی در دقیقه", "AFN 90 per minute", "۹۰ افغانۍ په دقیقه کې"),
            note = Tr("سایر مقصدها", "All other remaining destinations", "نور پاتې مقصدونه")
        ),
        SalaamService(
            title = Tr("۱۱۰ افغانی در دقیقه", "AFN 110 per minute", "۱۱۰ افغانۍ په دقیقه کې"),
            note = Tr("مقصدهای ماهواره‌ای", "All Satellite destinations", "ټول سپوږمکۍ مقصدونه")
        ),
        // ===== نرخ پیامک بین‌المللی =====
        SalaamService(
            title = Tr("نرخ پیامک بین‌المللی", "International SMS Rates", "د نړیوال پیغام نرخونه")
        ),
        SalaamService(
            title = Tr("۵ افغانی هر پیامک", "AFN 5 per SMS", "۵ افغانۍ هر پیغام"),
            note = Tr(
                "35+ destinations including India, Bangladesh, China, Pakistan, USA, Canada, UAE, Japan, South Korea, Thailand, Vietnam, Philippines",
                "35+ destinations including India, Bangladesh, China, Pakistan, USA, Canada, UAE, Japan, South Korea, Thailand, Vietnam, Philippines",
                "35+ destinations including India, Bangladesh, China, Pakistan, USA, Canada, UAE, Japan, South Korea, Thailand, Vietnam, Philippines"
            )
        ),
        SalaamService(
            title = Tr("۷ افغانی هر پیامک", "AFN 7 per SMS", "۷ افغانۍ هر پیغام"),
            note = Tr(
                "100+ destinations including Australia, UK, Germany, France, Russia, most European and African nations",
                "100+ destinations including Australia, UK, Germany, France, Russia, most European and African nations",
                "100+ destinations including Australia, UK, Germany, France, Russia, most European and African nations"
            )
        ),
        SalaamService(
            title = Tr("۱۰ افغانی هر پیامک", "AFN 10 per SMS", "۱۰ افغانۍ هر پیغام"),
            note = Tr(
                "Ukraine, Abkhazia, French Polynesia, New Caledonia, Vanuatu",
                "Ukraine, Abkhazia, French Polynesia, New Caledonia, Vanuatu",
                "Ukraine, Abkhazia, French Polynesia, New Caledonia, Vanuatu"
            )
        ),
        SalaamService(
            title = Tr("توجه", "Note", "یادونه"),
            note = Tr(
                "همه نرخ‌ها به افغانی است و ممکن است تغییر کند. اعداد داخل پرانتز نرخ هر دقیقه را نشان می‌دهند.",
                "All rates are in Afghan Afghani (AFN) and subject to change. Numbers in parentheses indicate specific per-minute rates.",
                "ټول نرخونه په افغانیو دي او ښايي بدل شي. په قوسینو کې شمېرې د هرې دقیقې نرخ ښيي."
            )
        )
    )
}