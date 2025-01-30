package com.example.digidex.adapters
import androidx.recyclerview.widget.DiffUtil
import com.example.digidex.database.models.DigiDexModel

class DigiDexDiffCallBack : DiffUtil.ItemCallback<DigiDexModel>() {
    override fun areItemsTheSame(oldItem: DigiDexModel, newItem: DigiDexModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DigiDexModel, newItem: DigiDexModel): Boolean {
        return oldItem == newItem
    }
}