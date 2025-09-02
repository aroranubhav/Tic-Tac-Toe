package com.maxi.tic_tac_toe.presentation.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.maxi.tic_tac_toe.R
import com.maxi.tic_tac_toe.databinding.FragmentSetupBinding
import com.maxi.tic_tac_toe.presentation.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            SetupFragment()

    }

    private val viewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            btnContinue.setOnClickListener {
                if (isInputValid()) {
                    val playerX = edtPX.text.toString()
                    val playerO = edtPO.text.toString()
                    viewModel.initGame(playerX, playerO)
                    findNavController().navigate(R.id.action_setupFragment_to_gameFragment)
                } else {
                    edtPX.doOnTextChanged { text, _, _, _ ->
                        if (!text.isNullOrBlank()) {
                            tilPX.error = null
                        }
                    }
                    edtPO.doOnTextChanged { text, _, _, _ ->
                        if (!text.isNullOrBlank()) {
                            tilPO.error = null
                        }
                    }
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        binding.apply {
            when {
                edtPX.text.isNullOrBlank() -> {
                    tilPX.error = resources.getString(R.string.enter_player_name)
                    return false
                }

                edtPO.text.isNullOrBlank() -> {
                    tilPO.error = resources.getString(R.string.enter_player_name)
                    return false
                }
            }
        }
        return true
    }
}