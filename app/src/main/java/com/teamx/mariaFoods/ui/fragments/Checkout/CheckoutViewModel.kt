package com.teamx.mariaFoods.ui.fragments.Checkout


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.checkout.CheckoutData
import com.teamx.mariaFoods.data.dataclasses.coupon.CouponData
import com.teamx.mariaFoods.data.dataclasses.getAddress.GetAddressData
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
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


    private val _checkoutResponse = MutableLiveData<Resource<CheckoutData>>()
    val checkout: LiveData<Resource<CheckoutData>>
        get() = _checkoutResponse

    fun checkout(param: JsonObject) {
        viewModelScope.launch {
            _checkoutResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.checkout(param).let {
                        if (it.isSuccessful) {
                            _checkoutResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _checkoutResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _checkoutResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _checkoutResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _checkoutResponse.postValue(Resource.error("No internet connection", null))
        }
    }

 private val _couponResponse = MutableLiveData<Resource<CouponData>>()
    val coupon: LiveData<Resource<CouponData>>
        get() = _couponResponse

    fun Applycoupon(param: JsonObject) {
        viewModelScope.launch {
            _couponResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.coupon(param).let {
                        if (it.isSuccessful) {
                            _couponResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _couponResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _couponResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _couponResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _couponResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    private val _addaddressResponse = MutableLiveData<Resource<SuccessData>>()
    val addaddress: LiveData<Resource<SuccessData>>
        get() = _addaddressResponse

    fun addAddress(param: JsonObject) {
        viewModelScope.launch {
            _addaddressResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.addAddress(param).let {
                        if (it.isSuccessful) {
                            _addaddressResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _addaddressResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _addaddressResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _addaddressResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _addaddressResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    private val _addressListResponse = MutableLiveData<Resource<GetAddressData>>()
    val addressList: LiveData<Resource<GetAddressData>>
        get() = _addressListResponse

    fun getAddress() {
        viewModelScope.launch {
            _addressListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAddress().let {
                        if (it.isSuccessful) {
                            _addressListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _addressListResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _addressListResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _addressListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _addressListResponse.postValue(Resource.error("No internet connection", null))
        }
    }

}