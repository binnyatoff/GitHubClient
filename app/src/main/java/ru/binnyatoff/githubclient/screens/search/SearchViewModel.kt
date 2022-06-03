package ru.binnyatoff.githubclient.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.models.Search
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val searchList = MutableLiveData<Search>()

    fun search(query: String) {
        viewModelScope.launch {
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