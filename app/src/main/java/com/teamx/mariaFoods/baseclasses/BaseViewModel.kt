package com.teamx.mariaFoods.baseclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.remote.Resource


open class BaseViewModel : ViewModel() {
      val _getCartListResponse = MutableLiveData<Resource<GetCartData>>()
      val getCartList: LiveData<Resource<GetCartData>>
            get() = _getCartListResponse
}
