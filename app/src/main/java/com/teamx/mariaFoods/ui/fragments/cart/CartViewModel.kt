package com.teamx.mariaFoods.ui.fragments.cart


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.dataclasses.products.ProductsData
import com.teamx.mariaFoods.data.dataclasses.removeCart.RemoveCartData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _productsResponse = MutableLiveData<Resource<ProductsData>>()
    val products: LiveData<Resource<ProductsData>>
        get() = _productsResponse

    fun getProducts() {
        viewModelScope.launch {
            _productsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getProducts().let {
                        if (it.isSuccessful) {
                            _productsResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _productsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _productsResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _productsResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _productsResponse.postValue(Resource.error("No internet connection", null))
        }
    }


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


    private val _changeSlotResponse = MutableLiveData<Resource<SuccessData>>()
    val changeSlotResponse: LiveData<Resource<SuccessData>>
        get() = _changeSlotResponse

    fun changeSlot(param: JsonObject) {
        viewModelScope.launch {
            _changeSlotResponse.postValue(Resource.loading(null))
            Log.d("TAG", "changeSlotPhone: first")
            Log.e("TAG", "changeSlotPhone: 1111111")
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.changeSlot(param).let {
                        if (it.isSuccessful) {
                            _changeSlotResponse.postValue(Resource.success(it.body()!!))
                            Log.d("TAG", "changeSlotPhone: first1 ${it.body()}")
                            Log.e("TAG", "changeSlotPhone: 1111111")

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            _changeSlotResponse.postValue(Resource.error(it.message(), null))
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "changeSlotPhone: ${it.code()}")
                            _changeSlotResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                            Log.d("TAG", "changeSlotPhone: first2")
                            Log.e("TAG", "changeSlotPhone: 1111111")


                        } else {
                            _changeSlotResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                            Log.d("TAG", "changeSlotPhone: first3")
                            Log.e("TAG", "changeSlotPhone: 1111111")

                        }
                    }
                } catch (e: Exception) {
                    _changeSlotResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _changeSlotResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    private val _increaseDecreaseResponse = MutableLiveData<Resource<SuccessData>>()
    val increaseDecreaseResponse: LiveData<Resource<SuccessData>>
        get() = _increaseDecreaseResponse

    fun increaseDecrease(param: JsonObject) {
        viewModelScope.launch {
            _increaseDecreaseResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.increseDecrease(param).let {
                        if (it.isSuccessful) {
                            _increaseDecreaseResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            _increaseDecreaseResponse.postValue(Resource.error(it.message(), null))
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())


                            _increaseDecreaseResponse.postValue(
                                Resource.error(
                                    jsonObj.getJSONArray(
                                        "errors"
                                    )[0].toString()
                                )
                            )


                        } else {
                            _increaseDecreaseResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )


                        }
                    }
                } catch (e: Exception) {
                    _increaseDecreaseResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _increaseDecreaseResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }
}