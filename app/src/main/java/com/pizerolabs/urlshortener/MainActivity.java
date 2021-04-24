package com.pizerolabs.urlshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText urlbox, shortUrl, shorturldisplay;
    Button shortlink, longlink;
    String link1, link2, link3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase mydatabase = this.openOrCreateDatabase("master", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS urllist(shorturl VARCHAR, longurl VARCHAR)");



        urlbox = findViewById(R.id.urlBox);
        shortUrl = findViewById(R.id.shortUrlBox);
        shorturldisplay = findViewById(R.id.output);
        shortlink = findViewById(R.id.button4);
        longlink = findViewById(R.id.button5);


        shortlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                link1 = urlbox.getText().toString();
                LinkShortner linkShortner = new LinkShortner();
                link2 = "https://zap" + linkShortner.getAlphaNumericString(8);
                //Toast.makeText(MainActivity.this, link2, Toast.LENGTH_SHORT).show();
                Cursor c = mydatabase.rawQuery("SELECT * FROM urllist WHERE shorturl = '" + link2 + "'", null);


                if (c.getCount() <= 0)  //Entry exists in database check
                {
                    try {


                        shorturldisplay.setText(link2);
                        mydatabase.execSQL("INSERT INTO urllist (shorturl, longurl) VALUES ('" + link2 + "' , '" + link1 + "')");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    //Code here incase duplicacy of shortlink occurs

                }
            }
        });
        longlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link3 = shortUrl.getText().toString();
                Cursor c = mydatabase.rawQuery("SELECT * FROM urllist WHERE shorturl = '" + link3 + "'", null);

                if (c.getCount() > 0)  //Entry exists in database check
                {
                    try {

                        c.moveToFirst();

                        while (c != null) {
                            int shorturlIndex = c.getColumnIndex("shorturl");
                            int longurlIndex = c.getColumnIndex("longurl");
                            String url = c.getString(longurlIndex);

                            shorturldisplay.setText(url);
                            c.moveToNext();

                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                url = "http://" + url;


                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "The Entered URL shortlink doesnt exist in our database", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}