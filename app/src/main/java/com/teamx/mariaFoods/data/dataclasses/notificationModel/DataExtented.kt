package com.teamx.mariaFoods.data.dataclasses.notificationModel

data class DataExtented(val item: Item)

data class Item(val name:String,val jaris: List<Jari>)

data class Jari(val title: String, val body: String, val time: String)