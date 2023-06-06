package com.teamx.mariaFoods.ui.fragments.Addresses


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.getAddress.GetAddressdData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _addressListResponse = MutableLiveData<Resource<GetAddressdData>>()
    val addressList: LiveData<Resource<GetAddressdData>>
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
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
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

}