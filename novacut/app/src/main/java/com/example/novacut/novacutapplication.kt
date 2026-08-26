package com.example.novacut

import android.app.Application
import android.content.ContentValues
import android.provider.MediaStore
import android.os.Environment

class NovaCutApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val resolver = contentResolver
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "novacut_crash_${System.currentTimeMillis()}.txt")
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                resolver.openOutputStream(it)?.use { stream ->
                    stream.write(throwable.stackTraceToString().toByteArray())
                }
            }
        }
    }
}
