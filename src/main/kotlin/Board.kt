import androidx.compose.runtime.*
import kotlin.math.max
import kotlin.math.min

class Board {

    var board by mutableStateOf(arrayOf(
        arrayOf(' ', ' ', ' '),
        arrayOf(' ', ' ', ' '),
        arrayOf(' ', ' ', ' ')
    ))
    var curSign = 'X'

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
    }

    fun switchSign() {
        curSign = if(curSign == 'X') 'O' else 'X'
    }
}
