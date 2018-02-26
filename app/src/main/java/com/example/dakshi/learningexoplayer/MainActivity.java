package com.example.dakshi.learningexoplayer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    private boolean playWhenReady=true;
    long playbackPosition=0;
    int currentWindow=0;
    Button button;
    String urls[]=new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urls[0]="https://firebasestorage.googleapis.com/v0/b/socialhitch-85f9b.appspot.com/o/Daru%20Badnaam-(Mr-Jatt.com).mp3?alt=media&token=199700d1-63da-46eb-8fdd-cc86b7e35e7f";
        urls[1]="https://firebasestorage.googleapis.com/v0/b/socialhitch-85f9b.appspot.com/o/Aaj%20Se%20Teri%20%20Padman_(SwagyJatt.CoM).mp3?alt=media&token=d66076ce-fea0-4c55-81e8-95331e3b0c59";
        urls[2]="https://firebasestorage.googleapis.com/v0/b/socialhitch-85f9b.appspot.com/o/Raghupati%20Raghav%20Raja%20Ram%20Instrumental%20Piano-(Mr-Jatt.com).mp3?alt=media&token=1d2efe83-0088-41f7-85b0-d7537979b43e";
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



        String text="There once was a speedy Hare who bragged about how fast he could run. Tired of hearing him boast, the Tortoise challenged him to a race. All the animals in the forest gathered to watch. The Hare ran down the road for a while and then paused to rest. He looked back at the tortoise and cried out, How do you expect to win this race when you are walking along at your slow, slow pace? The Hare stretched himself out alongside the road and fell asleep, thinking, \"There is plenty of time to relax.The Tortoise walked and walked, never ever stopping until he came to the finish line. The animals who were watching cheered so loudly for Tortoise that they woke up the Hare. The Hare stretched, yawned and began to run again, but it was too late. Tortoise had already crossed the finish line.";
        new ClassifyText().execute(text);
    }

    private void initializePlayer()
    {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(getString(R.string.media_url_mp3));
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        /*return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);*/

        // these are reused for both media sources we create below
/*        DefaultExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory =
                new DefaultHttpDataSourceFactory( "user-agent");

        ExtractorMediaSource videoSource =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);

        Uri audioUri = Uri.parse(getString(R.string.media_url_mp3));*/
        ExtractorMediaSource audioSource[]=new ExtractorMediaSource[3];

               audioSource[0] =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);
        uri = Uri.parse(getString(R.string.media_url1_mp3));
        audioSource[1] =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);

        uri = Uri.parse(getString(R.string.media_url2_mp3));
        audioSource[2] =
                new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);
        return new ConcatenatingMediaSource(audioSource);


       // new ConcatenatingMediaSource(audioSource,audioSource,audioSource,audioSource,audioSource);
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
        /*if (Util.SDK_INT > 23) {
            initializePlayer();
        }*/
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
        /*if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }*/
    }
    /*@SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }*/

    private class ClassifyText extends AsyncTask<String, Void, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("text to classify",strings[0]);

            String url = "http://api.meaningcloud.com/class-1.1?key=2943dd044c63d6125b8f02ed76803e43&txt="+strings[0]+"&model=IPTC_en";
            /*
            OkHttpClient client = new OkHttpClient();
            //MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            //RequestBody body = RequestBody.create(mediaType, "key=2943dd044c63d6125b8f02ed76803e43&txt=" + strings[0] + "&model=IPTC_en");
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
                Log.d("response of text api",response.toString());
                JSONObject jsonObject=new JSONObject(response.toString());
                Log.d("output",jsonObject+"");
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            StringRequest stringRequest=new StringRequest(url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response volley",response.toString());
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
