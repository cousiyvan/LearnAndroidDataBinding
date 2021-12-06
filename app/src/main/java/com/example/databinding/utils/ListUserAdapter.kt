package com.example.databinding.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.databinding.R
import com.example.databinding.models.User
import com.example.databinding.repo.UserRepository
import java.lang.Exception

class ListUserAdapter(
    val deleteClickListener: (User) -> Unit,
    val selectUser: (User) -> Unit
) : ListAdapter<User, ListUserAdapter.ListUserViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        return ListUserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.firstName, R.id.txtUserItemFirstname)
        holder.bind(current.lastName, R.id.txtUserItemLastName)
        holder.bind(current.likes, R.id.txtUserItemLikes)

        val btnDelete = holder.itemView.findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener { deleteClickListener(current) }

        val txtFirstName = holder.itemView.findViewById<TextView>(R.id.txtUserItemFirstname)
        txtFirstName.setOnClickListener { selectUser(current) }
    }

    class ListUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var firstname: String
        lateinit var lastname: String
        lateinit var likes: Integer

        fun bind(value: Any, fieldId: Int) {
            val txtView = itemView.findViewById<TextView>(fieldId)

            when {
                value is String -> txtView.text = value as String
                value is Int -> txtView.text = Integer.toString(value as Int)
                else -> throw Exception("Type nt excepted")
            }
        }

        companion object {
            fun create(parent: ViewGroup): ListUserViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
                return ListUserViewHolder(view)
            }
        }
    }

    class UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.firstName == newItem.firstName
                    &&
                    oldItem.lastName == newItem.lastName
        }
    }
}