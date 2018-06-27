package com.rainvisitor.todoList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rainvisitor.todoList.R
import com.rainvisitor.todoList.libs.Utils
import com.rainvisitor.todoList.models.Event
import kotlinx.android.synthetic.main.list_event.view.*

class EventAdapter(
        private val context: Context,
        private val contactList: MutableList<Event>,
        private val listener: OnItemClickListener) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Event, position: Int)
        fun onStateUpdate(item: Event, position: Int, state: Boolean)
        fun onItemEditClick(item: Event, position: Int)
        fun onItemLongClick(item: Event, position: Int)
    }

    init {

    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_event, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bindView(context, contactList[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(contactList[position], position)
        }
        holder.itemView.setOnClickListener {
            listener.onItemLongClick(contactList[position], position)
        }
        holder.mView.buttonEdit.setOnClickListener {
            listener.onItemEditClick(contactList[position], position)
        }
        holder.mView.checkState.setOnCheckedChangeListener { _, isChecked ->
            listener.onStateUpdate(contactList[position], position, isChecked)
        }
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindView(context: Context, event: Event) {
            mView.textDescription.text = event.description
            mView.checkState.isChecked = (event.state == 1)
            when (event.state!!) {
                1 -> {
                    Utils.addDeleteLine(mView.textDescription)
                    ContextCompat.getColor(context, R.color.disabled)
                    mView.cardView.setCardBackgroundColor(
                            ContextCompat.getColor(context, R.color.disabled))
                }
                else -> {
                    mView.cardView.setCardBackgroundColor(
                            ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }
}
