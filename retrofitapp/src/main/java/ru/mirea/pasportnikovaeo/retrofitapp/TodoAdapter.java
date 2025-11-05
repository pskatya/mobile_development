package ru.mirea.pasportnikovaeo.retrofitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private Context context;
    private List<Todo> todos;
    private ApiService apiService;

    public TodoAdapter(Context context, List<Todo> todos, ApiService apiService) {
        this.context = context;
        this.todos = todos;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);

        holder.textViewTitle.setText(todo.getTitle());
        holder.textViewId.setText("ID: " + todo.getId() + " | User: " + todo.getUserId());

        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(todo.isCompleted());

        loadImageWithPicasso(holder.imageViewTodo, todo.getId(), position);

        holder.checkBoxCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todo.setCompleted(isChecked);
                updateTodoOnServer(todo);
            }
        });
    }

    private void loadImageWithPicasso(ImageView imageView, int todoId, int position) {
        String imageUrl;

        // Разные размеры и темы для разнообразия
        int size = 150 + (position % 3) * 50; // 150, 200, 250
        String[] categories = {"cat", "dog", "nature", "city", "food", "animal"};
        String category = categories[position % categories.length];

        imageUrl = "https://loremflickr.com/" + size + "/" + size + "/" + category + "?lock=" + todoId;

        Picasso.get()
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .resize(120, 120)
                .centerCrop()
                .into(imageView);
    }

    private void updateTodoOnServer(Todo updatedTodo) {
        Call<Todo> call = apiService.updateTodo(updatedTodo.getId(), updatedTodo);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                if (response.isSuccessful()) {
                    Log.d("TodoAdapter", "Todo updated successfully! ID: " + updatedTodo.getId());
                } else {
                    Log.e("TodoAdapter", "Failed to update. Server response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                Log.e("TodoAdapter", "Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos != null ? todos.size() : 0;
    }
}