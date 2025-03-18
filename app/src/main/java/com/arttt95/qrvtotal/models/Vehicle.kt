package com.arttt95.qrvtotal.models

data class Vehicle(

    val id: String = "",
    val plate: String = "",
    val brand: String = "",
    val model: String = "",
    val year: Int?= null,
    val qru: String = "",
    val qth: String = "",
    val days: Int? = null

) {
    val plateLetters: String
        get() = plate.take(3) // Pega os 3 primeiros caracteres

    val plateNumbers: String
        get() = plate.drop(3) // Pega o restante da string

}
