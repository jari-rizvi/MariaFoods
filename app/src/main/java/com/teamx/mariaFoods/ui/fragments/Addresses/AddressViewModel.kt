package com.teamx.mariaFoods.ui.fragments.Addresses


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.editAddress.EditAddressData
import com.teamx.mariaFoods.data.dataclasses.getAddress.GetAddressData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

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
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _addressListResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
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

    private val _deleteaddressResponse = MutableLiveData<Resource<SuccessData>>()
    val deleteaddress: LiveData<Resource<SuccessData>>
        get() = _deleteaddressResponse

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            _deleteaddressResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.deleteAddress(id).let {
                        if (it.isSuccessful) {
                            _deleteaddressResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _deleteaddressResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                            _deleteaddressResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _deleteaddressResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _deleteaddressResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _deleteaddressResponse.postValue(Resource.error("No internet connection", null))
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
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _addaddressResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
//                            _addaddressResponse.postValue(Resource.error(it.message(), null))
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


    private val _updateaddressResponse = MutableLiveData<Resource<SuccessData>>()
    val updateaddress: LiveData<Resource<SuccessData>>
        get() = _updateaddressResponse

    fun updateAddress(param: JsonObject) {
        viewModelScope.launch {
            _updateaddressResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.updateAddress(param).let {
                        if (it.isSuccessful) {
                            _updateaddressResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _updateaddressResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
//                            _updateaddressResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _updateaddressResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _updateaddressResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _addaddressResponse.postValue(Resource.error("No internet connection", null))
        }
    }



 private val _editaddressResponse = MutableLiveData<Resource<EditAddressData>>()
    val editaddress: LiveData<Resource<EditAddressData>>
        get() = _editaddressResponse

    fun editAddress(id: Int) {
        viewModelScope.launch {
            _editaddressResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.editAddress(id).let {
                        if (it.isSuccessful) {
                            _editaddressResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _editaddressResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
//                            _editaddressResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _editaddressResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong",
                                    null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _editaddressResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _addaddressResponse.postValue(Resource.error("No internet connection", null))
        }
    }

}