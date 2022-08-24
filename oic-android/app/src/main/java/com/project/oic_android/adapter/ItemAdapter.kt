package com.project.oic_android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.oic_android.R
import com.project.oic_android.model.Word
import com.project.oic_android.ui.note.NoteFragment

class ItemAdapter(
    private val context: NoteFragment,
    private val dataset: List<Word>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    interface OnItemClickListener { fun onClick(v: View, data: Word, position: Int) }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) { this.listener = listener }

    // 레이아웃 내 view 연결
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val wordEn: TextView  = view.findViewById(R.id.word_en)
        val wordKr: TextView  = view.findViewById(R.id.word_kr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.imageView.setImageResource(item.imageResourceId)
        holder.wordEn.text = item.word_en
        holder.wordKr.text = item.word_kr
    }

    override fun getItemCount() = dataset.size
}
