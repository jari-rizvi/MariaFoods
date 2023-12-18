package com.teamx.mariaFoods.constants

import androidx.annotation.StringDef


object AppConstants {

    @StringDef(ApiConfiguration.BASE_URL)
    annotation class ApiConfiguration {
        companion object {
            //            const val BASE_URL = "https://raseefapi.teamxmv.com/api/"
//            const val BASE_URL = "http://192.168.100.23:8000/api/v1/"
            const val BASE_URL = "https://dev.dogtvfoods.com/api/v1/"
//            const val BASE_URL = "https://staging.dogtvfoods.com/api/v1/"

        }
    }

    @StringDef(DbConfiguration.DB_NAME)
    annotation class DbConfiguration {
        companion object {
            const val DB_NAME = "MariaFoods"
        }
    }


    @StringDef(
        DataStore.DATA_STORE_NAME,
        DataStore.LOCALIZATION_KEY_NAME,
        DataStore.USER_NAME_KEY,
        DataStore.TOKEN,
        DataStore.AVATAR,
        DataStore.FCM,
        DataStore.NUMBER,
        DataStore.LASTNAME,
        DataStore.FIRSTNAME,
        DataStore.EMAIL,
        DataStore.DETAILS
    )

    annotation class DataStore {
        companion object {
            const val DATA_STORE_NAME = "BaseProject"
            const val LOCALIZATION_KEY_NAME = "lang"
            const val USER_NAME_KEY = "user_name_key"
            const val TOKEN = "token"
            const val LASTNAME = "lastname"
            const val FIRSTNAME = "firstname"
            const val EMAIL = "email"
            const val VERIFIED = "email_or_otp_verified"
            const val ID = "_ID"
            const val FCM = "FCM"
            const val NAME = "NAME"
            const val PROVIDER_ID = "PROVIDER_ID"
            const val AVATAR = "avatar"
            const val DETAILS = "details"
            const val NUMBER = "number"
        }
    }

}