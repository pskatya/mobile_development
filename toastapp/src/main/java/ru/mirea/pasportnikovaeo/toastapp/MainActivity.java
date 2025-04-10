package ru.mirea.pasportnikovaeo.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText inputField = findViewById(R.id.inputField);
        Button countButton = findViewById(R.id.countButton);

        countButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = inputField.getText().toString();
                int charCount = inputText.length();

                String toastMessage = String.format(
                        "СТУДЕНТ № Х ГРУППА Х Количество символов - %d",
                        charCount
                );

                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        toastMessage,
                        Toast.LENGTH_SHORT
                );
                toast.show();
            }
        });
    }
}