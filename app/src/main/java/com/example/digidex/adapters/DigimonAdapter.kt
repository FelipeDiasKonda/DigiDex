package com.example.digidex.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digidex.database.models.DigiModel
import com.example.digidex.databinding.DigimonItemBinding


class DigimonAdapter(private val onClick: (DigiModel) -> Unit) : ListAdapter<DigiModel, DigimonAdapter.DigimonViewHolder>(DiffCallback()) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    private val selectedDidimons = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonViewHolder {
        val binding = DigimonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigimonViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: DigimonViewHolder, position: Int) {
        val digimon = getItem(position)
        holder.bind(digimon, selectedDidimons.contains(digimon.id))
    }

    inner class DigimonViewHolder(private val binding: DigimonItemBinding, val onClick: (DigiModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private var currentDigimon: DigiModel? = null

        init {
            itemView.setOnClickListener {
                currentDigimon?.let {
                    if(selectedDidimons.contains(it.id)){
                        selectedDidimons.remove(it.id)
                    }else{
                        selectedDidimons.add(it.id)
                    }
                    notifyItemChanged(adapterPosition)
                }
            }

            itemView.setOnLongClickListener {
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition)
                true
            }
        }

        fun bind(digimon: DigiModel, isSelected: Boolean) {
            currentDigimon = digimon
            binding.digimonNameTextView.text = digimon.name
            Glide.with(binding.digimonImageView.context)
                .load(digimon.image)
                .into(binding.digimonImageView)

            itemView.isSelected = isSelected
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.TRANSPARENT)
        }
    }

    fun getSelectedDigimons(): List<Int>{
        return selectedDidimons.toList()
    }

    class DiffCallback : DiffUtil.ItemCallback<DigiModel>() {
        override fun areItemsTheSame(oldItem: DigiModel, newItem: DigiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DigiModel, newItem: DigiModel): Boolean {
            return oldItem == newItem
        }
    }
}