package com.project.oic_android

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.project.oic_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinin.setOnClickListener { changeVisibility("signin")}
        binding.backIcon.setOnClickListener{ changeVisibility("logout") }

        // 구글 로그인
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                AuthApplication.auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { AuthApplication.email = account.email ; changeVisibility("login") }
                    else { changeVisibility("logout") }
                }
            }
            catch (e:ApiException) { changeVisibility("logout") }
        }
          // 로그아웃
//        binding.loginTalk.setOnClickListener {
//            AuthApplication.auth.signOut()
//            AuthApplication.email = null
//            changeVisibility("logout")
//        }

        // 구글 로그인
        binding.loginGoogle.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

        // 이메일 회원가입
        binding.actionJoinIn.setOnClickListener {
            val email = binding.usernameJoin.text.toString()
            val password = binding.passwordJoin.text.toString()
            AuthApplication.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                binding.usernameJoin.text.clear()
                binding.passwordJoin.text.clear()
                if(task.isSuccessful) {
                    AuthApplication.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{ sendTask ->
                        if (sendTask.isSuccessful) { Toast.makeText(baseContext, "$email 로 인증 메일 전송", Toast.LENGTH_SHORT).show(); changeVisibility("logout") }
                        else { Toast.makeText(baseContext, "인증 메일 전송 실패", Toast.LENGTH_SHORT).show(); changeVisibility("logout") }
                    }
                }
                else {
                    Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 이메일 로그인
        binding.login.setOnClickListener {
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            AuthApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task->
                    binding.username.text.clear()
                    binding.password.text.clear()
                    if (task.isSuccessful) {
                        if (AuthApplication.checkAuth()) { AuthApplication.email = email; changeVisibility("login") }
                        else {Toast.makeText(baseContext, "인증 메일을 확인해 주세요", Toast.LENGTH_SHORT).show()}
                    }
                    else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // login test
        binding.loginTalk.setOnClickListener {
            changeVisibility("login")
            finish()
        }

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else if (mode == "logout"){
            binding.run {
                backIcon.visibility = GONE
                greet.visibility = GONE
                usernameJoin.visibility = GONE
                passwordJoin.visibility = GONE
                actionJoinIn.visibility = GONE

                toolbarTitle.text = "로그인"
                appTitle.visibility = VISIBLE
                username.visibility = VISIBLE
                password.visibility = VISIBLE
                login.visibility = VISIBLE
                joinin.visibility = VISIBLE
                loginTalk.visibility = VISIBLE
                loginGoogle.visibility = VISIBLE
            }
        } // 로그인 화면
        else if (mode == "signin"){
            binding.run {
                backIcon.visibility = VISIBLE
                backIcon.visibility = VISIBLE
                greet.visibility = VISIBLE
                usernameJoin.visibility = VISIBLE
                passwordJoin.visibility = VISIBLE
                actionJoinIn.visibility = VISIBLE

                toolbarTitle.text = "회원가입"
                appTitle.visibility = GONE
                username.visibility = GONE
                password.visibility = GONE
                login.visibility = GONE
                joinin.visibility = GONE
                loginTalk.visibility = GONE
                loginGoogle.visibility = GONE
            }
        }
    }
}
