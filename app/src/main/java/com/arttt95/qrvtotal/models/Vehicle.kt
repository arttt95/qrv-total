package com.arttt95.qrvtotal.models

import android.graphics.drawable.Drawable

data class Vehicle(
    val id: String = "",
    val plateLetters: String = "",
    val plateNumbers: String = "",
    val brand: String = "N/I",
    val model: String = "N/I",
    val year: String = "N/I",
    val qru: String = "N/I",
    val qth: String = "N/I",
    val days: String = "0",
    val typeVehicle: String = "N/I",
    val color: String = "N/I",
    val licensing: String = "N/I"
)
