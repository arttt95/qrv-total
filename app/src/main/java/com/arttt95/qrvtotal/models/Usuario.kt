package com.arttt95.qrvtotal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
) : Parcelable
