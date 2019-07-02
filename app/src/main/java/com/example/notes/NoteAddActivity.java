package com.example.notes;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;



import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NoteAddActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    Button button, exanchoosedate;
    Realm realm;
    int day, month, year;
    String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        requestWindowFeature(Window.FEATURE_NO_TITLE);

//initilize mobile adds


        Realm.init(getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_add);



        initView();
    }

    private void initView() {
        editText = findViewById(R.id.addnotes);
        button = findViewById(R.id.submit);
        exanchoosedate = findViewById(R.id.examdate);
        button.setOnClickListener(this);
        exanchoosedate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (editText.getText().toString().length() != 0) {
                    if (!exanchoosedate.getText().toString().contains("Choose Exam date")) {

                        addNotesToRealm();

                    } else {

                        Toast.makeText(getApplicationContext(), "Choose Exam Date", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    editText.setError("Enter Something");
                }


                break;
            case R.id.examdate:
                Calendar calendaR = Calendar.getInstance();
                year = calendaR.get(Calendar.YEAR);
                month = calendaR.get(Calendar.MONTH);
                day = calendaR.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(NoteAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        exanchoosedate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
        }
    }

    private void addNotesToRealm() {
        try {
            realm.beginTransaction();
            RealmNotes realmNotes = realm.createObject(RealmNotes.class);

            realmNotes.setMyNotes(editText.getText().toString() + "#" + exanchoosedate.getText().toString());
            realm.commitTransaction();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            Toast.makeText(getApplicationContext(), "Note Added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
