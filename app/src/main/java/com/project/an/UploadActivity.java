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
import android.widget.VideoView;

public class UploadActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int VIDEO_RECORD_CODE = 101;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //chek for a camera
        if (isCameraAvailable()) {
            Log.i("VIDEO_RECORD_TAG", "Camera is available");
            getCameraPermission();
        } else {
            Log.i("VIDEO_RECORD_TAG", "Camera is not available");
        }


    }

    //check availability of a camera
    private boolean isCameraAvailable() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    //get camera permission
    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_RECORD_CODE) {
            if (resultCode == RESULT_OK) {
                videoUri = data.getData();
                Intent intent = new Intent(this, PlayVideoActivity.class);
                intent.putExtra("VIDEO_URI", videoUri.toString());
                startActivity(intent);
                Log.i("VIDEO_RECORD_TAG", "Video saved to: " + videoUri.toString());

            } else if (resultCode == RESULT_CANCELED) {
                Log.i("VIDEO_RECORD_TAG", "Video recording cancelled");
            } else {
                Log.i("VIDEO_RECORD_TAG", "Failed to record video");
            }
        }
    }


    //    ======================================================
    public void capturebtnPressed(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }
}