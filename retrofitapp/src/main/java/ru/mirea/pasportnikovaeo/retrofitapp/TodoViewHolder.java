package ru.mirea.pasportnikovaeo.retrofitapp;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    TextView textViewTitle;
    TextView textViewId;
    CheckBox checkBoxCompleted;
    ImageView imageViewTodo;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewId = itemView.findViewById(R.id.textViewId);
        checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        imageViewTodo = itemView.findViewById(R.id.imageViewTodo);
    }
}