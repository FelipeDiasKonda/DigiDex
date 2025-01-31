package com.example.digidex

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digidex.adapters.DigidexAdapter
import com.example.digidex.database.models.DigidexModel
import com.example.digidex.databinding.ActivityMainBinding
import com.example.digidex.viewmodels.DigidexViewModel

class MainActivity : AppCompatActivity() {

    private val digidexViewModel: DigidexViewModel by lazy {
        ViewModelProvider(this)[DigidexViewModel::class.java]
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val onItemLongClickListener: (DigidexModel) -> Unit = { _ ->

    }
    private val onItemClickListener: (DigidexModel) -> Unit = { digidex ->
        val intent = Intent(this, DigidexDetailsActivity::class.java)
        intent.putExtra("DIGIDEX_ID", digidex.id)
        startActivity(intent)
    }
    private val adapter: DigidexAdapter by lazy {
        DigidexAdapter(onItemClickListener, onItemLongClickListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        initsetup()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initsetup() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            binding.fab.isEnabled = false
            val dialog = NewDigidexFragment()
            dialog.show(supportFragmentManager, "AddTask")
            Handler(Looper.getMainLooper()).postDelayed({
                binding.fab.isEnabled = true
            }, 1000)
        }

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