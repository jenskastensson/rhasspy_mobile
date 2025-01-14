package org.rhasspy.mobile.logic.nativeutils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract.EXTRA_INITIAL_URI
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

abstract class AppActivity : AppCompatActivity() {

    private var resultCallback: ((activityResult: ActivityResult) -> Unit)? = null

    private val someActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            resultCallback?.invoke(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setup language
        setupLanguage()
    }

    fun createDocument(
        title: String,
        fileType: String,
        onResult: (activityResult: ActivityResult) -> Unit
    ) {
        resultCallback = onResult
        someActivityResultLauncher.launch(
            ActivityResultContracts.CreateDocument(fileType).createIntent(this, title)
        )
    }

    fun openDocument(types: Array<String>, onResult: (activityResult: ActivityResult) -> Unit) {
        resultCallback = onResult
        val intent = ActivityResultContracts.OpenDocument().createIntent(this, types)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val uriDownload: Uri =
                Uri.parse("content://com.android.externalstorage.documents/document/primary:Download")
            intent.putExtra(EXTRA_INITIAL_URI, uriDownload)
        }
        someActivityResultLauncher.launch(intent)
    }
}