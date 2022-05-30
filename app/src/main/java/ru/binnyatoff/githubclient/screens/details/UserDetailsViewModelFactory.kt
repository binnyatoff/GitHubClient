package ru.binnyatoff.githubclient.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.binnyatoff.githubclient.repository.Repository
import javax.inject.Inject

class UserDetailsViewModelFactory @Inject constructor(private var repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserDetailsViewModel(repository) as T
    }
}