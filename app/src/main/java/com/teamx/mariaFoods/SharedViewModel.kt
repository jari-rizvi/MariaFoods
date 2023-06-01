package com.teamx.mariaFoods

import androidx.lifecycle.MutableLiveData
import com.teamx.mariaFoods.baseclasses.BaseViewModel


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel : BaseViewModel() {

    val clickOnContinueBtn: MutableLiveData<Boolean>? = null


}