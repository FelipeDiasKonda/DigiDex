package com.example.digidex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.databinding.ItensBinding

class DigiDexAdapter(
    private val onItemClickListener: (DigiDexModel) -> Unit,
    private val onItemLongClickListener: (DigiDexModel) -> Unit
) : ListAdapter<DigiDexModel, DigiDexAdapter.DigiDexViewHolder>(DigiDexDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigiDexViewHolder {
        val binding = ItensBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigiDexViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DigiDexViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener(currentItem)
            true
        }
        holder.itemView.setOnClickListener {
            onItemClickListener(currentItem)
        }
    }

    inner class DigiDexViewHolder(private val binding: ItensBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(digidex: DigiDexModel) {
            binding.digidexNameTextView.text = digidex.name
            binding.digidexDescriptionTextView.text = digidex.description
        }
    }
}