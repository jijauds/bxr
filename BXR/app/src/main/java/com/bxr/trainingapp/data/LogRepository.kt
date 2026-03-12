package com.bxr.trainingapp.data

import android.content.Context
import android.util.Log
import com.bxr.trainingapp.data.SessionLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object LogRepository {
    private const val DIRECTORY_NAME = "userData"
    private const val FILE_NAME = "userData.json"

    fun loadLogs(context: Context): List<SessionLog> {
        return try {
            val path = context.getExternalFilesDir(null)
            val directory = File(path, DIRECTORY_NAME)
            val file = File(directory, FILE_NAME)

            if (!file.exists()) {
                Log.d("LogRepo", "No log file found at: ${file.absolutePath}")
                return emptyList()
            }

            val jsonString = file.readText().trim()

            if (jsonString.isEmpty() || jsonString == "[]") {
                return emptyList()
            }

            val listType = object : TypeToken<List<SessionLog>>() {}.type
            val logs: List<SessionLog>? = Gson().fromJson(jsonString, listType)

            logs ?: emptyList()

        } catch (e: Exception) {
            Log.e("LogRepo", "Error loading logs: ${e.message}")
            emptyList()
        }
    }
}