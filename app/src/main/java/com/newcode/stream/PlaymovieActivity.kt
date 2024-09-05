package com.newcode.stream

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util

class PlaymovieActivity : AppCompatActivity() {
    private lateinit var uri: Uri
    private var mPlayer: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var titleTextView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var barraTitulo: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playmovie)

        val url: String = intent.getStringExtra("url").toString()
        val title: String = intent.getStringExtra("title") ?: "Sin título"

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        playerView = findViewById(R.id.video_view)
        titleTextView = findViewById(R.id.content_title)
        backButton = findViewById(R.id.back_button)
        barraTitulo = findViewById(R.id.barraTitulo)

        titleTextView.text = title
        uri = Uri.parse(url)

        // Inicialmente los elementos serán invisibles
        barraTitulo.visibility = View.INVISIBLE

        initPlayer()

        backButton.setOnClickListener {
            finish()
        }

        // Sincronizar visibilidad del contenedor con los controles del reproductor
        playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                barraTitulo.animate().alpha(1f).setDuration(300).withStartAction {
                    barraTitulo.visibility = View.VISIBLE
                }
                // Animar los controles del reproductor
                playerView.findViewById<LinearLayout>(R.id.exo_controls)?.animate()?.alpha(1f)?.setDuration(300)?.withStartAction {
                    playerView.findViewById<LinearLayout>(R.id.exo_controls)?.visibility = View.VISIBLE
                }
            } else {
                barraTitulo.animate().alpha(0f).setDuration(300).withEndAction {
                    barraTitulo.visibility = View.INVISIBLE
                }
                // Animar los controles del reproductor
                playerView.findViewById<LinearLayout>(R.id.exo_controls)?.animate()?.alpha(0f)?.setDuration(300)?.withEndAction {
                    playerView.findViewById<LinearLayout>(R.id.exo_controls)?.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initPlayer() {
        hideSystemUI()
        mPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        // Manejar el final de la reproducción si es necesario
                    }
                }
            })
        }
        playerView.player = mPlayer
        mPlayer?.setMediaSource(buildMediaSource())
        mPlayer?.prepare()

        // Muestra los controles al iniciar la reproducción
        playerView.showController()

        // Fuerza la visibilidad del contenedor, título y botón de regreso al iniciar la película
        barraTitulo.visibility = View.VISIBLE
        playerView.findViewById<LinearLayout>(R.id.exo_controls)?.visibility = View.VISIBLE

        // Esconde el contenedor junto con los controles después del tiempo configurado
        playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                barraTitulo.visibility = View.VISIBLE
                playerView.findViewById<LinearLayout>(R.id.exo_controls)?.visibility = View.VISIBLE
            } else {
                barraTitulo.visibility = View.GONE
                playerView.findViewById<LinearLayout>(R.id.exo_controls)?.visibility = View.GONE
            }
        }
    }

    private fun buildMediaSource(): MediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        mPlayer?.run {
            playWhenReady = false
            release()
        }
        mPlayer = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        if ((Util.SDK_INT < 24 || mPlayer == null)) {
            initPlayer()
        }
    }
}
