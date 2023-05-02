package com.teamx.mariaFoods.constants

class NetworkCallPoints {
    companion object {
        const val LOGIN_PHONE = "token/phone";
        const val SIGN_UP = "register/phone";

        // Register Otp Verify

        const val OTP_VERIFY = "register/phone-verify";
        const val OTP_VERIFY_PHONE = "verify-phone-otp-code";
        const val RESEND_OTP_VERIFY = "register/resend-otp";


        // Create new password
        const val RESET_PASS = "reset-password";
        const val RESET_PASS_PHONE = "Reset-Password-Phone";
        const val CHANGE_PASSWORD = "change-password";


        const val FORGOT_PASS_PHONE = "forget-password-phone";


        var TOKENER = ""
    }
}