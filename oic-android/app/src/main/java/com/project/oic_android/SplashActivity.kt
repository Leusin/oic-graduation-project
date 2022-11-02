package com.project.oic_android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.project.oic_android.databinding.ActivitySplashBinding
import com.project.oic_android.login.LoginActivity

class SplashActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedIntentStatus: Bundle?) {
        super.onCreate(savedIntentStatus)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURIATION)
    }
    companion object{
        private const val DURIATION : Long = 2000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}