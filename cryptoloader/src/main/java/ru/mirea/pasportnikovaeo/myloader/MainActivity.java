package ru.mirea.pasportnikovaeo.myloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.InvalidParameterException;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 1234;
    private EditText editText;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        secretKey = AESUtils.generateKey();
    }

    public void onClickButton(View view) {
        String inputText = editText.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Введите текст для шифрования", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] encryptedText = AESUtils.encryptMsg(inputText, secretKey);

        Bundle bundle = new Bundle();
        bundle.putByteArray(MyLoader.ARG_ENCRYPTED, encryptedText);
        bundle.putByteArray(MyLoader.ARG_KEY, secretKey.getEncoded());

        LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Очистка ресурсов при необходимости
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == LoaderID) {
            return new MyLoader(this, bundle);
        }
        throw new InvalidParameterException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String decryptedText) {
        if (loader.getId() == LoaderID && decryptedText != null) {
            Toast.makeText(this, "Расшифрованный текст: " + decryptedText,
                    Toast.LENGTH_LONG).show();
        }
    }
}