package com.maxi.tic_tac_toe.presentation

import androidx.lifecycle.ViewModel
import com.maxi.tic_tac_toe.Board
import com.maxi.tic_tac_toe.model.GameSetup
import com.maxi.tic_tac_toe.model.Player
import com.maxi.tic_tac_toe.util.Constants.PLAYER_O
import com.maxi.tic_tac_toe.util.Constants.PLAYER_X
import com.maxi.tic_tac_toe.util.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private var _gameSetup: GameSetup? = null
    val gameSetup: GameSetup
        get() = _gameSetup ?: throw IllegalStateException("Game not setup!")

    private lateinit var board: Board

    fun initGame(playerXName: String, playerOName: String) {
        val playerX = Player(playerXName, PLAYER_X)
        val playerO = Player(playerOName, PLAYER_O)
        board = Board(
            playerX,
            playerO
        )
        _gameSetup = GameSetup(playerX, playerO, board)
    }

    fun resetGame() {
        if (::board.isInitialized) {
            board.initGame()
        }
    }

    fun placeMove(currentPlayer: Player, xPos: Int, yPos: Int): GameState {
        return board.placeMove(currentPlayer, xPos, yPos)
    }
}