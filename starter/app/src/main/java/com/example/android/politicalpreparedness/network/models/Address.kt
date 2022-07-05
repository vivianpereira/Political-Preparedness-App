package com.example.android.politicalpreparedness.network.models

data class Address(
    val line1: String,
    val line2: String? = null,
    val city: String,
    val state: String,
    val zip: String
)
