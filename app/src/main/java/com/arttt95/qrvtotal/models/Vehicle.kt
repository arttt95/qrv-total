package com.arttt95.qrvtotal.models

import android.graphics.drawable.Drawable

data class Vehicle(

    val id: String = "",
    val plate: String = "",
    val brand: String = "Marca",
    val model: String = "Modelo",
    val year: Int?= null,
    val qru: String = "QRU",
    val qth: String = "QTH",
    val days: Int? = null,
    val typeVehicle: String = "Tipo",
    val color: String = "Cor",

) {
    val plateLetters: String
        get() = plate.take(3) // Pega os 3 primeiros caracteres

    val plateNumbers: String
        get() = plate.drop(3) // Pega o restante da string

}
