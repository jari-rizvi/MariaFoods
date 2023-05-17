package com.teamx.mariaFoods.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.teamx.mariaFoods.R

fun View.snackbar(message: String){
/*    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    ).also {
        snackbar ->
        snackbar.setAction("Ok"){
            snackbar.dismiss()
        }
    }.show()*/

    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val returnCursor = this.query(uri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }


    val snackbar: Snackbar
    snackbar = Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_SHORT
    )
    val snackBarView = snackbar.getView()
    snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
    snackbar.show()
}