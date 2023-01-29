package com.example.project_one

import android.app.ProgressDialog
import android.content.Context

class MyProgressBar(str:String, private val context: Context) {
    val msg = str
    var progressBar: ProgressDialog = ProgressDialog(context)

    init {
        progressBar.setCancelable(true)
        progressBar.setMessage(msg)
    }

    fun show(){
        progressBar.show()
    }

    fun dismiss(){
        progressBar.dismiss()
    }
}