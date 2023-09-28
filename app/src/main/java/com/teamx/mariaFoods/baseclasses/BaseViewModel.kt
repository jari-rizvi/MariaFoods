package com.teamx.mariaFoods.baseclasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamx.mariaFoods.data.dataclasses.getCart.GetCartData
import com.teamx.mariaFoods.data.remote.Resource


abstract class BaseViewModel : ViewModel() {
     val _getCartListResponse = MutableLiveData<Resource<GetCartData>>()
 }
