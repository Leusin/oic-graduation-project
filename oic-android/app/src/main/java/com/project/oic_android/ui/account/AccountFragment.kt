package com.project.oic_android.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.oic_android.MainActivity
import com.project.oic_android.databinding.FragmentAccountBinding
import com.project.oic_android.login.AuthApplication

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 회원 정보
        val method = arguments?.getString("method")
        binding.methodText.text = (activity as MainActivity).getUserData()
        binding.idText.text = AuthApplication.email.toString()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로그인 버튼
        binding.logout.setOnClickListener { ClickLogout() }
        // 로그아웃 버튼
        binding.withdraw.setOnClickListener { ClickWithdraw() }
    }

    private fun ClickLogout() {
        LogoutDialog().show( childFragmentManager,"LogoutDialog" )
    }

    private fun ClickWithdraw() {
        WithdrawDialog().show( childFragmentManager,"WithdrawDialog" )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}