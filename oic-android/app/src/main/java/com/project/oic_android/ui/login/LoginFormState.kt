package com.project.oic_android.ui.login

/**
 * Data validation state of the login form.
 * 로그인 상태에 따른 상태를 저장하는 데이터 클래스
 */
data class LoginFormState (val usernameError: Int? = null,  // strings.xml 에 있는 값을 직접 설정해주기 때문에 Int?로 선언
                      val passwordError: Int? = null,
                      val isDataValid: Boolean = false)