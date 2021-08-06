package com.example.searchfilesample

import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter

object FileUtils {
    fun searchFile(file: File, key: String) {
        val list = file.listFiles(object : FileFilter {
            override fun accept(f: File?): Boolean {
                return (f != null && f.isDirectory) || (f != null && !f.isDirectory && f.absolutePath.contains(
                    key
                ))
            }
        })
        list?.forEach {
            if (it.isDirectory) {
                searchFile(it, key)
            } else {
                println("扫描到路径：${it.absolutePath}")
            }
        }
    }

    fun searchFile1(file: File, key: String) {
        file.list(object : FilenameFilter {
            override fun accept(f: File?, name: String?): Boolean {
                println("文件路径和文件名  ${f?.absolutePath} :  $name")
                return (f != null && f.isDirectory) || (f != null && !f.isDirectory && f.absolutePath.contains(
                    key
                ))
            }
        }).forEach {
            val file1 = File(it)
            if (file1.isDirectory) {
                if (it.equals("/Android")) {
                    println("Android是文件夹")
                }
                searchFile(file1, key)
            } else {
                if (file1.absolutePath.contains(key)) {
                    println("扫描到路径：${file1.absolutePath}")
                }
            }
        }
    }


    fun searchFile2(file: File, key: String) {
        file.list { f, name ->
            if (f != null && name != null) {
                if (name.contains(key) && name.contains(".")) {
                    println("输出扫描路径：${f.absolutePath}/$name")
                } else {
                    File(f, name).let {
                        if (it.isDirectory) {
                            searchFile2(it, key)
                        }
                    }
                }
            }
            false
        }
    }
}