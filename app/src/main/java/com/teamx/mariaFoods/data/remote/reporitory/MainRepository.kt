package com.teamx.mariaFoods.data.remote.reporitory

import com.teamx.mariaFoods.data.local.db.AppDao
import com.teamx.mariaFoods.data.local.dbModel.CartDao
import com.teamx.mariaFoods.data.remote.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService, var localDataSource: AppDao, var localDataSource2: CartDao
) {




}