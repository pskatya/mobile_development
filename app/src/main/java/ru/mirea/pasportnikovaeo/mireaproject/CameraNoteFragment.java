package ru.mirea.pasportnikovaeo.mireaproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraNoteFragment extends Fragment {
    private static final String TAG = "CameraNoteFragment";
    private String currentPhotoPath;

    // Views
    private ImageView imageView;
    private EditText noteEditText;
    private Button saveButton;

    // Activity result launchers (modern replacement for startActivityForResult)
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    displayCapturedImage();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    handleGalleryImage(result.getData());
                }
            });

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists() && !storageDir.mkdirs()) {
            Log.e(TAG, "Failed to create directory");
            throw new IOException("Failed to create image directory");
        }

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_note, container, false);

        imageView = view.findViewById(R.id.image_view);
        noteEditText = view.findViewById(R.id.note_edit_text);
        saveButton = view.findViewById(R.id.save_button);
        Button takePhotoButton = view.findViewById(R.id.take_photo_button);

        saveButton.setEnabled(false);

        takePhotoButton.setOnClickListener(v -> dispatchTakePictureIntent());
        saveButton.setOnClickListener(v -> saveNoteWithPhoto());

        return view;
    }

    private void dispatchTakePictureIntent() {
        try {
            if (!requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                Toast.makeText(requireContext(), "Камера недоступна", Toast.LENGTH_LONG).show();
                return;
            }

            File photoFile = createImageFile();
            if (photoFile == null) {
                Toast.makeText(requireContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getApplicationContext().getPackageName() + ".fileprovider",
                    photoFile
            );

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                takePictureLauncher.launch(takePictureIntent);
            } else {
                showCameraAppNotFoundDialog();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка камеры: " + e.getMessage());
            Toast.makeText(requireContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayCapturedImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            saveButton.setEnabled(true);
        } else {
            Toast.makeText(requireContext(), "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleGalleryImage(Intent data) {
        Uri selectedImage = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
            imageView.setImageBitmap(bitmap);
            saveButton.setEnabled(true);
            currentPhotoPath = selectedImage.toString();
        } catch (IOException e) {
            Log.e(TAG, "Ошибка загрузки изображения: " + e.getMessage());
            Toast.makeText(requireContext(), "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNoteWithPhoto() {
        String noteText = noteEditText.getText().toString().trim();
        if (noteText.isEmpty()) {
            Toast.makeText(requireContext(), "Введите текст заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPhotoPath == null) {
            Toast.makeText(requireContext(), "Сначала сделайте фото", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(requireContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();
        resetForm();
    }

    private void resetForm() {
        imageView.setImageResource(0);
        noteEditText.setText("");
        currentPhotoPath = null;
        saveButton.setEnabled(false);
    }

    private void showCameraAppNotFoundDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Камера не найдена")
                .setMessage("Установить приложение камеры или выбрать фото из галереи?")
                .setPositiveButton("Установить", (dialog, which) -> openPlayStoreForCamera())
                .setNegativeButton("Галерея", (dialog, which) -> openGallery())
                .setNeutralButton("Отмена", null)
                .show();
    }

    private void openPlayStoreForCamera() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.google.android.GoogleCamera")));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.GoogleCamera")));
        }
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhoto.resolveActivity(requireActivity().getPackageManager()) != null) {
            pickImageLauncher.launch(pickPhoto);
        } else {
            Toast.makeText(requireContext(), "Приложение галереи не найдено", Toast.LENGTH_SHORT).show();
        }
    }
}