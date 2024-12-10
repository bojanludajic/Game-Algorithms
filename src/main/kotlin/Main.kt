import algorithms.runAIvsAIAlphaBeta
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TicTacToe(
    manager: BoardManager
) {
    var expanded by mutableStateOf(false)
    val difficulties = manager.getDifficulties()
    var selectedOptionText by remember { mutableStateOf(difficulties[2]) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = true,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .padding(10.dp)

            ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = {  },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    manager.getDifficulties().forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                manager.setDifficulty(selectionOption)
                                expanded = false
                            }
                        ){
                            Text(text = selectionOption)
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(0.75f)
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
}

fun main() = application {
    Window(
        title = "Tic-Tac-Toe Minimax",
        onCloseRequest = ::exitApplication,
        //resizable = false
    ) {
        val manager = remember { BoardManager() }
        TicTacToe(manager)
        LaunchedEffect(Unit) {
            delay(1000)

            val start = System.currentTimeMillis()
            manager.demo()
            val end = System.currentTimeMillis()
            println("Minimax algoritam gotov za: ${(end - start).toDouble() / 1000} sekundi.")
            manager.resetBoard()
            delay(1000)

            val start1 = System.currentTimeMillis()
            manager.demoAlphaBeta()
            val end1 = System.currentTimeMillis()
            println("Alpha beta pruning algoritam gotov za: ${(end1 - start1).toDouble() / 1000} sekundi.")
            delay(1000)
            manager.resetBoard()
        }
    }
}
