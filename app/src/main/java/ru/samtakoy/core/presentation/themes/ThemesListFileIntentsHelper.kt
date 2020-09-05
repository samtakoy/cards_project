package ru.samtakoy.core.presentation.themes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import net.rdrei.android.dirchooser.DirectoryChooserActivity
import net.rdrei.android.dirchooser.DirectoryChooserConfig
import java.lang.ref.WeakReference

internal class ThemesListFileIntentsHelper(
        var targetFragment: WeakReference<Fragment>,
        val ctx: WeakReference<Context>,
        val reqCodePermissions: Int) {

    private fun isSamsung(): Boolean {
        return Build.MANUFACTURER.toLowerCase().contains("samsung")
    }

    fun checkFilesReadPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(ctx.get()!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            targetFragment.get()!!.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), reqCodePermissions)
            return false
        }
        return true
    }

    fun fileIntent(reqCode: Int, MIME_TYPE: String) {

        if (checkFilesReadPermission()) {
            if (isSamsung()) {
                selectFileOrFolderOnSamsung(reqCode, MIME_TYPE)
            } else {
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.type = MIME_TYPE
                i.addCategory(Intent.CATEGORY_OPENABLE)
                targetFragment.get()!!.startActivityForResult(Intent.createChooser(i, "Select File To Import"), reqCode)
            }
        }
    }

    fun folderIntent21(reqCode: Int) {
        if (checkFilesReadPermission()) {
            if (isSamsung()) {
                selectFileOrFolderOnSamsung(reqCode, "file/*")
            } else {
                val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                targetFragment.get()!!.startActivityForResult(i, reqCode)
            }
        }
    }

    fun folderIntent(reqCode: Int) {
        val chooserIntent = Intent(ctx.get()!!, DirectoryChooserActivity::class.java)
        val config = DirectoryChooserConfig.builder()
                .newDirectoryName("new directory")
                .allowReadOnlyDirectory(true)
                .allowNewDirectoryNameModification(true) //.TODO
                .build()
        chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config)
        targetFragment.get()!!.startActivityForResult(chooserIntent, reqCode)
    }

    fun selectFileOrFolderOnSamsung(reqCode: Int, MIME_TYPE: String) {
        val intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
        intent.putExtra("CONTENT_TYPE", MIME_TYPE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        targetFragment.get()!!.startActivityForResult(intent, reqCode)
    }
}