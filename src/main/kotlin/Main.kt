import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


@Composable
fun TicTacToe(
    manager: BoardManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        repeat(3) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                repeat(3) { colIndex ->
                    val bgColor = manager.backgroundColor()
                    Box(
                        modifier = Modifier
                            .border(2.dp, Color.Black)
                            .fillMaxSize()
                            .weight(1f)
                            .background(bgColor)
                            .then(if (manager.isClickable()) Modifier.clickable {
                                manager.play(rowIndex, colIndex)
                            } else Modifier
                            )

                    ) {
                        Text(
                            text = "${manager.get(rowIndex, colIndex)}",
                            modifier = Modifier
                                .align(Alignment.Center),
                            style = MaterialTheme.typography.h1
                        )
                    }

                }
            }
        }
    }
}

fun main() = application {
    Window(
        title = "Tic-Tac-Toe Minimax",
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        val manager = remember { BoardManager() }
        TicTacToe(manager)
        LaunchedEffect(Unit) {
            //manager.demo()
            delay(200)
            manager.playAI()
        }
    }
}
