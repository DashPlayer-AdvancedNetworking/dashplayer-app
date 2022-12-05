package com.project.an;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadActivity extends AppCompatActivity {

    private Button btnUpload;
    private Button btnCancel;
    private MaterialAutoCompleteTextView formVideoTitle;
    private MaterialAutoCompleteTextView formVideoDescription;
    private String videoUri;
    private String mediaFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //set up upload and cancel buttons
        btnUpload = findViewById(R.id.btnUploadVideo);
        btnCancel = findViewById(R.id.btnCancelVideo);

        //redirect to main page if cancel button is pressed
        btnCancel.setOnClickListener(
                view -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        );

        //save in android/media/com.project.<app name>
        mediaFolder = createFolder(this);
        Log.i("FOLDER", mediaFolder);

        //get title and description for captured video
        formVideoTitle = findViewById(R.id.formVideoTitle);
        formVideoDescription = findViewById(R.id.formVideoDescription);

        // Get URL for the video
        videoUri = Uri.parse(getIntent().getStringExtra("VIDEO_URL")).toString();

        String videoRecordTime = getIntent().getStringExtra("VIDEO_RECORD_TIME");

        //send to server if upload button is pressed. then redirect to main page
        btnUpload.setOnClickListener(
                view -> {
                    verifyStoragePermissions(this);
                    Log.i("STORAGE_PERMISSION", "Got permission to store");
                    UploadVideoFile uploadVideoFile = new UploadVideoFile();
                    uploadVideoFile.execute();
                    storeMedia(Uri.parse(videoUri));

                    Intent intent = new Intent(this, MainActivity.class);
//                    player.stop();
                    startActivity(intent);
                    finish();
                }
        );


    }

    protected static String createFolder(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            File[] directories = new File[0];
            directories = context.getExternalMediaDirs();


            for (int i = 0; i < directories.length; i++) {
                if (directories[i].getName().contains(context.getPackageName())) {
                    return directories[i].getAbsolutePath();
                }
            }

        }

        return null;
    }

    //save video in local storage
    private void storeMedia(Uri videoUri) {
        String saveFolder = mediaFolder + "/Video.mp4";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            saveFolder = mediaFolder + "/" + formVideoTitle.getText().toString() + ".mp4";
        }

        Log.i("SAVE_LOCATION", saveFolder);

        try {
            InputStream in = getContentResolver().openInputStream(videoUri);
            UploadActivity.createFileFromInputStream(in, saveFolder);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static File createFileFromInputStream(InputStream inputStream, String fileName) {

        try {
            File f = new File(fileName);
            f.setWritable(true, false);
            OutputStream outputStream = new FileOutputStream(f);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            System.out.println("error in creating a file");
            e.printStackTrace();
        }

        return null;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private class UploadVideoFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            videoUri = getRealPathFromURI(Uri.parse(videoUri));

            Log.i("MY_VIDEO_PATH", videoUri);

            File video_source = new File(videoUri);

            Map<String, RequestBody> map = new HashMap<>();
            map.put("title", RequestBody.create(MediaType.parse("text/plain"), formVideoTitle.getText().toString()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                map.put("description", RequestBody.create(MediaType.parse("text/plain"), LocalDate.now().toString() + "\n" + formVideoDescription.getText().toString()));
            }

            try {
                RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), video_source);
                MultipartBody.Part partImage = MultipartBody.Part.createFormData("video", video_source.getName(), reqBody);
                API api = RetrofitClient.getInstance().getAPI();
                Call<ResponseBody> upload = api.uploadVideo(partImage, map);;
                upload.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Upload Complete", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("error", response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.e("error", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                Log.e("error", "URL error: " + e.getMessage(), e);
            }

            return null;
        }
    }
}

