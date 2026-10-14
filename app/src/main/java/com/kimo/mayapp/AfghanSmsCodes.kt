package com.kimo.mayapp

object AfghanSmsCodes {

    // کد چک بیلانس بسته‌های پیام
    const val CHECK = "*367*4#"

    class Codes(val on: String, val off: String?)

    // کلید: (شماره تب، شماره بسته در همان تب)
    // تب 0 = روزانه، بسته 0 = «500 پیام روزانه»
    private val map = mapOf(
        Pair(0, 0) to Codes(on = "*367*1#", off = "*367*2#")
    )

    fun get(tab: Int, index: Int): Codes? = map[Pair(tab, index)]
}