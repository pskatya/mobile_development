package ru.mirea.pasportnikovaeo.simplefragmentapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment1, fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        View rootView = findViewById(getRootViewId());
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        fragment1 = new FirstFragment();
        fragment2 = new SecondFragment();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setupButtons();
        }
    }

    private int getRootViewId() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                ? R.id.main_land
                : R.id.main;
    }

    private void setupButtons() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Button btnFirstFragment = findViewById(R.id.btnFirstFragment);
        btnFirstFragment.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment1)
                    .commit();
        });

        Button btnSecondFragment = findViewById(R.id.btnSecondFragment);
        btnSecondFragment.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment2)
                    .commit();
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recreate();
    }
}