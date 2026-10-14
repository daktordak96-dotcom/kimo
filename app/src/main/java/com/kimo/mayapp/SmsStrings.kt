package com.kimo.mayapp

import android.content.Context

object SmsStrings {

    private val data = mapOf(
        // روزانه
        "sms_daily_title" to mapOf(
            "fa" to "200 پیام روزانه",
            "en" to "200 SMS Daily",
            "ps" to "200 ورځني پیغامونه"
        ),
        "sms_daily_price" to mapOf(
            "fa" to "10 افغانی", "en" to "10 AFN", "ps" to "10 افغانۍ"
        ),
        "sms_daily_desc" to mapOf(
            "fa" to "200 پیام برای یک روز به اتصالات به قیمت 10 افغانی.\nبرای فعال سازی روی دکمه سبز و برای غیرفعال سازی روی دکمه سرخ فشار دهید.\nبرای فعال سازی BDS و برای غیر فعال سازی PU BDS را به شماره 3378 ارسال نمائید.\nبرای چک کردن بیلانس باقیمانده 505 را به 606 بفرستید.",
            "en" to "200 SMS for one day on Etisalat, price 10 AFN.\nTo activate press the green button, to deactivate press the red button.\nTo activate send BDS and to deactivate send PU BDS to 3378.\nTo check your remaining balance send 505 to 606.",
            "ps" to "د یوې ورځې لپاره 200 پیغامونه په اتصالاتو کې، بیه 10 افغانۍ.\nد فعالولو لپاره پر شنه تڼۍ او د غیر فعالولو لپاره پر سره تڼۍ کلیک وکړئ.\nد فعالولو لپاره BDS او د غیر فعالولو لپاره PU BDS ته 3378 ولیږئ.\nد پاتې بیلانس د کتلو لپاره 505 ته 606 ولیږئ."
        ),
        // هفتگی
        "sms_weekly_title" to mapOf(
            "fa" to "2000 پیام هفته ای",
            "en" to "2000 SMS Weekly",
            "ps" to "2000 اونیز پیغامونه"
        ),
        "sms_weekly_price" to mapOf(
            "fa" to "30 افغانی", "en" to "30 AFN", "ps" to "30 افغانۍ"
        ),
        "sms_weekly_desc" to mapOf(
            "fa" to "2000 پیام برای یک هفته به اتصالات به قیمت 30 افغانی.\nبرای فعال سازی روی دکمه سبز و برای غیرفعال سازی روی دکمه سرخ فشار دهید.\nبرای فعال سازی BWS و برای غیر فعال سازی PU BWS را به شماره 3378 ارسال نمائید.\nبرای چک کردن بیلانس باقیمانده 505 را به 606 بفرستید.",
            "en" to "2000 SMS for one week on Etisalat, price 30 AFN.\nTo activate press the green button, to deactivate press the red button.\nTo activate send BWS and to deactivate send PU BWS to 3378.\nTo check your remaining balance send 505 to 606.",
            "ps" to "د یوې اونۍ لپاره 2000 پیغامونه په اتصالاتو کې، بیه 30 افغانۍ.\nد فعالولو لپاره پر شنه تڼۍ او د غیر فعالولو لپاره پر سره تڼۍ کلیک وکړئ.\nد فعالولو لپاره BWS او د غیر فعالولو لپاره PU BWS ته 3378 ولیږئ.\nد پاتې بیلانس د کتلو لپاره 505 ته 606 ولیږئ."
        ),
        // ماهانه
        "sms_monthly_title" to mapOf(
            "fa" to "20000 پیام ماهانه",
            "en" to "20000 SMS Monthly",
            "ps" to "20000 میاشتني پیغامونه"
        ),
        "sms_monthly_price" to mapOf(
            "fa" to "65 افغانی", "en" to "65 AFN", "ps" to "65 افغانۍ"
        ),
        "sms_monthly_desc" to mapOf(
            "fa" to "20000 پیام برای یک ماه به اتصالات به قیمت 65 افغانی.\nبرای فعال سازی روی دکمه سبز و برای غیرفعال سازی روی دکمه سرخ فشار دهید.\nبرای فعال سازی BMS و برای غیر فعال سازی PU BMS را به شماره 3378 ارسال نمائید.\nبرای چک کردن بیلانس باقیمانده 505 را به 606 بفرستید.",
            "en" to "20000 SMS for one month on Etisalat, price 65 AFN.\nTo activate press the green button, to deactivate press the red button.\nTo activate send BMS and to deactivate send PU BMS to 3378.\nTo check your remaining balance send 505 to 606.",
            "ps" to "د یوې میاشتې لپاره 20000 پیغامونه په اتصالاتو کې، بیه 65 افغانۍ.\nد فعالولو لپاره پر شنه تڼۍ او د غیر فعالولو لپاره پر سره تڼۍ کلیک وکړئ.\nد فعالولو لپاره BMS او د غیر فعالولو لپاره PU BMS ته 3378 ولیږئ.\nد پاتې بیلانس د کتلو لپاره 505 ته 606 ولیږئ."
        ),
        // ترکیبی: بسته اول
        "sms_combo1_title" to mapOf(
            "fa" to "1000 دقیقه پریمیوم",
            "en" to "1000 Premium Minutes",
            "ps" to "1000 پریمیم دقیقې"
        ),
        "sms_combo1_price" to mapOf(
            "fa" to "200 افغانی", "en" to "200 AFN", "ps" to "200 افغانۍ"
        ),
        "sms_combo1_desc" to mapOf(
            "fa" to "بسته 1000 دقیقه تماس اتصالات + 200 ام بی انترنت برای 30 روز به قیمت 200 افغانی. تمدید خودکار موجود است.\nبرای فعال سازی M200 و برای غیرفعال سازی PU M200 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید یا به 888 زنگ بزنید.",
            "en" to "1000 Etisalat call minutes + 200 MB internet for 30 days, price 200 AFN. Auto-renewal is available.\nTo activate send M200 and to deactivate send PU M200 to 3378.\nTo check your balance send 505 to 606 or call 888.",
            "ps" to "د اتصالاتو 1000 د زنګ دقیقې + 200 MB انټرنټ د 30 ورځو لپاره، بیه 200 افغانۍ. اتوماتیک تمدید شته.\nد فعالولو لپاره M200 او د غیر فعالولو لپاره PU M200 ته 3378 ولیږئ.\nد بیلانس د کتلو لپاره 505 ته 606 ولیږئ یا 888 ته زنګ ووهئ."
        ),
        // ترکیبی: بسته دوم
        "sms_combo2_title" to mapOf(
            "fa" to "125 دقیقه + 50 پیامک",
            "en" to "125 Minutes + 50 SMS",
            "ps" to "125 دقیقې + 50 پیغامونه"
        ),
        "sms_combo2_price" to mapOf(
            "fa" to "40 افغانی", "en" to "40 AFN", "ps" to "40 افغانۍ"
        ),
        "sms_combo2_desc" to mapOf(
            "fa" to "بسته 125 دقیقه تماس اتصالات + 50 پیامک برای یک روز به قیمت 40 افغانی. تمدید خودکار موجود است.\nبرای فعال سازی 700 و برای غیرفعال سازی PU 700 را به 3378 ارسال نمائید.\nبرای بررسی موجودی 505 را به 606 ارسال نمائید یا به 888 زنگ بزنید.",
            "en" to "125 Etisalat call minutes + 50 SMS for one day, price 40 AFN. Auto-renewal is available.\nTo activate send 700 and to deactivate send PU 700 to 3378.\nTo check your balance send 505 to 606 or call 888.",
            "ps" to "د اتصالاتو 125 د زنګ دقیقې + 50 پیغامونه د یوې ورځې لپاره، بیه 40 افغانۍ. اتوماتیک تمدید شته.\nد فعالولو لپاره 700 او د غیر فعالولو لپاره PU 700 ته 3378 ولیږئ.\nد بیلانس د کتلو لپاره 505 ته 606 ولیږئ یا 888 ته زنګ ووهئ."
        )
    )

    fun get(context: Context, key: String): String =
        data[key]?.get(Strings.currentLang(context)) ?: data[key]?.get("fa") ?: key
}