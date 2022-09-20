package com.example.realmdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.realmdb.model.UserModel

class UserAdapter(val listUsers: List<UserModel>,val listener: IUserListener) : RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {

    interface IUserListener{
        fun onClick(user: UserModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(v)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, i: Int) {
        holder.tvName.text = listUsers[i].name?:""
        holder.tvAge.text = listUsers[i].age.toString()
        holder.tvId.text = "#" + listUsers[i].id

        holder.clMain.setOnClickListener {
            listener.onClick(listUsers[i])
        }
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    class UsersViewHolder(internal var mView: View) : RecyclerView.ViewHolder(mView){
        var clMain: View = mView.findViewById(R.id.clMain)
        var tvName: TextView = mView.findViewById(R.id.tvName)
        var tvAge: TextView = mView.findViewById(R.id.tvAge)
        var tvId: TextView = mView.findViewById(R.id.tvId)
    }
}