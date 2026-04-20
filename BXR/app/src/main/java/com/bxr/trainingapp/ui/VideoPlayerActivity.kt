package com.bxr.trainingapp.ui

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R

class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player)

        val videoRes = intent.getIntExtra("VIDEO_RES_ID", -1)
        val videoView = findViewById<VideoView>(R.id.videoView)

        val uri = Uri.parse("android.resource://$packageName/$videoRes")
        videoView.setVideoURI(uri)
        videoView.start()
    }
}