package com.cs442.team11.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Set;

public class optionActivity extends PreferenceActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    Context context=this;
    static String PlayerName;
    public static final String TWO_PLAYER = "two_player";
    int skin=2;
    ListView myListView;
    ArrayAdapter<String> ScoreArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_option);
        addPreferencesFromResource(R.xml.options);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getActionBar();

        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //final String symbol = SP.getString("symbol", "1");
        final boolean check = SP.getBoolean("two_player", false);
       // final Preference pref = (Preference) findPreference("two_player");
        final Preference sym = findPreference("symbol");
        final Preference level=findPreference("level");
        final Preference name=findPreference("player");
        String name1=SP.getString("player", "null");

        final CheckBoxPreference pref = (CheckBoxPreference) findPreference("two_player");
        if(pref.isChecked())
        {
            sym.setSelectable(false);
            level.setSelectable(false);
            name.setSelectable(false);
        }
        else
        {
            sym.setSelectable(true);
            level.setSelectable(true);
            name.setSelectable(true);
        }


        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                PreferenceScreen screen = getPreferenceScreen();
                if (pref.isChecked()) {
                   // screen.removePreference(logout);
                    sym.setSelectable(false);
                    level.setSelectable(false);
                    name.setSelectable(false);
                    PlayerName = "";
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popup);
                    dialog.setTitle("Set Player Names");
                    final EditText player1 = (EditText) dialog.findViewById(R.id.player1);
                    player1.setText("");
                    final EditText player2 = (EditText) dialog.findViewById(R.id.player2);
                    player2.setText("");
                    Button ok = (Button) dialog.findViewById(R.id.button);
                    ok.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  if(player1.getText().toString().equals(""))
                                                      NewgameActivity.player_name_1="Player1";
                                                  else
                                                      NewgameActivity.player_name_1 = player1.getText();
                                                  if(player2.getText().toString().equals(""))
                                                      NewgameActivity.player_name_2="Player2";
                                                  else
                                                      NewgameActivity.player_name_2 = player2.getText();
                                                  dialog.dismiss();
                                              }
                                          }
                    );
                    dialog.show();
                }
                else
                {
                    sym.setSelectable(true);
                    level.setSelectable(true);
                    name.setSelectable(true);
                }
                return true;
            }
        });

        Preference invite=(Preference)findPreference("invite");
        invite.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(optionActivity.this, BluetoothActivity.class);
                    startActivity(intent);

            return true;
        }
        });


        Preference button = (Preference)findPreference(getString(R.string.button));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.help);
                dialog.setTitle("HELP");
                final TextView text = (TextView) dialog.findViewById(R.id.help);
                text.setText("The Player Who Succeeds in placing three respective marks (e.g.0-0-0) in a horizontal, vertical or diagonal row wins the Tic Tac Toe game.\n GOOD LUCK AND ENJOY!!!!!!!");
                dialog.show();
                return true;
            }
        });

        Preference score=(Preference)findPreference("scores");
        score.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ContentResolver cr=getContentResolver();
              /*  Cursor c = getContentResolver().query(
                        Uri.parse(ScoreProvider.CONTENT_URI + Uri.encode(ScoreProvider.KEY_SCORE)),
                        null, null, null, BaseColumns._ID + " DESC " + " LIMIT 5");*/
                Cursor c = cr.query(ScoreProvider.CONTENT_URI, null, null, null, ScoreProvider.KEY_SCORE +" "+"DESC");
                int id=c.getColumnIndexOrThrow(ScoreProvider.KEY_ID);
                int name=c.getColumnIndexOrThrow(ScoreProvider.KEY_NAME);
                int score=c.getColumnIndexOrThrow(ScoreProvider.KEY_SCORE);
                int pos=c.getColumnIndex(ScoreProvider.KEY_ID);
               // c.moveToFirst();

                Dialog mdialog = new Dialog(optionActivity.this);
                mdialog.setContentView(R.layout.score);
                mdialog.setTitle("Top Scores");
                myListView =(ListView)mdialog.findViewById(R.id.score);
                ScoreArrayAdapter = new ArrayAdapter<String>(optionActivity.this, android.R.layout.simple_list_item_1);
                while (c.moveToNext()) {
                    ScoreArrayAdapter.add(c.getString(name) + "        " + "Score:" + c.getString(score));
                }

                myListView.setAdapter(ScoreArrayAdapter);
                mdialog.show();
                return true;

            }
        });

        Preference button1 = (Preference)findPreference(getString(R.string.button1));
        button1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                new AlertDialog.Builder(optionActivity.this)
                        .setTitle("Quit :")
                        .setMessage("Are You Sure??")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes",
                                new Dialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        stopService(new Intent(getApplicationContext(), TicTacToeActivity.class));
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                        .show();
                return true;
            }
        });

        Preference button2 = (Preference)findPreference(getString(R.string.button2));
        button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.advanced);
                dialog.setTitle("ADVANCED SETTINGS");
                final Button adv = (Button) dialog.findViewById(R.id.background);
                final Button audio=(Button) dialog.findViewById(R.id.audio);
                //Button video=(Button) dialog.findViewById(R.id.video);
                final String control=audio.getText().toString();
                audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String control = audio.getText().toString();
                        if (control.equals("SOUND OFF")) {
                            audio.setText("SOUND ON");
                            TicTacToeActivity.mp.stop();
                            TicTacToeActivity.mp.release();
                        }
                        if (control.equals("SOUND ON")) {
                            audio.setText("SOUND OFF");
                            TicTacToeActivity.mp = MediaPlayer.create(optionActivity.this, R.raw.a);
                            TicTacToeActivity.mp.start();
                        }
                        //dialog.dismiss();
                    }
                });
               // dialog.show();
                adv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(optionActivity.this,ColorPickerActivity.class);
                        startActivity(intent);
                        //dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
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

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                ScoreProvider.CONTENT_URI, null, null, null, null);

        return loader;
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
       /* int keyTaskIndex2 = cursor.getColumnIndexOrThrow(ScoreProvider.KEY_ID);
        int keyTaskIndex = cursor.getColumnIndexOrThrow(ScoreProvider.KEY_NAME);
        int keyTaskIndex1 = cursor.getColumnIndexOrThrow(ScoreProvider.KEY_SCORE);
//        ITEMS.clear();
        //DummyContent.ITEMS.clear();
        StringBuilder res=new StringBuilder();
        while (cursor.moveToNext()) {
              *//*DummyContent.DummyItem newItem = new DummyContent.DummyItem(cursor.getString(keyTaskIndex2),cursor.getString(keyTaskIndex),cursor.getString(keyTaskIndex1));
              DummyContent.ITEMS.add(newItem);*//*
            ScoreArrayAdapter.add(cursor.getString(keyTaskIndex2)+ cursor.getString(keyTaskIndex)+cursor.getString(keyTaskIndex1));
        }
        cursor.close();*/
    }



}
