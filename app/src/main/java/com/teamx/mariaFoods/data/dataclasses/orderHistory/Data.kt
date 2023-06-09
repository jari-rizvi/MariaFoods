package com.teamx.mariaFoods.data.dataclasses.orderHistory

data class Data(
    val applied_coupon_code: Any,
    val coupon_discount_amount: Int,
    val created_at: String,
    val delivery_status: String,
    val id: Int,
    val payment_status: String,
    val product: Product,
    val scheduled_delivery_info: ScheduledDeliveryInfo,
    val shipping_cost: Int,
    val updated_at: String
)