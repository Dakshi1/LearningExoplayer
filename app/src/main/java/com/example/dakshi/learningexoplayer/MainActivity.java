package com.example.dakshi.learningexoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    static Context mcontext;
    static ArrayList<String> m_audio_link;
    static SimpleExoPlayerView playerView;
    static SimpleExoPlayer player;
    private static boolean playWhenReady=true;
    static long playbackPosition=0;
    static int currentWindow=0;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView=findViewById(R.id.video_view);
        playerView.setControllerShowTimeoutMs(0);
        playerView.setUseArtwork(false);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Util.SDK_INT > 23) {
                    //initializePlayer();
                    sendText();
                }
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendText() {

        String text="Morne Morkel has provided some long-awaited clarity on his future after confirming that he will be retiring from international cricket at the end of South Africa's Test series against Australia. The 33-year-old fast bowler announced his decision in Durban on Monday (February 26) as the Proteas began their preparation for a four-match series against an opponent that they have never beaten in a Test series on home soil.\n" +
                "\n" +
                "Morkel's future has been the subject of speculation since last August, when he was linked with a Kolpak move. He subsequently admitted that his international career could be cut short unless he was provided assurances about his place at the 2019 World Cup. But while coach Ottis Gibson later said that Morkel was a part of his plans for the tournament, Morkel has now decided to put family first.\n" +
                "\n" +
                "\"It was an extremely tough decision but I feel the time is right to start a new chapter,\" Morkel said. \"I have a young family and a foreign wife, and the current demanding international schedule has put a lot of strain us. I have to put them first and this decision will only benefit us going forward.\"\n" +
                "\n" +
                "Morkel went on to cite the recent separation from his Australian wife and two-year-old son - due to his wife's work commitments - as part of the reason for his decision. \"Playing for the Proteas is something very special. But family comes first. My wife and my family was away for 10 weeks now for work commitments, which was very tricky for me. After sitting them down, and discussing it with family, we decided - I decided - it's better for me to start a chapter with them and focus more on family.\"\n" +
                "\n" +
                "Morkel did not say exactly where that new chapter will take him, but confirmed that he wanted to keep playing cricket at a high level. Morkel went unsold at the IPL auction last month, but a stint in English county cricket would be an appealing prospect. Morkel's family joined him for most of the three months that South Africa spent in England last year.\n" +
                "\n" +
                "\"I still feel great mentally and physically, and yes, I will be playing in other leagues around the world,\" he said. \"Out of respect to CSA I have not put pen to paper on any deal. My focus is 100% on winning this series (against Australia). I'll make a decision once everything is done.\"\n" +
                "\n" +
                "While Morkel has given up on the dream of winning a World Cup, he will still have the opportunity to tick an important box: a Test series win against Australia at home. During his career, South Africa have won series in England and Australia, and drawn a series in India, but the Proteas are yet to beat their fiercest rival in a Test series at home since readmission.\n" +
                "\n" +
                "On an individual level, Morkel will have the opportunity to pass 300 Test wickets. He is currently on 294 after 83 Test matches, and he is likely to remain fifth on South Africa's list of most successful Test bowlers - Allan Donald, who is ahead of him, took 330.\n" +
                "\n" +
                "In 117 ODIs, Morkel claimed 188 wickets at 25.32. Since returning from a serious back injury a year ago, he has become a key member of the ODI side. But the emergence of Lungi Ngidi in both Tests and ODIs suggests that his absence will be covered for, and Morkel expressed his own confidence in the future of South Africa's fast bowling arsenal.\n" +
                "\n" +
                "\"I honestly think the crop of fast bowlers we have in the squad, give them a couple of months under Ottis and in this environment, they'll be more than capable of winning that trophy for us,\" he said of the World Cup. \"Unfortunately things didn't go our way in the white-ball formats against India, but give guys time, give them overs, give them the opportunity to grow.\n" +
                "\n" +
                "\"I'm very positive about and confident in the culture of the senior guys in the squad, that these guys will grow. I leave with a very happy heart. I'm always going to be a phone call away for the younger guys who want to have a chat. I'll always have an open line to them and to the team, and I would like to grow with them in this journey although I'm not going to be in the change room.\"\n" +
                "\n";
        new ClassifyText(MainActivity.this).execute(text);
    }

    public static void initializePlayer(Context context, ArrayList<String> audio_link)
    {
        mcontext=context;
        m_audio_link=audio_link;
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        MediaSource mediaSource = buildMediaSource(audio_link);
        player.prepare(mediaSource, true, false);
    }

    private static MediaSource buildMediaSource(ArrayList<String> audio_link) {

        ExtractorMediaSource audioSource[]=new ExtractorMediaSource[audio_link.size()];
        Uri uri;
        for(int i=0;i<audio_link.size();i++)
        {
            uri=Uri.parse(audio_link.get(i));
            audioSource[i] =
                    new ExtractorMediaSource.Factory(
                            new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                            createMediaSource(uri);
        }

        return new ConcatenatingMediaSource(audioSource);
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
        if ((Util.SDK_INT <= 23 || player == null) && mcontext!=null) {
            initializePlayer(mcontext, m_audio_link);
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
