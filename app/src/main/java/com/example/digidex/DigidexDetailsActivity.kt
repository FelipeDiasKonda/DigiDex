package com.example.digidex

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.digidex.adapters.DigidexDatabaseAdapter
import com.example.digidex.database.models.DigimonModel
import com.example.digidex.databinding.DialogDigimonDetailBinding
import com.example.digidex.databinding.DigimonsDigidexBinding
import com.example.digidex.viewmodels.DigidexDetailsViewModel

class DigidexDetailsActivity : AppCompatActivity() {

    private val binding: DigimonsDigidexBinding by lazy {
        DigimonsDigidexBinding.inflate(layoutInflater)
    }
    private val viewModel: DigidexDetailsViewModel by lazy {
        ViewModelProvider(this)[DigidexDetailsViewModel::class.java]
    }
    private val adapter: DigidexDatabaseAdapter by lazy {
        DigidexDatabaseAdapter(
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
        binding.digimonRecyclerView.apply {
        layoutManager = GridLayoutManager(this@DigidexDetailsActivity,2)
        adapter = this@DigidexDetailsActivity.adapter
        }
    }



    private fun showDigimonDetailsDialog(digimon: DigimonModel) {
        val dialogBinding = DialogDigimonDetailBinding.inflate(layoutInflater)

        dialogBinding.digimonNameTextView.text = digimon.name
        dialogBinding.digimonDetailsTextView.text = getString(
            R.string.digimon_details,
            digimon.level,
            digimon.attribute,
            digimon.fields,
            digimon.type,
            digimon.description
        )

        dialogBinding.addDigimonButton.visibility = View.GONE

        Glide.with(this)
            .load(digimon.image)
            .into(dialogBinding.digimonImageView)

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()
            .show()

    }
}