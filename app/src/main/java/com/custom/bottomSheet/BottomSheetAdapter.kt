package com.custom.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.base.BaseRecyclerAdapter
import com.kagroup.baseProject.R
import com.kagroup.baseProject.databinding.IndexBottomSheetBinding

class BottomSheetAdapter : BaseRecyclerAdapter<BottomSheetModel, BottomSheetAdapter.BottomSheetViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding: IndexBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.index_bottom_sheet, parent, false
        )
        return BottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val item: BottomSheetModel = items!![position]
        item.selected = position == selectedPos
        holder.bind(item)
        holder.itemView.setOnClickListener {
            selectedPos = position
            onITemClickListener.onItemClick(pos = position,item = item)
            notifyDataSetChanged()
        }
    }


    class BottomSheetViewHolder(private var binding: IndexBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BottomSheetModel) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}