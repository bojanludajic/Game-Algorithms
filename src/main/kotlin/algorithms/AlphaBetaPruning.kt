package algorithms

import kotlinx.coroutines.delay
import model.Board
import kotlin.math.max
import kotlin.math.min

/**
 * Fajl namenjen za lepse skladistenje funkcija vezanih za alpha-beta pruning algoritam
 * iz klase model.Board, kako bi njen glavni fajl bio citljiviji. Realizovano pomocu Kotlin
 * extension funkcija. U najboljem slucaju vremenska kompleksnost se svodi na
 * O(b^(d/2)) - gde je b prosecan broj poteza po stanju, a d dubina stabla igre.
 * U najgorem slucaju algoritam sve, logicno, svodi na minimaks algoritam.
 */

/**
 * Alpha-beta pruning (obrezivanje) minimax algoritam primenjen nad stablom igre, kako
 * bi se izbegla evaluacija podstabala do kojih se nikada ne bi ni stizalo.
 */
private fun Board.minimaxAlphaBeta(
    board: Array<Array<Char>>,
    depth: Int,
    alpha: Int,
    beta: Int,
    maximizing: Boolean
): Int {
    if(moves >= 5) {
        val winner = check()
        if (winner != ' ') {
            return when (winner) {
                'X' -> 10 - depth
                'O' -> depth - 10
                else -> 0
            }
        }
    }

    var a = alpha
    var b = beta

    if (maximizing) {
        var best = Int.MIN_VALUE
        outer@ for (i in 0..2) {
            for (j in 0..2) {
                if (isSafe(i, j)) {
                    playSim(board, i, j)
                    moves++
                    val score = minimaxAlphaBeta(board, depth + 1, a, b, false)
                    undo(board, i, j)
                    moves--
                    best = max(best, score)
                    a = max(a, score)
                    if (b <= a) {
                        break@outer
                    }
                }
            }
        }
        return best
    } else {
        var best = Int.MAX_VALUE
        outer@ for (i in 0..2) {
            for (j in 0..2) {
                if (isSafe(i, j)) {
                    playSim(board, i, j)
                    moves++
                    val score = minimaxAlphaBeta(board, depth + 1, a, b, true)
                    undo(board, i, j)
                    moves--
                    best = min(best, score)
                    b = min(b, score)
                    if (b <= a) {
                        break@outer
                    }
                }
            }
        }
        return best
    }
}

fun Board.getBestMoveXAlphaBeta(): Pair<Int, Int> {
    if(moves == 1) {
        return (1 to 1)
    }

    var bestScore = Int.MIN_VALUE
    var bestMove: Pair<Int, Int> = 0 to 0

    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') {
                playSim(board, i, j)
                val score = minimaxAlphaBeta(board, 0, Int.MIN_VALUE, Int.MAX_VALUE, false)
                undo(board, i, j)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i to j
                }
            }
        }
    }
    return bestMove
}

fun Board.getBestMoveOAlphaBeta(): Pair<Int, Int> {
    if(moves == 2 && isSafe(1, 1)) {
        return (1 to 1)
    }

    var bestScore = Int.MAX_VALUE
    var bestMove: Pair<Int, Int> = 0 to 0

    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') {
                playSim(board, i, j)
                val score = minimaxAlphaBeta(board, 0, Int.MIN_VALUE, Int.MAX_VALUE, true)
                undo(board, i, j)
                if (score < bestScore) {
                    bestScore = score
                    bestMove = i to j
                }
            }
        }
    }
    return bestMove
}

fun Board.playAIAlphaBeta() {
    moves++
    val i: Int
    val j: Int
    if (curSign == 'X') {
        val pair = getBestMoveXAlphaBeta()
        i = pair.first
        j = pair.second
    } else {
        val pair = getBestMoveOAlphaBeta()
        i  = pair.first
        j = pair.second
    }
    play(board, i, j)
}

fun Board.runAIvsAIAlphaBeta() {
    moves = 0
    var winner = ' '
    while (winner == ' ') {
        playAIAlphaBeta()
        winner = check()
        if (winner != ' ') break
        playAIAlphaBeta()
        winner = check()
    }
}