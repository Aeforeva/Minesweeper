package io.github.aeforeva.minesweeper.model

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class CustomGridLayoutManager(context: Context?, row: Int) : GridLayoutManager(context, row) {

    private var isScrollEnabled = false

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically()
    }
}

/** In Fragment try this when LinearLayoutManager needed */
//        val noScroll = object : LinearLayoutManager(requireContext()) {
//            override fun canScrollVertically() = false
//        }