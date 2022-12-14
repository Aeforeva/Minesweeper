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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minesweeper.adapters.CellAdapter
import com.example.minesweeper.databinding.FragmentOneBinding

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

        viewModel.createCells(10, 10)
        viewModel.blockSelectedCell(viewModel.cells[0])
        viewModel.populateCellsWithMines(10, 10, 10)
        viewModel.unBlockSelectedCell(viewModel.cells[0])
        viewModel.countMinesNearBy(10, 10)
        viewModel.logMinesId()

        val adapter = CellAdapter(viewModel.cells) {
            Log.d("CELL", it.toString())
            vibratePhone()
        }
        binding.recycler.layoutManager = GridLayoutManager(context, 10)
        binding.recycler.adapter = adapter
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun Fragment.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}