package com.base

import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter

abstract class BaseRecyclerSwipeAdapter<T, VH : RecyclerView.ViewHolder>: RecyclerSwipeAdapter<VH>() {

    var items: MutableList<T>? = ArrayList()
    var selectedPos = -1

    lateinit var onITemClickListener: OnITemClickListener<T>

    override fun getItemCount(): Int {
        return if (items == null) 0 else items!!.size
    }

    fun setData(newITems: List<T>) {
        items?.clear()
        items?.addAll(newITems)
        notifyDataSetChanged()
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        /**
         * return your swipe layout id
         */
        return 0
    }

    interface OnITemClickListener<T> {
        fun onItemClick(pos: Int, item: T)
    }
}