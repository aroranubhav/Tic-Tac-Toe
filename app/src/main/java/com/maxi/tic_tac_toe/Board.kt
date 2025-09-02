package com.maxi.tic_tac_toe

import com.maxi.tic_tac_toe.model.Player
import com.maxi.tic_tac_toe.util.Constants.ALREADY_OCCUPIED_CELL_MOVE
import com.maxi.tic_tac_toe.util.Constants.BACKWARD
import com.maxi.tic_tac_toe.util.Constants.DEFAULT_BOARD_SIZE
import com.maxi.tic_tac_toe.util.Constants.FORWARD
import com.maxi.tic_tac_toe.util.Constants.OUT_OF_BOUND_MOVE
import com.maxi.tic_tac_toe.util.GameState
import javax.inject.Singleton

@Singleton
class Board(
    private val playerX: Player,
    private val playerO: Player,
    private val size: Int = DEFAULT_BOARD_SIZE
) {

    private lateinit var board: MutableList<MutableList<String>>
    private lateinit var rowCounts: MutableMap<Int, MutableMap<String, Int>>
    private lateinit var colCounts: MutableMap<Int, MutableMap<String, Int>>
    private lateinit var diagonalCounts: MutableMap<String, MutableMap<String, Int>>
    private var currMovesCount = 0

    init {
        initGame()
    }

    fun initGame() {
        board = MutableList(size) { MutableList(size) { "" } }
        rowCounts = mutableMapOf()
        colCounts = mutableMapOf()
        diagonalCounts = mutableMapOf()
        currMovesCount = 0
    }

    fun placeMove(player: Player, xPos: Int, yPos: Int): GameState {
        if ((xPos !in 0 until size) || (yPos !in 0 until size)) { //not needed in our app atleast -- would be useful in case we have a CLI app.
            GameState.IllegalMove(OUT_OF_BOUND_MOVE)
        } else if (board[xPos][yPos] != "") {
            GameState.IllegalMove(ALREADY_OCCUPIED_CELL_MOVE)
        }

        val identifier = player.identifier
        board[xPos][yPos] = identifier
        currMovesCount += 1

        //row
        val rowInner = rowCounts.getOrPut(xPos) { mutableMapOf() }
        rowInner[identifier] = (rowInner[identifier] ?: 0) + 1

        if (rowCounts[xPos]!![identifier] == size) {
            return GameState.Win(player)
        }

        //column
        val colInner = colCounts.getOrPut(yPos) { mutableMapOf() }
        colInner[identifier] = (colInner[identifier] ?: 0) + 1

        if (colCounts[yPos]!![identifier] == size) {
            return GameState.Win(player)
        }

        //diagonals
        if (xPos == yPos) {
            val forwardDiagonalInner = diagonalCounts.getOrPut(FORWARD) { mutableMapOf() }
            forwardDiagonalInner[identifier] = (forwardDiagonalInner[identifier] ?: 0) + 1

            if (diagonalCounts[FORWARD]!![identifier] == size) {
                return GameState.Win(player)
            }
        }

        if (xPos + yPos == size - 1) {
            val backwardDiagonalInner = diagonalCounts.getOrPut(BACKWARD) { mutableMapOf() }
            backwardDiagonalInner[identifier] = (backwardDiagonalInner[identifier] ?: 0) + 1

            if (diagonalCounts[BACKWARD]!![identifier] == size) {
                return GameState.Win(player)
            }
        }

        if (currMovesCount == size * size) {
            return GameState.Tie
        }

        val currentPlayer = if (currMovesCount % 2 == 0) playerX else playerO
        return GameState.Ongoing(currentPlayer)
    }
}