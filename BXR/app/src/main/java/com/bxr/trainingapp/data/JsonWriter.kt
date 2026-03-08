package com.bxr.trainingapp.data

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.bxr.trainingapp.sessions.Handedness
import com.bxr.trainingapp.sessions.Reps
import com.bxr.trainingapp.sessions.SessionTracker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

data class JSONOutput (
    val id : Int,
    var duration : Int,
    var handedness: Handedness,
    var errors : MutableList<String>,
    var reps : Reps,
    var punchType: String,
)
class JsonWriter (context: Context) {
    val path = context.getExternalFilesDir(null)
    val directory = File(path, "userData")
    var file = File(path, "userData"+".json")
    var content = "[]"
    val gson = Gson()
    var jsonContents: List<JSONOutput> = listOf()
    init {
        directory.mkdirs()
        if (file.exists()){
            content = file.readText()
            Log.d("JSONBEFORE", jsonContents.toString())
        }
        jsonContents = gson.fromJson(content, Array<JSONOutput>::class.java).toList()
    }

    public fun createJSONObject(tracker: SessionTracker, punchType: String) {
        val duration = tracker.endTime.toEpochMilli() - tracker.startTime.toEpochMilli()
        val formState = tracker.formState
        var id: Int
        if (jsonContents.isEmpty()){
            id = 0
        } else {
            id = jsonContents.last().id + 1
        }
        val jsonOutput = JSONOutput(id, duration.toInt()/1000, tracker.handedness, formState.errors, formState.reps, punchType)
        saveJSONToInternalStorage(jsonOutput)
    }

    private fun saveJSONToInternalStorage(json: JSONOutput) {
        val jsonMutableList = jsonContents.toMutableList()
        jsonMutableList.add(json)
        val jsonAdded = gson.toJson(jsonMutableList)
        Log.d("JSONAFTER", jsonAdded)
        file.writeText(jsonAdded)
        Log.d("JSON", file.readText())
        Log.d("DIRECTORY", file.toString())
    }
}