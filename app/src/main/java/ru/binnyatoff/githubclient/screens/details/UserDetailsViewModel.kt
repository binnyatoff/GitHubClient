package ru.binnyatoff.githubclient.screens.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.binnyatoff.githubclient.retrofit.Api
import ru.binnyatoff.githubclient.models.User_Details
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private val api: Api): ViewModel() {

    val errorMessage = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var userDetails = MutableLiveData<User_Details>()

    fun getUserDeatails(user: String) {
        loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.user_details(user)
                if (response.isSuccessful) {
                    userDetails.postValue(response.body())
                    Log.e("TAG", response.body().toString())
                    loading.postValue(false)
                }
            } catch (e: Exception) {
                loading.postValue(false)
                Log.e("TAG", e.toString())
                errorMessage.postValue(true)
            }
        }
    }
}