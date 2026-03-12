package com.bxr.trainingapp.data

import android.content.Context
import android.util.Log
import com.bxr.trainingapp.sessions.SessionTracker
import com.google.gson.Gson
import java.io.File


class JsonWriter(context: Context) {

    private val path = context.getExternalFilesDir(null)
    private val directory = File(path, "userData")
    private val file = File(directory, "userData.json")
    private val gson = Gson()

    private var jsonContents: MutableList<SessionLog> = mutableListOf()

    init {
        try {
            if (!directory.exists()) {
                val created = directory.mkdirs()
                Log.d("JSON_INIT", "Directory created: $created")
            }

            if (file.exists()) {
                val content = file.readText().trim()

                if (content.isNotEmpty() && content != "[]") {
                    val listType = object : com.google.gson.reflect.TypeToken<MutableList<SessionLog>>() {}.type
                    jsonContents = gson.fromJson(content, listType) ?: mutableListOf()
                    Log.d("JSON_INIT", "Successfully loaded ${jsonContents.size} logs")
                } else {
                    jsonContents = mutableListOf()
                    Log.d("JSON_INIT", "File is empty, starting new list")
                }
            } else {
                jsonContents = mutableListOf()
                Log.d("JSON_INIT", "No file found, initializing empty list")
            }
        } catch (e: Exception) {
            Log.e("JSON_INIT", "Error initializing JsonWriter: ${e.message}")
            jsonContents = mutableListOf()
        }
    }

    fun createJSONObject(tracker: SessionTracker, punchType: String) {

        val duration =
            ((tracker.endTime.toEpochMilli() - tracker.startTime.toEpochMilli()) / 1000).toInt()

        val formState = tracker.formState

        val id = if (jsonContents.isEmpty()) 0 else jsonContents.last().id + 1

        val jsonOutput = SessionLog(
            id = id,
            duration = duration,
            handedness = tracker.handedness.toString(),
            reps = com.bxr.trainingapp.data.Reps(
                total = tracker.formState.reps.total,
                correct = tracker.formState.reps.correct,
                wrong = tracker.formState.reps.wrong
            ),
            repResults = tracker.formState.repResults,
            punchType = punchType
        )

        saveJSONToInternalStorage(jsonOutput)
    }

    private fun saveJSONToInternalStorage(json: SessionLog) {
        jsonContents.add(json)

        val jsonAdded = gson.toJson(jsonContents)
        file.writeText(jsonAdded)

        Log.d("JSON_AFTER", jsonAdded)
        Log.d("DIRECTORY", file.absolutePath)
    }

    fun clearData() {
        jsonContents.clear()
        file.writeText("[]")
    }
}
