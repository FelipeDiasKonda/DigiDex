package com.example.digidex.adapters
import androidx.recyclerview.widget.DiffUtil
import com.example.digidex.database.models.DigidexModel

class DigidexDiffCallBack : DiffUtil.ItemCallback<DigidexModel>() {
    override fun areItemsTheSame(oldItem: DigidexModel, newItem: DigidexModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DigidexModel, newItem: DigidexModel): Boolean {
        return oldItem == newItem
    }
}