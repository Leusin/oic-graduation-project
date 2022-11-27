package com.project.oic_android.ui.note

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.project.oic_android.MainActivity
import com.project.oic_android.R
import com.project.oic_android.WordDetailActivity
import com.project.oic_android.adapters.ItemAdapter
import com.project.oic_android.databinding.FragmentNoteBinding
import com.project.oic_android.modelData.Word
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    val data = Datasource().words // 아이템 배열
    lateinit var itemAdapter: ItemAdapter// 어댑터

    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNoteBinding.inflate(inflater, container, false)
//        val root: View = binding.root

//        itemAdapter  = ItemAdapter(this, data)

//        return root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

//        initRecyclerView()
//        recyclerViewClickEvent()

//        switchButtonEvent()
//    }

//    private fun initRecyclerView(){
//        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        itemAdapter.notifyDataSetChanged()
//        binding.recyclerView.adapter = itemAdapter
//    }

//    private fun recyclerViewClickEvent(){
//        itemAdapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener{
//            override fun onItemClick(view: View, data: Word, position: Int) {
//                Intent(context, WordDetailActivity::class.java).apply {
//                    putExtra("data", data)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }.run { startActivity(this) }
//            }
//        })
//    }

    private lateinit var adapter: ItemAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter = ItemAdapter()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        switchButtonEvent()
    }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.setReverseLayout(true)
        layoutManager.setStackFromEnd(true)
        recycler_view.layoutManager = layoutManager

        adapter = ItemAdapter()

//        adapter.items.add(Word("expiate", "속죄하다"))
//        adapter.items.add(Word("straw", "빨대"))
//        adapter.items.add(Word("chair", "의자"))

        binding.recyclerView.adapter = adapter
        binding.switch1.isChecked = false

        databaseRef =
            FirebaseDatabase.getInstance("https://oicproject-fda8d-default-rtdb.firebaseio.com/").reference

        databaseRef.orderByKey().limitToFirst(30).addValueEventListener(
            object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                loadCommentList(snapshot)
            }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("test", "loadItem:onCancelled : ${error.toException()}")
                }
        })
    }

    fun loadCommentList(dataSnapshot: DataSnapshot) {
        val collectionIterator = dataSnapshot!!.children.iterator()
        if (collectionIterator.hasNext()) {
            adapter.items.clear()
            val comments = collectionIterator.next()
            val itemsIterator = comments.children.iterator()
            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val map = currentItem.value as HashMap<String, Any>
                val wordEn = map["word_eng"].toString()
                val wordKr = map["word_kor"].toString()

                adapter.items.add(Word(wordEn, wordKr))
            }
            adapter.notifyDataSetChanged()
        }
    }


    private fun switchButtonEvent(){
        binding.switch1.isChecked = false
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                // 뜻 나타남 이벤트 추가
                for (i in 0..adapter.items.size-1) {
                    binding.recyclerView[i].word_kr.setVisibility(View.VISIBLE)
                }
            } else {
                // 뜻 없어짐 이벤트 추가
                for (i in 0..adapter.items.size-1) {
                    binding.recyclerView[i].word_kr.setVisibility(View.INVISIBLE)
                }
            }
        }
    }

    private fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}