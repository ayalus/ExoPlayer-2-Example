package com.ayalus.exoplayer2example;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;



/*
Created by: Ayal Fieldust
Date: 8/2017

Description:
This Example app was created to show a simple example of ExoPlayer Version 2.5.1.
There is an option to play mp4 files or live stream content.
Exoplayer provides options to play many different formats, so the code can easily be tweaked to play the requested format.
scroll down to "ADJUST HERE:" I & II to change between sources.
 */

public class MainActivity extends AppCompatActivity implements VideoRendererEventListener {


    private static final String TAG = "MainActivity";
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private TextView resolutionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resolutionTextView = new TextView(this);
        resolutionTextView = (TextView) findViewById(R.id.resolution_textView);

//// I. ADJUST HERE:
////CHOOSE CONTENT: LiveStream / SdCard
//
////LIVE STREAM SOURCE: * Livestream links may be out of date so find any m3u8 files online and replace:
//
////        Uri mp4VideoUri =Uri.parse("http://81.7.13.162/hls/ss1/index.m3u8"); //random 720p source
////        Uri mp4VideoUri =Uri.parse("http://54.255.155.24:1935//Live/_definst_/amlst:sweetbcha1novD235L240P/playlist.m3u8"); //Radnom 540p indian channel
//        Uri mp4VideoUri =Uri.parse("http://cbsnewshd-lh.akamaihd.net/i/CBSNHD_7@199302/index_700_av-p.m3u8"); //CNBC
        Uri mp4VideoUri =Uri.parse("http://live.field59.com/wwsb/ngrp:wwsb1_all/playlist.m3u8"); //ABC NEWS
////        Uri mp4VideoUri =Uri.parse("FIND A WORKING LINK ABD PLUg INTO HERE"); //PLUG INTO HERE<------------------------------------------
//
//
////VIDEO FROM SD CARD: (2 steps. set up file and path, then change videoSource to get the file)
////        String urimp4 = "path/FileName.mp4"; //upload file to device and add path/name.mp4
////        Uri mp4VideoUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+urimp4);


        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //test

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

        int h = simpleExoPlayerView.getResources().getConfiguration().screenHeightDp;
        int w = simpleExoPlayerView.getResources().getConfiguration().screenWidthDp;
        Log.v(TAG, "height : " + h + " weight: " + w);
        ////Set media controller
        simpleExoPlayerView.setUseController(false);//set to true or false to see controllers
        simpleExoPlayerView.requestFocus();
        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(liveStreamUri);

        //// II. ADJUST HERE:

        ////        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
        ////Produces Extractor instances for parsing the media data.
        //        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        //This is the MediaSource representing the media to be played:
        //FOR SD CARD SOURCE:
        //        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

        //FOR LIVESTREAM LINK:
        MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.addListener(new ExoPlayer.EventListener() {


            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged... ");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState+"|||isDrawingCacheEnabled():"+simpleExoPlayerView.isDrawingCacheEnabled());
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                player.stop();
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.setVideoDebugListener(this);
    }



    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged [" + " width: " + width + " height: " + height + "]");
        resolutionTextView.setText("RES:(WxH):" + width + "X" + height + "\n           " + height + "p");//shows video info
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }
//-------------------------------------------------------ANDROID LIFECYCLE---------------------------------------------------------------------------------------------

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        player.release();
    }
}
