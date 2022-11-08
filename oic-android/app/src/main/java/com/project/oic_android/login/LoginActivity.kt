package com.project.oic_android.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View.*
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.oic_android.MainActivity
import com.project.oic_android.R
import com.project.oic_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinin.setOnClickListener { changeVisibility("signin")}
        binding.backIcon.setOnClickListener{ changeVisibility("logout") }

        // 상태바 색 변경
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.darkblue)
        }

        // 구글 로그인
       // val requestLauncher = registerForActivityResult(
        //    ActivityResultContracts.StartActivityForResult()) {
      //      val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
     //       try {
      //          val account = task.getResult(ApiException::class.java)!!
      //          val credential = GoogleAuthProvider.getCredential(account.idToken, null)
      //          AuthApplication.auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                    // 로그인 성공
       //             if (task.isSuccessful) { AuthApplication.email = account.email; changeVisibility("구글 계정")}
      //              else { changeVisibility("logout") }
     //           }
     //       }
    //        catch (e:ApiException) { changeVisibility("logout") }
     //   }

        // 구글 로그인 버튼
     //   binding.loginGoogle.setOnClickListener {
    //        val gso = GoogleSignInOptions
    //            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    //            .requestIdToken(getString(R.string.default_web_client_id))  // 오류 표시 문시 바람 실행 시 별 문제 없음
    //            .requestEmail()
    //            .build()
     //       val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
    //        requestLauncher.launch(signInIntent)
   //     }

        // 이메일 회원가입 버튼
//        binding.actionJoinIn.setOnClickListener {
//            val email = binding.usernameJoin.text.toString()
//            val password = binding.passwordJoin.text.toString()
//            val passwordCk = binding.passwordJoinCheck.text.toString()
//            AuthApplication.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
//                binding.usernameJoin.text.clear()
//                binding.passwordJoin.text.clear()
//                binding.passwordJoinCheck.text.clear()
//                if(task.isSuccessful) {
//                    AuthApplication.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{ sendTask ->
                        //if (sendTask.isSuccessful) { Toast.makeText(baseContext, "$email 로 인증 메일 전송", Toast.LENGTH_SHORT).show(); changeVisibility("logout") }
                        //else { Toast.makeText(baseContext, "인증 메일 전송 실패", Toast.LENGTH_SHORT).show(); changeVisibility("logout") }
//                    }
//                }
//                else {
//                    Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        // 회원가입
        auth = Firebase.auth
        // 회원가입 버튼
        binding.actionJoinIn.setOnClickListener {
            createAccount(binding.usernameJoin.text.toString(), binding.passwordJoin.text.toString())
        }

        // 이메일 로그인
        binding.login.setOnClickListener {
            //changeVisibility("이메일")
            signIn(binding.username.text.toString(), binding.password.text.toString())

//           val email = binding.username.text.toString()
//            val password = binding.password.text.toString()
 //           AuthApplication.auth.signInWithEmailAndPassword(email, password)
 //               .addOnCompleteListener(this) { task->
  //                  binding.username.text.clear()
  //                  binding.password.text.clear()
    //                if (task.isSuccessful) {

    //                    if (AuthApplication.checkAuth()) { // 로그인 성공
      //                      AuthApplication.email = email; changeVisibility("이메일")
        //                    val intent = Intent(this, MainActivity::class.java)
         //               }
          //              else { Toast.makeText(baseContext, "인증 메일을 확인해 주세요", Toast.LENGTH_SHORT).show() }
           //         }
                }

        // 카카오톡 로그인
        binding.loginTalk.setOnClickListener {
            changeVisibility("카카오 계정")
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

    // 계정 생성
    private fun createAccount(email:  String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password.length < 8) Toast.makeText(this, "8글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
            else {
                auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) {
                        task -> if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            changeVisibility("logout")
                        }
                       else {
                    Toast.makeText(this, "8글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                         Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        changeVisibility("logout")
                    }
                }
         }
       }
    }

    // 로그인
    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "환영합니다", Toast.LENGTH_SHORT).show()
                    if (AuthApplication.checkAuth()) { // 로그인 성공
                        AuthApplication.email = email
                    } else {
                    }

                    changeVisibility("이메일")
                    //val intent = Intent(this, MainActivity::class.java)
                    //startActivity(intent)
            }
                else {
                    Toast.makeText(baseContext, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }

    // 화면 전환
    private fun changeVisibility(mode: String){
        if (mode == "logout"){
            binding.run {
                backIcon.visibility = GONE
                greet.visibility = GONE
                usernameJoin.visibility = GONE
                passwordJoin.visibility = GONE
                passwordJoinCheck.visibility = GONE
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
                passwordJoinCheck.visibility = VISIBLE
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
        } // 회원가입 화면
        else if (mode == "이메일") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            // 메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("method", mode)
            startActivity(intent)
            finish()
        }
    }
}

