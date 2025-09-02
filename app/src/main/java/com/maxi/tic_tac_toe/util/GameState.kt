package com.maxi.tic_tac_toe.util

import com.maxi.tic_tac_toe.model.Player

sealed class GameState {

    data class Win(val player: Player) : GameState()

    data class Ongoing(val player: Player) : GameState()

    data object Tie : GameState()

    data class IllegalMove(val message: String) :
        GameState() //not useful for our app, would be useful in case of a CLI app.
}