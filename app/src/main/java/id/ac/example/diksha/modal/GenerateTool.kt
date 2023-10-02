package id.ac.example.diksha.modal

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import id.ac.example.diksha.R
import javax.inject.Inject


class GenerateTool @Inject constructor(private val context: Context) {
    private var toast: Toast? = null
    val token = "932e9a3b33214a9cb3026ab9b648603f"
    val requestTime = 20000
    val requestRetries = 0


    fun initialDialog(context: Context): Dialog {
        val mDialog = Dialog(context)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.custom_progres_bar)
        mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return mDialog
    }

    fun popUpMessage(context: Context, tittle: String, message: String) {
        val alert = AlertDialog.Builder(context).create()
        alert.setTitle(tittle)
        alert.setMessage(message)
        alert.setCancelable(false)
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }

    fun popUpMessageFinish(context: Context, tittle: String, message: String) {
        val builder = AlertDialog.Builder(context).create()
        builder.setTitle(tittle)
        builder.setMessage(message)
        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setOnDismissListener {
            (context as Activity).finish()
        }
        builder.show()


    }

    fun showToast(text: String) {

        toast?.cancel()
        toast = Toast.makeText(context, "message", Toast.LENGTH_SHORT)
        toast?.setText(text)
        toast?.setGravity(Gravity.BOTTOM, 0, 50)
        toast?.show()
    }

}