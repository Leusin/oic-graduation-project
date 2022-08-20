package com.project.oic_android.ui.note

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.oic_android.WordActivity
import com.project.oic_android.databinding.FragmentNoteBinding
import com.project.oic_android.network.Word

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    val data = Datasource().words // 아이템 배열
    val itemAdapter = ItemAdapter(this, data) // 어댑터

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val data = Datasource().words
        binding.recyclerView.adapter = itemAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = itemAdapter
        itemAdapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener{
            override fun onClick(v: View, data: Word, position: Int) {
                Intent(context, WordActivity::class.java).apply{
                    // 여기서데이터 전달
                }.run { startActivity(this) }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}