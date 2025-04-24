package ru.mirea.pasportnikovaeo.myloader;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_ENCRYPTED = "encrypted";
    public static final String ARG_KEY = "key";

    private final Bundle args;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (args == null) return null;

        byte[] encryptedText = args.getByteArray(ARG_ENCRYPTED);
        byte[] keyBytes = args.getByteArray(ARG_KEY);

        if (encryptedText == null || keyBytes == null) return null;

        SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        return AESUtils.decryptMsg(encryptedText, originalKey);
    }
}