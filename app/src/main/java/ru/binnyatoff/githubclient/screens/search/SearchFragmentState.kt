package ru.binnyatoff.githubclient.screens.search

import ru.binnyatoff.githubclient.repository.models.SearchResult

sealed class SearchFragmentState {
    object Loading : SearchFragmentState()
    data class Error(val e: String) : SearchFragmentState()

    data class Loaded(val searchResult: SearchResult) : SearchFragmentState()

}

sealed class SearchFragmentEvent(){
    data class Search(val query:String):SearchFragmentEvent()
}