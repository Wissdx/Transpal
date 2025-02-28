package fr.esgi.transpal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import fr.esgi.transpal.network.dto.UserModel

class UserAdapter(
    private val context: Context,
    private var users: List<UserModel>
) : BaseAdapter(), Filterable {

    private var filteredUsers: List<UserModel> = users

    override fun getCount(): Int = filteredUsers.size

    override fun getItem(position: Int): Any = filteredUsers[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        val user = filteredUsers[position]

        val userNameTextView = view.findViewById<TextView>(R.id.user_name)
        val userEmailTextView = view.findViewById<TextView>(R.id.user_email)

        userNameTextView.text = user.name
        userEmailTextView.text = user.email

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredUsers = if (charString.isEmpty()) {
                    users
                } else {
                    users.filter {
                        it.name.contains(charString, true) || it.email.contains(charString, true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredUsers
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredUsers = results?.values as List<UserModel>
                notifyDataSetChanged()
            }
        }
    }
}