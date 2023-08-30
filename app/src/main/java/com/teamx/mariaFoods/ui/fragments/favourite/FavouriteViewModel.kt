package com.teamx.mariaFoods.ui.fragments.favourite


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.wishList.GetWishlist
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {



    private val _getWishlistResponse = MutableLiveData<Resource<GetWishlist>>()
    val getWishlistResponse: LiveData<Resource<GetWishlist>>
        get() = _getWishlistResponse


    fun getWishList() {
        viewModelScope.launch {
            _getWishlistResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getWishList().let {
                        if (it.isSuccessful) {
                            _getWishlistResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _getWishlistResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _getWishlistResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )

                        }
                    }
                } catch (e: Exception) {
                    _getWishlistResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getWishlistResponse.postValue(Resource.error("No internet connection", null))
        }
    }


}