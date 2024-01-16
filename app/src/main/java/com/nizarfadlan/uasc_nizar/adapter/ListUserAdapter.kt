package com.nizarfadlan.uasc_nizar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nizarfadlan.uasc_nizar.R
import com.nizarfadlan.uasc_nizar.model.User

class ListUserAdapter(
    ctx: Context,
    private val listUser: ArrayList<User>
): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        var tvShow: TextView = itemView.findViewById(R.id.tv_show)
    }

    private var inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentUser = listUser[position]

        holder.tvName.text = currentUser.name
        holder.tvEmail.text = currentUser.email
        holder.tvShow.setOnClickListener {
            itemClickListener?.onItemClick(currentUser)
        }
    }

    override fun getItemCount(): Int = listUser.size
}