package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.teamx.mariaFoods.baseclasses.BaseViewModel
import com.teamx.mariaFoods.data.dataclasses.banners.BannerListData
import com.teamx.mariaFoods.data.dataclasses.products.ProductsData
import com.teamx.mariaFoods.data.remote.Resource
import com.teamx.mariaFoods.data.remote.reporitory.MainRepository
import com.teamx.mariaFoods.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class Dashboard @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val _bannerListResponse = MutableLiveData<Resource<BannerListData>>()
    val bannerList: LiveData<Resource<BannerListData>>
        get() = _bannerListResponse
    fun bannerList() {
        viewModelScope.launch {
            _bannerListResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getBanners().let {
                        if (it.isSuccessful) {
                            _bannerListResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 403) {
                            _bannerListResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            _bannerListResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _bannerListResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _bannerListResponse.postValue(Resource.error("No internet connection", null))
        }
    }

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
                            _productsResponse.postValue(Resource.error("Some thing went wrong", null))
                        }
                    }
                } catch (e: Exception) {
                    _productsResponse.postValue(Resource.error("${e.message}", null))
                }
            } else _productsResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}