package com.example.myschedule30;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ScheduleItem> scheduleItemList;
    private Context context;
    private DatabaseHelper dbHelper;

    public ScheduleAdapter(List<ScheduleItem> scheduleItemList, Context context, DatabaseHelper dbHelper) {
        this.scheduleItemList = scheduleItemList;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleItem scheduleItem = scheduleItemList.get(position);
        holder.itemNameTextView.setText(scheduleItem.getItemName());
        holder.itemTypeTextView.setText(scheduleItem.getItemType());
        holder.itemDateTextView.setText(scheduleItem.getItemDate());

        // Отримуємо стан виконання завдання
        boolean isDone = scheduleItem.isDone();

        // Встановлюємо кнопку як доступну або недоступну залежно від стану завдання
        holder.markDoneButton.setEnabled(!isDone);

        // Встановлюємо колір кнопки залежно від стану завдання
        int colorResId = isDone ? R.color.black : R.color.black; // Визначаємо колір в залежності від стану
        holder.markDoneButton.setBackgroundColor(ContextCompat.getColor(context, colorResId));

        // Встановлюємо слухача для кнопки "Виконано"
        holder.markDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Позначаємо завдання як виконане
                markAsDone(scheduleItem.getId());

                // Встановлюємо кнопку як недоступну та змінюємо колір
                holder.markDoneButton.setEnabled(false);
                holder.markDoneButton.setBackgroundColor(ContextCompat.getColor(context, R.color.black));

                // Відображаємо сповіщення про виконане завдання
                Toast.makeText(context, "Завдання виконано", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemTypeTextView;
        TextView itemDateTextView;
        Button markDoneButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemTypeTextView = itemView.findViewById(R.id.itemTypeTextView);
            itemDateTextView = itemView.findViewById(R.id.itemDateTextView);
            markDoneButton = itemView.findViewById(R.id.markDoneButton);
        }
    }

    // Метод для оновлення стану виконання завдання в базі даних
    private void markAsDone(int itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Створіть об'єкт ContentValues для оновлення стану виконання
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IS_DONE, 1); // Встановіть стан виконання на 1 (виконано)

        // Створіть умову WHERE для оновлення лише потрібного запису
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        // Виконайте оновлення в базі даних
        int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        if (rowsUpdated > 0) {
            Toast.makeText(context, "Завдання виконано", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Помилка позначення завдання як виконаного", Toast.LENGTH_SHORT).show();
        }
    }

}

