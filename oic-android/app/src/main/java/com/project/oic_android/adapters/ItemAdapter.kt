package com.project.oic_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.oic_android.R
import com.project.oic_android.adapters.ItemAdapter.ItemViewHolder
import com.project.oic_android.modelData.Word
import com.project.oic_android.ui.note.NoteFragment

class ItemAdapter(
    private val context: NoteFragment,
    private val dataset: MutableList<Word>
) : RecyclerView.Adapter<ItemViewHolder>() {

    // ClickListener 기능 추가가
    interface OnItemClickListener{
        fun onItemClick(view: View, data: Word, position: Int)
    }
    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    // 해당 position 에 데이터를 뷰홀더의 아이템 뷰에 표시
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    // 아이템 뷰를 저장하는 뷰홀더
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordEn: TextView  = itemView.findViewById(R.id.word_en)
        val wordKr: TextView  = itemView.findViewById(R.id.word_kr)
        //val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: Word) {
            wordEn.text = item.word_eng
            wordKr.text = item.word_kor
            //Glide.with(itemView).load(item.img).into(imageView)

            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, position)
                }
            }
        }
    }
}
