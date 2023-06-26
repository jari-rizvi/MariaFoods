package com.teamx.mariaFoods.data.dataclasses.orderHistory
import com.google.errorprone.annotations.Keep

@Keep
data class Jun(
    val Total: String?,
    val applied_coupon_code: String?,
    val coupon_discount_amount: String?,
    val created_at: String?,
    val delivery_charges: String?,
    val delivery_status: String?,
    val id: Int?,
    val order_quantity: Int?,
    val payment_status: String?,
    val product: Product,
    val scheduled_delivery_info: ScheduledDeliveryInfo,
    val shipping_cost: Int?,
    val subTotal: String?,
    val updated_at: String?,
    val vat: String?
)