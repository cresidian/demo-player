package com.example.demoplayer.ui.playlist

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.demoplayer.adapters.TrackCallback
import com.example.demoplayer.adapters.TracksAdapter
import com.example.demoplayer.core.setVisible
import com.example.demoplayer.databinding.ActivityPlaylistBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PlaylistActivity : AppCompatActivity() {

    private lateinit var bind: ActivityPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var player: MediaPlayer
    private val handlerPlayerProgressBar = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityPlaylistBinding.inflate(layoutInflater)
        val view = bind.root
        setContentView(view)
        init()
        initViewModelObservers()
        initPlayer()
    }

    private fun init() {
        bind.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = bind.etSearch.text.toString()
                if (query.isNotEmpty()) {
                    viewModel.search(query)
                }
                return@OnEditorActionListener true
            }
            false
        })
        bottomSheetBehavior = BottomSheetBehavior.from(bind.playerLayout.bottomSheet)
        bottomSheetBehavior.isDraggable = false
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        })
        bind.noRecordsView.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                hidePlayer()
            } else {
                showPlayer()
            }
        }
        //To disable seekbar drag by user
        bind.playerLayout.seekBar.setOnTouchListener { _, _ -> true }
    }

    private fun hidePlayer() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showPlayer() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initViewModelObservers() {
        with(viewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewStates.collect { uiState ->
                        when (uiState) {
                            is PlaylistViewModel.PlaylistViewStates.ShowLoad -> {
                                bind.loaderView.setVisible(uiState.isLoad)
                            }
                            is PlaylistViewModel.PlaylistViewStates.SetTracks -> {
                                bind.noRecordsView.setVisible(uiState.tracks.isEmpty())
                                bind.rvSongs.adapter = if (uiState.tracks.isNotEmpty()) {
                                    TracksAdapter(uiState.tracks, object : TrackCallback {
                                        override fun onTrackSelected(url: String) {
                                            playTrackFromUrl(url)
                                        }
                                    })
                                } else {
                                    null
                                }
                            }
                            is PlaylistViewModel.PlaylistViewStates.ShowError -> {
                                bind.noRecordsView.setVisible(true)
                                bind.rvSongs.adapter = null
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initPlayer() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        player = MediaPlayer().apply {
            setAudioAttributes(audioAttributes)
            setOnPreparedListener {
                start()
                setAudioProgress()
                bind.playerLayout.ibPlay.setImageResource(android.R.drawable.ic_media_pause)
            }
            setOnCompletionListener {
                bind.playerLayout.ibPlay.setImageResource(android.R.drawable.ic_media_play)
                release()
            }
            bind.playerLayout.ibPlay.setOnClickListener {
                val resource = if (this.isPlaying) {
                    pause()
                    android.R.drawable.ic_media_play
                } else {
                    start()
                    android.R.drawable.ic_media_pause
                }
                bind.playerLayout.ibPlay.setImageResource(resource)
            }
        }
    }

    private fun playTrackFromUrl(
        url: String
    ) {
        showPlayer()
        if (player.isPlaying) {
            player.reset()
            bind.playerLayout.ibPlay.setImageResource(android.R.drawable.ic_media_play)
        }
        player.setDataSource(url)
        player.prepareAsync()
    }

    private fun setAudioProgress() {
        val totalDuration = player.duration
        bind.playerLayout.seekBar.max = totalDuration
        runnable = object : Runnable {
            override fun run() {
                try {
                    val currentPos = player.currentPosition
                    bind.playerLayout.seekBar.progress = currentPos
                    handlerPlayerProgressBar.postDelayed(this, PLAYER_PROGRESS_DELAY)
                } catch (ed: IllegalStateException) {
                    ed.printStackTrace()
                }
            }
        }
        handlerPlayerProgressBar.postDelayed(runnable, PLAYER_PROGRESS_DELAY)
    }

    override fun onDestroy() {
        player.stop()
        player.release()
        if (::runnable.isInitialized) {
            handlerPlayerProgressBar.removeCallbacks(runnable)
        }
        super.onDestroy()
    }

    companion object {
        private const val PLAYER_PROGRESS_DELAY = 50L
    }
}