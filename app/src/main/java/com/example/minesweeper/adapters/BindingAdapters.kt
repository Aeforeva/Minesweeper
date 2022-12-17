package com.example.minesweeper.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import com.example.minesweeper.ui.GameState

@BindingAdapter("gameState")
fun setNewGameButton(textView: TextView, gameState: GameState) {
    textView.setTextColor(Color.BLACK)
    when (gameState) {
        GameState.WIN -> textView.text = "\uD83D\uDE0E" // 😎
        GameState.LOSS -> textView.text = "\uD83E\uDD2F" // 🤯
        else -> textView.text = "\uD83D\uDE42" // 🙂
    }
}

@BindingAdapter("gameType", "id")
fun setBoldText(button: Button, gameType: Int, id: Int) {
    if (gameType == id) button.setTextColor(Color.WHITE) else button.setTextColor(Color.BLACK)
}

@BindingAdapter("minesNearBy", "isMine", "isFlag", "isOpen", "isWrongCell")
fun contentResolve(
    textView: TextView,
    minesNearBy: Int,
    isMine: Boolean,
    isFlag: Boolean,
    isOpen: Boolean,
    isWrongCell: Boolean
) {
    textView.setTextColor(Color.BLACK)
    textView.setBackgroundColor(Color.LTGRAY)
    if (!isOpen && isFlag) textView.text = "\uD83D\uDEA9" // 🚩
    if (isOpen && isMine) textView.text = "\uD83D\uDCA3" // 💣
//    if (isOpen && isMine) textView.text = "\uD83D\uDE02" // 😂
    if (isOpen && !isMine) {
        textView.setBackgroundColor(Color.WHITE)
        textView.text = minesNearBy.toString()
        when (minesNearBy) {
            0 -> textView.text = ""
            1 -> textView.setTextColor(Color.BLUE)
            2 -> textView.setTextColor(Color.GREEN)
            3 -> textView.setTextColor(Color.RED)
            4 -> textView.setTextColor(Color.rgb(0,0,139))
            5 -> textView.setTextColor(Color.rgb(139,0,0))
            6 -> textView.setTextColor(Color.rgb(0,139,0))
            7 -> textView.setTextColor(Color.BLACK)
            8 -> textView.setTextColor(Color.BLACK)
        }
    }
    if (isWrongCell) textView.setBackgroundColor(Color.RED)
}