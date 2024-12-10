package algorithms

import model.Board
import kotlin.math.max
import kotlin.math.min

/**
 * Fajl namenjen za lepse skladistenje funkcija vezanih za minimax algoritam iz
 * klase model.Board, kako bi njen glavni fajl bio citljiviji. Realizovano pomocu Kotlin
 * extension funkcija. Vremenska kompleksnost je O(b^d), gde je b (branching factor),
 * prosecan broj poteza po stanju, a d dubina stabla igre.
 * Prosecno vreme za AI vs AI partiju: 0.624 sec
 */

/**
 *  Minimax algoritam koji trazi najbolji moguci potez. Ovaj algoritam proverava
 *  celo stablo igre, dok ne dodje do terminalnih stanja, u kojima vrsi evaluaciju,
 *  i u zavisnosti od ishoda, dodeljuje vednost tom listu (1 - za pobedu 'X', 0 za
 *  nereseno i -1 za pobedu 'O'). Nakon sto dodje do terminalnog stanja, rekurzivno
 *  se vraca i u zavisnosti od toga koji igrac je na redu u svakom prethodnom stanju
 */
fun Board.minimax(board: Array<Array<Char>>, depth: Int, maximizing: Boolean, maxDepth: Int): Int {
    val winner = check()
    // terminalno stanje, vrsi se evaluacija stanja
    if(winner != ' ') {
        return when(winner) {
            'X' -> 10 - depth
            'O' -> depth - 10
            else -> 0
        }
    }

    if(depth == maxDepth) {
        return 0
    }

    if(maximizing) {
        var best = Int.MIN_VALUE
        for(i in 0..2) {
            for(j in 0..2) {
                if(isSafe(i, j)) {
                    playSim(board, i, j)
                    val score = minimax(board, depth + 1, false, maxDepth)
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
                if(isSafe(i, j)) {
                    playSim(board, i, j)
                    val score = minimax(board, depth + 1, true, maxDepth)
                    undo(board, i, j)
                    best = min(best, score)
                }
            }
        }
        return best
    }
}

fun Board.getBestMoveX(difficulty: Int): Pair<Int, Int> {
    moves++
    if(moves == 1) {
        return (1 to 1)
    }
    if(moves == 2) {
        return (0 to 0)
    }
    var bestScore = Int.MIN_VALUE
    var bestMove: Pair<Int, Int> = 0 to 0

    for(i in 0..2) {
        for(j in 0..2) {
            if(board[i][j] == ' ') {
                playSim(board, i, j)
                val score = minimax(board, 0, false, difficulty)
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

fun Board.getBestMoveO(difficulty: Int): Pair<Int, Int> {
    moves++
//    if(moves == 1) {
//        return (1 to 1)
//    }
    if(moves == 2 && isSafe(0, 0)) {
        return (0 to 0)
    }
    var bestScore = Int.MAX_VALUE
    var bestMove: Pair<Int, Int> = 0 to 0

    for(i in 0..2) {
        for(j in 0..2) {
            if(board[i][j] == ' ') {
                playSim(board, i, j)
                val score = minimax(board, 0, true, difficulty)
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

fun Board.playSim(board: Array<Array<Char>>, i: Int, j: Int) {
    board[i][j] = curSign
    switchSign()
}

fun Board.playAI(difficulty: Int) {
    val i: Int
    val j: Int
    if(curSign == 'X') {
        i = getBestMoveX(difficulty).first
        j = getBestMoveX(difficulty).second
    } else {
        i = getBestMoveO(difficulty).first
        j = getBestMoveO(difficulty).second

    }
    play(board, i, j)
}

/**
 *  Simulacija racunar sam protiv sebe (uvek nereseno)
 */
suspend fun Board.runAIvsAI() {
    var winner = ' '
    while (winner == ' ') {
        playAI(9)
        //delay(1000)
        winner = check()
        if (winner != ' ') break
        playAI(9)
        winner = check()
        //delay(1000)
    }
    //printBoard() // za debagovanje
}