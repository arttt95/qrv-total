package com.arttt95.qrvtotal.models

import android.graphics.drawable.Drawable

data class Vehicle(

    val id: String = "",
    val plateLetters: String = "",
    val plateNumbers: String = "",
    val brand: String = "Marca",
    val model: String = "Modelo",
    val year: Int?= null,
    val qru: String = "QRU",
    val qth: String = "QTH",
    val days: Int? = null,
    val typeVehicle: String = "Tipo",
    val color: String = "Cor",

)
