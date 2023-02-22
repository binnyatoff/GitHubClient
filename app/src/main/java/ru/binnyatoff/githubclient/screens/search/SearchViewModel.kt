package ru.binnyatoff.githubclient.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.binnyatoff.githubclient.data.network.NetworkResult
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.repository.models.SearchResult
import ru.binnyatoff.githubclient.screens.helpers.EventHandler
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    EventHandler<SearchFragmentEvent> {
    private val _searchFragmentState = MutableLiveData<SearchFragmentState>()
    val searchFragmentState: LiveData<SearchFragmentState> = _searchFragmentState

    private fun search(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val response = repository.searchNew(query)) {
                    is NetworkResult.Error -> TODO()
                    is NetworkResult.Exception -> TODO()
                    is NetworkResult.Success -> {
                        _searchFragmentState.postValue(SearchFragmentState.Loaded(response.data))
                    }
                }
            }
        }
    }

    override fun obtainEvent(event: SearchFragmentEvent) {
        when (event) {
            is SearchFragmentEvent.Search -> search(event.query)
        }
    }
}