package com.arttt95.qrvtotal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arttt95.qrvtotal.adapters.VehicleAdapter
import com.arttt95.qrvtotal.databinding.ActivityVehicleListBinding
import com.arttt95.qrvtotal.models.Vehicle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VehicleListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityVehicleListBinding.inflate(layoutInflater)
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var vehicleAdapter: VehicleAdapter
    private var vehicleList = mutableListOf<Vehicle>()
    private var registration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvVehicles.layoutManager = LinearLayoutManager(this)

        binding.btnAddVehicle.setOnClickListener {
            startActivity(Intent(
                this, AddVehicleActivity::class.java
            ))
        }

        loadVehicles()

    }

    override fun onStop() {
        super.onStop()
        registration?.remove()
    }

    private fun loadVehicles() {

        firestore.collection("vehicles")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("VehicleListActivity", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    vehicleList.clear()
                    for (doc in snapshot.documents) {
                        val vehicle = doc.toObject(Vehicle::class.java)
                        if (vehicle != null) {
                            vehicleList.add(vehicle.copy(id = doc.id))
                        }
                    }

                    Log.d("VehicleListActivity", "Loaded vehicles: ${vehicleList.size}")

                    if (vehicleList.isEmpty()) {
                        binding.textNoVehicles.visibility = View.VISIBLE
                        binding.rvVehicles.visibility = View.GONE
                    } else {
                        binding.textNoVehicles.visibility = View.GONE
                        binding.rvVehicles.visibility = View.VISIBLE
                        vehicleAdapter = VehicleAdapter(vehicleList) { vehicle ->
                            //Startando AlertDialog para excluir
//                            showDeleteConfirmation(vehicle)

                            // Startando EditVehicleActivity
                            val intent = Intent(this, EditVehicleActivity::class.java)
                            intent.putExtra("VEHICLE_ID", vehicle.id)
                            startActivity(intent)
                        }
                        binding.rvVehicles.adapter = vehicleAdapter
                    }
                } else {
                    Log.d("VehicleListActivity", "Current data: null")
                }
            }

    }

    private fun showDeleteConfirmation(vehicle: Vehicle) {

        AlertDialog.Builder(this)
            .setTitle("Excluir Veículo")
            .setMessage("Deseja excluir o veículo ${vehicle.plateLetters}-${vehicle.plateNumbers}?")
            .setPositiveButton("SIM") { _, _ ->
                deleteVehicle(vehicle)
            }.setNegativeButton("Não", null)
            .show()

    }

    private fun deleteVehicle(vehicle: Vehicle) {
        lifecycleScope.launch {
            try {
                firestore.collection("vehicles")
                    .document(vehicle.id)
                    .delete()
                    .await()
                Log.d("VehicleListActivity", "Vehicle deleted: ${vehicle.id}")
            } catch (err: Exception) {
                Log.e("VehicleListActivity", "Erro deleting vehicle: ", err )
            }
        }
    }
}