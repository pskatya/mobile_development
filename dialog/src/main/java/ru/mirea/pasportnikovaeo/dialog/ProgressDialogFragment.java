package ru.mirea.pasportnikovaeo.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {

    private ProgressDialog progressDialog;
    private Handler handler;
    private Runnable runnable;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);

        startProgress();

        return progressDialog;
    }

    private void startProgress() {
        handler = new Handler();
        runnable = new Runnable() {
            int progress = 0;
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 5;
                    progressDialog.setProgress(progress);
                    handler.postDelayed(this, 200);
                } else {
                    progressDialog.dismiss();
                }
            }
        };
        handler.postDelayed(runnable, 200);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}