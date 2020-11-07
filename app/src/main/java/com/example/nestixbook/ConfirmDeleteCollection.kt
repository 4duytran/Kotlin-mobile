package com.example.nestixbook

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDeleteCollection(val collectionTite:String): DialogFragment() {

    interface ConfirmDialog {
        fun onDialogPosClick()
        fun onDialogNegClick()
    }

    var listener : ConfirmDialog? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure delete the collection \"$collectionTite\" from your list?")
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                dialog, id -> listener?.onDialogPosClick()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {dialog, id ->
                listener?.onDialogNegClick()
            })
        return builder.create()
    }
}