package com.project.an;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private Uri videoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplayer);
        //get video view by id
        videoView = findViewById(R.id.playVideo);
        //set media controllers
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        Intent intent = getIntent();
        // Set video url  for video view
        if(intent != null && intent.hasExtra("VIDEO_URI")) {
            videoUri = Uri.parse(intent.getStringExtra("VIDEO_URI"));
            Log.i("VIDEO_VIEW_TAG", "Video URI: " + videoUri);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }

    }

}
