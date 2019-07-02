package com.example.notes;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Adapter extends RecyclerView.Adapter<Adapter.MyAd> {

    private Context context;
    private ArrayList<String> list = new ArrayList<>();
    private String data, exam, date;
    private Realm realm;
    int cYear, cMonth, cdAY;

    public Adapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter.MyAd onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapterdata, viewGroup, false);
        return new MyAd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyAd myAd, final int i) {


        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfiguration);

        data = list.get(i);
        exam = data.substring(0, data.indexOf("#"));
        date = data.substring(data.indexOf("#") + 1);

        Calendar calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cdAY = calendar.get(Calendar.DAY_OF_MONTH);

        String cDate = cdAY + "-" + (cMonth + 1) + "-" + cYear;

        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        try {
            date1 = myFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = myFormat.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = 0;
        if (date2 != null) {
            if (date1 != null) {
                diff = date2.getTime() - date1.getTime();
            }
        }
        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        String check = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        Log.v("gth", check);

        if (check.contains("-")) {

            String mLeftDays = check.replace("-", "");
            myAd.days.setText(mLeftDays + " " + " Days Left");
            myAd.color.setBackgroundColor(Color.GREEN);myAd.color.setBackgroundResource(R.drawable.roundbg);
        } else {
            String mPassedDays = check.replace("-", "");
            myAd.days.setText(mPassedDays + " " + " Days Passed");
            myAd.color.setBackgroundResource(R.drawable.roundbggreen);
        }

        myAd.textView.setText(exam);
        myAd.dated.setText(date);
        myAd.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String note = list.get(i);
                    realm.beginTransaction();
                    RealmResults<RealmNotes> notes = realm.where(RealmNotes.class)
                            .equalTo("myNotes", note).findAll();
                    notes.deleteAllFromRealm();
                    realm.commitTransaction();
                    list.remove(i);
                    notifyItemChanged(i);
                    notifyItemRangeChanged(i, list.size());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyAd extends RecyclerView.ViewHolder {
        TextView textView, dated, days,color;
        Button button;

        MyAd(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.note);
            days = itemView.findViewById(R.id.days);
            color=itemView.findViewById(R.id.colore);
            button = itemView.findViewById(R.id.delete);
            dated = itemView.findViewById(R.id.tv);
        }
    }
}
