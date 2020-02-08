package com.sarthak.exoplayerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    SimpleExoPlayer simpleExoPlayer;
    Boolean isPlaying;
    SeekBar seekBar;
    TextView time,duration;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isPlaying = false;

        seekBar = findViewById(R.id.seekBar);
        time = findViewById(R.id.timee);
        duration = findViewById(R.id.durationnn);
        setupPlayer();
        setTime();
        final Button button = findViewById(R.id.btn_pauseee);
        ImageButton button1 = findViewById(R.id.btn_ffwddd);
        ImageButton button2 = findViewById(R.id.btn_prev);

        handler = new Handler();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 5000);
                seekBar.setProgress(Integer.parseInt(String.valueOf(simpleExoPlayer.getCurrentPosition() + 5000)));
                seekBar.setMax(Integer.parseInt(String.valueOf(simpleExoPlayer.getDuration())));
                changeSeekBar();
                setTime();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 5000);
                seekBar.setProgress(Integer.parseInt(String.valueOf(simpleExoPlayer.getCurrentPosition() - 5000)));
                seekBar.setMax(Integer.parseInt(String.valueOf(simpleExoPlayer.getDuration())));
                changeSeekBar();
                setTime();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(Integer.parseInt(String.valueOf(simpleExoPlayer.getDuration())));
                changeSeekBar();
                setTime();
                if(!isPlaying){

                    simpleExoPlayer.setPlayWhenReady(true);
                    isPlaying = true;
                    button.setText("Pause");
                    seekBar.setProgress(Integer.parseInt(String.valueOf(simpleExoPlayer.getCurrentPosition())));

                }
                else if(isPlaying){
                    simpleExoPlayer.setPlayWhenReady(false);
                    //simpleExoPlayer.release();
                    button.setText("Play");
                    isPlaying = false;
                    seekBar.setProgress(Integer.parseInt(String.valueOf(simpleExoPlayer.getCurrentPosition())));

                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    simpleExoPlayer.seekTo(progress);
                    setTime();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void setupPlayer(){

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,null,DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        //BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Global

        TrackSelector trackSelector = new DefaultTrackSelector();//(new AdaptiveTrackSelection.Factory(renderersFactory));//global

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory,trackSelector); //Global

        //final DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_audio");//Global
        //final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();//Global
        //MediaSource mediaSource = new ExtractorMediaSource(uri, httpDataSourceFactory, extractorsFactory, null, null);//Local

        Uri uri = Uri.parse("http://awscdn.podcasts.com/Max-Weber-Authority-Power-Theory-dc90.mp3");
        String userAgent = Util.getUserAgent(this,"Sunokitaab");
        ExtractorMediaSource mediaSource =new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(this,userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);

        simpleExoPlayer.prepare(mediaSource);//Local
        simpleExoPlayer.setPlayWhenReady(true);//Local
    }

    @Override
    protected void onDestroy() {
        simpleExoPlayer.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
       // simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.release();
        super.onPause();
    }
    private void setTime() {

        if(simpleExoPlayer!=null) {


            long songCurrentTime = simpleExoPlayer.getCurrentPosition();
            long songTotalTime = simpleExoPlayer.getDuration();

            long secondT = (songCurrentTime / 1000) % 60;
            long minuteT = (songCurrentTime / (1000 * 60)) % 60;

            long secondD = (songTotalTime / 1000) % 60;
            long minuteD = (songTotalTime / (1000 * 60)) % 60;

            String timerD = String.format("%02d:%02d", minuteD, secondD);
            String timerT = String.format("%02d:%02d", minuteT, secondT);


            time.setText("" + String.valueOf(timerT));
            duration.setText("" + String.valueOf(timerD));
        }

    }

    private void changeSeekBar() {


        if(simpleExoPlayer!=null){
            seekBar.setProgress(Integer.parseInt(String.valueOf(simpleExoPlayer.getCurrentPosition())));
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekBar();
                    setTime();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }
}
