package com.namghjk.dailyselfie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Adapter.FileAdapter;
import Model.File;
import Notification.ReminderBoardCast;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID ="CHANEL_1" ;
    ListView lvImage;
    FileAdapter fileadapter;
    ArrayList<File> fileArrayList = new ArrayList<>();
    java.io.File[] files;
    public static final int CAMERA_REQUEST_CODE = 102;
    private String dirPath = "/storage/emulated/0/Android/data/com.namghjk.dailyselfie/files/Pictures/";
    private String currentPhotoPath;
    public static final int TIME_PUSH_NOTIFI = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChanel();
        CheckPermission();
        addControls();
        getFileInDir();
        addEvents();
    }

    private void addEvents() {
        lvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowImage(i);
            }
        });
    }

    private void addControls() {
        lvImage = findViewById(R.id.lvfile);
        getFileInDir();
        fileadapter = new FileAdapter(MainActivity.this,R.layout.listview_image,fileArrayList);
        lvImage.setAdapter(fileadapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        pushNoti();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pushNoti();
        startService(new Intent(this, ReminderBoardCast.class));
    }

    private void CheckPermission(){
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int readPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);


            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED ||
                    cameraPermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.camera1:
            dispatchTakePictureIntent();
            break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getFileInDir(){
        files = new java.io.File[]{};
        java.io.File directory = new java.io.File(dirPath);
        if(directory.exists()){
            files = directory.listFiles();
            fileArrayList.clear();
            if(files.length > 0){
                for (int i = 0; i < files.length; i++) {
                    fileArrayList.add(new File(files[i].getName(), files[i].getAbsolutePath()));
                }
            }
        }
    }

    private void ShowImage(int i){
        Intent intent = new Intent(MainActivity.this,showImage_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filename",fileArrayList.get(i).getFileName());
        bundle.putString("filepath",fileArrayList.get(i).getFilePath());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            getFileInDir();
            fileadapter.notifyDataSetChanged();
        }
    }

    private java.io.File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        java.io.File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        java.io.File image = java.io.File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        currentPhotoPath = image.getAbsolutePath();


        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            java.io.File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }

        }
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifyAppSelfyChannel";
            String des = "Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("notifyAppSelfy", name, importance);
            notificationChannel.setDescription(des);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void pushNoti() {
        Intent intent = new Intent(MainActivity.this, ReminderBoardCast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (TIME_PUSH_NOTIFI * 1000), pendingIntent);
    }


}