package com.kimo.mayapp

class RoshanService(
    val title: Tr,
    val code: Tr? = null,
    val note: Tr? = null,
    val extras: List<Tr> = emptyList(),
    val rows: List<Pair<String, String>> = emptyList(),
    val action: Int = RoshanServicesData.INFO,
    val target: String = "",
    val smsBody: String = "",
    val button: Tr? = null
)

object RoshanServicesData {

    const val INFO = 0
    const val DIAL = 1
    const val SMS = 2

    val screenTitle = Tr(
        "خدمات روشن",
        "Roshan Services",
        "د روشن خدمتونه"
    )

    private val btnLoan = Tr("قرضه", "Loan", "پور")

    val items = listOf(
        RoshanService(
            title = Tr("دقیقه باقیمانده", "Remaining Minutes", "پاتې دقیقې"),
            code = Tr("*444#", "*444#", "*444#"),
            action = DIAL,
            target = "*444#",
            button = Tr("دقیقه باقیمانده", "Remaining Minutes", "پاتې دقیقې")
        ),
        RoshanService(
            title = Tr("قرضه 40 افغانی", "40 AFN Loan", "40 افغانۍ پور"),
            code = Tr("*122#", "*122#", "*122#"),
            action = DIAL,
            target = "*122#",
            button = btnLoan
        ),
        RoshanService(
            title = Tr("قرضه 100MB", "100MB Loan", "100MB پور"),
            note = Tr(
                "حروف 'L 100' را به 200 بفرستید",
                "Send the letters 'L 100' to 200",
                "'L 100' توري 200 ته ولیږئ"
            ),
            action = SMS,
            target = "200",
            smsBody = "L 100",
            button = btnLoan
        ),
        RoshanService(
            title = Tr("ارسال کردیت", "Send Credit", "کریډیټ لیږل"),
            code = Tr("*123*079xxxxxxx*مبلغ#", "*123*079xxxxxxx*AMOUNT#", "*123*079xxxxxxx*مبلغ#")
        ),
        RoshanService(
            title = Tr("ارسال انترنت MB", "Send Internet MB", "انټرنټ MB لیږل"),
            code = Tr("*500*مبلغ*079xxxxxxx#", "*500*AMOUNT*079xxxxxxx#", "*500*مبلغ*079xxxxxxx#"),
            extras = listOf(
                Tr(
                    "مبلغ = 10MB, 20MB, 30MB, 40MB, 50MB",
                    "Amount = 10MB, 20MB, 30MB, 40MB, 50MB",
                    "مبلغ = 10MB, 20MB, 30MB, 40MB, 50MB"
                ),
                Tr(
                    "حداکثر انتقال در یک روز 150MB",
                    "Maximum transfer per day: 150MB",
                    "په یوه ورځ کې اعظمي لیږد 150MB"
                ),
                Tr(
                    "هزینه ارسال Af 5",
                    "Transfer fee: Af 5",
                    "د لیږلو لګښت Af 5"
                )
            )
        ),
        RoshanService(
            title = Tr("تنظیمات APN", "APN Settings", "د APN تنظیمات"),
            rows = listOf(
                "Name:" to "Roshan",
                "APN:" to "internet",
                "Username:" to "gprs",
                "Password:" to "roshan"
            )
        ),
        RoshanService(
            title = Tr("اضافه کردن کردیت", "Add Credit", "کریډیټ اضافه کول"),
            code = Tr("*222*xxxxxxxxxxxxxx#", "*222*xxxxxxxxxxxxxx#", "*222*xxxxxxxxxxxxxx#")
        ),
        RoshanService(
            title = Tr("فعال سازی 3G", "Activate 3G", "3G فعالول"),
            note = Tr(
                "حروف '3G' را به 555 بفرستید",
                "Send the letters '3G' to 555",
                "'3G' توري 555 ته ولیږئ"
            ),
            action = SMS,
            target = "555",
            smsBody = "3G",
            button = Tr("فعال سازی", "Activate", "فعالول")
        )
    )
}