package com.ayalus.exoplayer2example;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;



/*
Created by: Ayal Fieldust
Date: 10/2016

Description:
This Example app was created to show a simple example of ExoPlayer Version 2.
There is an option to play mp4 files or live stream.
Exoplayer provides options to play many different formats, so the code can easily be tweaked to play the requested format.
 */

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ExoPlayer.EventListener exoPlayerEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG,"portrait detected...");
        setContentView(R.layout.activity_main);

// 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector( videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

//Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

// Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);



// I. ADJUST HERE:

//CHOOSE CONTENT: Livestream links may be out of date so find any m3u8 files online and replace:

//VIDEO FROM SD CARD: ( 2 steps. set up file and path, then change videoSource to get the file)

//        String urimp4 = "path/FileName.mp4"; //upload file to device and add path/name.mp4
//        Uri mp4VideoUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp4);


//building cam live stream link:
        Uri mp4VideoUri =Uri.parse("https://wowza.jwplayer.com/live/jelly.stream/playlist.m3u8");

//Random indian livestream link (low quality):
//        Uri mp4VideoUri =Uri.parse("http://54.255.155.24:1935//Live/_definst_/amlst:sweetbcha1novD235L240P/playlist.m3u8");




// Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
//Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();



// II. ADJUST HERE:

//This is the MediaSource representing the media to be played:
//FOR SD CARD SOURCE:
//        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

//FOR LIVESTREAM LINK:
        MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

// Prepare the player with the source.
        player.prepare(loopingSource);

        player.addListener(new ExoPlayer.EventListener() {
                               @Override
                               public void onTimelineChanged(Timeline timeline, Object manifest) {
                                Log.v(TAG,"Listener-onTimelineChanged...");

                               }

                               @Override
                               public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                                   Log.v(TAG,"Listener-onTracksChanged...");

                               }

                               @Override
                               public void onLoadingChanged(boolean isLoading) {
                                   Log.v(TAG,"Listener-onLoadingChanged...");

                               }

                               @Override
                               public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                   Log.v(TAG,"Listener-onPlayerStateChanged...");

                               }

                               @Override
                               public void onPlayerError(ExoPlaybackException error) {
                                   Log.v(TAG,"Listener-onPlayerError...");
                                   player.stop();
                                   player.prepare(loopingSource);
                                   player.setPlayWhenReady(true);

                               }

                               @Override
                               public void onPositionDiscontinuity() {
                                   Log.v(TAG,"Listener-onPositionDiscontinuity...");

                               }
                           });

        player.setPlayWhenReady(true); //run file/link when ready to play.


    }//End of onCreate




    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart()...");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy()...");
        player.release();

    }

}

