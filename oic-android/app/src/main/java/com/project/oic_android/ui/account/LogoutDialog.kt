package com.project.oic_android.ui.account

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.oic_android.login.AuthApplication
import com.project.oic_android.login.LoginActivity
import com.project.oic_android.R
import com.project.oic_android.databinding.FragmentDialogBinding

class LogoutDialog : DialogFragment() {
    private var _binding: FragmentDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textView.text = getString(R.string.logout_massage)
        binding.logoutOk.text = getString(R.string.logout_ok)

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.logoutClear.setOnClickListener { dismiss() }
        binding.logoutCancel.setOnClickListener { dismiss() }

        // 로그아웃
        binding.logoutOk.setOnClickListener {
            activity?.let {
                AuthApplication.auth.signOut()
                AuthApplication.email = null

                // 로그인 페이지로 이동
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)

                Toast.makeText(activity, "로그아웃 하였습니다", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}