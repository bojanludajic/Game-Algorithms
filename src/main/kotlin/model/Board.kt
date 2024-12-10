package model

import androidx.compose.runtime.*

class Board {

    var board by mutableStateOf(arrayOf(
        arrayOf(' ', ' ', ' '),
        arrayOf(' ', ' ', ' '),
        arrayOf(' ', ' ', ' ')
    ))
    var curSign = 'X'
    var moves = 0


    fun isSafe(i: Int, j: Int): Boolean {
        return board[i][j] == ' '
    }

    fun check(): Char {
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') return board[i][0]
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') return board[0][i]
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') return board[0][0]
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') return board[0][2]

        return if (isTie()) 'T' else ' '
    }

    fun isTie(): Boolean {
        return board.all {
            it.all { it != ' ' }
        }
    }

    fun play(board: Array<Array<Char>>, i: Int, j: Int) {
        val newBoard = board.mapIndexed { rowInd, row ->
            row.mapIndexed { colInd, col ->
                if(rowInd == i && colInd == j) curSign else col
            }.toTypedArray()
        }.toTypedArray()
        switchSign()
        moves++
        this.board = newBoard
    }

    fun undo(board: Array<Array<Char>>, i: Int, j: Int) {
        board[i][j] = ' '
        switchSign()

    }

    fun printBoard() {
        println(" ${board[0][0]} | ${board[0][1]} | ${board[0][2]}")
        println("---|---|---")
        println(" ${board[1][0]} | ${board[1][1]} | ${board[1][2]}")
        println("---|---|---")
        println(" ${board[2][0]} | ${board[2][1]} | ${board[2][2]}")
        println()
    }

    fun resetBoard() {
        board = arrayOf(
            arrayOf(' ', ' ', ' '),
            arrayOf(' ', ' ', ' '),
            arrayOf(' ', ' ', ' ')
        )
        curSign = 'X'
        moves = 0
    }

    fun switchSign() {
        curSign = if(curSign == 'X') 'O' else 'X'
    }

    fun copy(): Board {
        val newBoard = Board()
        newBoard.board = this.board.map { it.clone() }.toTypedArray()
        newBoard.curSign = this.curSign
        return newBoard
    }

    fun getKeyAsBitmask(): Int {
        var key = 0
        for (i in 0..2) {
            for (j in 0..2) {
                key = key shl 2
                key = key or when (board[i][j]) {
                    'X' -> 1
                    'O' -> 2
                    else -> 0
                }
            }
        }
        return key
    }
}
