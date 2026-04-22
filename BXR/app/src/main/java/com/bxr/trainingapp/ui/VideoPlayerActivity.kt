package com.bxr.trainingapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R
import androidx.core.net.toUri

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var seekBar: SeekBar

    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player)

        val videoRes = intent.getIntExtra("VIDEO_RES_ID", -1)
        if (videoRes == -1) {
            finish()
            return
        }

        videoView = findViewById(R.id.videoView)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        seekBar = findViewById(R.id.seekBar)
        val btnClose = findViewById<ImageView>(R.id.btnClose)

        val uri = "android.resource://$packageName/$videoRes".toUri()
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mp ->
            seekBar.max = videoView.duration

            videoView.start()
            isPlaying = true
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)

            updateSeekBar()
        }

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                videoView.pause()
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
            } else {
                videoView.start()
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
                updateSeekBar()
            }
            isPlaying = !isPlaying
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoView.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        btnClose.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            finish()
        }
    }

    private fun updateSeekBar() {
        seekBar.progress = videoView.currentPosition

        if (videoView.isPlaying) {
            handler.postDelayed({ updateSeekBar() }, 500)
        }
    }
}