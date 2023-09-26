package com.teamx.mariaFoods.ui.fragments.cart


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.dataclasses.removeCart.RemoveCartData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _getCartListResponse = MutableLiveData<Resource<GetCartData>>()
    val getCartList: LiveData<Resource<GetCartData>>
        get() = _getCartListResponse

    fun getCart() {
        viewModelScope.launch {
            _getCartListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getCart().let {
                        if (it.isSuccessful) {
                            _getCartListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _getCartListResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _getCartListResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getCartListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getCartListResponse.postValue(Resource.error("No internet connection", null))
        }
    }

    private val _removeCartResponse = MutableLiveData<Resource<RemoveCartData>>()
    val removeCart: LiveData<Resource<RemoveCartData>>
        get() = _removeCartResponse

    fun removeCart(cart_id: Int) {
        viewModelScope.launch {
            _removeCartResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.removeCart(cart_id).let {
                        if (it.isSuccessful) {
                            _removeCartResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _removeCartResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _removeCartResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _removeCartResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _removeCartResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun getGuestCart(guest_id: Int) {
        viewModelScope.launch {
            _getCartListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getGuestCart(guest_id).let {
                        if (it.isSuccessful) {
                            _getCartListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _getCartListResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _getCartListResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getCartListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getCartListResponse.postValue(Resource.error("No internet connection", null))
        }
    }

}