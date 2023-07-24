package com.teamx.mariaFoods.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.teamx.mariaFoods.MainApplication.Companion.context
import com.teamx.mariaFoods.constants.AppConstants
import com.teamx.mariaFoods.data.dataclasses.login.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreProvider(context: Context) {

    //Create the dataStore
//    private val dataStore : DataStore<Preferences> = context.createDataStore(name = AppConstants.DataStore.DATA_STORE_NAME)

    //Create some keys
    companion object {
        //        private val Context.dataStore: AppConstants.DataStore<Preferences> by preferencesDataStore(name = AppConstants.DataStore.DATA_STORE_NAME)
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
        val VERIFIED = intPreferencesKey(AppConstants.DataStore.VERIFIED)
        val ID = intPreferencesKey(AppConstants.DataStore.ID)
        val NAME = stringPreferencesKey(AppConstants.DataStore.NAME)
        val PROVIDER_ID = stringPreferencesKey(AppConstants.DataStore.PROVIDER_ID)


    }

    //Store data
    suspend fun storeData(
        isLocalizationKey: Boolean,
        name: String,
        token: String,
        details: String
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
            it.remove(FIRSTNAME)
            it.remove(EMAIL)
            it.remove(NUMBER)
            it.remove(LASTNAME)
            it.remove(AVATAR)
            it.remove(ID)
        }

    }

    val firstName: Flow<String?> = context.dataStore.data.map {
        it[FIRSTNAME]
    }

    val lastName: Flow<String?> = context.dataStore.data.map {
        it[LASTNAME]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map {
        it[EMAIL]
    }
    val userNumber: Flow<String?> = context.dataStore.data.map {
        it[NUMBER]
    }

    val userFlow: Flow<User> = context.dataStore.data
        .map { preferences ->
            val first_name = preferences[FIRSTNAME] ?: ""
            val last_name = preferences[LASTNAME] ?: ""
            val email = preferences[EMAIL] ?: ""
            val phone = preferences[NUMBER] ?: ""
            val verified = preferences[VERIFIED] ?: 0
            val _id = preferences[ID] ?: 0
            val name = preferences[NAME] ?: ""
            val avatar = preferences[AVATAR] ?: ""
            val provider_id = preferences[PROVIDER_ID] ?: ""
            User(
                first_name = first_name,
                email = email,
                last_name = last_name,
                phone = phone,
                email_or_otp_verified = verified,
                id = _id,
                avatar = avatar,
                name = name,
                provider_id = provider_id as String,
                with_email_and_pass = true
            )
        }


    suspend fun saveUserDetails(user: User) {
        context.dataStore.edit {
            it[FIRSTNAME] = user.first_name
            it[LASTNAME] = user.last_name
            it[EMAIL] = user.email
            it[NUMBER] = user.phone
            it[AVATAR] = user.avatar
            it[VERIFIED] = user.email_or_otp_verified
            it[ID] = user.id
            it[NAME] = user.name
            it[PROVIDER_ID] = user.provider_id
        }
    }


}