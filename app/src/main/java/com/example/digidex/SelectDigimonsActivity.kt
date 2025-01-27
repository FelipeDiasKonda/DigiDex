package com.example.digidex

import com.example.digidex.adapters.DigimonAdapter
import android.os.Bundle
import android.util.Log
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

        viewModel.digimons.observe(this) { digimons ->
            Log.d("DATA_OBSERVED", "Digimons observed")
            adapter.submitList(digimons)
        }
        binding.confirmButton.setOnClickListener {
            val selectedDigimonsIds = adapter.getSelectedDigimons()
            val digidexId = intent.getIntExtra("id",-1)
                val selectedDigimons = viewModel.allDigimons.filter { it.id in selectedDigimonsIds }
                viewModel.addDigimonstoDigidex(digidexId, selectedDigimons)
                Toast.makeText(this, "Digimons adicionados", Toast.LENGTH_SHORT).show()
                finish()
        }
    }


}