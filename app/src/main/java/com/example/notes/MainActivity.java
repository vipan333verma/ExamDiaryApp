package com.example.notes;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    ImageView imageView;
    TextView textView;
    Realm realm;
    Adapter adapter;
    String myNotes;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addoption, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);
        initView();
        getDataFromRealm();


    }

    private void initView() {
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adding:
                startActivity(new Intent(getApplicationContext(), NoteAddActivity.class));
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        getDataFromRealm();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void getDataFromRealm() {
        list.clear();
        RealmResults<RealmNotes> notes = realm.where(RealmNotes.class).findAllAsync();

        for (RealmNotes realmNotes : notes)
        {


            myNotes = realmNotes.getMyNotes();
            list.addAll(Collections.singleton(myNotes));
            adapter = new Adapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }


//            recyclerView.setVisibility(View.GONE);
//            textView.setVisibility(View.GONE);
//            imageView.setVisibility(View.GONE);
//


}



