package com.teamx.mariaFoods.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.teamx.mariaFoods.constants.AppConstants

class PrefHelper private constructor() {

    companion object {
        private val sharePref = PrefHelper()
        private lateinit var sharedPreferences: SharedPreferences
        private val USER_ID = "userId"
        private val DAYS = "days"
        private val TIME = "time"


        fun getInstance(context: Context): PrefHelper {
            if (!::sharedPreferences.isInitialized) {
                synchronized(PrefHelper::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences =
                            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }

    val firstname: String??
        get() = sharedPreferences.getString(
            AppConstants.DataStore.FIRSTNAME,
            ""
        )
    val lastname: String?? get() = sharedPreferences.getString(AppConstants.DataStore.LASTNAME, "")
    val email: String?? get() = sharedPreferences.getString(AppConstants.DataStore.EMAIL, "")
    val avatar: String?? get() = sharedPreferences.getString(AppConstants.DataStore.AVATAR, "")
    val number: String?? get() = sharedPreferences.getString(AppConstants.DataStore.NUMBER, "")
    fun saveProfile(
        firstname: String?,
        lastname: String?,
        email: String?,
        avatar: String?,
        number: String?
    ) {
        sharedPreferences.edit().putString(AppConstants.DataStore.FIRSTNAME, firstname).apply()
        sharedPreferences.edit().putString(AppConstants.DataStore.LASTNAME, lastname).apply()
        sharedPreferences.edit().putString(AppConstants.DataStore.EMAIL, email).apply()
        sharedPreferences.edit().putString(AppConstants.DataStore.AVATAR, avatar).apply()
        sharedPreferences.edit().putString(AppConstants.DataStore.NUMBER, number).apply()
    }


    val getUserId: String?
        get() = if (sharedPreferences.getString(USER_ID, "")!!.isNotEmpty()) {
            sharedPreferences.getString(USER_ID, "")
        } else {
            null
        }


    fun savaUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID, userId).apply()
    }

    val days: Int?
        get() = sharedPreferences.getInt(DAYS, 0)


    fun savaDays(date: Int) {
        sharedPreferences.edit().putInt(DAYS, date).apply()
    }

    val time: Int?
        get() = sharedPreferences.getInt(TIME, 0)


    fun savaTime(time: Int) {
        sharedPreferences.edit().putInt(TIME, time).apply()
    }


}