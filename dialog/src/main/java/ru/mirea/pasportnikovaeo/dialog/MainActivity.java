package ru.mirea.pasportnikovaeo.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // AlertDialog
    public void onClickShowDialog(View view) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "alert");
    }

    public void onOkClicked() {
        showToast("Вы выбрали \"Иду дальше\"!");
    }

    public void onCancelClicked() {
        showToast("Вы выбрали \"Нет\"!");
    }

    public void onNeutralClicked() {
        showToast("Вы выбрали \"На паузе\"!");
    }

    // TimePickerDialog
    public void onClickShowTimeDialog(View view) {
        TimeDialogFragment timeDialog = new TimeDialogFragment();
        timeDialog.show(getSupportFragmentManager(), "time");
    }

    public void onTimeSet(int hour, int minute) {
        showToast("Выбрано время: " + hour + ":" + minute);
    }

    // DatePickerDialog
    public void onClickShowDateDialog(View view) {
        DateDialogFragment dateDialog = new DateDialogFragment();
        dateDialog.show(getSupportFragmentManager(), "date");
    }

    public void onDateSet(int year, int month, int day) {
        showToast("Выбрана дата: " + day + "." + (month + 1) + "." + year);
    }

    // ProgressDialog
    public void onClickShowProgressDialog(View view) {
        ProgressDialogFragment progressDialog = new ProgressDialogFragment();
        progressDialog.show(getSupportFragmentManager(), "progress");
    }

    // Snackbar
    public void onClickShowSnackbar(View view) {
        Snackbar.make(findViewById(R.id.main),
                        "Это Snackbar сообщение",
                        Snackbar.LENGTH_LONG)
                .setAction("Действие", v -> showToast("Snackbar действие!"))
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}