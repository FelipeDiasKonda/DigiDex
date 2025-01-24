package com.example.digidex

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.digidex.database.models.DigiDexModel
import com.example.digidex.databinding.NewDexFragmentBinding
import com.example.digidex.viewmodels.AddDigiDexViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewDigiDexFragment : BottomSheetDialogFragment() {

    private val binding: NewDexFragmentBinding by lazy {
        NewDexFragmentBinding.inflate(layoutInflater)
    }

    private val addDigiDexViewModel: AddDigiDexViewModel by lazy {
        ViewModelProvider(this)[AddDigiDexViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            dialogInterface as BottomSheetDialog
            val bottomSheet = binding.root.parent as FrameLayout
            bottomSheet.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.peekHeight = 0
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                it.layoutParams.height = (resources.displayMetrics.heightPixels * 0.4).toInt()
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addDigiDexBtn.setOnClickListener {
            val title = binding.AddTitleEditText.text.toString()
            val description = binding.AddDescEditText.text.toString()
            if (title.isNotBlank()) {
                val newDigiDex = DigiDexModel(id, title, description)
                addDigiDexViewModel.addDigiDex(newDigiDex)


                val intent = Intent(requireContext(), SelectDigimonsActivity::class.java)
                startActivity(intent)
            } else {

                Toast.makeText(
                    requireContext(),
                    "O título não pode estar vazio",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}