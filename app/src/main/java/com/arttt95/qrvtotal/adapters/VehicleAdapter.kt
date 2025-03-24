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
        val isEven = position % 2 == 0

        val bgColor = if (isEven) "#333333" else "#F7F7F7"
        val textColor = if (isEven) "#FFFFFF" else "#000000"
        val rootColor = if (isEven) "#F7F7F7" else "#333333"

        // Cor do fundo do item
        binding.root.setBackgroundColor(Color.parseColor(rootColor))

        val buttons = listOf(
            binding.btnPlateLetters,
            binding.btnPlateNumbers,
            binding.btnBrand,
            binding.btnModel,
            binding.btnYear,
            binding.btnQru,
            binding.btnQth,
            binding.btnDays,
            binding.btnTypeVehicle,
            binding.btnColor
        )

        // Aplica cor de fundo e texto a todos os botões
        for (btn in buttons) {
            btn.setBackgroundColor(Color.parseColor(bgColor))
            btn.setTextColor(Color.parseColor(textColor))
        }

        // Aplica tint no ícone do botão do tipo de veículo
        binding.btnTypeVehicle.compoundDrawablesRelative.forEach { drawable ->
            drawable?.setTint(Color.parseColor(textColor))
        }
    }

}