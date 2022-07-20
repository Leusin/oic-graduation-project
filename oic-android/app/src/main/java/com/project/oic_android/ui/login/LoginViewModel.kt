package com.project.oic_android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.project.oic_android.R
import com.project.oic_android.data.LoginRepository
import com.project.oic_android.data.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()  // 앞에 "_"가 있으면 수정 가능한 변수
    val loginFormState: LiveData<LoginFormState> = _loginForm  // 수정 불가능한 변수(읽기만 가능)

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)  // 입력받은 username 가 유효한지 확인
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)  // 입력받은 password 가 유효한지 확인
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)  // LoginFormState 의 형태로 _loginForm 에 저장
        }
    }

    // A placeholder username validation check
    // username 이 유효한지 확인
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {  //  '@' 이 포함되어 있으면 이메일 포맷과 일치하는지 확인하고, '@' 이 포함되지 않으면 빈칸인지 확인
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}