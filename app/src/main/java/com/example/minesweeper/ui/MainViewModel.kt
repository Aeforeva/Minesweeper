package com.example.minesweeper.ui

import androidx.lifecycle.ViewModel
import com.example.minesweeper.model.Cell

class MainViewModel : ViewModel() {

    val cells = mutableListOf<Cell>()
    var gameType = 1

    fun createCells(xMax: Int, yMax: Int) {
        cells.clear()
        var y: Int = 0
        while (y < yMax) {
            var x: Int = 0
            while (x < xMax) {
                cells.add(Cell(x, y))
                x++
            }
            y++
        }
    }

    fun blockSelectedCell(cell: Cell) {
        cell.isMine = true
    }

    fun populateCellsWithMines(x: Int, y: Int, mines: Int) {
        val randomRange: IntRange = 0 until x * y
        var mines = mines
        while (mines > 0) {
            if (!cells[randomRange.random()].isMine) {
                cells[randomRange.random()].isMine = true
                mines--
            }
        }
    }

    fun unBlockSelectedCell(cell: Cell) {
        cell.isMine = false
    }

    fun countMinesNearBy(xMax: Int, yMax: Int) {
        for (cell in cells) {
            // ↖
            if (cell.x > 0 && cell.y > 0) {
                val index = cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y - 1 }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // ↑
            if (cell.y > 0) {
                val index = cells.indexOfFirst { it.x == cell.x && it.y == cell.y - 1 }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // ↗
            if (cell.x < xMax - 1 && cell.y > 0) {
                val index = cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y - 1 }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // ←
            if (cell.x > 0) {
                val index = cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // →
            if (cell.x < xMax - 1) {
                val index = cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // ↙
            if (cell.x > 0 && cell.y < yMax - 1) {
                val index = cells.indexOfFirst { it.x == cell.x - 1 && it.y == cell.y + 1 }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // ↓
            if (cell.y < yMax - 1) {
                val index = cells.indexOfFirst { it.x == cell.x && it.y == cell.y + 1 }
                if (cells[index].isMine) cell.minesNearBy++
            }
            // ↘
            if (cell.x < xMax - 1 && cell.y < yMax - 1) {
                val index = cells.indexOfFirst { it.x == cell.x + 1 && it.y == cell.y + 1 }
                if (cells[index].isMine) cell.minesNearBy++
            }
        }
    }

    fun isPlayerWin(): Boolean {
        return cells.indexOfFirst { !it.isMine && !it.isOpen } < 0
    }
}