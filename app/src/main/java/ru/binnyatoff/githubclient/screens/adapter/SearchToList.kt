package ru.binnyatoff.githubclient.screens.adapter

import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.fragment.app.Fragment
import ru.binnyatoff.githubclient.R
import ru.binnyatoff.githubclient.screens.adapter.Adapter


open class SearchToList(contentLayoutId: Int) : Fragment(contentLayoutId) {
    private val adapter = Adapter()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    fun getMyAdapter():Adapter{
        return adapter
    }
}