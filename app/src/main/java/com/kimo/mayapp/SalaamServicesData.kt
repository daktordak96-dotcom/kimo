package com.kimo.mayapp

class SalaamService(
    val title: Tr,
    val code: Tr? = null,
    val note: Tr? = null,
    val extras: List<Tr> = emptyList(),
    val rows: List<Pair<String, String>> = emptyList(),
    val button: Tr? = null,
    val action: Int = SalaamServicesData.INFO,
    val target: String = "",
    val smsBody: String = ""
)

object SalaamServicesData {

    const val INFO = 0
    const val DIAL = 1
    const val SMS = 2

    val screenTitle = Tr("خدمات سلام", "Salaam Services", "د سلام خدمتونه")

    val items: List<SalaamService> = listOf(
        // دقیقه باقیمانده
        SalaamService(
            title = Tr("دقیقه باقیمانده", "Remaining Minutes", "پاتې دقیقې"),
            code = Tr("*888#", "*888#", "*888#"),
            button = Tr("دقیقه باقیمانده", "Remaining Minutes", "پاتې دقیقې"),
            action = DIAL,
            target = "*888#"
        ),
        // قرضه 20 افغانی
        SalaamService(
            title = Tr("قرضه 20 افغانی", "20 AFN Loan", "د 20 افغانیو پور"),
            code = Tr("*742#", "*742#", "*742#"),
            button = Tr("قرضه", "Get Loan", "پور واخلئ"),
            action = DIAL,
            target = "*742#"
        ),
        // ارسال کردیت
        SalaamService(
            title = Tr("ارسال کردیت", "Send Credit", "کریډیټ لیږل"),
            code = Tr("*741*074xxxxxxx*مبلغ#", "*741*074xxxxxxx*Amount#", "*741*074xxxxxxx*مقدار#")
        ),
        // تنظیمات APN
        SalaamService(
            title = Tr("تنظیمات APN", "APN Settings", "د APN تنظیمات"),
            rows = listOf(
                "Name" to "Salaam",
                "APN" to "salaam",
                "Username" to "salaam",
                "Password" to "salaam"
            )
        ),
        // اضافه کردن کردیت
        SalaamService(
            title = Tr("اضافه کردن کردیت", "Add Credit", "کریډیټ اضافه کول"),
            code = Tr("*777*xxxxxxxxxxxxxxx#", "*777*xxxxxxxxxxxxxxx#", "*777*xxxxxxxxxxxxxxx#")
        ),
        // فعال سازی 3G
        SalaamService(
            title = Tr("فعال سازی 3G", "Activate 3G", "3G فعالول"),
            note = Tr(
                "حروف 'DATA' را به 740 بفرستید",
                "Send the letters 'DATA' to 740",
                "'DATA' توري 740 ته ولیږئ"
            ),
            button = Tr("فعال سازی", "Activate", "فعالول"),
            action = SMS,
            target = "740",
            smsBody = "DATA"
        ),
        // با من تماس بگیر
        SalaamService(
            title = Tr("با من تماس بگیر", "Call Me Back", "ما ته زنګ ووهئ"),
            code = Tr("*666*074xxxxxxx#", "*666*074xxxxxxx#", "*666*074xxxxxxx#")
        )
    )
}