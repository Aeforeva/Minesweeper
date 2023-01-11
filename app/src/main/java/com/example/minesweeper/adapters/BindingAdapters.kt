package com.example.minesweeper.adapters

import android.graphics.Color
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.minesweeper.R
import com.example.minesweeper.ui.GameState

@BindingAdapter("gameState")
fun setNewGameButton(imageView: ImageView, gameState: GameState) {
    when (gameState) {
        GameState.WIN -> imageView.setImageResource(R.drawable.game_win)
        GameState.LOSS -> imageView.setImageResource(R.drawable.game_loss)
        else -> imageView.setImageResource(R.drawable.game_new)
    }
}

@BindingAdapter("gameType", "id")
fun setTextColor(button: Button, gameType: Int, id: Int) {
    if (gameType == id) button.setTextColor(Color.BLACK)
    else button.setTextColor(Color.rgb(125, 125, 125))
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
    if (!isOpen && isFlag) imageView.setImageResource(R.drawable.flag_small)
    if (isOpen && isMine) imageView.setImageResource(R.drawable.mine)
    if (isWrongCell && isFlag) imageView.setImageResource(R.drawable.mine_wrong)
    if (isWrongCell && isMine) imageView.setImageResource(R.drawable.explosion)

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