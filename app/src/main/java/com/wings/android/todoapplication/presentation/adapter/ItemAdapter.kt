package com.wings.android.todoapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wings.android.todoapplication.R
import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.databinding.ListItemBinding

class ItemAdapter :RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    //DiffUtil
    private val callback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.content == newItem.content && oldItem.deadline == newItem.deadline
        }

    }

    val differ = AsyncListDiffer<Item>(this, callback)

    //Listener
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(item: Item)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    //ViewHolder
    inner class ItemViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                tvContent.text = item.content
                tvDeadline.text = item.deadline.toString()

                root.setOnClickListener {
                    listener.onItemClick(item)
                }
            }

        }
    }
}

