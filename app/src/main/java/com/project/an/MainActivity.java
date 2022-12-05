package com.project.an;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int VIDEO_RECORD_CODE = 101;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get camera permission
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Log.i("VIDEO_RECORD_TAG", "Camera is available");
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        }else {
            Log.i("VIDEO_RECORD_TAG", "Camera is not available");
        }
    }


    //if upload and capture button pressed, start video capture
    public void uploadbtnPress(View view) {
        Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(captureIntent, VIDEO_RECORD_CODE);

    }

    //if watch videos button pressed, load playlist
    public void watchbtnPress(View view) {
        Intent watchVidsIntent = new Intent(this, WatchVideosActivity.class);
        startActivity(watchVidsIntent);
    }

    //redirect to video uploadig form after successfully captured a video.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_RECORD_CODE) {
            if (resultCode == RESULT_OK) {
                //upload part
                assert data != null;
                videoUri = data.getData();
                Log.i("MY_VIDEO_URI", videoUri.toString());
                Intent uploadVideosIntent = new Intent(this, UploadActivity.class);
                uploadVideosIntent.putExtra("VIDEO_URL", videoUri.toString());
                startActivity(uploadVideosIntent);
                finish();
                Log.i("RESULT_OK", "onActivityResult: Video captured");
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("VIDEO_RECORD_TAG", "Recording cancelled");
            } else {
                Log.i("VIDEO_RECORD_TAG", "Recording Failed");
            }
        }
    }
}