package ru.binnyatoff.githubclient.screens.feed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ru.binnyatoff.githubclient.models.User
import ru.binnyatoff.githubclient.R
import java.util.*
import kotlin.collections.ArrayList

interface clickDelegate {
    fun onClick(currentUser: User)
}

class UsersAdapter : RecyclerView.Adapter<ViewHolder>(), Filterable {

    private var usersList = emptyList<User>()
    private var usersFilterList = emptyList<User>()
    private var delegate: clickDelegate? = null

    fun attachDelegate(delegate: clickDelegate) {
        this.delegate = delegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view, delegate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = usersFilterList[position]
        holder.bind(currentUser)

    }

    override fun getItemCount(): Int = usersFilterList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(user: List<User>) {
        usersList = user
        usersFilterList = usersList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    usersFilterList = usersList
                } else {
                    val resultList = ArrayList<User>()
                    for (user in usersList) {
                        if (user.login.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(user)
                        }
                    }
                    usersFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = usersFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFilterList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}