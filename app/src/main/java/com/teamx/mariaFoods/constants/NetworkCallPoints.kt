package com.teamx.mariaFoods.constants

class NetworkCallPoints {
    companion object {
        const val LOGIN = "users/signin"
        const val LOGIN_PHONE = "users/signin"
        const val SIGN_UP = "users/singup"
        const val LOGOUT = "users/signout"
        const val SOCIAL_LOGIN = "users/socials"
        const val EDIT_PROFILE = "settings/profile"
        const val PROFILE_IMAGE = "settings/profile-photo"


        // Register Otp Verify

        const val OTP_VERIFY = "users/otp/verify"
        const val OTP_VERIFY_FORGOT_EMAIL = "users/email/verify"
        const val OTP_VERIFY_PHONE = "verify-phone-otp-code"
        const val RESEND_OTP_VERIFY = "register/resend-otp"


        // Create new password
        const val RESET_PASS_EMAIL = "users/email/reset/password"
        const val RESET_PASS = "users/email/reset/password/update"
        const val CHANGE_PASSWORD = "settings/change-password"
        const val CHANGE_PHONE = "settings/change-phone"
        const val CHANGE_PHONE_VERIFY = "settings/change-phone-verify"
        const val FORGOT_PASS_PHONE = "forget-password-phone"


        //Get Countries
        const val COUNTRIES = "users/address/get-country-city-states"
        const val BANNERS = "app/slides"
        const val PRODUCTS = "products/latest"

        //Get OrderHistory
        const val GET_ORDER_HISTORY = "products/order/history"
        const val CANCEL_ORDER = "products/order/cancel"

        //Get Notification
        const val GET_NOTIFICATION = "users/get-notifications"
        const val SETTING_NOTIFICATION = "settings/fcm-on-off"

        //Ger Addresses
        const val GET_ADDRESS = "users/address/get-addresses"
        const val DELETE_ADDRESS = "users/address/delete-address"
        const val ADD_ADDRESS = "users/address/new-address"
        const val UPDATE_ADDRESS = "users/address/update-address"
        const val EDIT_ADDRESS = "users/address/edit-address?"

        //Add to cart
        const val ADD_CART = "products/add-to-cart"
        const val GET_CART = "products/get-cart"


        //Stripe
        const val GET_CARDS = "users/stripe/get_payment_method"
        const val GET_DEFAULT_CARDS = "users/stripe/get_default_payment_method"
        const val SET_DEFAULT_CARDS = "users/stripe/set_default_payment_method"
        const val DELETE_STRIPE_CARDS = "users/stripe/remove_payment_method"


        //Checkout
        const val CHECKOUT = "products/order/checkout"
        const val ADD_COUPON = "products/apply-coupon"

        //Terms&Condition
        const val TERMS_CONDITION = "app/get-terms-and-conditions"
        const val GET_HELP = "app/get-help"


        var TOKENER = ""
    }
}