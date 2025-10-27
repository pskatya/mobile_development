package ru.mirea.pasportnikovaeo.recyclerviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<Event> events;
    private Context context;

    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.textViewTitle.setText(event.getTitle());
        holder.textViewYear.setText(String.valueOf(event.getYear()));
        holder.textViewDescription.setText(event.getDescription());

        // Загрузка изображения из ресурсов
        String imageName = event.getImageName();
        int resId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName()
        );

        if (resId != 0) {
            holder.imageViewEvent.setImageResource(resId);
        }

        // Обработчик кликов
        holder.itemView.setOnClickListener(v -> {
            android.widget.Toast.makeText(
                    context,
                    "Событие: " + event.getTitle(),
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}