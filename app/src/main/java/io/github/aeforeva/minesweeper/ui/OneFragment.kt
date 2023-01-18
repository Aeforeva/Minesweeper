package io.github.aeforeva.minesweeper.ui

import android.content.*
import android.content.Context.VIBRATOR_SERVICE
import android.media.MediaPlayer
import android.os.*
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import io.github.aeforeva.minesweeper.TimerService
import io.github.aeforeva.minesweeper.adapters.CellAdapter
import io.github.aeforeva.minesweeper.model.Cell
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import io.github.aeforeva.minesweeper.model.CustomGridLayoutManager
import io.github.aeforeva.minesweeper.R
import io.github.aeforeva.minesweeper.data.*
import io.github.aeforeva.minesweeper.databinding.FragmentOneBinding
import kotlinx.coroutines.Dispatchers.Main

class OneFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentOneBinding
    private lateinit var serviceIntent: Intent
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceIntent = Intent(context, TimerService::class.java)
        requireActivity().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

        /** Get saved values from sharedPref */
        sharedPref = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE)
        viewModel.gameType.value = sharedPref.getInt(GAME_TYPE, 1)
        viewModel.easyScore = sharedPref.getInt(EASY_HIGH_SCORE, 999)
        viewModel.mediumScore = sharedPref.getInt(MEDIUM_HIGH_SCORE, 999)
        viewModel.hardScore = sharedPref.getInt(HARD_HIGH_SCORE, 999)
        viewModel.easyName = sharedPref.getString(EASY_TOP_PLAYER, "Developer")!!
        viewModel.mediumName = sharedPref.getString(MEDIUM_TOP_PLAYER, "Developer")!!
        viewModel.hardName = sharedPref.getString(HARD_TOP_PLAYER, "Developer")!!
        viewModel.playerName = sharedPref.getString(PLAYER_NAME, "Player")!!
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            viewModel.time.value = intent.getIntExtra(TimerService.TIME_EXTRA, 0)
        }
    }

    private fun starTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, viewModel.time.value)
        requireActivity().startService(serviceIntent)
    }

    private fun stopTimer() {
        requireActivity().stopService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.gameState.value == GameState.PLAYING) starTimer()
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recycler.layoutManager = GridLayoutManager(context, viewModel.xMax)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameEasy.setOnClickListener { newGame(1) }

        binding.gameMedium.setOnClickListener { newGame(2) }

        binding.gameHard.setOnClickListener { newGame(3) }

        binding.newGameButton.setOnClickListener { newGame(viewModel.gameType.value!!) }

        binding.highScore.setOnClickListener {
            findNavController().navigate(R.id.action_oneFragment_to_scoreFragment)
        }

        /** If it is not first start only render current state */
        if (viewModel.isFirstStart) {
            newGame(viewModel.gameType.value!!)
            viewModel.isFirstStart = false
        } else {
            setAdapter()
        }
    }

    private fun newGame(gameType: Int) {
        stopTimer()
        viewModel.gameType.value = gameType
        viewModel.setGameParameters(gameType)
        binding.recycler.layoutManager = CustomGridLayoutManager(context, viewModel.xMax)
        viewModel.setNewGame()
        setAdapter()

        sharedPref.edit() { putInt(GAME_TYPE, gameType).apply() }
    }

    private fun setAdapter() {
        binding.recycler.adapter = CellAdapter(viewModel.cells, {
            onClick(it)
            reRender(it)
//            setAdapter()
        }, {
            onLongClick(it)
//            setAdapter()
        })
    }

    private fun reRender(cell: Cell) {
        for (i in viewModel.itemToNotify) {
            binding.recycler.adapter?.notifyItemChanged(i, cell)
        }
        viewModel.itemToNotify.clear()
    }

    /** GAME LOGIC */
    private fun onClick(cell: Cell) {
        // First click logic (mine safe)
        if (viewModel.gameState.value == GameState.NEW && !cell.isFlag) {
            viewModel.startGame(cell)
            starTimer()
        }
        // Main game logic
        if (viewModel.gameState.value == GameState.PLAYING && !cell.isFlag) {
            // Open single cell
            cell.isOpen = true
            binding.recycler.adapter?.notifyItemChanged(cell.id, cell)
            // Mine = game loss
            if (cell.isMine) {
                viewModel.endGame(cell)
                vibratePhone()
                stopTimer()
                return
            }
            // Fast open nearby cells when mines marked, if wrong = game loss
            if (cell.isOpen && cell.minesNearBy > 0) viewModel.openNearBy(cell)
            if (viewModel.gameState.value == GameState.LOSS) {
                vibratePhone()
                stopTimer()
                return
            }
            // Mass open when possible
            if (cell.minesNearBy == 0) viewModel.openChainReaction(cell)
            // Call reRender() here to try improve UI performance, I guess isPlayerWin() check might take a bit time
            reRender(cell)
            // Check win condition
            if (viewModel.isPlayerWin()) {
                viewModel.endGame(cell)
                stopTimer()
                val coinSound: MediaPlayer = MediaPlayer.create(requireContext(), R.raw.coin)
                coinSound.start()
                if (viewModel.isNewHighScore()) askPlayerNameDialog()
            }
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
            val arrowSound: MediaPlayer = MediaPlayer.create(context, R.raw.arrow_whoosh)
            arrowSound.start()
        }
    }

    private fun askPlayerNameDialog() {
        val nameInput = EditText(requireContext())
        nameInput.inputType = InputType.TYPE_CLASS_TEXT
        nameInput.gravity = Gravity.CENTER
        nameInput.filters = arrayOf(LengthFilter(13))
        nameInput.setText(viewModel.playerName)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.new_high_score, viewModel.time.value))
            .setMessage(R.string.enter_name)
            .setView(nameInput)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel.playerName = nameInput.editableText.toString()
                setNewHighScore()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setNewHighScore() {
        when (viewModel.gameType.value) {
            1 -> {
                viewModel.easyScore = viewModel.time.value!!
                viewModel.easyName = viewModel.playerName
                sharedPref.edit() { putInt(EASY_HIGH_SCORE, viewModel.time.value!!).apply() }
                sharedPref.edit() { putString(EASY_TOP_PLAYER, viewModel.playerName).apply() }
            }
            2 -> {
                viewModel.mediumScore = viewModel.time.value!!
                viewModel.mediumName = viewModel.playerName
                sharedPref.edit() { putInt(MEDIUM_HIGH_SCORE, viewModel.time.value!!).apply() }
                sharedPref.edit() { putString(MEDIUM_TOP_PLAYER, viewModel.playerName).apply() }
            }
            3 -> {
                viewModel.hardScore = viewModel.time.value!!
                viewModel.hardName = viewModel.playerName
                sharedPref.edit() { putInt(HARD_HIGH_SCORE, viewModel.time.value!!).apply() }
                sharedPref.edit() { putString(HARD_TOP_PLAYER, viewModel.playerName).apply() }
            }
        }
        sharedPref.edit() { putString(PLAYER_NAME, viewModel.playerName).apply() }
    }
}

fun Fragment.vibratePhone() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 11
        val vibratorManager =
            context?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context?.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
}

//fun Fragment.vibratePhone() {
//    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//    if (Build.VERSION.SDK_INT >= 26) {
//    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
//    } else {
//        vibrator.vibrate(200)
//    }
//}