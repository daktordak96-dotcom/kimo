package com.kimo.mayapp

data class PackageModel(
    val title: String,
    val price: String,
    val description: String,
    val activationCode: String,
    val deactivationCode: String,
    val checkBalanceCode: String
)
