package io.github.aeforeva.minesweeper.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aeforeva.minesweeper.model.Cell
import kotlinx.coroutines.launch

enum class GameState { NEW, PLAYING, WIN, LOSS }

class MainViewModel : ViewModel() {

    val cells = mutableListOf<Cell>()
    val gameState = MutableLiveData<GameState>()
    val minesLeft = MutableLiveData(0)
    val time = MutableLiveData(0)
    val gameType = MutableLiveData(1)
    val itemToNotify = mutableSetOf<Int>()

    // High Scores
    var easyScore = 999
    var mediumScore = 999
    var hardScore = 999
    var easyName = "Developer"
    var mediumName = "Developer"
    var hardName = "Developer"
    var playerName = "Player"

    // Game parameters
    var xMax = 10
    var yMax = 10
    var minesToSet = 10
    var isFirstStart = true

//    init {
//        setGameParameters(gameType.value!!)
//        setNewGame()
//    }

    fun setGameParameters(gameType: Int) {
        when (gameType) {
            1 -> {
                xMax = 10
                yMax = 10
                minesToSet = 10
            }
            2 -> {
                xMax = 14
                yMax = 18
                minesToSet = 40
            }
            3 -> {
                xMax = 16
                yMax = 24
                minesToSet = 80
            }
        }
    }

    fun intToThreeIntString(num: Int): String {
        val c = num % 10
        val b = (num - c) % 100 / 10
        val a = (num - 10 * b - c) % 1000 / 100
        return "$a$b$c"
    }

    fun setNewGame() {
        gameState.value = GameState.NEW
        time.value = 0
        minesLeft.value = minesToSet
        createCells(xMax, yMax)
    }

    fun startGame(cell: Cell) {
        blockSelectedCell(cell)
        populateCellsWithMines(xMax, yMax, minesToSet)
        unBlockSelectedCell(cell)
        countMinesNearByForAllCells(xMax, yMax)
        gameState.value = GameState.PLAYING
    }

    fun endGame(lastCell: Cell) {
//        viewModelScope.launch {
        if (isPlayerWin()) {
            gameState.value = GameState.WIN
            minesLeft.value = 0
            for (cell in cells) {
                if (cell.isMine) {
                    cell.isFlag = true
                    itemToNotify.add(cell.id)
                }
            }
        } else {
            lastCell.isWrongCell = true
            itemToNotify.add(lastCell.id)
            gameState.value = GameState.LOSS
            for (cell in cells) {
                if (cell.isMine && !cell.isFlag) {
                    cell.isOpen = true
                    itemToNotify.add(cell.id)
                }
                if (cell.isFlag && !cell.isMine) {
                    cell.isWrongCell = true
                    itemToNotify.add(cell.id)
                }
            }
        }
        Log.d("Game", gameState.value.toString())
//        }
    }

    fun isPlayerWin(): Boolean {
        return cells.indexOfFirst { !it.isMine && !it.isOpen && !it.isWrongCell } < 0
    }

    private fun createCells(xMax: Int, yMax: Int) {
        cells.clear()
        var id = 0
        var y = 0
        while (y < yMax) {
            var x = 0
            while (x < xMax) {
                cells.add(Cell(x, y, id))
                id++
                x++
            }
            y++
        }
    }

    private fun blockSelectedCell(cell: Cell) {
        cell.isMine = true
    }

    private fun populateCellsWithMines(x: Int, y: Int, minesToSet: Int) {
        val randomRange: IntRange = 0 until x * y
        var mines = minesToSet
        while (mines > 0) {
            val randomIndex = randomRange.random()
            if (!cells[randomIndex].isMine) {
                cells[randomIndex].isMine = true
                mines--
            }
        }
    }

    private fun unBlockSelectedCell(cell: Cell) {
        cell.isMine = false
    }

    private fun getNearByCellsIndexes(cell: Cell, xMax: Int, yMax: Int): List<Int> {
        val nearByCellsIndexes = mutableListOf<Int>()
        if (cell.x > 0 && cell.y > 0) { // ??? index of this
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y - 1 })
        }
        if (cell.y > 0) { // ??? index of that
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x && it.y == cell.y - 1 })
        }
        if (cell.x < xMax - 1 && cell.y > 0) { // ???
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y - 1 })
        }
        if (cell.x > 0) { // ???
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y })
        }
        if (cell.x < xMax - 1) { // ???
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y })
        }
        if (cell.x > 0 && cell.y < yMax - 1) { // ???
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y + 1 })
        }
        if (cell.y < yMax - 1) { // ???
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x && it.y == cell.y + 1 })
        }
        if (cell.x < xMax - 1 && cell.y < yMax - 1) { // ???
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y + 1 })
        }
        return nearByCellsIndexes
    }

    private fun countMinesNearByForAllCells(xMax: Int, yMax: Int) {
        for (cell in cells) {
            val indexes = getNearByCellsIndexes(cell, xMax, yMax)
            for (i in indexes) {
                if (cells[i].isMine) cell.minesNearBy++
            }
        }
    }

    fun openChainReaction(cell: Cell) {
        cell.isCheck = true
        cell.isOpen = true
        itemToNotify.add(cell.id)
        val indexes = getNearByCellsIndexes(cell, xMax, yMax)
        for (i in indexes) {
            if (!cells[i].isCheck) {
                cells[i].isCheck = true
                cells[i].isOpen = true
                itemToNotify.add(i)
                if (cells[i].minesNearBy == 0) openChainReaction(cells[i])
            }
        }
    }

    fun openNearBy(cell: Cell) {
        val indexes = getNearByCellsIndexes(cell, xMax, yMax)
        var flagsNearBy = 0
        for (i in indexes) {
            if (cells[i].isFlag) flagsNearBy++
        }
        if (cell.minesNearBy == flagsNearBy) { // or <= but in original minesweeper it's ==
            for (i in indexes) {
                if (!cells[i].isFlag) {
                    // Open single cell
                    cells[i].isOpen = true
                    itemToNotify.add(i)
                    // Mine = game loss
                    if (cells[i].isMine) endGame(cells[i])
                    // Mass open when possible
                    if (cells[i].minesNearBy == 0) openChainReaction(cells[i])
                }
            }
        }
    }

    fun isNewHighScore(): Boolean {
        return when (gameType.value) {
            1 -> time.value!! < easyScore
            2 -> time.value!! < mediumScore
            3 -> time.value!! < hardScore
            else -> false
        }
    }
}