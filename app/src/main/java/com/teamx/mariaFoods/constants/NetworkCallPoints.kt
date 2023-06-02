package com.teamx.mariaFoods.constants

class NetworkCallPoints {
    companion object {
        const val LOGIN = "users/signin"
        const val LOGIN_PHONE = "users/signin"
        const val SIGN_UP = "users/singup"
        const val LOGOUT = "users/signout"
        const val SOCIAL_LOGIN = "users/socials"


        // Register Otp Verify

        const val OTP_VERIFY = "users/otp/verify"
        const val OTP_VERIFY_FORGOT_EMAIL = "users/email/verify"
        const val OTP_VERIFY_PHONE = "verify-phone-otp-code"
        const val RESEND_OTP_VERIFY = "register/resend-otp"


        // Create new password
        const val RESET_PASS_EMAIL = "users/email/reset/password"
        const val RESET_PASS = "users/email/reset/password/update"
        const val CHANGE_PASSWORD = "change-password"
        const val FORGOT_PASS_PHONE = "forget-password-phone"


        //Get Countries
        const val COUNTRIES = "users/address/get-country-city-states"
        const val BANNERS = "app/slides"
        const val PRODUCTS = "products/latest"

        //Get OrderHistory
        const val GET_ORDER_HISTORY = "products/order/history"

        var TOKENER = ""
    }
}