package ru.mirea.pasportnikovaeo.lesson4;
import android.os.Bundle;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.pasportnikovaeo.lesson4.databinding.ActivityPlayerBinding;
public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupPlayer();
    }

    private void setupPlayer() {
        // Загрузка данных трека
        binding.songTitle.setText("Bohemian Rhapsody");

        // Обработчики кнопок
        binding.btnPlay.setOnClickListener(v -> togglePlayPause());
        binding.btnPrev.setOnClickListener(v -> playPrevious());
        binding.btnNext.setOnClickListener(v -> playNext());

        // Настройка SeekBar
        binding.seekBar.setMax(100);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Перемотка трека
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void togglePlayPause() {
        isPlaying = !isPlaying;
        binding.btnPlay.setImageResource(
                isPlaying ? R.drawable.ic_pause : R.drawable.ic_play
        );
    }

    private void playPrevious() {
        binding.songTitle.setText("Another One Bites the Dust");
    }

    private void playNext() {
        binding.songTitle.setText("We Will Rock You");
    }
}
