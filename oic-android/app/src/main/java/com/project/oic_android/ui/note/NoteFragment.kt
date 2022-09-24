package com.project.oic_android.ui.note

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.oic_android.adapters.ItemAdapter
import com.project.oic_android.databinding.FragmentNoteBinding
import com.project.oic_android.modelData.Word

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    val data = Datasource().words // 아이템 배열
    lateinit var itemAdapter: ItemAdapter// 어댑터

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        itemAdapter  = ItemAdapter(this, data)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        recyclerViewClickEvent()

        switchButtonEvent()
    }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        itemAdapter.notifyDataSetChanged()
        binding.recyclerView.adapter = itemAdapter
    }

    private fun recyclerViewClickEvent(){
        itemAdapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener{
            override fun onItemClick(view: View, data: Word, position: Int) {
                Intent(context, WordDetailActivity::class.java).apply {
                    putExtra("data", data)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
    }

    private fun switchButtonEvent(){
        binding.switch1.isChecked = true
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                // 뜻 나타남 이벤트 추가
            } else {
                // 뜻 없어짐 이벤트 추가
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}