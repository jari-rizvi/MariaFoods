package com.teamx.mariaFoods.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.teamx.mariaFoods.R

class DialogHelperClass {
    companion object {

        fun loadingDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_loading)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            return dialog
        }


        fun errorDialog(context: Context, errorMessage: String) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_layout_error)
            Log.d("TAG", "errorDialog: eoorrr")
            val errorTextMessage = dialog.findViewById<TextView>(R.id.tv_error_message)
            val tv_title_text = dialog.findViewById<TextView>(R.id.tv_title_text)
            tv_title_text.visibility = View.GONE
            if (errorMessage.contains("job was cancelled", true)) {
                return
            }
            errorTextMessage.text = errorMessage
            dialog.show()
        }


        interface DialogCallBack {
            fun onCnfrmClicked()
            fun onCnclClicked()
        }


        fun defaultCardDialog(context: Context, dialogCallBack: DialogCallBack, boo: Boolean) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.default_card_dialog)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
            )


            val removeBtn = dialog.findViewById<TextView>(R.id.removeBtn)
            removeBtn.setOnClickListener {
                if (boo) {
                    dialogCallBack.onCnfrmClicked()
                } else {
                    dialogCallBack.onCnclClicked()
                }
                dialog.dismiss()
            }

            val cancelBtn = dialog.findViewById<TextView>(R.id.cancelBtn)
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }


    }
}