package ru.mirea.pasportnikovaeo.mireaproject;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VoiceNoteFragment extends Fragment {
    private MediaRecorder mediaRecorder;
    private String outputFile;
    private Button recordButton;
    private Button playButton;
    private Button saveButton;
    private TextView statusText;
    private EditText noteEditText;
    private LinearLayout notesContainer;

    private boolean isRecording = false;
    private boolean isPlaying = false;
    private MediaPlayer mediaPlayer;
    private List<VoiceNote> voiceNotes = new ArrayList<>();

    private static class VoiceNote {
        String title;
        String filePath;
        String date;

        VoiceNote(String title, String filePath, String date) {
            this.title = title;
            this.filePath = filePath;
            this.date = date;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voice_note, container, false);

        // Инициализация UI элементов
        recordButton = rootView.findViewById(R.id.record_button);
        playButton = rootView.findViewById(R.id.play_button);
        saveButton = rootView.findViewById(R.id.save_button);
        statusText = rootView.findViewById(R.id.status_text);
        noteEditText = rootView.findViewById(R.id.note_edit_text);
        notesContainer = rootView.findViewById(R.id.notes_container);

        // Создаем папку для записей
        File recordingsDir = new File(requireContext().getExternalFilesDir(null), "Recordings");
        if (!recordingsDir.exists()) {
            recordingsDir.mkdirs();
        }

        recordButton.setOnClickListener(v -> toggleRecording());
        playButton.setOnClickListener(v -> togglePlayback());
        saveButton.setOnClickListener(v -> saveVoiceNote());

        return rootView;
    }

    private void toggleRecording() {
        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "Recording_" + timeStamp + ".3gp";
            outputFile = new File(requireContext().getExternalFilesDir("Recordings"), fileName).getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;
            recordButton.setText("Остановить запись");
            statusText.setText("Запись...");
            playButton.setEnabled(false);
            saveButton.setEnabled(false);
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Ошибка записи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        isRecording = false;
        recordButton.setText("Начать запись");
        statusText.setText("Запись завершена");
        playButton.setEnabled(true);
        saveButton.setEnabled(true);
    }

    private void togglePlayback() {
        if (!isPlaying) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();

            isPlaying = true;
            playButton.setText("Остановить");
            statusText.setText("Воспроизведение...");
            recordButton.setEnabled(false);

            mediaPlayer.setOnCompletionListener(mp -> stopPlaying());
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        isPlaying = false;
        playButton.setText("Воспроизвести");
        statusText.setText("Готово");
        recordButton.setEnabled(true);
    }

    private void saveVoiceNote() {
        String noteText = noteEditText.getText().toString().trim();
        if (noteText.isEmpty()) {
            Toast.makeText(requireContext(), "Введите описание заметки", Toast.LENGTH_SHORT).show();
            return;
        }

        if (outputFile == null) {
            Toast.makeText(requireContext(), "Сначала сделайте запись", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date());
        VoiceNote newNote = new VoiceNote(noteText, outputFile, date);
        voiceNotes.add(newNote);
        addNoteToView(newNote);

        noteEditText.setText("");
        outputFile = null;
        statusText.setText("Заметка сохранена");
        playButton.setEnabled(false);

        Toast.makeText(requireContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();
    }

    private void addNoteToView(VoiceNote note) {
        View noteView = LayoutInflater.from(requireContext()).inflate(R.layout.item_voice_note_simple, notesContainer, false);

        TextView titleView = noteView.findViewById(R.id.note_title);
        TextView dateView = noteView.findViewById(R.id.note_date);
        Button playNoteButton = noteView.findViewById(R.id.play_note_button);

        titleView.setText(note.title);
        dateView.setText(note.date);

        playNoteButton.setOnClickListener(v -> playSavedNote(note.filePath));

        notesContainer.addView(noteView);
    }

    private void playSavedNote(String filePath) {
        stopPlaying();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

            isPlaying = true;
            statusText.setText("Воспроизведение сохраненной записи...");

            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                statusText.setText("Готово");
            });
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRecording();
        stopPlaying();
    }
}