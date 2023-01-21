package dev.graansma.tunebot_android

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var controller: Controller
    private val network = object: Network() {
        override fun onScanComplete(macs: Set<String>) {
            controller.updateMasterList(macs)
        }
    }
    private var scheduler: ScheduledExecutorService? = null
    private val mediaPlayer = MediaPlayer()

    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var playPauseButton: Button
    private lateinit var currentSongText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = Controller(PromotionLevel.valueOf(getString(R.string.promotion_level)))

        currentSongText = findViewById(R.id.currentSongText)
        nextButton = findViewById(R.id.nextButton)
        nextButton.setOnClickListener { next() }
        previousButton = findViewById(R.id.previousButton)
        previousButton.setOnClickListener { previous() }
        playPauseButton = findViewById(R.id.playPauseButton)
        playPauseButton.setOnClickListener { playPause() }

        mediaPlayer.setOnPreparedListener { play() }
        mediaPlayer.setOnCompletionListener { next() }
        mediaPlayer.setScreenOnWhilePlaying(true)
    }

    override fun onResume() {
        super.onResume()
        scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler?.scheduleWithFixedDelay(network::startScan, 0, 10, TimeUnit.MINUTES)
    }

    override fun onStop() {
        mediaPlayer.stop()
        mediaPlayer.release()
        scheduler?.shutdownNow()
        network.endScan()
        super.onStop()
    }

    private fun setSong(song: Song) {
        pause()
        mediaPlayer.release()
        mediaPlayer.setDataSource(song.url)
        mediaPlayer.prepare()
        currentSongText.post { currentSongText.text = song.toString() }
    }

    private var isPaused = true
    private fun play() {
        isPaused = false
        Log.d("action", "play")
        playPauseButton.post { playPauseButton.setBackgroundResource(android.R.drawable.ic_media_pause) }
        //mediaPlayer.start()
    }
    private fun pause() {
        isPaused = true
        Log.d("action", "pause")
        playPauseButton.post { playPauseButton.setBackgroundResource(android.R.drawable.ic_media_play) }
        mediaPlayer.pause()
    }
    private fun playPause() {
        if(isPaused) play() else pause()
    }
    private fun next() {
        Log.d("action", "next")
        setSong(controller.nextSong())
    }
    private fun previous() {
        Log.d("action", "previous")
        setSong(controller.previousSong())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> playPause()
            KeyEvent.KEYCODE_MEDIA_PLAY -> play()
            KeyEvent.KEYCODE_MEDIA_PAUSE -> pause()
            KeyEvent.KEYCODE_MEDIA_NEXT -> next()
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> previous()
        }
        return super.onKeyDown(keyCode, event)
    }
}