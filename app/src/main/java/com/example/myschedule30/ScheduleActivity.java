package com.example.myschedule30;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private Button addItemButton;
    private DatabaseHelper dbHelper;

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<ScheduleItem> scheduleItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        addItemButton = findViewById(R.id.button2);
        dbHelper = new DatabaseHelper(this);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        scheduleItemList = new ArrayList<>();

        adapter = new ScheduleAdapter(scheduleItemList, ScheduleActivity.this, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Завантаження даних з бази даних при створенні активності
        loadFromDatabase();
    }

    private void showAddItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Додати новий елемент");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        dialogBuilder.setView(dialogView);

        final EditText itemNameEditText = dialogView.findViewById(R.id.itemNameEditText);
        final EditText itemTypeEditText = dialogView.findViewById(R.id.itemTypeEditText);
        final EditText itemDateEditText = dialogView.findViewById(R.id.itemDateEditText);

        dialogBuilder.setPositiveButton("Додати", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String itemName = itemNameEditText.getText().toString().trim();
                String itemType = itemTypeEditText.getText().toString().trim();
                String itemDate = itemDateEditText.getText().toString().trim();

                // Додавання до бази даних
                addToDatabase(itemName, itemType, itemDate);
            }
        });

        dialogBuilder.setNegativeButton("Скасувати", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void addToDatabase(String itemName, String itemType, String itemDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, itemName);
        values.put(DatabaseHelper.COLUMN_TYPE, itemType);
        values.put(DatabaseHelper.COLUMN_DATE, itemDate);
        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();

        if (newRowId != -1) {
            Toast.makeText(this, "Дані успішно збережено!", Toast.LENGTH_SHORT).show();
            // Оновіть список scheduleItemList тут
            scheduleItemList.add(new ScheduleItem((int) newRowId, itemName, itemType, itemDate, false));
            // Повідомте адаптер про зміни
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Помилка при збереженні даних", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_TYPE,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_IS_DONE
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String itemType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE));
            String itemDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            boolean isDone = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_DONE)) == 1;
            scheduleItemList.add(new ScheduleItem(id, itemName, itemType, itemDate, isDone));
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Очистіть список перед завантаженням нових даних, щоб уникнути дублювання
        scheduleItemList.clear();
        loadFromDatabase();
        adapter.notifyDataSetChanged();
    }
}
