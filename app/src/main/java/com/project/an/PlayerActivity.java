package com.project.an;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.StyledPlayerView;
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
        try {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setContentView(R.layout.activity_player);
            // This is the URL for the video
            String videoURL = getIntent().getStringExtra("VIDEO_URL");
            String videoName = getIntent().getStringExtra("VIDEO_TITLE");
            // This is the extra to check whether this is a recorded video
            StyledPlayerView playerView = findViewById(R.id.video_player_view);
            playerView.setControllerShowTimeoutMs(3000);

            btnVideoQuality = findViewById(R.id.video_quality);
            btnVideoQuality.setOnClickListener(this);
            videoTitle = findViewById(R.id.video_title);

            videoTitle.setText(videoName);

            TrackSelector trackSelector = new DefaultTrackSelector();

            // Initializes ExoPlayer
            player = new ExoPlayer.Builder(this)
                    .setTrackSelector(trackSelector)
                    .build();
            playerView.setPlayer(player);

            MediaItem mediaItem;

            // Builds media item from MPD
            mediaItem = new MediaItem.Builder()
                    .setUri(videoURL)
                    .setMimeType(MimeTypes.APPLICATION_MPD).build();

            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
            handler = new Handler();
            runnable = () -> {
                btnVideoQuality.setVisibility(View.GONE);
                videoTitle.setVisibility(View.GONE);
            };
            startHandler();
        }catch(Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    private void startHandler(){
        handler.postDelayed(runnable, 3000);
    }

    private void stopHandler(){
        handler.removeCallbacks(runnable);
    }

    // Show title and video quality change button at a user interaction
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

    // Show Track selection dialog on click.
    @Override
    public void onClick(View view) {
        if (view == btnVideoQuality && !isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(player)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForPlayer(
                            player,
                            dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), null);
        }
    }
}