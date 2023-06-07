package com.teamx.mariaFoods.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.teamx.mariaFoods.MainApplication.Companion.context
import com.teamx.mariaFoods.constants.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreProvider(context: Context) {

    //Create the dataStore
//    private val dataStore : DataStore<Preferences> = context.createDataStore(name = AppConstants.DataStore.DATA_STORE_NAME)

    //Create some keys
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstants.DataStore.DATA_STORE_NAME)
        val IS_LOCALIZATION_KEY =
            booleanPreferencesKey(AppConstants.DataStore.LOCALIZATION_KEY_NAME)
        val USER_NAME_KEY = stringPreferencesKey(AppConstants.DataStore.USER_NAME_KEY)
        val TOKEN = stringPreferencesKey(AppConstants.DataStore.TOKEN)
        val NUMBER = stringPreferencesKey(AppConstants.DataStore.NUMBER)
        val AVATAR = stringPreferencesKey(AppConstants.DataStore.AVATAR)
        val DETAILS = stringPreferencesKey(AppConstants.DataStore.DETAILS)
        val FIRSTNAME = stringPreferencesKey(AppConstants.DataStore.FIRSTNAME)
        val LASTNAME = stringPreferencesKey(AppConstants.DataStore.LASTNAME)
        val EMAIL = stringPreferencesKey(AppConstants.DataStore.EMAIL)


    }

    //Store data
    suspend fun storeData(
        isLocalizationKey: Boolean, name: String, token: String, details: String
    ) {
        context.dataStore.edit {
            it[IS_LOCALIZATION_KEY] = isLocalizationKey
            it[USER_NAME_KEY] = name
            it[TOKEN] = token
        }

    }

    //get Token by using this
    val token: Flow<String?> = context.dataStore.data.map {
        it[TOKEN]
    }
    val details: Flow<String?> = context.dataStore.data.map {
        it[DETAILS]
    }

    suspend fun saveUserToken(token: String) {
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }

    suspend fun removeAll() {
        context.dataStore.edit {
            it.remove(TOKEN)
        }

    }


    suspend fun saveUserDetails(
        firstname: String, lastname: String, email: String, number: String
    ) {
        context.dataStore.edit {
            it[FIRSTNAME] = firstname
            it[LASTNAME] = lastname
            it[EMAIL] = email
            it[NUMBER] = number
        }
    }


}