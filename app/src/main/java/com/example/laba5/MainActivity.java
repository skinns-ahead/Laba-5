package com.example.laba5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextData;
    private TextView textViewData;

    private final String sharedPreferencesName = "AppPreferences";
    private final String fileName = "dataFile.txt";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextData = findViewById(R.id.editTextData);
        textViewData = findViewById(R.id.textViewData);
        Button buttonSavePreferences = findViewById(R.id.buttonSavePreferences);
        Button buttonSaveFile = findViewById(R.id.buttonSaveFile);
        Button buttonLoadPreferences = findViewById(R.id.buttonLoadPreferences);
        Button buttonLoadFile = findViewById(R.id.buttonLoadFile);

        // Сохранение в SharedPreferences
        buttonSavePreferences.setOnClickListener(v -> saveToPreferences(editTextData.getText().toString()));

        // Сохранение во внешний файл
        buttonSaveFile.setOnClickListener(v -> saveToFile(editTextData.getText().toString()));

        // Загрузка данных из SharedPreferences
        buttonLoadPreferences.setOnClickListener(v -> loadFromPreferences());

        // Загрузка данных из файла
        buttonLoadFile.setOnClickListener(v -> loadFromFile());
    }

    private void saveToPreferences(String data) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedData", data);
        editor.apply();
        textViewData.setText("Данные сохранены в SharedPreferences: " + data);
    }

    private void loadFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        String savedData = sharedPreferences.getString("savedData", "Нет данных");
        textViewData.setText("Данные из SharedPreferences: " + savedData);
    }

    private void saveToFile(String data) {
        File file = new File(getExternalFilesDir(null), fileName);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(data.getBytes());
            textViewData.setText("Данные сохранены во внешний файл: " + data);
        } catch (IOException e) {
            e.printStackTrace();
            textViewData.setText("Ошибка при записи данных в файл.");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadFromFile() {
        File file = new File(getExternalFilesDir(null), fileName);
        if (file.exists()) {
            String data = null;
            try {
                data = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            textViewData.setText("Данные из файла: " + data);
        } else {
            textViewData.setText("Файл не найден.");
        }
    }
}