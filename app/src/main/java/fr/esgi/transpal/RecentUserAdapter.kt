package fr.esgi.transpal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.esgi.transpal.network.dto.UserModel

class RecentUserAdapter(
    private val context: Context,
    private val users: List<UserModel>,
    private val onUserClick: (UserModel) -> Unit
) : RecyclerView.Adapter<RecentUserAdapter.RecentUserViewHolder>() {

    inner class RecentUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userEmail: TextView = itemView.findViewById(R.id.user_email)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUserClick(users[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentUserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_small, parent, false)
        return RecentUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentUserViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = user.name
        holder.userEmail.text = user.email
    }

    override fun getItemCount(): Int = users.size
}