package ru.binnyatoff.githubclient.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.data.Repository
import ru.binnyatoff.githubclient.data.models.Search
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val searchList = MutableLiveData<Search>()

    fun search(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.search(query)
                if (response.isSuccessful) {
                    searchList.postValue(response.body())
                }
            } catch (e: Exception) {
            }
        }
    }
}