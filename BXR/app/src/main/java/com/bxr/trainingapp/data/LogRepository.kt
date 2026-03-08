package com.bxr.trainingapp.data

import android.content.Context
import com.bxr.trainingapp.model.SessionLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object LogRepository {
    fun loadLogs(context: Context): List<SessionLog> {
        return try {
            val file = File(context.getExternalFilesDir(null), "userData.json")

            if (!file.exists()) {
                android.util.Log.e("LogRepo", "FILE NOT FOUND AT: ${file.absolutePath}")
                return emptyList()
            }

            val jsonString = file.readText()
            val listType = object : TypeToken<List<SessionLog>>() {}.type
            Gson().fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}