package com.example.digidex

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digidex.adapters.DigiDexAdapter
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.databinding.ActivityMainBinding
import com.example.digidex.viewmodels.DigiDexViewModel
import com.example.digidex.viewmodels.DigiDexViewModelFactory

class MainActivity : AppCompatActivity() {

    private val digidexViewModel: DigiDexViewModel by lazy {
     ViewModelProvider(this,DigiDexViewModelFactory(application))[DigiDexViewModel::class.java]
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val onItemLongClickListener: (DigiDexModel) -> Unit = { digidex ->

    }
    private val adapter: DigiDexAdapter by lazy {
        DigiDexAdapter(digidexViewModel, onItemLongClickListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        initsetup()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initsetup() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        digidexViewModel.allDigiDexes.observe(this) { digidexes ->
            if (digidexes.isNullOrEmpty()) {
                binding.emptyMessage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyMessage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(digidexes) {
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }

    }
}




