package com.example.minesweeper.adapters

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("logThis")
fun doLog(textView: TextView, string: String?) {
    Log.d("Binding Adapter", string.toString())
}

//@BindingAdapter("minesNearBy")
//fun contentResolve(textView: TextView, minesNearBy: Int) {
//    textView.text = minesNearBy.toString()
//}

@BindingAdapter("isMine", "minesNearBy")
fun contentResolve(textView: TextView, isMine: Boolean, minesNearBy: Int) {
    if (isMine) {
        textView.text = "\uD83D\uDCA3"
        textView.text = "\uD83D\uDEA9"
        textView.setBackgroundColor(Color.LTGRAY)
    } else {
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

//@BindingAdapter("listData")
//fun bindRecyclerView(recyclerView: RecyclerView, data: List<Dot>?) {
//    val adapter = recyclerView.adapter as DotAdapter
//    adapter.submitList(data)
//}