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
data class DataExtented1(val item: Item1)


@Keep
data class Item1(val name: String?, val jariis: List<Jari1>)


@Keep
data class Jari1(val id: Int?, val name: String?, val quantity: String?, val price: String?,val created_at: String?, val delivery_status: String?)