package com.project.oic_android

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.project.oic_android.databinding.ActivityLoginBinding
import com.project.oic_android.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinin.setOnClickListener { changeVisibility("signin")}
        binding.backIcon.setOnClickListener{ changeVisibility("logout") }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


    // 화면 전환
    fun changeVisibility(mode: String){
        if (mode == "login") {

        }
        else if (mode == "logout"){
            binding.run {
                backIcon.visibility = View.GONE
                toolbarTitle.text = "로그인"
                appTitle.visibility = View.VISIBLE
                username.visibility = View.VISIBLE
                password.visibility = View.VISIBLE
                login.visibility = View.VISIBLE
                joinin.visibility = View.VISIBLE
                loginTalk.visibility = View.VISIBLE
                loginGoogle.visibility = View.VISIBLE
            }
        } // 로그인 화면
        else if (mode == "signin"){
            binding.run {
                backIcon.visibility = View.VISIBLE
                toolbarTitle.text = "회원가입"
                appTitle.visibility = View.GONE
                username.visibility = View.GONE
                password.visibility = View.GONE
                login.visibility = View.GONE
                joinin.visibility = View.GONE
                loginTalk.visibility = View.GONE
                loginGoogle.visibility = View.GONE
            }
        }
    }
}