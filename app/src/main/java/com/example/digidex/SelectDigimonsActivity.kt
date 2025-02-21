package com.example.digidex

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.digidex.adapters.DigimonAdapter
import com.example.digidex.database.models.DigimonModel
import com.example.digidex.databinding.DialogDigimonDetailBinding
import com.example.digidex.databinding.DigimonsListBinding
import com.example.digidex.viewmodels.SelectDigimonsViewModel

class SelectDigimonsActivity : AppCompatActivity() {

    private val binding: DigimonsListBinding by lazy {
        DigimonsListBinding.inflate(layoutInflater)
    }
    private val viewModel: SelectDigimonsViewModel by lazy {
        ViewModelProvider(this)[SelectDigimonsViewModel::class.java]
    }
    private val adapter: DigimonAdapter by lazy {
        DigimonAdapter(
            onClick = { digimon -> viewModel.fetchDigimonDetailsLiveData(digimon.id) },
        )
    }

    private var digidexId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        viewModel.getLastDigiDexId().observe(this) { id ->
            digidexId = id ?: -1
            if (digidexId == -1) {
                finish()
                return@observe
            }
            setupUI()
        }

        viewModel.digimonDetails.observe(this) { digimon ->
            digimon?.let {
                showDigimonDetailsDialog(it)
            } ?: run {
                Toast.makeText(this, getString(R.string.api_failure), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupUI() {
        binding.digimonRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.digimonRecyclerView.adapter = adapter

        viewModel.fetchDigimons()
        setupSpinners()

        viewModel.digimons.observe(this) { digimons ->
            adapter.submitList(digimons)
        }
        binding.confirmButton.setOnClickListener {
            val selectedDigimonsIds = adapter.getSelectedDigimons()
            if (selectedDigimonsIds.isEmpty()) {
                checkIfDigiDexIsEmptyAndFinish()
            } else {
                viewModel.addDigimonstoDigidex(digidexId, selectedDigimonsIds) {
                    finish()
                }
            }
        }
    }

    private fun checkIfDigiDexIsEmptyAndFinish() {
        viewModel.isDigiDexEmpty { isEmpty ->
            if (isEmpty) {
                Toast.makeText(this, getString(R.string.empty_digidex), Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    private fun setupSpinners() {
        val levelAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.level_filter,
            android.R.layout.simple_spinner_item
        )
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.levelFilter.adapter = levelAdapter

        val attributeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.attribute_filter,
            android.R.layout.simple_spinner_item
        )
        attributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.attributeFilter.adapter = attributeAdapter

        binding.levelFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLevel =
                    if (position == 0) null else parent.getItemAtPosition(position) as String
                val selectedAttribute =
                    if (binding.attributeFilter.selectedItemPosition == 0) null else binding.attributeFilter.selectedItem as String?
                viewModel.setFilter(selectedLevel, selectedAttribute)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.setFilter(
                    null,
                    if (binding.attributeFilter.selectedItemPosition == 0) null else binding.attributeFilter.selectedItem as String?
                )
            }
        }

        binding.attributeFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedAttribute =
                        if (position == 0) null else parent.getItemAtPosition(position) as String
                    val selectedLevel =
                        if (binding.levelFilter.selectedItemPosition == 0) null else binding.levelFilter.selectedItem as String?
                    viewModel.setFilter(selectedLevel, selectedAttribute)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    viewModel.setFilter(
                        if (binding.levelFilter.selectedItemPosition == 0) null else binding.levelFilter.selectedItem as String?,
                        null
                    )
                }
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

        Glide.with(this)
            .load(digimon.image)
            .into(dialogBinding.digimonImageView)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.addDigimonButton.setOnClickListener {
            viewModel.addOneDigimontoDigidex(digidexId, listOf(digimon.id)) {
                viewModel.setDigiDexNotEmpty()
                Toast.makeText(this, "Digimon adicionado ao DigiDex", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}
