package com.nizarfadlan.uasc_nizar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.uasc_nizar.R
import com.nizarfadlan.uasc_nizar.model.Todo

class ListTodoAdapter(
    ctx: Context,
    private val listTodo: ArrayList<Todo>
): RecyclerView.Adapter<ListTodoAdapter.ListViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(todo: Todo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status)
    }

    private var inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentTodo = listTodo[position]
        val status = if (currentTodo.complete) "Complete" else "Pending"

        holder.tvTitle.text = currentTodo.title
        holder.tvStatus.text = status
        holder.tvStatus.setBackgroundResource(
            if (currentTodo.complete) R.drawable.chip_complete else R.drawable.chip_pending
        )
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(currentTodo)
        }
    }

    override fun getItemCount(): Int = listTodo.size
}