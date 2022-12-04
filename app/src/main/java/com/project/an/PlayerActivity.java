package com.project.an;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.MimeTypes;

public class PlayerActivity extends AppCompatActivity
        implements View.OnClickListener{


    ExoPlayer player;
    ImageButton btnVideoQuality;
    TextView videoTitle;

    Handler handler;
    Runnable runnable;

    private boolean isShowingTrackSelectionDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
            Log.i("aemond", "In player activity");
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            Log.i("aemond", "Before set content");
            setContentView(R.layout.activity_player);
            Log.i("aemond", "Content is set");
            // This is the URL for the video
            String videoURL = getIntent().getStringExtra("VIDEO_URL");
            String videoName = getIntent().getStringExtra("VIDEO_TITLE");
            Log.i("aemond", videoURL);
            Log.i("aemond", videoName);
            // This is the extra to check whether this is a recorded video
            int isRecorded = getIntent().getIntExtra("IS_MP4", 0);
            Log.i("aemond", "Recorded " + isRecorded);
            StyledPlayerView playerView = findViewById(R.id.video_player_view);
            playerView.setControllerShowTimeoutMs(3000);

            btnVideoQuality = findViewById(R.id.video_quality);
            btnVideoQuality.setOnClickListener(this);
            videoTitle = findViewById(R.id.video_title);

            videoTitle.setText(videoName);
            Log.i("aemond", "Video title set");


            TrackSelector trackSelector = new DefaultTrackSelector();

            player = new ExoPlayer.Builder(this)
                    .setTrackSelector(trackSelector)
                    .build();
            Log.i("aemond", "Player Initialized");
            playerView.setPlayer(player);


            MediaItem mediaItem;

            if (isRecorded == 0) {
                // TODO: TAG -> playlist manifest type .MPD or .M3U8
                mediaItem = new MediaItem.Builder()
                        .setUri(videoURL)
                        .setMimeType(MimeTypes.APPLICATION_MPD).build();
                Log.i("aemond", "In recorded");
            } else {

                // Code to set video title

                // TODO: TAG -> playlist manifest type .MPD or .M3U8
                mediaItem = new MediaItem.Builder()
                        .setUri(videoURL)
                        .build();
                Log.i("aemond", "In not recorded");

            }

            player.setMediaItem(mediaItem);
            Log.i("aemond", "Media item set");
            player.prepare();
            Log.i("aemond", "Player prepared");
            player.setPlayWhenReady(true);
            Log.i("aemond", "Set play when ready");
            handler = new Handler();
            runnable = () -> {
                btnVideoQuality.setVisibility(View.GONE);
                videoTitle.setVisibility(View.GONE);
            };
            Log.i("aemond", "Runnable");
            startHandler();
            Log.i("aemond", "Start habdler");
//        }catch(Exception e){
//            Log.e("aemond", e.getMessage());
//        }
    }

    private void startHandler(){
        handler.postDelayed(runnable, 3000);
    }

    private void stopHandler(){
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        videoTitle.setVisibility(View.VISIBLE);
        btnVideoQuality.setVisibility(View.VISIBLE);
        stopHandler();
        startHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }

    @Override
    public void onClick(View view) {
        if (view == btnVideoQuality && !isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(player)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForPlayer(
                            player,
                            /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
        }
    }
}