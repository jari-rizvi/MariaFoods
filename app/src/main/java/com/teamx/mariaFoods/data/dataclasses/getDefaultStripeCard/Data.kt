package com.teamx.mariaFoods.data.dataclasses.getDefaultStripeCard
import com.google.errorprone.annotations.Keep

@Keep
data class Data(
    val billing_details: BillingDetails,
    val card: Card,
    val created: Int?,
    val customer: String?,
    val id: String?,
    val livemode: Boolean,
    val metadata: List<Any>,
    val `object`: String?,
    val type: String?
)