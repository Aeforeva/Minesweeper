package com.example.minesweeper

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.minesweeper.ui.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        viewModel.gameType.observe(this) {
            if (it == 3) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.windowInsetsController?.show(WindowInsets.Type.statusBars())
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}