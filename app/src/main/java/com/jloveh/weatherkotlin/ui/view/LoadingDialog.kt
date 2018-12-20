package com.jloveh.weatherkotlin.ui.view

import android.app.Dialog
import android.content.Context
import com.jloveh.weatherkotlin.R

object LoadingDialog {
    private var dialog: Dialog? = null

    fun show(context: Context) {
        cancel()
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()
    }

    fun cancel() {
        dialog?.dismiss()
    }
}
