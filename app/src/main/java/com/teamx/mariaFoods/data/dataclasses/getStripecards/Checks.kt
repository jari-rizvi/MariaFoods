package com.teamx.mariaFoods.data.dataclasses.getStripecards
import com.google.errorprone.annotations.Keep

@Keep
data class Checks(
    val address_line1_check: Any?,
    val address_postal_code_check: Any?,
    val cvc_check: String?
)