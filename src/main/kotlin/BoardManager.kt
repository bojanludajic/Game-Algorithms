import algorithms.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.*
import model.Board


class BoardManager {

    private var board by mutableStateOf(Board())
    private var busy by mutableStateOf(false)
    private val difficulties = mutableListOf("Easy", "Medium", "Impossible")
    private var difficulty by mutableStateOf(9)

    fun get(i: Int, j: Int): Char {
        return board.board[i][j]
    }

    fun getDifficulties(): MutableList<String> = difficulties

    fun setDifficulty(difficulty: String) {
        when(difficulty) {
            "Easy" -> this.difficulty = 1
            "Medium" -> this.difficulty = 3
            "Impossible" -> this.difficulty = 9
        }
    }

    fun isClickable() = !busy

    fun backgroundColor(): Color = when(board.check()) {
        'X' -> Color.Green
        'O' -> Color.Yellow
        'T' -> Color.Gray
        else -> Color.White
    }

    fun playAI(difficulty: Int) {
        board.playAI(difficulty)
    }

    fun playAIOptimal() {
        board.playAIAlphaBeta()
    }

    fun play(i: Int, j: Int) {
        if(board.isSafe(i, j) && board.check() == ' ') {
            CoroutineScope(Dispatchers.Default).launch {
                board.play(board.board, i, j)
                busy = true
                delay(500)
                if(board.check() == ' ') {
                    playAI(difficulty)
                    if(board.check() != ' ') {
                        delay(500)
                        board.switchSign()
                        board.resetBoard()
                    }
                } else {
                    board.switchSign()
                    board.resetBoard()
                }
                busy = false
            }
        }
    }

    suspend fun demo() {
        board.runAIvsAI()
    }

    fun demoAlphaBeta() {
        board.runAIvsAIAlphaBeta()
    }

    fun resetBoard() {
        board.resetBoard()
    }
}

