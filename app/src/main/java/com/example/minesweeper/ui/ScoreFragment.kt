package com.example.minesweeper.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.minesweeper.R
import com.example.minesweeper.data.*
import com.example.minesweeper.databinding.FragmentScoreBinding


class ScoreFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentScoreBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScoreBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE)
        setScoresContent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            val action = ScoreFragmentDirections.actionScoreFragmentToOneFragment()
            findNavController().navigate(action)
        }
        binding.buttonReset.setOnClickListener {
            confirmResetScores()
        }
    }

    private fun confirmResetScores() {

        AlertDialog.Builder(requireContext())
            .setTitle("Delete high scores")
            .setMessage("Are you sure you want to delete high scores?")
            .setPositiveButton(android.R.string.yes) { _, _ ->
                resetScores()
                setScoresContent()
            }
            .setNegativeButton(android.R.string.no, null)
//            .setIcon(android.R.drawable.ic_dialog_alert)
            .setIcon(R.drawable.flag)
            .show()
    }

    private fun resetScores() {
        viewModel.easyScore = 999
        viewModel.mediumScore = 999
        viewModel.hardScore = 999
        viewModel.easyName = "Developer"
        viewModel.mediumName = "Developer"
        viewModel.hardName = "Developer"
        viewModel.playerName = "Player"

        sharedPref.edit() { putInt(EASY_HIGH_SCORE, 999).apply() }
        sharedPref.edit() { putString(EASY_TOP_PLAYER, "Developer").apply() }
        sharedPref.edit() { putInt(MEDIUM_HIGH_SCORE, 999).apply() }
        sharedPref.edit() { putString(MEDIUM_TOP_PLAYER, "Developer").apply() }
        sharedPref.edit() { putInt(HARD_HIGH_SCORE, 999).apply() }
        sharedPref.edit() { putString(HARD_TOP_PLAYER, "Developer").apply() }
        sharedPref.edit() { putString(PLAYER_NAME, "Player").apply() }
    }

    private fun setScoresContent() {
        binding.easyScore.text = "${viewModel.easyScore} sec."
        binding.easyName.text = viewModel.easyName
        binding.mediumScore.text = "${viewModel.mediumScore} sec."
        binding.mediumName.text = viewModel.mediumName
        binding.hardScore.text = "${viewModel.hardScore} sec."
        binding.hardName.text = viewModel.hardName
    }
}