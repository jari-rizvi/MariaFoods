package com.teamx.mariaFoods.ui.fragments.payment


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.getStripecards.StripeCardsData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _getStripeCardsResponse = MutableLiveData<Resource<StripeCardsData>>()
    val getStripeCardsResponse: LiveData<Resource<StripeCardsData>>
        get() = _getStripeCardsResponse


    fun getStripeCardss() {
        viewModelScope.launch {
            _getStripeCardsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getCards().let {
                        if (it.isSuccessful) {
                            _getStripeCardsResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _getStripeCardsResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _getStripeCardsResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )

                        }
                    }
                } catch (e: Exception) {
                    _getStripeCardsResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _getStripeCardsResponse.postValue(Resource.error("No internet connection", null))
        }
    }

    private val _deletestripecardResponse = MutableLiveData<Resource<SuccessData>>()
    val deletestripecard: LiveData<Resource<SuccessData>>
        get() = _deletestripecardResponse

    fun deleteStripeCard(id: String) {
        viewModelScope.launch {
            _deletestripecardResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.deleteCard(id).let {
                        if (it.isSuccessful) {
                            _deletestripecardResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _deletestripecardResponse.postValue(
                                Resource.error(
                                    jsonObj.getJSONArray(
                                        "errors"
                                    )[0].toString()
                                )
                            )
                        } else {
                            _deletestripecardResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _deletestripecardResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _deletestripecardResponse.postValue(
                Resource.error(
                    "No internet connection", null
                )
            )
        }
    }

    private val _setDefaultCardResponse = MutableLiveData<Resource<SuccessData>>()
    val setDefaultCard: LiveData<Resource<SuccessData>>
        get() = _setDefaultCardResponse

    fun setDefaultCard(param: JsonObject) {
        viewModelScope.launch {
            _setDefaultCardResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.setDefaultCard(param).let {
                        if (it.isSuccessful) {
                            _setDefaultCardResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _setDefaultCardResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
//                            _setDefaultCardResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _setDefaultCardResponse.postValue(
                                Resource.error(
                                    "Some thing went wrong", null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _setDefaultCardResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _setDefaultCardResponse.postValue(Resource.error("No internet connection", null))
        }
    }


}