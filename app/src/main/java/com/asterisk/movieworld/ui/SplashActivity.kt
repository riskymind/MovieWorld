package com.asterisk.movieworld.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.asterisk.movieworld.R
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launchWhenStarted {
            delay(3000)
            Intent(this@SplashActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}