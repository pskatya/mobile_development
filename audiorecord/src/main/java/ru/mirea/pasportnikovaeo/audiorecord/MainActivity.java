package ru.mirea.pasportnikovaeo.audiorecord;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private boolean isWork = false;
    private String recordFilePath;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    // Новый способ запроса разрешений
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    permissions -> {
                        boolean allGranted = permissions.values().stream()
                                .allMatch(Boolean::booleanValue);
                        isWork = allGranted;

                        if (!allGranted) {
                            Toast.makeText(this,
                                    "Permissions not granted",
                                    Toast.LENGTH_SHORT).show();
                            showPermissionDeniedDialog();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Путь для сохранения записи
        recordFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp").getAbsolutePath();

        // Проверка и запрос разрешений
        checkPermissions();

        Button recordButton = findViewById(R.id.recordButton);
        Button playButton = findViewById(R.id.playButton);
        playButton.setEnabled(false);

        recordButton.setOnClickListener(v -> {
            if (isStartRecording) {
                if (isWork) {
                    startRecording();
                    recordButton.setText("Stop recording");
                    playButton.setEnabled(false);
                    isStartRecording = false;
                } else {
                    showPermissionDeniedDialog();
                }
            } else {
                stopRecording();
                recordButton.setText("Start recording");
                playButton.setEnabled(true);
                isStartRecording = true;
            }
        });

        playButton.setOnClickListener(v -> {
            if (isStartPlaying) {
                startPlaying();
                playButton.setText("Stop playing");
                recordButton.setEnabled(false);
                isStartPlaying = false;
            } else {
                stopPlaying();
                playButton.setText("Start playing");
                recordButton.setEnabled(true);
                isStartPlaying = true;
            }
        });
    }

    private void checkPermissions() {
        String[] requiredPermissions;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+)
            requiredPermissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_MEDIA_AUDIO
            };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10-12 (API 29-32)
            requiredPermissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        } else {
            // Android 9 и ниже (API <= 28)
            requiredPermissions = new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        boolean allGranted = true;
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (allGranted) {
            isWork = true;
        } else {
            requestPermissionLauncher.launch(requiredPermissions);
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission required")
                .setMessage("This app needs audio recording permissions to work properly")
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void startRecording() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(recordFilePath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AudioRecord", "prepare() failed", e);
            Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (IllegalStateException e) {
                Log.e("AudioRecord", "stop failed", e);
            }
            recorder.release();
            recorder = null;
            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPlaying() {
        try {
            player = new MediaPlayer();
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            Toast.makeText(this, "Playing started", Toast.LENGTH_SHORT).show();

            player.setOnCompletionListener(mp -> {
                stopPlaying();
                Button playButton = findViewById(R.id.playButton);
                playButton.setText("Start playing");
                Button recordButton = findViewById(R.id.recordButton);
                recordButton.setEnabled(true);
                isStartPlaying = true;
            });
        } catch (IOException e) {
            Log.e("AudioRecord", "playback failed", e);
            Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Playing stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
        stopPlaying();
    }
}