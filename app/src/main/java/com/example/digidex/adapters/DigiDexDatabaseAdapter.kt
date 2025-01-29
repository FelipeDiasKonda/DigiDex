package com.example.digidex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digidex.database.models.DigiModel
import com.example.digidex.databinding.DigimonItemBinding

class DigiDexDatabaseAdapter(
    private val onClick: (DigiModel) -> Unit
) : ListAdapter<DigiModel, DigiDexDatabaseAdapter.DigimonViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigimonViewHolder {
        val binding = DigimonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigimonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DigimonViewHolder, position: Int) {
        val digimon = getItem(position)
        holder.bind(digimon)
    }

    inner class DigimonViewHolder(
        private val binding: DigimonItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(digimon: DigiModel) {
            with(binding) {
                digimonNameTextView.text = digimon.name
                Glide.with(root.context)
                    .load(digimon.image)
                    .into(digimonImageView)
                root.setOnClickListener { onClick(digimon) }
            }
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