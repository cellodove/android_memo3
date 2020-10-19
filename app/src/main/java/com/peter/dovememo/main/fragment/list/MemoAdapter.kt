package com.peter.dovememo.main.fragment.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peter.memo3.R
import com.peter.memo3.databinding.ItemMemoBinding
import com.peter.memo3.db.table.memo.Memo

class MemoAdapter(private val listener: MemoListListener) : ListAdapter<Memo, ItemHolder>(
    object : DiffUtil.ItemCallback<Memo>() {
        override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean =
            oldItem.id != newItem.id
    }
) {
    interface MemoListListener {
        fun onMemoItemClick(position: Int)
        fun onMemoItemLongClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_memo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
            setListener(listener)
        }
    }
}

class ItemHolder(private val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(memo: Memo) {
        binding.memo = memo
    }

    fun setListener(listener: MemoAdapter.MemoListListener) {
        binding.container.apply {
            setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMemoItemClick(adapterPosition)
                }
            }
            setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMemoItemLongClick(adapterPosition)
                }

                return@setOnLongClickListener true
            }
        }
    }
}