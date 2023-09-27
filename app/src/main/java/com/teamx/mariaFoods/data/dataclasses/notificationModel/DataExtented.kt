package com.teamx.mariaFoods.data.dataclasses.notificationModel
import androidx.annotation.Keep
@Keep
data class DataExtented(val item: Item)

@Keep
data class Item(
    val name: String?,
    val jaris: List<Jari>
)


@Keep
data class Jari(val title: String?, val body: String?, val time: String?)



@Keep
data class MainDateOrderList(val item: MainOrderList)


@Keep
data class MainOrderList(val name: String?,val jariis: List<HelperOrderList>)

@Keep
data class ProductOrderList(val name: String?,
                            val quantity: String?,
                            val price: String?,
                            var created_at: String? = "",
                            var delivery_status: String? = ""
)

@Keep
data class HelperOrderList(val total: String?, val id: Int?, val orderId: String?, val name: String?, val quantity: String?, val price: String?, val created_at: String?, val delivery_status: String?, val productorderlist: List<ProductOrderList>)