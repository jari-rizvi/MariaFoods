package com.teamx.mariaFoods.data.dataclasses.notificationModel

data class DataExtented(val item: Item)

data class Item(
    val name: String,
    val jaris: List<Jari>
)

data class Jari(val title: String, val body: String, val time: String)


data class DataExtented1(val item: Item1)

data class Item1(val name: String, val jariis: List<Jari1>)

data class Jari1(val name: String, val quantity: String, val price: String)
//data class Productt(val name: String, val min_price: String, val time: String)