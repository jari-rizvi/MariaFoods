package com.teamx.mariaFoods.ui.fragments.help


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.termsCondition.TermsConditonData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _termsConditionResponse = MutableLiveData<Resource<TermsConditonData>>()
    val termsConditionResponse: LiveData<Resource<TermsConditonData>>
        get() = _termsConditionResponse


    fun termsConditions() {
        viewModelScope.launch {
            _termsConditionResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.termsCondition().let {
                        if (it.isSuccessful) {
                            _termsConditionResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _termsConditionResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _termsConditionResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )

                        }
                    }
                } catch (e: Exception) {
                    _termsConditionResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _termsConditionResponse.postValue(Resource.error("No internet connection", null))
        }
    }

 private val _helpResponse = MutableLiveData<Resource<TermsConditonData>>()
    val helpResponse: LiveData<Resource<TermsConditonData>>
        get() = _helpResponse


    fun getHelp() {
        viewModelScope.launch {
            _helpResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getHelp().let {
                        if (it.isSuccessful) {
                            _helpResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _helpResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _helpResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )

                        }
                    }
                } catch (e: Exception) {
                    _helpResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _helpResponse.postValue(Resource.error("No internet connection", null))
        }
    }




}