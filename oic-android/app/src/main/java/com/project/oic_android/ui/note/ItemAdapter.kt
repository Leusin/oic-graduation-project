package com.project.oic_android.ui.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.oic_android.R
import com.project.oic_android.network.Word

class ItemAdapter(
    private val context: NoteFragment,
    private val dataset: List<Word>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {


    interface OnItemClickListener { fun onClick(v: View, data: Word, position: Int) }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) { this.listener = listener }

    // 레이아웃 내 view 연결
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val wordEn: TextView  = view.findViewById(R.id.word_en)
        val wordKr: TextView  = view.findViewById(R.id.word_kr)

        fun bind(item: Word) {
            imageView.setImageResource(item.img)
            wordEn.text = item.word_eng
            wordKr.text = item.word_kor

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) { itemView.setOnClickListener{ listener?.onClick(itemView, item, position) } }
        }
    }

    // 아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    // View 에 내용 입력
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataset[position])

    }

    // 리스트 내 아이템 개수
    override fun getItemCount() = dataset.size

}
