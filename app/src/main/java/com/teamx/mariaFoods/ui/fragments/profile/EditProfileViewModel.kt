package com.teamx.mariaFoods.ui.fragments.profile


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.signup.SignupData
import com.teamx.mariaFoods.data.dataclasses.sucessData.SuccessData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _editProfileResponse = MutableLiveData<Resource<SignupData>>()
    val editProfileResponse: LiveData<Resource<SignupData>>
        get() = _editProfileResponse
    fun editProfiles(param: JsonObject) {
        viewModelScope.launch {
            _editProfileResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.editProfile(param).let {
                        if (it.isSuccessful) {
                            _editProfileResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                            _editProfileResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))

                        } else {
                            _editProfileResponse.postValue(Resource.error("Some thing went wrong", null))

                        }
                    }
                } catch (e: Exception) {
                    _editProfileResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _editProfileResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    private val _changePasswordResponse = MutableLiveData<Resource<SuccessData>>()
    val changePasswordResponse: LiveData<Resource<SuccessData>>
        get() = _changePasswordResponse

    fun changePassword(param: JsonObject) {
        viewModelScope.launch {

            _changePasswordResponse.postValue(Resource.loading(null))
            Log.d("TAG", "loginPhone: first")
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.changePass(param).let {
                        if (it.isSuccessful) {
                            _changePasswordResponse.postValue(Resource.success(it.body()!!))
                            Log.d("TAG", "loginPhone: sucess")
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 422 || it.code() == 400) {
                            val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())

                            Log.d("TAG", "loginPhone: ${it.code()}")
                            _changePasswordResponse.postValue(Resource.error(jsonObj.getJSONArray("errors")[0].toString()))
                            Log.d("TAG", "loginPhone: ${it.code()}")
                        } else {
                            Log.d("TAG", "loginPhone: else")
                            _changePasswordResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _changePasswordResponse.postValue(Resource.error("${e.message}", null))
                }
            } else {
                _changePasswordResponse.postValue(Resource.error("No internet connection", null))
            }

        }
    }


}