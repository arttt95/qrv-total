package com.arttt95.qrvtotal.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arttt95.qrvtotal.R
import com.arttt95.qrvtotal.databinding.ItemVehicleBinding
import com.arttt95.qrvtotal.models.Vehicle

class VehicleAdapter(
    private val items: List<Vehicle>,
    private val onItemClick: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    inner class VehicleViewHolder(
        private val binding: ItemVehicleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.btnPlateLetters.text = vehicle.plateLetters
            binding.btnPlateNumbers.text = vehicle.plateNumbers
            binding.btnBrand.text = vehicle.brand
            binding.btnModel.text = vehicle.model
            binding.btnYear.text = vehicle.year
            binding.btnQru.text = when (vehicle.qru) {
                "Ação Criminosa" -> "AC"
                "Procurado" -> "MP"
                "Sequestro" -> "Seq"
                else -> vehicle.qru
            }
            binding.btnQth.text = vehicle.qth
            binding.btnDays.text = vehicle.days
            binding.btnColor.text = vehicle.color

            binding.btnTypeVehicle.apply {
                when (vehicle.typeVehicle) {
                    "Carro" -> {
                        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_car_24, 0, 0)
                        text = ""
                    }
                    "Moto" -> {
                        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_moto_24, 0, 0)
                        text = ""
                    }
                    "Ônibus" -> {
                        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bus_24, 0, 0)
                        text = ""
                    }
                    "Caminhão" -> {
                        setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_truck_24, 0, 0)
                        text = ""
                    }
                    else -> setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }

            clickOnItem(vehicle, binding)

            // Alterna cores para cada item
            colorirItems(position, binding)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = items[position]
        holder.bind(vehicle)
    }

    override fun getItemCount(): Int = items.size

    private fun clickOnItem(vehicle: Vehicle, binding: ItemVehicleBinding) {

        binding.root.setOnClickListener {
            onItemClick(vehicle)
        }



        binding.btnPlateLetters.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnPlateNumbers.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnBrand.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnModel.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnYear.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnQru.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnQth.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnDays.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnTypeVehicle.setOnClickListener {
            onItemClick(vehicle)
        }

        binding.btnColor.setOnClickListener {
            onItemClick(vehicle)
        }

    }

    private fun colorirItems(position: Int, binding: ItemVehicleBinding) {
        if (position % 2 == 0) {

            binding.root.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnPlateLetters.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnPlateNumbers.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnBrand.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnModel.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnYear.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnQru.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnQth.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnDays.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnTypeVehicle.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnColor.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite

            binding.btnPlateLetters.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnPlateNumbers.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnBrand.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnModel.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnYear.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnQru.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnQth.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnDays.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnTypeVehicle.setTextColor(Color.parseColor("#FFFFFF")) // Branco
            binding.btnColor.setTextColor(Color.parseColor("#FFFFFF")) // Branco
        } else {
            binding.root.setBackgroundColor(Color.parseColor("#333333")) // Cinza grafite
            binding.btnPlateLetters.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnPlateNumbers.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnBrand.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnModel.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnYear.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnQru.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnQth.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnDays.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnTypeVehicle.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro
            binding.btnColor.setBackgroundColor(Color.parseColor("#F7F7F7")) // Cinza claro

            binding.btnPlateLetters.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnPlateNumbers.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnBrand.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnModel.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnYear.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnQru.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnQth.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnDays.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnTypeVehicle.setTextColor(Color.parseColor("#000000")) // Preto
            binding.btnColor.setTextColor(Color.parseColor("#000000")) // Preto
        }
    }

}