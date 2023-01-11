package io.github.aeforeva.minesweeper.ui

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
import io.github.aeforeva.minesweeper.R
import io.github.aeforeva.minesweeper.data.*
import io.github.aeforeva.minesweeper.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentScoreBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoreBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE)
        setScoresContent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_scoreFragment_to_oneFragment)
        }
        binding.buttonReset.setOnClickListener {
            confirmResetScores()
        }
    }

    private fun confirmResetScores() {

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.reset_high_scores)
            .setMessage(R.string.confirm_question)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                resetScores()
                setScoresContent()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(android.R.drawable.presence_busy)
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
        binding.easyScore.text = getString(R.string.add_sec, viewModel.easyScore)
        binding.easyName.text = viewModel.easyName
        binding.mediumScore.text = getString(R.string.add_sec, viewModel.mediumScore)
        binding.mediumName.text = viewModel.mediumName
        binding.hardScore.text = getString(R.string.add_sec, viewModel.hardScore)
        binding.hardName.text = viewModel.hardName
    }
}