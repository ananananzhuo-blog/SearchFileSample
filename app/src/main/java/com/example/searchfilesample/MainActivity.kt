package com.example.searchfilesample

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.mvvm.callback.CallData
import com.ananananzhuo.mvvm.callback.Callback
import kotlinx.coroutines.*
import java.io.File
import kotlin.concurrent.thread

class MainActivity : CustomAdapterActivity() {
    val scope = MainScope()
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "申请权限", callback = object : Callback {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun callback(callData: CallData) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        callData.itemData.run {
                            content = "已经有权限了"
                            notifyDataSetChange()
                        }
                    } else {
                        startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                    }
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 12
                    )
                }
            }
        }),
        ItemData(title = "扫描sd卡", callback = object : Callback {
            override fun callback(callData: CallData) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    callData.itemData.run {
                        content = "安卓10需要特殊适配，sb,(有心情再去适配android10)"
                        notifyDataSetChange()
                    }
                    return
                }
                val file = Environment.getExternalStorageDirectory()
                scope.launch(Dispatchers.IO) {
                    val startStamp = System.currentTimeMillis()
                    FileUtils.searchFile2(file, "apk")
                    val duration = System.currentTimeMillis()-startStamp
                    withContext(Dispatchers.Main){
                        callData.itemData.run {
                            content="扫描耗时：${duration/1000}"
                            notifyDataSetChange()
                        }
                    }
                }
            }
        })
    )

    override fun showFirstItem(): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}