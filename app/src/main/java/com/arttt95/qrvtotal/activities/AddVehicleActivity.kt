package com.arttt95.qrvtotal.activities

import android.os.Bundle
import android.text.InputFilter
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arttt95.qrvtotal.databinding.ActivityAddVehicleBinding
import com.arttt95.qrvtotal.utils.Arrays
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
    private lateinit var licensingAdapter: ArrayAdapter<String>

    private val brands = Arrays.brands
    private val cities = Arrays.nearbyCities + Arrays.citiesOfSaoPaulo.sorted()
    private val colors = Arrays.colors
    private val qruList = listOf("B01", "B04", "Ação Criminosa") + listOf("N/I", "Outro", "Sequestro").sorted()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        filtrarPlate()
        setupDropdowns()

        binding.btnInserir.setOnClickListener {

            addVehicle()

        }

    }

    private fun setupDropdowns() {

        val sortedBrands = brands.sortedArray() // Ordena o array de marcas
        brandAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sortedBrands)
        binding.editTextAddBrand.setAdapter(brandAdapter)

        val years = (1980..2025).reversed().map { it.toString().takeLast(2) }.toTypedArray()
        yearAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)
        binding.editTextAddYear.setAdapter(yearAdapter)

        qruAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, qruList)
        binding.editTextAddQru.setAdapter(qruAdapter)

        qthAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        binding.editTextAddQth.setAdapter(qthAdapter)

        typeVehicleAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("Carro", "Moto", "Ônibus", "Caminhão"))
        binding.editTextAddTypeVehicle.setAdapter(typeVehicleAdapter)

        colorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colors)
        binding.editTextAddColor.setAdapter(colorAdapter)

        val yearsLicensing = (2000..2025).reversed().map { it.toString().takeLast(2) }.toTypedArray()
        licensingAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, yearsLicensing)
        binding.editTextAddLicensing.setAdapter(licensingAdapter)

    }

    private fun addVehicle() {

        val plateLetters = binding.editTextAddPlateLetters.text.toString().trim()
        val plateNumbers = binding.editTextAddPlateNumbers.text.toString().trim()
        val brand = binding.editTextAddBrand.text.toString().trim()
        val model = binding.editTextAddModel.text.toString().trim()
        val year = binding.editTextAddYear.text.toString().trim()
        val qru = binding.editTextAddQru.text.toString().trim()
        val qth = binding.editTextAddQth.text.toString().trim()
        val typeVehicle = binding.editTextAddTypeVehicle.text.toString().trim()
        val color = binding.editTextAddColor.text.toString().trim()
        val licensing = binding.editTextAddLicensing.text.toString().trim()

        if(!validarCampos()) {
            return
        }

        val vehicleId = firestore.collection("vehicles").document().id


        val vehicleData = hashMapOf(
            "id" to vehicleId,
            "plateLetters" to plateLetters,
            "plateNumbers" to plateNumbers,
            "brand" to brand.ifEmpty { "N/I" },
            "model" to model.ifEmpty { "N/I" },
            "year" to year.ifEmpty { "N/I" },
            "qru" to qru.ifEmpty { "N/I" },
            "qth" to qth.ifEmpty { "N/I" },
            "days" to "0",
            "createdAt" to FieldValue.serverTimestamp(),
            "typeVehicle" to typeVehicle.ifEmpty { "N/I" },
            "color" to color.ifEmpty { "N/I" },
            "licensing" to licensing.ifEmpty { "N/I" },
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

    private fun validarCampos(): Boolean {
        val plateLetters = binding.editTextAddPlateLetters.text.toString().trim()
        val plateNumbers = binding.editTextAddPlateNumbers.text.toString().trim()

        var valid = true

        if (plateLetters.isEmpty()) {
            binding.textInputAddPlateLetters.error = "Preencha as letras da placa"
            valid = false
        } else {
            binding.textInputAddPlateLetters.error = null
        }

        if (plateNumbers.isEmpty()) {
            binding.textInputAddPlateNumbers.error = "Preencha os números da placa"
            valid = false
        } else {
            binding.textInputAddPlateNumbers.error = null
        }

        return valid
    }

    private fun filtrarPlate() {

        // Filtro para aceitar somente letras maiúsculas (A-Z)
        val lettersFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!source[i].isLetter() || !source[i].isUpperCase()) {
                    return@InputFilter ""
                }
            }
            null
        }

        // Filtro para o campo de plateNumbers (4 caracteres):
        // Posição 0, 2 e 3: somente dígitos
        // Posição 1: dígito ou letra maiúscula
        val plateNumbersFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val newText = dest.toString().substring(0, dstart) +
                    source.subSequence(start, end) +
                    dest.toString().substring(dend)
            if (newText.length > 4) return@InputFilter ""
            for (i in newText.indices) {
                when (i) {
                    0, 2, 3 -> {
                        if (!newText[i].isDigit()) return@InputFilter ""
                    }
                    1 -> {
                        if (!(newText[i].isDigit() || (newText[i].isLetter() && newText[i].isUpperCase()))) {
                            return@InputFilter ""
                        }
                    }
                }
            }
            null
        }

        // Aplique os filtros aos campos:
        binding.editTextAddPlateLetters.filters = arrayOf(InputFilter.LengthFilter(3), lettersFilter)
        binding.editTextAddPlateNumbers.filters = arrayOf(InputFilter.LengthFilter(4), plateNumbersFilter)

    }
}