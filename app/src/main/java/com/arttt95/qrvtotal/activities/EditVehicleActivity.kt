package com.arttt95.qrvtotal.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.arttt95.qrvtotal.databinding.ActivityEditVehicleBinding
import com.arttt95.qrvtotal.models.Vehicle
import com.arttt95.qrvtotal.utils.exibirMensagem
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditVehicleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditVehicleBinding.inflate(layoutInflater)
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
    private lateinit var licensingAdapter: ArrayAdapter<String>

    private val brands = arrayOf("Toyota", "Ford", "Chevrolet", "Nissan", "Honda", "Fiat", "BMW", "Mercedes", "Land Rover", "Mitsubishi", "Renault", "Hyundai", "Outros")
    private val cities = arrayOf("SEM QTH", "Campinas", "Sta Barbara", "Piracicaba", "Monte-Mor", "Hortolândia", "Sumaré", "Limeira", "Paulínia", "Nova Odessa", "São Paulo", "Outras")
    private val colors = arrayOf("PT", "BR", "CZ", "VM", "AZ", "VD", "AM", "LR")
    private val qruList = listOf("B01", "B04", "Ação Criminosa") + listOf("SEM QRU", "Outro", "Sequestro").sorted()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClearModelIcon()

        val vehicleId = intent.getStringExtra("VEHICLE_ID")
        if (vehicleId == null) {
            exibirMensagem("Veículo não informado")
            finish()
        } else {
            setupDropdowns()
            loadVehicleData(vehicleId)
        }

        binding.btnEditar.setOnClickListener {
            if (vehicleId != null) {
                updateVehicleData(vehicleId)
            }
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClearModelIcon() {
        binding.editTextEditModel.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_END = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.editTextEditModel.right - binding.editTextEditModel.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    binding.editTextEditModel.setText("")
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun setupDropdowns() {

        val sortedBrands = brands.sortedArray()
        brandAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sortedBrands)
        binding.editTextEditBrand.setAdapter(brandAdapter)

        val years = (1980..2025).reversed().map { it.toString().takeLast(2) }.toTypedArray()
        yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)
        binding.editTextEditYear.setAdapter(yearAdapter)

        qruAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, qruList)
        binding.editTextEditQru.setAdapter(qruAdapter)

        qthAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        binding.editTextEditQth.setAdapter(qthAdapter)

        typeVehicleAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("Carro", "Moto", "Ônibus", "Caminhão"))
        binding.editTextEditTypeVehicle.setAdapter(typeVehicleAdapter)

        colorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colors)
        binding.editTextEditColor.setAdapter(colorAdapter)

        /*// Adiciona a verificação de nulidade e tratamento de exceção
        if (binding.editTextEditLicensing != null && licensingAdapter != null) {
            try {
                binding.editTextEditLicensing.setAdapter(licensingAdapter)
            } catch (e: Exception) {
                e.printStackTrace()
                exibirMensagem("Erro ao configurar o adaptador de licenciamento: ${e.message}")
            }
        } else {
            exibirMensagem("editTextEditLicensing ou licensingAdapter é nulo.")
        }*/

        /*val licensingYears = (2000..2025).reversed().map { it.toString().takeLast(2) }.toTypedArray()
        licensingAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, licensingYears)
        binding.editTextEditYear.setAdapter(licensingAdapter)*/

        val licensingYears = (2000..2025).reversed().map { it.toString().takeLast(2) }.toTypedArray()
        licensingAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, licensingYears)
        binding.editTextEditLicensing.setAdapter(licensingAdapter)

    }

    private fun updateVehicleData(vehicleId: String) {

        val plateLetters = binding.editTextEditPlateLetters.text.toString().trim()
        val plateNumbers = binding.editTextEditPlateNumbers.text.toString().trim()
        val brand = binding.editTextEditBrand.text.toString().trim()
        val model = binding.editTextEditModel.text.toString().trim()
        val year = binding.editTextEditYear.text.toString().trim()
        val licensing = binding.editTextEditLicensing.text.toString().trim()
        val qru = binding.editTextEditQru.text.toString().trim()
        val qth = binding.editTextEditQth.text.toString().trim()
        val typeVehicle = binding.editTextEditTypeVehicle.text.toString().trim()
        val color = binding.editTextEditColor.text.toString().trim()

        val updates = hashMapOf<String, Any>(
            "plateLetters" to plateLetters,
            "plateNumbers" to plateNumbers,
            "brand" to brand,
            "model" to model,
            "year" to year,
            "licensing" to licensing,
            "qru" to qru,
            "qth" to qth,
            "typeVehicle" to typeVehicle,
            "color" to color,
            "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )

        lifecycleScope.launch {
            try {
                firestore.collection("vehicles").document(vehicleId)
                    .update(updates).await()
                exibirMensagem("Veículo atualizado com sucesso!")
                finish()
            } catch (e: Exception) {
                exibirMensagem("Erro ao atualizar veículo: ${e.message}")
            }
        }
    }

    private fun loadVehicleData(vehicleId: String) {

        lifecycleScope.launch {
            try {
                val doc = firestore.collection("vehicles").document(vehicleId).get().await()
                if (doc.exists()) {
                    val vehicle = doc.toObject<Vehicle>()?.copy(id = vehicleId)
                    if (vehicle != null) {
                        binding.editTextEditPlateLetters.setText(vehicle.plateLetters)
                        binding.editTextEditPlateNumbers.setText(vehicle.plateNumbers)
                        binding.editTextEditBrand.setText(vehicle.brand)
                        binding.editTextEditModel.setText(vehicle.model)
                        binding.editTextEditYear.setText(vehicle.year)
                        binding.editTextEditLicensing.setText(vehicle.licensing)
                        binding.editTextEditQru.setText(vehicle.qru)
                        binding.editTextEditQth.setText(vehicle.qth)
                        binding.editTextEditTypeVehicle.setText(vehicle.typeVehicle)
                        binding.editTextEditColor.setText(vehicle.color)
                    }
                } else {
                    exibirMensagem("Veículo não encontrado")
                    finish()
                }

            } catch (e: Exception) {
                exibirMensagem("Erro ao carregar os dados do veículo")
                finish()
            }
        }

    }


}