package com.cs442.team11.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.service.textservice.SpellCheckerService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/*import com.facebook.login.LoginClient;*/

public class NewgameActivity extends AppCompatActivity {

    Context context = this;
    int count = 0;                // to count the number of moves made.
    int arr[][] =
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};    // array which stores the movements made.
    int player = 1;                // sets the player no. to 1 by default.
    int game_mode = 1;            // default 0 : h Vs h ; 1 : h Vs Comp
    int analysis_arr[][] =
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}};    // analysis_arr
    int map_arr[][] =
            {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};    // friend and enemy map initialization.
    static int user_symbol = 0;            // default 0: 0 to user, X to computer.
    int cross = R.drawable.system_cross;    // default values.
    int dot = R.drawable.system_dot;        // default values.
    int yellow = R.drawable.happy;
    int red = R.drawable.red;
    int correct=R.drawable.correct;
    int wrong=R.drawable.wrong;

    // player names initialized with default values.
    static CharSequence player_name_1 = "Player 1";
    static CharSequence player_name_2 = "Player 2";
    int score_player_1 = 0;
    int score_player_2 = 0;
    static String PlayerName;
    static boolean check;
    static Animation animRotate;
    static Animation animScale;
    static Animation animTranslate;
    public static String anim;
    public static boolean resume;
    public static boolean state;
    private Chronometer chronometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_newgame);
        // setContentView(R.layout.level);

       // loadPreferences();
        new_game();
    }

    public void new_game() {

        setContentView(R.layout.activity_newgame);
        // reset the game view. (this must be the first line in this function)
        animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        animScale=AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        animTranslate=AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        check = SP.getBoolean("two_player", false);
        final String symbol = SP.getString("symbol", "1");
        ImageView sym = (ImageView) findViewById(R.id.sym);
        ImageView sym1=(ImageView)findViewById(R.id.sym1);
        anim=SP.getString("animation","1");

        if (check == true) {
            game_mode = 0;
            TextView symtitle=(TextView) findViewById(R.id.symtitle);
            symtitle.setVisibility(View.INVISIBLE);

        } else {
            game_mode = 1;
            PlayerName = SP.getString("player", "Player");
            /*TextView name = (TextView) findViewById(R.id.name);
            name.setText("Player: " + PlayerName);*/
            if(PlayerName.toString().equals(""))
                player_name_2="Player1";
            else
                player_name_2 = PlayerName;
            if (symbol.equals("1")) {
                user_symbol = 0;
                sym.setImageResource(R.drawable.system_dot);
                sym1.setImageResource(R.drawable.system_cross);
            } else if (symbol.equals("2")) {
                user_symbol = 1;
                sym.setImageResource(R.drawable.system_cross);
                sym1.setImageResource(R.drawable.system_dot);
            } else if (symbol.equals("3")) {
                user_symbol = 2;
                sym.setImageResource(R.drawable.happy);
                sym1.setImageResource(R.drawable.red);
            } else if (symbol.equals("4")) {
                user_symbol = 3;
                sym.setImageResource(R.drawable.red);
                sym1.setImageResource(R.drawable.happy);
            } else if(symbol.equals("5")){
                user_symbol=4;
                sym.setImageResource(R.drawable.correct);
                sym1.setImageResource(R.drawable.wrong);
            } else if(symbol.equals("6")){
                user_symbol=5;
                sym.setImageResource(R.drawable.wrong);
                sym1.setImageResource(R.drawable.correct);
            }

        }


        final ImageButton b3 = (ImageButton) findViewById(R.id.b3);
        final ImageButton b2 = (ImageButton) findViewById(R.id.b2);
        final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

        final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
        final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
        final ImageButton b4 = (ImageButton) findViewById(R.id.b4);

        final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
        final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
        final ImageButton b7 = (ImageButton) findViewById(R.id.b7);

        b1.setOnClickListener(button_listener);
        b2.setOnClickListener(button_listener);
        b3.setOnClickListener(button_listener);
        b4.setOnClickListener(button_listener);
        b5.setOnClickListener(button_listener);
        b6.setOnClickListener(button_listener);
        b7.setOnClickListener(button_listener);
        b8.setOnClickListener(button_listener);
        b9.setOnClickListener(button_listener);

        /*SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        state = sharedPreferences.getBoolean("state", false);

        if(resume==true) {
            b1.setClickable(state);
            b2.setClickable(state);
            b3.setClickable(state);
            b4.setClickable(state);
            b5.setClickable(state);
            b6.setClickable(state);
            b7.setClickable(state);
            b8.setClickable(state);
            b9.setClickable(state);
        }*/
       // else {
            b1.setClickable(true);
            b2.setClickable(true);
            b3.setClickable(true);
            b4.setClickable(true);
            b5.setClickable(true);
            b6.setClickable(true);
            b7.setClickable(true);
            b8.setClickable(true);
            b9.setClickable(true);
       // }

        set_score(3);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                arr[i][j] = 0;
        if ((game_mode == 1) && (count % 2 != 0))
            CompGame();
    }

    public void CompGame() {
        player = 1;
        count++;
        analysis_array();
        if (easy_move_win() == true)
            return;
        else if (easy_move_block() == true)
            return;
        else {
            map();
            best_move();
        }

    }

    public void best_move() {
        int highest = 0, k = 0;
        int pos[][] = {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}};
        int random_index = 0;
        int x = 0, y = 0;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (map_arr[i][j] > highest)
                    highest = map_arr[i][j];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (map_arr[i][j] == highest) {
                    pos[k][0] = i;
                    pos[k][1] = j;
                    k++;
                }

        random_index = ((int) (Math.random() * 10)) % (k);
        x = pos[random_index][0];
        y = pos[random_index][1];

        comp_play(x, y);
    }

    public void map() {
        int k = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                map_arr[i][j] = 1;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if ((arr[i][j] == 1) || (arr[i][j] == 2))
                    map_arr[i][j] = 0;

        for (int i = 0; i < 8; i++) {
            if (((analysis_arr[i][0] == 1) && (analysis_arr[i][1] == 0)) || ((analysis_arr[i][0] == 0) && (analysis_arr[i][1] == 1)))
                if (i < 3) {
                    for (int j = 0; j < 3; j++)
                        if (map_arr[i][j] != 0)
                            map_arr[i][j] += 1;
                } else if (i < 6) {
                    for (int j = 0; j < 3; j++)
                        if (map_arr[j][i - 3] != 0)
                            map_arr[j][i - 3] += 1;
                } else if (i == 6) {
                    k = 0;
                    for (int m = 0; m < 3; m++) {
                        if (map_arr[m][k] != 0)
                            map_arr[m][k] += 1;
                        k++;
                    }
                } else if (i == 7) {
                    k = 2;
                    for (int m = 0; m < 3; m++) {
                        if (map_arr[m][k] != 0)
                            map_arr[m][k] += 1;
                        k--;
                    }
                }
        }
    }

    public boolean easy_move_win() {
        boolean flag = false;
        int i, k = 0;
        for (i = 0; i < 8; i++)
            if ((analysis_arr[i][0] == 2) && (analysis_arr[i][1] == 0)) {
                flag = true;
                break;
            }

        if (flag == true) {
            if (i < 3) {
                for (int j = 0; j < 3; j++)
                    if (arr[i][j] == 0) {
                        comp_play(i, j);
                        return true;
                    }
            } else if (i < 6) {
                for (int j = 0; j < 3; j++)
                    if (arr[j][i - 3] == 0) {
                        comp_play(j, (i - 3));
                        return true;
                    }
            } else if (i == 6) {
                for (int j = 0; j < 3; j++) {
                    if (arr[j][k] == 0) {
                        comp_play(j, k);
                        return true;
                    }
                    k++;
                }
            } else if (i == 7) {
                k = 2;
                for (int j = 0; j < 3; j++) {
                    if (arr[j][k] == 0) {
                        comp_play(j, k);
                        return true;
                    }
                    k--;
                }
            }
        }
        return false;
    }

    public boolean easy_move_block() {
        boolean flag = false;
        int i, k = 0;
        for (i = 0; i < 8; i++)
            if ((analysis_arr[i][0] == 0) && (analysis_arr[i][1] == 2)) {
                flag = true;
                break;
            }

        if (flag == true) {
            if (i < 3) {
                for (int j = 0; j < 3; j++)
                    if (arr[i][j] == 0) {
                        comp_play(i, j);
                        return true;
                    }
            } else if (i < 6) {
                for (int j = 0; j < 3; j++)
                    if (arr[j][i - 3] == 0) {
                        comp_play(j, (i - 3));
                        return true;
                    }
            } else if (i == 6) {
                for (int j = 0; j < 3; j++) {
                    if (arr[j][k] == 0) {
                        comp_play(j, k);
                        return true;
                    }
                    k++;
                }
            } else if (i == 7) {
                k = 2;
                for (int j = 0; j < 3; j++) {
                    if (arr[j][k] == 0) {
                        comp_play(j, k);
                        return true;
                    }
                    k--;
                }
            }
        }
        return false;
    }


    public void comp_play(int x, int y) {
        final ImageButton ib_tmp = (ImageButton) findViewById(R.id.b1);
        int ib_id = ib_tmp.getId();
        if ((x == 0) && (y == 0)) {
            // ib_id same as initialized value.
        } else {
            if (x == 0)
                ib_id -= y;
            else if (x == 1)
                ib_id += (3 - y);
            else if (x == 2)
                ib_id += (6 - y);
        }

        final ImageButton ib = (ImageButton) findViewById(ib_id);
       // ib.startAnimation(animRotate);
        Toast.makeText(this," Android is Thinking", Toast.LENGTH_SHORT).show();
        final Runnable r = new Runnable() {
            public void run() {
                if(anim.equals("1"))
                ib.startAnimation(animRotate);
                else if(anim.equals("2"))
                    ib.startAnimation(animScale);
                else  if(anim.equals("3"))
                    ib.startAnimation(animTranslate);
                if (user_symbol == 0)
                    ib.setImageResource(cross);
                else if (user_symbol == 1)
                    ib.setImageResource(dot);
                if (user_symbol == 2)
                    ib.setImageResource(red);
                else if (user_symbol == 3)
                    ib.setImageResource(yellow);
                if(user_symbol==4)
                    ib.setImageResource(wrong);
                else if(user_symbol==5)
                    ib.setImageResource(correct);
            }
        };
        ib.setClickable(false);
        ib.postDelayed(r, 1200);
        after_move(ib);
    }

    public void after_move(ImageButton ib) {
        CharSequence pos_str = "";
        int pos = 0;
        boolean result = false;
        pos_str = (CharSequence) ib.getTag();
        pos = (int) pos_str.charAt(0) - 48;
        if (player == 1) {
            if (pos < 4)
                arr[0][pos - 1] = 1;
            else if (pos < 7)
                arr[1][(pos - 1) % 3] = 1;
            else if (pos < 10)
                arr[2][(pos - 1) % 3] = 1;
        } else {
            if (pos < 4)
                arr[0][pos - 1] = 2;
            else if (pos < 7)
                arr[1][(pos - 1) % 3] = 2;
            else if (pos < 10)
                arr[2][(pos - 1) % 3] = 2;
        }

        result = result_check(player);
        if (result == true) {
            if (player == 1) {
                set_score(1);
                if (game_mode == 0) {
                    show_result("Congrats. " + player_name_1 + " wins !!");
                } else {
                    show_result("Computer Wins !!");
                }
            } else {
                set_score(2);
                if (game_mode == 0) {
                    show_result("Congrats. " + player_name_2 + " wins !!");
                } else {

                    ContentResolver cr = getContentResolver();
                    Cursor c = cr.query(ScoreProvider.CONTENT_URI, null, null, null, null);
                    int id=c.getColumnIndexOrThrow(ScoreProvider.KEY_ID);
                    int name=c.getColumnIndexOrThrow(ScoreProvider.KEY_NAME);
                    int score=c.getColumnIndexOrThrow(ScoreProvider.KEY_SCORE);
                    ContentValues values = new ContentValues();
                    values.put(ScoreProvider.KEY_NAME, PlayerName);
                    values.put(ScoreProvider.KEY_SCORE, score_player_2);
                  /*  while (c.moveToNext()) {
                        if(c.getString(name).equals(PlayerName)) {
                            score += score_player_2;
                            values.put(ScoreProvider.KEY_SCORE, score);
                            cr.update(ScoreProvider.CONTENT_URI, values, "_name=?", new String[]{PlayerName});
                        }
                        else
                        {*/
                            if(c.getCount()==0)
                            {
                                Uri uri = cr.insert(ScoreProvider.CONTENT_URI, values);
                               // Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                while (c.moveToNext()) {
                                   /* Cursor c1 = cr.query(ScoreProvider.CONTENT_URI, null, null, null, null);
                                    c1.moveToFirst();
                                    int i = c1.getColumnIndex(ScoreProvider.KEY_ID);*/
                                    if(c.getString(name).equals(PlayerName))
                                        cr.update(ScoreProvider.CONTENT_URI, values, "_id=?", new String[]{c.getString(id)});
                                    else
                                        cr.insert(ScoreProvider.CONTENT_URI,values);
                                }
                            }
                        //}
                   // }

                    show_result("Congrats. You have won !!");
                }
            }
            chronometer.stop();
            return;

        } else if ((result == false) && arr_isFull()) {
            show_result("    Game Draw !    ");
            return;
        }

        if ((game_mode == 1) && (player == 2) && (result == false)) {
            CompGame();
        } else {
        }
    }

    public void analysis_array() {
        for (int i = 0; i < 8; i++)
            analysis_arr[i][0] = analysis_arr[i][1] = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (arr[i][j] == 1)
                    analysis_arr[i][0] += 1;
                else if (arr[i][j] == 2)
                    analysis_arr[i][1] += 1;


        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (arr[j][i] == 1)
                    analysis_arr[i + 3][0] += 1;
                else if (arr[j][i] == 2)
                    analysis_arr[i + 3][1] += 1;

        int k = 0;
        for (int i = 0; i < 3; i++) {
            if (arr[i][k] == 1)
                analysis_arr[6][0] += 1;
            else if (arr[i][k] == 2)
                analysis_arr[6][1] += 1;
            k++;
        }

        k = 2;
        for (int i = 0; i < 3; i++) {
            if (arr[i][k] == 1)
                analysis_arr[7][0] += 1;
            else if (arr[i][k] == 2)
                analysis_arr[7][1] += 1;
            k--;
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newgame, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    View.OnClickListener button_listener = new View.OnClickListener() {
        public void onClick(View v) {
            ImageButton ibutton = (ImageButton) v;
            ibutton.setClickable(false);
           /* SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("state", ibutton.isClickable());
            resume=true;*/
            chronometer=(Chronometer)findViewById(R.id.mychronometer);
            chronometer.start();
            if(anim.equals("1"))
            ibutton.startAnimation(animRotate);
            else if(anim.equals("2"))
                ibutton.startAnimation(animScale);
            else if(anim.equals("3"))
                ibutton.startAnimation(animTranslate);
            count++;
            if ((count % 2 != 0) && (game_mode == 0)) {
                player = 1;
                ibutton.setImageResource(cross);
            } else if ((count % 2 == 0) || (game_mode == 1)) {
                player = 2;
                if ((user_symbol == 0) && (game_mode == 1))
                    ibutton.setImageResource(dot);
                else if ((user_symbol == 1) && (game_mode == 1))
                    ibutton.setImageResource(cross);
                else if ((user_symbol == 2) && (game_mode == 1))
                    ibutton.setImageResource(yellow);
                else if ((user_symbol == 3) && (game_mode == 1))
                    ibutton.setImageResource(red);
                else if ((user_symbol == 4) && (game_mode == 1))
                    ibutton.setImageResource(correct);
                else if ((user_symbol == 5) && (game_mode == 1))
                    ibutton.setImageResource(wrong);
                else
                    ibutton.setImageResource(dot);
            }

            after_move(ibutton);
        }
    };

   /* public  void loadPreferences()
    {
        final ImageButton b3 = (ImageButton) findViewById(R.id.b3);
        final ImageButton b2 = (ImageButton) findViewById(R.id.b2);
        final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

        final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
        final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
        final ImageButton b4 = (ImageButton) findViewById(R.id.b4);

        final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
        final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
        final ImageButton b7 = (ImageButton) findViewById(R.id.b7);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean  state = sharedPreferences.getBoolean("state", false);
        b1.setEnabled(state);
        b2.setEnabled(state);
        b3.setEnabled(state);
        b4.setEnabled(state);
        b5.setEnabled(state);
        b6.setEnabled(state);
        b7.setEnabled(state);
        b8.setEnabled(state);
        b9.setEnabled(state);
    } */
   /* public void savePreferences()
    {
        final ImageButton b3 = (ImageButton) findViewById(R.id.b3);
        final ImageButton b2 = (ImageButton) findViewById(R.id.b2);
        final ImageButton b1 = (ImageButton) findViewById(R.id.b1);

        final ImageButton b6 = (ImageButton) findViewById(R.id.b6);
        final ImageButton b5 = (ImageButton) findViewById(R.id.b5);
        final ImageButton b4 = (ImageButton) findViewById(R.id.b4);

        final ImageButton b9 = (ImageButton) findViewById(R.id.b9);
        final ImageButton b8 = (ImageButton) findViewById(R.id.b8);
        final ImageButton b7 = (ImageButton) findViewById(R.id.b7);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("state", b1.isClickable());
        editor.putBoolean("state", b2.isClickable());
        editor.putBoolean("state", b3.isClickable());
        editor.putBoolean("state", b4.isClickable());
        editor.putBoolean("state", b5.isClickable());
        editor.putBoolean("state", b6.isClickable());
        editor.putBoolean("state", b7.isClickable());
        editor.putBoolean("state", b8.isClickable());
        editor.putBoolean("state", b9.isClickable());
        editor.commit();   // I missed to save the data to preference here,.
    }*/

    public void set_score(int player_number) {
        TextView tv = (TextView) findViewById(R.id.scoreboard);

        if (player_number == 1)
            score_player_1 += 1;
        else if (player_number == 2)
            score_player_2 += 1;
        else ;
        if (game_mode == 1) {
            player_name_1 = "Computer";

        }
        CharSequence score_txt =
                player_name_1 + " : " + score_player_1 + "                   " + player_name_2 + " : " + score_player_2;

        tv.setText(score_txt);
    }

    public boolean show_result(CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new_game();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }

    public boolean result_check(int player_local) {
        boolean win = true;
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] != player_local) {
                    win = false;
                    break;
                }
            }
            if (win == true) {
                return true;
            }
            win = true;
        }
        win = true;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[j][i] != player_local) {
                    win = false;
                    break;
                }
            }
            if (win == true) {
                return true;
            }
            win = true;
        }

        win = true;

        for (int i = 0; i < 3; i++)
            if (arr[i][k++] != player_local) {
                win = false;
                break;
            }

        if (win == true) {
            return true;
        }

        k = 2;
        win = true;

        for (int i = 0; i < 3; i++)
            if (arr[i][k--] != player_local) {
                win = false;
                break;
            }

        if (win == true) {
            return true;
        }

        return false;
    }

    public boolean arr_isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (arr[i][j] == 0)
                    return false;
        return true;
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences settings = getSharedPreferences("color", 0); //1
        String s1 = settings.getString("cc", null);
        if (s1 != null) {
            RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.rel);
            linearLayout.setBackgroundColor(Integer.parseInt(s1));
        } else {
            RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.rel);

        }
    }

   /* public void Facebook()
    {
        // start Facebook Login
        SpellCheckerService.Session.openActiveSession(this, true, new SpellCheckerService.Session.StatusCallback() {

            // callback when session changes state
            public void call(SpellCheckerService.Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    // make request to;2 the /me API
                    LoginClient.Request.executeMeRequestAsync(session, new LoginClient.Request.
                            GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {

                            if (user != null) {
                                TextView welcome = (TextView) findViewById(R.id.welcome);
                                welcome.setText("Hello " + user.getName() + "!");
                            }
                        }
                    });
                }
            }
        }
    }*/

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_newgame, container, false);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        //loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        return view;
    }*/
  /*  @Override
    public void onDestroy() {
        savePreferences();
        resume=true;
        super.onDestroy();
    }*/
}
