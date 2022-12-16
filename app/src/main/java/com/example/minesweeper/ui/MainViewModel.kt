package com.example.minesweeper.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minesweeper.model.Cell

enum class GameState { NEW, PLAYING, WIN, LOSS }

class MainViewModel : ViewModel() {

    val cells = mutableListOf<Cell>()
    val gameState = MutableLiveData<GameState>()
    val minesLeft = MutableLiveData<Int>(0)

    // Game parameters
    var xMax = 10
    var yMax = 10
    var minesToSet = 10

    init {
        setNewGame()
    }

    fun setNewGame() {
        gameState.value = GameState.NEW
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

    fun endGame(wrongCell: Cell) {
        if (isPlayerWin()) {
            gameState.value = GameState.WIN
            for (cell in cells) {
                if (cell.isMine) cell.isFlag = true
            }
            // TODO High Score
        } else {
            // TODO highlight the wrong cell with mine
            gameState.value = GameState.LOSS
            for (cell in cells) {
                if (cell.isMine && !cell.isFlag) cell.isOpen = true
            }
        }
    }

    fun isPlayerWin(): Boolean {
        return cells.indexOfFirst { !it.isMine && !it.isOpen } < 0
    }

    fun createCells(xMax: Int, yMax: Int) {
        cells.clear()
        var y = 0
        while (y < yMax) {
            var x = 0
            while (x < xMax) {
                cells.add(Cell(x, y))
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
        if (cell.x > 0 && cell.y > 0) { // ↖ index of this
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y - 1 })
        }
        if (cell.y > 0) { // ↑ index of that
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x && it.y == cell.y - 1 })
        }
        if (cell.x < xMax - 1 && cell.y > 0) { // ↗
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y - 1 })
        }
        if (cell.x > 0) { // ←
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y })
        }
        if (cell.x < xMax - 1) { // →
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y })
        }
        if (cell.x > 0 && cell.y < yMax - 1) { // ↙
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y + 1 })
        }
        if (cell.y < yMax - 1) { // ↓
            nearByCellsIndexes.add(cells.indexOfFirst { it.x == cell.x && it.y == cell.y + 1 })
        }
        if (cell.x < xMax - 1 && cell.y < yMax - 1) { // ↘
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
        val indexes = getNearByCellsIndexes(cell, xMax, yMax)
        for (i in indexes) {
            if (!cells[i].isCheck) {
                cells[i].isCheck = true
                cells[i].isOpen = true
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
        if (cell.minesNearBy == flagsNearBy) {
            for (i in indexes) {
                if (!cells[i].isFlag && cells[i].isMine) endGame(cells[i])
                if (!cells[i].isFlag && cells[i].minesNearBy == 0) openChainReaction(cells[i])
                if (!cells[i].isFlag) cells[i].isOpen = true
            }
        }
    }
}