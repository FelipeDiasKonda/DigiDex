package com.example.digidex

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.digidex.adapters.DigimonAdapter
import com.example.digidex.database.models.DigiModel
import com.example.digidex.databinding.DialogDigimonDetailBinding
import com.example.digidex.databinding.DigimonsDigidexBinding
import com.example.digidex.viewmodels.DigiDexDetailsViewModel

class DigiDexDetailsActivity : AppCompatActivity() {

    private val binding: DigimonsDigidexBinding by lazy {
        DigimonsDigidexBinding.inflate(layoutInflater)
    }
    private val viewModel: DigiDexDetailsViewModel by lazy {
        ViewModelProvider(this)[DigiDexDetailsViewModel::class.java]
    }
    private val adapter: DigimonAdapter by lazy {
        DigimonAdapter(
            onClick = { digimon -> showDigimonDetailsDialog(digimon) },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val digidexId = intent.getIntExtra("DIGIDEX_ID", -1)
        if (digidexId == -1) {
            Log.e("DIGIDEX_ERROR", "Invalid DigiDex ID")
            Toast.makeText(this, "Invalid DigiDex ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupUI()
        viewModel.loadDigimons(digidexId)

        viewModel.digimons.observe(this) { digimons ->
            Log.d("DIGIMONS_OBSERVED", "Digimons observed: $digimons")
            adapter.submitList(digimons)
        }
    }

    private fun setupUI() {
        binding.digimonRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.digimonRecyclerView.adapter = adapter
    }

    private fun showDigimonDetailsDialog(digimon: DigiModel) {
        val dialogBinding = DialogDigimonDetailBinding.inflate(layoutInflater)

        dialogBinding.digimonNameTextView.text = digimon.name
        dialogBinding.digimonDetailsTextView.text = getString(R.string.digimon_details, digimon.level, digimon.attribute, digimon.type, digimon.description)

        Glide.with(this)
            .load(digimon.image)
            .into(dialogBinding.digimonImageView)

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}