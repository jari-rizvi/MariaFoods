package com.teamx.mariaFoods.data.dataclasses.login

data class User(
    val avatar: Any,
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val email_or_otp_verified: Int,
    val email_verified_at: String,
    val first_name: String,
    val id: Int,
    val is_banned: Int,
    val last_name: String,
    val name: String,
    val new_email_verification_code: Any,
    val phone: String,
    val postal_code: Any,
    val provider_id: Any,
    val role_id: Any,
    val shop_id: Any,
    val updated_at: String,
    val user_balance: Int,
    val user_type: String,
    val verification_code: Any
)