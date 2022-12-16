package com.example.minesweeper.adapters

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.minesweeper.ui.GameState

@BindingAdapter("gameState")
fun setNewGameButton(textView: TextView, gameState: GameState) {
    when (gameState) {
        GameState.WIN -> textView.text = "\uD83D\uDE0E" // 😎
//        GameState.LOSS -> textView.text = "\uD83E\uDD2F" // 🤯
        GameState.LOSS -> textView.text = "\uD83D\uDE35" // 😵
        else -> textView.text = "\uD83D\uDE42" // 🙂
    }
}

@BindingAdapter("minesNearBy", "isMine", "isFlag", "isOpen")
fun contentResolve(
    textView: TextView,
    minesNearBy: Int,
    isMine: Boolean,
    isFlag: Boolean,
    isOpen: Boolean
) {
    textView.setBackgroundColor(Color.LTGRAY)
    if (!isOpen && isFlag) textView.text = "\uD83D\uDEA9" // 🚩
//    if (isOpen && isMine) textView.text = "\uD83D\uDCA3" // 💣
    if (isOpen && isMine) textView.text = "\uD83D\uDE02" // 😂
    if (isOpen && !isMine) {
        textView.setBackgroundColor(Color.WHITE)
        textView.text = minesNearBy.toString()
        when (minesNearBy) {
            0 -> textView.text = ""
            1 -> textView.setTextColor(Color.BLUE)
            2 -> textView.setTextColor(Color.GREEN)
            3 -> textView.setTextColor(Color.RED)
            4 -> textView.setTextColor(Color.BLUE)
            5 -> textView.setTextColor(Color.GREEN)
            6 -> textView.setTextColor(Color.RED)
            7 -> textView.setTextColor(Color.BLUE)
            8 -> textView.setTextColor(Color.GREEN)
        }
    }
}