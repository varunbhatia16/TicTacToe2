package com.cs442.team11.tictactoe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ColorPickerActivity extends AppCompatActivity {

    TextView text1;
    int color = 0xffffff00;

    public static int colour=0xffffff00;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        final View button1 = findViewById(R.id.button1);
        final View button2 = findViewById(R.id.button2);
        final View button3 = findViewById(R.id.button3);
        text1 = (TextView) findViewById(R.id.text1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("color",0);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("cc",String.valueOf(0xffff0000) );
                editor.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("color",0);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                colour =0xff0000ff;
                editor.putString("cc", String.valueOf(0xff0000ff) );
                editor.commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("color",0);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                colour =0xff00ff00;
                editor.putString("cc", String.valueOf(0xff00ff00) );
                editor.commit();

            }
        });
    }
}
