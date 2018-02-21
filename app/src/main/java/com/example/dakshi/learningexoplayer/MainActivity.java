package com.example.dakshi.learningexoplayer;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    private boolean playWhenReady=true;
    long playbackPosition=0;
    int currentWindow=0;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView=findViewById(R.id.video_view);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializePlayer()
    {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(getString(R.string.media_url3_mp3));
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        /*return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);*/

        // these are reused for both media sources we create below
        DefaultExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory =
                new DefaultHttpDataSourceFactory( "user-agent");

        ExtractorMediaSource videoSource =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);

        Uri audioUri = Uri.parse(getString(R.string.media_url_mp3));
        ExtractorMediaSource audioSource =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(audioUri);

        return new ConcatenatingMediaSource(audioSource, videoSource);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
