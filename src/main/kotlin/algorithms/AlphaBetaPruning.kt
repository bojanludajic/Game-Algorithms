package algorithms

import model.Board
import kotlin.math.max
import kotlin.math.min

/**
 * Fajl namenjen za lepse skladistenje funkcija vezanih za alpha-beta pruning algoritam
 * iz klase model.Board, kako bi njen glavni fajl bio citljiviji. Realizovano pomocu Kotlin
 * extension funkcija. U najboljem slucaju vremenska kompleksnost se svodi na
 * O(b^(d/2)) - gde je b prosecan broj poteza po stanju, a d dubina stabla igre.
 * U najgorem slucaju algoritam sve, logicno, svodi na minimaks algoritam.
 * Prosecno vreme za AI vs AI partiju: 0.081 sec (7.7 puta brze vreme izvrsavanj)
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
    val winner = check()
    if (winner != ' ') {
        return when (winner) {
            'X' -> 10 - depth
            'O' -> depth - 10
            else -> 0
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
                    val score = minimaxAlphaBeta(board, depth + 1, a, b, false)
                    undo(board, i, j)
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
                    val score = minimaxAlphaBeta(board, depth + 1, a, b, true)
                    undo(board, i, j)
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
    val i: Int
    val j: Int
    if (curSign == 'X') {
        i = getBestMoveXAlphaBeta().first
        j = getBestMoveXAlphaBeta().second
    } else {
        i = getBestMoveOAlphaBeta().first
        j = getBestMoveOAlphaBeta().second

    }
    play(board, i, j)
}

fun Board.runAIvsAIAlphaBeta() {
    var winner = ' '
    while (winner == ' ') {
        playAIAlphaBeta()
        //delay(1000)
        winner = check()
        if (winner != ' ') break
        playAIAlphaBeta()
        winner = check()
        //delay(1000)
    }
}