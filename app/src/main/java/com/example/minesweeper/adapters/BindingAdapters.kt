package com.example.minesweeper.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import com.example.minesweeper.R
import com.example.minesweeper.ui.GameState
import kotlinx.coroutines.channels.ChannelResult.Companion.closed

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
    if (gameType == id) button.setTextColor(
        Color.rgb(
            125,
            125,
            125
        )
    ) else button.setTextColor(Color.BLACK)
}

@BindingAdapter("minesNearBy", "isMine", "isFlag", "isOpen", "isWrongCell")
fun contentResolve(
    imageView: ImageView,
    minesNearBy: Int,
    isMine: Boolean,
    isFlag: Boolean,
    isOpen: Boolean,
    isWrongCell: Boolean
) {
    imageView.setImageResource(R.drawable.close)
    if (!isOpen && isFlag) imageView.setImageResource(R.drawable.flag)
    if (isOpen && isMine) imageView.setImageResource(R.drawable.mine)
    if (isWrongCell && isFlag) imageView.setImageResource(R.drawable.minewrong)
    if (isWrongCell && isMine) imageView.setImageResource(R.drawable.minedetonanted)

    if (isOpen && !isMine) {
        when (minesNearBy) {
            0 -> imageView.setImageResource(R.drawable.open)
            1 -> imageView.setImageResource(R.drawable.num_one)
            2 -> imageView.setImageResource(R.drawable.num_two)
            3 -> imageView.setImageResource(R.drawable.num_three)
            4 -> imageView.setImageResource(R.drawable.num_four)
            5 -> imageView.setImageResource(R.drawable.num_five)
            6 -> imageView.setImageResource(R.drawable.num_six)
            7 -> imageView.setImageResource(R.drawable.num_seven)
            8 -> imageView.setImageResource(R.drawable.num_eight)
        }
    }
}