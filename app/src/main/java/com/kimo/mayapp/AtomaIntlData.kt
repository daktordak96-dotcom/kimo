package com.kimo.mayapp

import android.content.Context

class AtomaIntl(
    val title: Tr,
    val price: Tr,
    val intro: Tr,
    val onSmsBody: String? = null,   // اگر پر باشد، فعال‌سازی با پیامک به 303
    val offSmsBody: String? = null,
    val onDial: String? = null,      // اگر پر باشد، فعال‌سازی با شماره‌گیری
    val onAns: String = "",
    val offDial: String? = null,
    val offAns: String = "",
    val checkDial: String
) {
    fun description(ctx: Context): String {
        val sb = StringBuilder(intro.get(ctx))
        if (onSmsBody != null) {
            sb.append("\n").append(SMS_LINE.get(ctx)
                .replace("{on}", onSmsBody).replace("{off}", offSmsBody ?: "U"))
        } else {
            sb.append("\n").append(AtomaPackage.ON_ANS.get(ctx)
                .replace("{code}", onDial ?: "").replace("{ans}", onAns))
            sb.append("\n").append(AtomaPackage.OFF_ANS.get(ctx)
                .replace("{code}", offDial ?: "").replace("{ans}", offAns))
        }
        sb.append("\n").append(CHECK.get(ctx).replace("{code}", checkDial))
        return sb.toString()
    }

    companion object {
        val SMS_LINE = Tr(
            "برای فعال سازی '{on}' را به 303 بفرستید. برای غیر فعال سازی '{off}' را به 303 بفرستید.",
            "To activate send '{on}' to 303. To deactivate send '{off}' to 303.",
            "د فعالولو لپاره '{on}' 303 ته ولیږئ. د غیر فعالولو لپاره '{off}' 303 ته ولیږئ."
        )
        val CHECK = Tr(
            "برای دیدن بالانس باقیمانده {code} شماره گیری کنید.",
            "To see your remaining balance dial {code}.",
            "د پاتې بیلانس د کتلو لپاره {code} ډایل کړئ."
        )
    }
}

object AtomaIntlData {

    const val SMS_NUMBER = "303"

    private fun smsPkg(
        titleFa: String, titleEn: String, titlePs: String,
        price: String, minutes: String, days: Int, daysFa: String,
        code: String, check: String
    ) = AtomaIntl(
        title = Tr(titleFa, titleEn, titlePs),
        price = Tr("$price افغانی", "$price AFN", "$price افغانۍ"),
        intro = Tr(
            "بسته $minutes دقیقه مکالمه به ایران (اپراتورهای سلولی) برای $daysFa به قیمت $price افغانی.",
            "$minutes minutes of calls to Iran (mobile operators) for $days day(s), price $price AFN.",
            "د $days ورځو لپاره ایران ته $minutes دقیقې خبرې (ګرځنده اپراتورونه)، بیه $price افغانۍ."
        ),
        onSmsBody = code, offSmsBody = "U", checkDial = check
    )

    private fun kakaPkg(
        cityFa: String, cityEn: String, cityPs: String, check: String
    ) = AtomaIntl(
        title = Tr(
            "ایران کاکا $cityFa روزانه - 10 دقیقه",
            "Iran Kaka $cityEn Daily - 10 Minutes",
            "ایران کاکا $cityPs ورځنی - 10 دقیقې"
        ),
        price = Tr("50 افغانی", "50 AFN", "50 افغانۍ"),
        intro = Tr(
            "بسته 10 دقیقه مکالمه به ایران (اپراتورهای سلولی) ویژه $cityFa برای 1 روز به قیمت 50 افغانی.",
            "10 minutes of calls to Iran (mobile operators) for $cityEn, 1 day, price 50 AFN.",
            "د 1 ورځې لپاره ایران ته 10 دقیقې خبرې ($cityPs ځانګړې)، بیه 50 افغانۍ."
        ),
        onDial = "*344#", onAns = "1,2,1",
        offDial = "*247#", offAns = "1,2,2",
        checkDial = check
    )

    val packages = listOf(
        smsPkg("ایران روزانه - 10 دقیقه", "Iran Daily - 10 Minutes", "ایران ورځنی - 10 دقیقې",
            "65", "10", 1, "1 روز", "ID", "*789*72#"),
        smsPkg("ایران هفتگی - 20 دقیقه", "Iran Weekly - 20 Minutes", "ایران اونیز - 20 دقیقې",
            "130", "20", 7, "7 روز", "IW", "*789*73#"),
        smsPkg("ایران ماهانه - 40 دقیقه", "Iran Monthly - 40 Minutes", "ایران میاشتنی - 40 دقیقې",
            "260", "40", 30, "30 روز", "IM", "*789*74#"),
        kakaPkg("هرات", "Herat", "هرات", "*789*165#"),
        kakaPkg("قندهار", "Kandahar", "کندهار", "*789*173#")
    )
}