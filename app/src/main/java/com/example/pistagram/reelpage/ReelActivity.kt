package com.example.pistagram.reelpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.pistagram.R
import com.example.pistagram.utils.CredsEditor
import kotlin.math.abs

class ReelActivity : AppCompatActivity() {
    private lateinit var reelContainer: VideoView
    private lateinit var reelLikesCount: TextView
    private lateinit var reelCommentsCount: TextView
    private lateinit var reelSharesCount: TextView
    private lateinit var songAvatar: ImageView
    private lateinit var reelTrackName: TextView
    private lateinit var reelDescription: TextView
    private lateinit var reelUsername: TextView
    private lateinit var gestureDetector: GestureDetector
    private val reelFetcher = ReelFetcher()
    private var currentReelIndex = 0
    private var reels: List<Reel> = listOf()
    val credsEditor = CredsEditor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeUI()
        setupGestureDetector()
        val lastPagePlayed =
            credsEditor.readFileFromInternalStorage(this@ReelActivity, "lastPagePlayed")?.toInt() ?: 0
        reelFetcher.fetchReels(lastPagePlayed+1) { fetchedReels ->
            if (fetchedReels != null) {
                reels = fetchedReels
                updateUI(reels[currentReelIndex])
            }
        }
    }

    private fun initializeUI() {
        reelContainer = findViewById(R.id.reelContainer)
        reelLikesCount = findViewById(R.id.reelLikesCount)
        reelCommentsCount = findViewById(R.id.reelCommentsCount)
        reelSharesCount = findViewById(R.id.reelSharesCount)
        songAvatar = findViewById(R.id.songAvatar)
        reelTrackName = findViewById(R.id.reelTrackName)
        reelDescription = findViewById(R.id.reelDescription)
        reelUsername = findViewById(R.id.reelUsername)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val diffY = e2.y.minus(e1?.y ?: 0f)
                val diffX = e2.x.minus(e1?.x ?: 0f)

                if (abs(diffY) > abs(diffX) && abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        showPreviousReel()
                    } else {
                        showNextReel()
                    }
                    return true
                }
                return false
            }
        })

        findViewById<VideoView>(R.id.reelContainer).setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun updateUI(reel: Reel) {
        reelContainer.setVideoPath(reel.videoUrl)
        reelContainer.start()
        reelLikesCount.text = reel.likesCount.toString()
        reelCommentsCount.text = reel.commentsCount.toString()
        reelSharesCount.text = reel.sharesCount.toString()
        reelTrackName.text = reel.trackName
        reelDescription.text = reel.reelDescription
        reelUsername.text = reel.username
        Glide.with(this@ReelActivity)
            .load(reel.trackAvatar)
            .placeholder(R.drawable.ic_home_profile)
            .error(R.drawable.ic_launcher_foreground)
            .into(songAvatar)
    }

    private fun showNextReel() {
        if (currentReelIndex < reels.size - 1) {
            currentReelIndex++
            updateUI(reels[currentReelIndex])
        } else {
            credsEditor.writeFileOnInternalStorage(this@ReelActivity, "lastPagePlayed", currentReelIndex.toString())
            reelFetcher.fetchReels(currentReelIndex / 10 + 1) { fetchedReels ->
                if (!fetchedReels.isNullOrEmpty()) {
                    reels = reels + fetchedReels
                    currentReelIndex++
                    updateUI(reels[currentReelIndex])
                }
            }
        }
    }

    private fun showPreviousReel() {
        if (currentReelIndex > 0) {
            currentReelIndex--
            updateUI(reels[currentReelIndex])
        }
    }
}
