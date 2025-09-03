package com.maxi.tic_tac_toe.presentation.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.maxi.tic_tac_toe.databinding.FragmentGameBinding
import com.maxi.tic_tac_toe.model.GameSetup
import com.maxi.tic_tac_toe.model.Player
import com.maxi.tic_tac_toe.presentation.SharedViewModel
import com.maxi.tic_tac_toe.util.Constants.TIE
import com.maxi.tic_tac_toe.util.Constants.WINS
import com.maxi.tic_tac_toe.util.GameState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var gameSetup: GameSetup
    private lateinit var currentPlayer: Player

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        gameSetup = viewModel.gameSetup
        setupIndicesForGrid(binding.boardGrid)
        currentPlayer = gameSetup.playerX

        val playerX = "${gameSetup.playerX.name} (${gameSetup.playerX.identifier})"
        val playerO = "${gameSetup.playerO.name} (${gameSetup.playerO.identifier})"
        binding.apply {

            playerXInfo.text = playerX
            playerOInfo.text = playerO

            cell00.apply {
                setupActions(this)
            }
            cell01.apply {
                setupActions(this)
            }
            cell02.apply {
                setupActions(this)
            }
            cell10.apply {
                setupActions(this)
            }
            cell11.apply {
                setupActions(this)
            }
            cell12.apply {
                setupActions(this)
            }
            cell20.apply {
                setupActions(this)
            }
            cell21.apply {
                setupActions(this)
            }
            cell22.apply {
                setupActions(this)
            }

            btnReset.setOnClickListener {
                viewModel.resetGame()
                clearUiState()
                updateGridCellsState(true)
            }
        }
    }

    private fun setupIndicesForGrid(grid: GridLayout) {
        for (i in 0 until grid.rowCount) {
            for (j in 0 until grid.columnCount) {
                val index = i * grid.columnCount + j
                val cell = grid.getChildAt(index) as TextView
                cell.tag = Pair(i, j)
            }
        }
    }

    private fun setupActions(view: TextView) {
        view.apply {
            setOnClickListener {
                val position = view.tag as Pair<Int, Int>
                text = currentPlayer.identifier
                isEnabled = false
                val gameState = viewModel.placeMove(currentPlayer, position.first, position.second)
                checkGameState(gameState)
            }
        }
    }

    private fun checkGameState(gameState: GameState) {
        when (gameState) {
            is GameState.Win -> {
                updateGridCellsState(false)
                val message = "${currentPlayer.name} (${currentPlayer.identifier}) $WINS!"
                updateGameStateMessage(message)
            }

            is GameState.Tie -> {
                updateGridCellsState(false)
                updateGameStateMessage(TIE)
            }

            is GameState.Ongoing -> {
                currentPlayer = gameState.player
            }

            /**
             * Will not come across this use case in our scenario!
             */
            is GameState.IllegalMove -> {
                Snackbar.make(
                    binding.root,
                    gameState.message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateGameStateMessage(message: String) {
        Snackbar
            .make(
                binding.root,
                message,
                Snackbar.LENGTH_LONG
            ).show()
    }

    private fun clearUiState() {
        binding.boardGrid.forEach {
            (it as TextView).text = ""
        }
        currentPlayer = gameSetup.playerX
    }

    private fun updateGridCellsState(isEnabled: Boolean) {
        binding.boardGrid.forEach {
            it.isEnabled = isEnabled
        }
    }
}