package com.example.myschedule30;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;


public class GradesActivity extends AppCompatActivity {

    public DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        // Знаходження кнопки "Додати оцінку" за її ID
        Button addGradeButton = findViewById(R.id.button5);

        // Ініціалізація об'єкта DatabaseHelper
        dbHelper = new DatabaseHelper(this);



        // Встановлення обробника подій для кнопки
        addGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показати діалогове вікно для додавання оцінки
                showAddExamDialog();
            }
        });
    }

    private void showAddExamDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Додати іспит");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exam, null);
        dialogBuilder.setView(dialogView);

        EditText examNameEditText = dialogView.findViewById(R.id.examNameEditText);
        EditText examGradeEditText = dialogView.findViewById(R.id.examGradeEditText);

        dialogBuilder.setPositiveButton("Зберегти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String examName = examNameEditText.getText().toString().trim();
                String examGradeString = examGradeEditText.getText().toString().trim();

                if (!examName.isEmpty() && !examGradeString.isEmpty()) {
                    double examGrade = Double.parseDouble(examGradeString);
                    insertExamData(examName, examGrade);
                } else {
                    Toast.makeText(GradesActivity.this, "Будь ласка, введіть назву і оцінку іспиту", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Скасувати", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void insertExamData(String name, double grade) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EXAM_NAME_COLUMN, name);
        values.put(DatabaseHelper.EXAM_GRADE_COLUMN, grade);
        long result = db.insert(DatabaseHelper.EXAM_TABLE_NAME, null, values);

        if (result == -1) {
            // Помилка вставки
            Log.e("GradesActivity", "Failed to insert data into exams table");
        } else {
            // Успішна вставка
            Log.i("GradesActivity", "Data inserted successfully into exams table");
        }
    }


}