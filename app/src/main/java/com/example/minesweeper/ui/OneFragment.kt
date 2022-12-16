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
        binding.recycler.layoutManager = GridLayoutManager(context, viewModel.xMax)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newGameButton.setOnClickListener {
            viewModel.setNewGame()
            oneClickLogic()
        }

        oneClickLogic()
    }

    private fun oneClickLogic() {
        binding.recycler.adapter = CellAdapter(viewModel.cells, {
            onClick(it)
            oneClickLogic()
        }, {
            onLongClick(it)
            oneClickLogic()
        })
    }

    private fun onClick(cell: Cell) {
        if (viewModel.gameState.value == GameState.NEW) {
            viewModel.startGame(cell)
        }
        if (viewModel.gameState.value == GameState.PLAYING) {
            if (cell.isMine) {
                viewModel.endGame(cell)
                vibratePhone()
            }
            if (cell.isOpen && cell.minesNearBy > 0) viewModel.openNearBy(cell)
            if (!cell.isOpen && !cell.isFlag) cell.isOpen = true
            if (!cell.isMine && cell.minesNearBy == 0) viewModel.openChainReaction(cell)
            if (viewModel.isPlayerWin()) viewModel.endGame(cell)
        }
    }


    private fun onLongClick(cell: Cell) {
        if (!cell.isOpen) {
            if (!cell.isFlag) {
                viewModel.minesLeft.value = viewModel.minesLeft.value?.dec()
            } else {
                viewModel.minesLeft.value = viewModel.minesLeft.value?.inc()
            }
            cell.isFlag = !cell.isFlag
        }
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
