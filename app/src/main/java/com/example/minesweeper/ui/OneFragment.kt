package com.example.minesweeper.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.minesweeper.R

class OneFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createCells(10, 10)
        viewModel.blockSelectedCell(viewModel.cells[0])
        viewModel.populateCellsWithMines(10, 10, 10)
        viewModel.unBlockSelectedCell(viewModel.cells[0])
        viewModel.countMinesNearBy(10, 10)
    }
}