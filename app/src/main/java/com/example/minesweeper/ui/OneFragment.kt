package com.example.minesweeper.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minesweeper.R
import com.example.minesweeper.adapters.CellAdapter
import com.example.minesweeper.databinding.FragmentOneBinding
import com.example.minesweeper.model.Cell

class OneFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentOneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recycler.layoutManager = GridLayoutManager(context, 10)

        viewModel.createCells(10, 10)
        firstClick()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneClickLogic()
    }

    fun firstClick() {
        viewModel.blockSelectedCell(viewModel.cells[0])
        viewModel.populateCellsWithMines(10, 10, 10)
        viewModel.unBlockSelectedCell(viewModel.cells[0])
        viewModel.countMinesNearBy(10, 10)
    }

    fun oneClickLogic() {
        binding.recycler.adapter = CellAdapter(viewModel.cells, {
            onClick(it)
            oneClickLogic()
        }, {
            onLongClick(it)
            oneClickLogic()
        })
    }

    private fun onClick(cell: Cell) {
        if (!cell.isOpen) cell.isOpen = true
        Log.d("isOpen", "${cell.id} ${cell.isOpen}")
    }

    private fun onLongClick(cell: Cell) {
        cell.isFlag = !cell.isFlag
        Log.d("isFlag", "${cell.id} ${cell.isFlag}")
    }
}

fun Fragment.vibratePhone() {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}
