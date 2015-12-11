package com.cs442.team11.tictactoe;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class AdvancedgameActivity extends AppCompatActivity {

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        GlobalData.setHeight(size.y);
        GlobalData.setWidth(size.x);
        View vw = new GameView(this);
        setContentView(vw);
        getWindow().setBackgroundDrawableResource(R.drawable.advancebackground);
       // getWindow().setBackgroundDrawableResource(R.color.back);

    }


}
