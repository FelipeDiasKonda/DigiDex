package com.example.digidex

import com.example.digidex.adapters.DigimonAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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
        DigimonAdapter { digimon ->
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.digimonRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.digimonRecyclerView.adapter = adapter

        viewModel.fetchDigimons()
        setupSpinners()

        viewModel.digimons.observe(this) { digimons ->
            Log.d("DATA_OBSERVED", "Digimons observed")
            adapter.submitList(digimons)
        }
        binding.confirmButton.setOnClickListener {
            val selectedDigimonsIds = adapter.getSelectedDigimons()
            Log.d("SELECTED_IDS", "Selected Digimons IDs: $selectedDigimonsIds")
            val selectedDigimons = adapter.currentList.filter { it.id in selectedDigimonsIds }
            Log.d("SELECTED_DIGIMONS", "Selected Digimons: $selectedDigimons")
            val digidexId = intent.getIntExtra("id", -1)
            viewModel.addDigimonstoDigidex(digidexId, selectedDigimons)
            Toast.makeText(this, "Digimons added", Toast.LENGTH_SHORT).show()
            finish()
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
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLevel = if (position == 0) null else parent.getItemAtPosition(position) as String
                val selectedAttribute = if (binding.attributeFilter.selectedItemPosition == 0) null else binding.attributeFilter.selectedItem as String?
                viewModel.fetchDigimons(level = selectedLevel, attribute = selectedAttribute)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.fetchDigimons(level = null, attribute = if (binding.attributeFilter.selectedItemPosition == 0) null else binding.attributeFilter.selectedItem as String?)
            }
        }

        binding.attributeFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedAttribute = if (position == 0) null else parent.getItemAtPosition(position) as String
                val selectedLevel = if (binding.levelFilter.selectedItemPosition == 0) null else binding.levelFilter.selectedItem as String?
                viewModel.fetchDigimons(level = selectedLevel, attribute = selectedAttribute)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.fetchDigimons(level = if (binding.levelFilter.selectedItemPosition == 0) null else binding.levelFilter.selectedItem as String?, attribute = null)
            }
        }
    }
}