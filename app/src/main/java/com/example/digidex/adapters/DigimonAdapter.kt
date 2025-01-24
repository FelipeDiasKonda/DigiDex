package com.example.digidex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digidex.database.models.DigiModel
import com.example.digidex.databinding.DigimonItemBinding


class DigimonAdapter(private val onClick: (DigiModel) -> Unit) :
    ListAdapter<DigiModel, DigimonAdapter.DigimonViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonViewHolder {
        val binding = DigimonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigimonViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: DigimonViewHolder, position: Int) {
        val digimon = getItem(position)
        holder.bind(digimon)
    }

    class DigimonViewHolder(
        private val binding: DigimonItemBinding,
        val onClick: (DigiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentDigimon: DigiModel? = null

        init {
            itemView.setOnClickListener {
                currentDigimon?.let {
                    onClick(it)
                }
            }
        }

        fun bind(digimon: DigiModel) {
            currentDigimon = digimon
            binding.digimonNameTextView.text = digimon.name
            Glide.with(binding.digimonImageView.context)
                .load(digimon.image)
                .into(binding.digimonImageView)
        }
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