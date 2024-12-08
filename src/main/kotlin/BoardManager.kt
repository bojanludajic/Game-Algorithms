import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.*


class BoardManager {

    private var board by mutableStateOf(Board())
    private var busy by mutableStateOf(false)

    fun get(i: Int, j: Int): Char {
        return board.board[i][j]
    }

    fun isClickable() = !busy

    fun backgroundColor(): Color = when(board.check()) {
        'X' -> Color.Green
        'O' -> Color.Yellow
        'T' -> Color.Gray
        else -> Color.White
    }

    fun playAI() {
        board.playAI()
        board.printBoard()
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
                    playAIOptimal()
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

    suspend fun demoAlphaBeta() {
        board.runAIvsAIAlphaBeta()
    }

    fun resetBoard() {
        board.resetBoard()
    }
}

