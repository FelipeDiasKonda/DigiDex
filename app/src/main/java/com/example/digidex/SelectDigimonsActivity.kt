package com.example.digidex

import com.example.digidex.adapters.DigimonAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.digidex.databinding.DigimonsListBinding
import com.example.digidex.viewmodels.SelectDigimonsViewModel


class SelectDigimonsActivity : AppCompatActivity() {

    private val binding: DigimonsListBinding by lazy {
        DigimonsListBinding.inflate(layoutInflater)
    }
    private val viewModel: SelectDigimonsViewModel by viewModels()
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
    }
}