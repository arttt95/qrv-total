package com.arttt95.qrvtotal.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.qrvtotal.databinding.ActivityAddVehicleBinding
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
    private lateinit var yearAdapter: ArrayAdapter<String>
    private lateinit var qruAdapter: ArrayAdapter<String>
    private lateinit var qthAdapter: ArrayAdapter<String>
    private lateinit var typeVehicleAdapter: ArrayAdapter<String>
    private lateinit var colorAdapter: ArrayAdapter<String>

    private val brands = arrayOf("Toyota", "Ford", "Chevrolet", "Nissan", "Honda", "Fiat", "BMW", "Mercedes", "Land Rover", "Mitsubishi", "Renault", "Hyundai", "Outros")
    private val cities = arrayOf("SEM QTH", "Campinas", "Sta Barbara", "Piracicaba", "Monte-Mor", "Hortolândia", "Sumaré", "Limeira", "Paulínia", "Nova Odessa", "São Paulo", "Outras")
    private val colors = arrayOf("PT", "BR", "CZ", "VM", "AZ", "VD", "AM", "LR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupDropdowns()

        binding.btnInserir.setOnClickListener {

            addVehicle()

        }

    }

    private fun setupDropdowns() {

        val sortedBrands = brands.sortedArray() // Ordena o array de marcas
        brandAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sortedBrands)
        binding.editTextAddBrand.setAdapter(brandAdapter)

        val years = (1980..2024).reversed().map { it.toString().takeLast(2) }.toTypedArray()
        yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)
        binding.editTextAddYear.setAdapter(yearAdapter)

        qruAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("SEM QRU", "B01", "B04", "Sequestro", "Ação Criminosa", "Procurado"))
        binding.editTextAddQru.setAdapter(qruAdapter)

        qthAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        binding.editTextAddQth.setAdapter(qthAdapter)

        typeVehicleAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("Carro", "Moto", "Ônibus", "Caminhão"))
        binding.editTextAddTypeVehicle.setAdapter(typeVehicleAdapter)

        colorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colors)
        binding.editTextAddColor.setAdapter(colorAdapter)

    }

    private fun addVehicle() {

        val plateLetters = binding.editTextAddPlateLetters.text.toString().trim()
        val plateNumbers = binding.editTextAddPlateNumbers.text.toString().trim()
        val brand = binding.editTextAddBrand.text.toString().trim()
        val model = binding.editTextAddModel.text.toString().trim()
        val year = binding.editTextAddYear.text.toString().trim().toIntOrNull()
        val qru = binding.editTextAddQru.text.toString().trim()
        val qth = binding.editTextAddQth.text.toString().trim()
        val typeVehicle = binding.editTextAddTypeVehicle.text.toString().trim()
        val color = binding.editTextAddColor.text.toString().trim()

        if(!validarCampos()) {
            return
        }

        val vehicleId = firestore.collection("vehicles").document().id

        val days = 0

        val vehicleData = hashMapOf(
            "id" to vehicleId,
            "plateLetters" to plateLetters,
            "plateNumbers" to plateNumbers,
            "brand" to brand,
            "model" to model,
            "year" to year,
            "qru" to qru,
            "qth" to qth,
            "days" to days,
            "createdAt" to FieldValue.serverTimestamp(),
            "typeVehicle" to typeVehicle,
            "color" to color,
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

        val plateLetters = binding.editTextAddPlateLetters.text.toString().trim()
        val plateNumbers = binding.editTextAddPlateNumbers.text.toString().trim()
        val brand = binding.editTextAddBrand.text.toString().trim()
        val model = binding.editTextAddModel.text.toString().trim()
        val qru = binding.editTextAddQru.text.toString().trim()
        val qth = binding.editTextAddQth.text.toString().trim()
        val typeVehicle = binding.editTextAddTypeVehicle.text.toString().trim()
        val color = binding.editTextAddColor.text.toString().trim()

        if(plateLetters.isNotEmpty()) {

            binding.textInputAddPlateLetters.error = null

            if(plateNumbers.isNotEmpty()) {

                binding.textInputAddPlateNumbers.error = null

                if(brand.isNotEmpty()) {

                    binding.textInputAddBrand.error = null

                    if(model.isNotEmpty()) { // model

                        binding.textInputAddModel.error = null

                        if(qru.isNotEmpty()) { // qru

                            binding.textInputAddQru.error = null

                            if(qth.isNotEmpty()) { // qth

                                binding.textInputAddQth.error = null

                                if(typeVehicle.isNotEmpty()) {

                                    binding.textInputAddTypeVehicle.error = null

                                    if(color.isNotEmpty()) {

                                        binding.textInputAddColor.error = null
                                        return true

                                    } else {

                                        binding.textInputAddColor.error = "Preencha a cor do veículo"
                                        return false

                                    }

                                } else {

                                    binding.textInputAddTypeVehicle.error = "Preencha o tipo do veículo"
                                    return false
                                }

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

                // else plateNumbers
                binding.textInputAddPlateNumbers.error = "Preencha os números da placa"
                return false
            }

        } else {
            // else plateLetters
            binding.textInputAddPlateLetters.error = "Preencha as letras da placa"
            return false
        }

    }
}