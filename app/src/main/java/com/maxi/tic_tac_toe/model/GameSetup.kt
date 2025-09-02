package com.maxi.tic_tac_toe.model

import com.maxi.tic_tac_toe.Board

data class GameSetup(
    val playerX: Player,
    val playerO: Player,
    val board: Board
)
