package com.arttt95.qrvtotal.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.qrvtotal.R
import com.arttt95.qrvtotal.adapters.VehicleAdapter
import com.arttt95.qrvtotal.databinding.ActivityAddVehicleBinding
import com.arttt95.qrvtotal.models.Vehicle
import com.arttt95.qrvtotal.utils.exibirMensagem
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddVehicleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddVehicleBinding.inflate(layoutInflater)
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var brandAdapter: ArrayAdapter<String>

    private val brands = arrayOf("Toyota", "Ford", "Chevrolet", "Nissan", "Honda", "Fiat", "BMW", "Mercedez", "Land Rover", "Mitsubish", "Renault", "Hyunday", "Outros")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnInserir.setOnClickListener {

            addVehicle()

        }

    }

    private fun addVehicle() {

        val plate = binding.editTextAddPlate.text.toString().trim()
        val brand = binding.editTextAddBrand.text.toString().trim()
        val model = binding.editTextAddModel.text.toString().trim()
        val year = binding.editTextAddYear.text.toString().trim().toIntOrNull()
        val qru = binding.editTextAddQru.text.toString().trim()
        val qth = binding.editTextAddQth.text.toString().trim()

        if(!validarCampos()) {
            return
        }

        val vehicleId = firestore.collection("vehicles").document().id

        val days = 0

        /*val vehicle = Vehicle(
            id = vehicleId,
            plate = plate,
            brand = brand,
            model = model,
            year = year,
            qru = qru,
            qth = qth,
            days = days
        )*/

        val plateLetters = plate.take(3)
        val plateNumbers = plate.drop(3)

        val vehicleData = hashMapOf(
            "id" to vehicleId,
            "plate" to plate,
            "plateLetters" to plateLetters,
            "plateNumbers" to plateNumbers,
            "brand" to brand,
            "model" to model,
            "year" to year,
            "qru" to qru,
            "qth" to qth,
            "days" to days,
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("vehicles")
            .document(vehicleId)
            .set(vehicleData)
            .addOnSuccessListener {
                exibirMensagem("Veículo adicionado com sucesso")
                finish() // Encerra a Activity e retorna à lista
            }.addOnFailureListener { err ->
                err.printStackTrace()
                exibirMensagem("Erro ao adicionar veiculo: ${err.message}")

            }

    }

    private fun validarCampos() : Boolean {

        val plate = binding.editTextAddPlate.text.toString().trim()
        val brand = binding.editTextAddBrand.text.toString().trim()
        val model = binding.editTextAddModel.text.toString().trim()
        val qru = binding.editTextAddQru.text.toString().trim()
        val qth = binding.editTextAddQth.text.toString().trim()

        if(plate.isNotEmpty()) {

            binding.textInputAddPlate.error = null

            if(brand.isNotEmpty()) {

                binding.textInputAddBrand.error = null

                if(model.isNotEmpty()) { // model

                    binding.textInputAddModel.error = null

                    if(qru.isNotEmpty()) { // qru

                        binding.textInputAddQru.error = null

                        if(qth.isNotEmpty()) { // qth

                            binding.textInputAddQth.error = null
                            return true

                        } else {
                            //else qth
                            binding.textInputAddQth.error = "Preencha o QTH"
                            return false
                        }

                    } else {
                        // qru else
                        binding.textInputAddQru.error = "Preencha o QRU"
                        return false
                    }
                } else {
                    // model else
                    binding.textInputAddModel.error = "Preencha o modelo"
                    return false
                }

            }else {
                // else brand
                binding.textInputAddBrand.error = "Preencha a marca"
                return false
            }

        } else {
            // else plate
            binding.textInputAddPlate.error = "Preencha a Placa"
            return false
        }

    }
}