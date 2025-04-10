package ru.mirea.pasportnikovaeo.favoruitebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    private EditText bookTitleEditText;
    private EditText bookQuoteEditText;
    private TextView receivedDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Получение данных из MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            receivedDataTextView = findViewById(R.id.textViewReceivedData);
            String bookName = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quote = extras.getString(MainActivity.QUOTES_KEY);
            receivedDataTextView.setText(String.format(
                    "Полученные данные:\nКнига: %s\nЦитата: %s",
                    bookName, quote));
        }

        bookTitleEditText = findViewById(R.id.bookTitleEditText);
        bookQuoteEditText = findViewById(R.id.bookQuoteEditText);
        Button sendButton = findViewById(R.id.sendDataButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookTitle = bookTitleEditText.getText().toString();
                String quote = bookQuoteEditText.getText().toString();
                String resultText = String.format(
                        "Название Вашей любимой книги: %s. Цитата: %s",
                        bookTitle, quote);

                Intent data = new Intent();
                data.putExtra(MainActivity.USER_MESSAGE, resultText);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }
}