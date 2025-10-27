package ru.mirea.pasportnikovaeo.recyclerviewapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewEvent;
    public TextView textViewTitle;
    public TextView textViewYear;
    public TextView textViewDescription;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        imageViewEvent = itemView.findViewById(R.id.imageViewEvent);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewYear = itemView.findViewById(R.id.textViewYear);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
    }
}