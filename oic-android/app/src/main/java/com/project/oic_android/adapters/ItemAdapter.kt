package com.project.oic_android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import com.bumptech.glide.Glide
import com.project.oic_android.MainActivity
import com.project.oic_android.R
import com.project.oic_android.WordDetailActivity
//import com.project.oic_android.adapters.ItemAdapter.ItemViewHolder
import com.project.oic_android.modelData.Word
import com.project.oic_android.ui.note.NoteFragment
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_note.*
import org.w3c.dom.Text

//class ItemAdapter(
//    private val context: NoteFragment,
//    private val dataset: MutableList<Word>
//) : RecyclerView.Adapter<ItemViewHolder>() {

    // ClickListener 기능 추가가
  //  interface OnItemClickListener{
  //      fun onItemClick(view: View, data: Word, position: Int)
  //  }
  //  private var listener: OnItemClickListener? = null
  //  fun setOnItemClickListener(listener: OnItemClickListener) {
  //      this.listener = listener
  //  }

    // 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
   // override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
     //   val adapterLayout = LayoutInflater.from(parent.context)
      //      .inflate(R.layout.list_item, parent, false)
      //  return ItemViewHolder(adapterLayout)
   // }

  //  override fun getItemCount() = dataset.size

    // 해당 position 에 데이터를 뷰홀더의 아이템 뷰에 표시
   // override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
     //   holder.bind(dataset[position])
   // }

    // 아이템 뷰를 저장하는 뷰홀더

    //inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //    val wordEn: TextView  = itemView.findViewById(R.id.word_en)
    //    val wordKr: TextView  = itemView.findViewById(R.id.word_kr)
        //val imageView: ImageView = itemView.findViewById(R.id.imageView)

    //   fun bind(item: Word) {
    //        wordEn.text = item.word_eng
    //        wordKr.text = item.word_kor
            //Glide.with(itemView).load(item.img).into(imageView)

    //        val position = adapterPosition
    //        if(position != RecyclerView.NO_POSITION) {
    //            itemView.setOnClickListener {
    //                listener?.onItemClick(itemView, item, position)
    //            }
    //        }
    //    }
    //}

//class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
class ItemAdapter: RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    var items = ArrayList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

   /* interface OnItemClickListener{
              fun onItemClick(view: View, data: Word, position: Int) {
                  val context = view.getContext()
                  Toast.makeText(context, "클릭", Toast.LENGTH_SHORT).show()
                  val intent = Intent(context, WordDetailActivity::class.java)
                  intent.putExtra("data", data.word_eng)
                  context.startActivity(intent)
                  }
            }

          private var listener: OnItemClickListener? = null
          fun setOnItemClickListener(listener: OnItemClickListener) {
              this.listener = listener
          }
*/
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
//        holder.bind(item)
     }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val context = itemView.getContext()
                Toast.makeText(itemView?.context, "클릭 = ${itemView.word_en.text}", Toast.LENGTH_LONG).show()
                val intent = Intent(context, WordDetailActivity::class.java)
                intent.putExtra("data", "${itemView.word_en.text}")
                context.startActivity(intent)
            }
        }
        fun setItem(item: Word) {
            itemView.word_en.text = item.word_eng
            itemView.word_kr.text = item.word_kor
         }
/*
        fun bind(item: Word) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, position)
                }
            }
        }
*/
/*        fun switchButtonEvent() {
            val sw: Switch
            sw = itemView.findViewById(R.id.switch1)

            sw.isChecked = true
            sw.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // 뜻 나타남 이벤트 추가
                    itemView.word_kr.setVisibility(View.VISIBLE)
                } else {
                    // 뜻 없어짐 이벤트 추가
                    itemView.word_kr.setVisibility(View.INVISIBLE)
                }
            }
        }*/
    }
}
