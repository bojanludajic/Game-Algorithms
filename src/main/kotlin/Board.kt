import androidx.compose.runtime.*
import kotlinx.coroutines.delay
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

    /**
     *
     */
    fun play(board: Array<Array<Char>>, i: Int, j: Int) {
        val newBoard = board.mapIndexed { rowInd, row ->
            row.mapIndexed { colInd, col ->
                if(rowInd == i && colInd == j) curSign else col
            }.toTypedArray()
        }.toTypedArray()

        curSign = if(curSign == 'X') 'O' else 'X'
        this.board = newBoard
    }

    private fun playSim(board: Array<Array<Char>>, i: Int, j: Int) {
        board[i][j] = curSign
        curSign = if(curSign == 'X') 'O' else 'X'
    }

    fun playAI() {
        val i: Int
        val j: Int
        if(curSign == 'X') {
            i = getBestMoveX().first
            j = getBestMoveX().second
        } else {
            i = getBestMoveO().first
            j = getBestMoveO().second

        }
        play(board, i, j)
    }

    fun undo(board: Array<Array<Char>>, i: Int, j: Int) {
        board[i][j] = ' '
        curSign = if(curSign == 'X') 'O' else 'X'

    }

    /**
     *  Minimax algoritam koji trazi najbolji moguci potez
     */
    fun minimax(board: Array<Array<Char>>, depth: Int, maximizing: Boolean): Int {
        val winner = check()
        // terminalno stanje, vrsi se evaluacija stanja
        if(winner != ' ') {
            return when(winner) {
                'X' -> 1
                'O' -> -1
                else -> 0
            }
        }

        if(maximizing) {
            var best = Int.MIN_VALUE
            for(i in 0..2) {
                for(j in 0..2) {
                    if(board[i][j] == ' ') {
                        playSim(board, i, j)
                        val score = minimax(board, depth + 1, false)
                        undo(board, i, j)
                        best = max(best, score)
                    }
                }
            }
            return best
        } else {
            var best = Int.MAX_VALUE
            for(i in 0..2) {
                for(j in 0..2) {
                    if(board[i][j] == ' ') {
                        playSim(board, i, j)
                        val score = minimax(board, depth + 1, true)
                        undo(board, i, j)
                        best = min(best, score)
                    }
                }
            }
            return best
        }

    }

    fun getBestMoveX(): Pair<Int, Int> {
        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int> = 0 to 0

        for(i in 0..2) {
            for(j in 0..2) {
                if(board[i][j] == ' ') {
                    playSim(board, i, j)
                    val score = minimax(board, 0, false)
                    undo(board, i, j)
                    if(score > bestScore) {
                        bestScore = score
                        bestMove = i to j
                    }
                }
            }
        }
        return bestMove
    }

    fun getBestMoveO(): Pair<Int, Int> {
        var bestScore = Int.MAX_VALUE
        var bestMove: Pair<Int, Int> = 0 to 0

        for(i in 0..2) {
            for(j in 0..2) {
                if(board[i][j] == ' ') {
                    playSim(board, i, j)
                    val score = minimax(board, 0, true)
                    undo(board, i, j)
                    if(score < bestScore) {
                        bestScore = score
                        bestMove = i to j
                    }
                }
            }
        }
        return bestMove
    }

    fun printBoard() {
        println(" ${board[0][0]} | ${board[0][1]} | ${board[0][2]}")
        println("---|---|---")
        println(" ${board[1][0]} | ${board[1][1]} | ${board[1][2]}")
        println("---|---|---")
        println(" ${board[2][0]} | ${board[2][1]} | ${board[2][2]}")
        println()
    }

    /**
     *  Simulacija racunar sam protiv sebe (uvek nereseno)
     */
    suspend fun runAIvsAI() {
        var winner = ' '
        while (winner == ' ') {
            printBoard()
            playAI()
            delay(1000)
            winner = check()
            if (winner != ' ') break
            playAI()
            winner = check()
            delay(1000)
        }
        printBoard() // za debagovanje
    }
}
