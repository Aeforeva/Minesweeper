package com.example.minesweeper.model

data class Cell(
    val x: Int,
    val y: Int,
    val id: String = "$x.$y",
    var minesNearBy: Int = 0,
    var isMine: Boolean = false,
    var isFlag: Boolean = false,
    var isOpen: Boolean = false
)