package com.cs442.team11.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class  TicTacToeActivity extends AppCompatActivity {

    public static MediaPlayer mp;
    private LoginButton loginButton;
    private TextView info;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.first);

        TextView mLink = (TextView) findViewById(R.id.link);
        if (mLink != null) {
            mLink.setMovementMethod(LinkMovementMethod.getInstance());
        }

        int resId=R.raw.a;
        mp=MediaPlayer.create(this, resId);
        mp.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cs442.team11.tictactoe",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        PreferenceManager.setDefaultValues(this, R.xml.options, false);
        callbackManager = CallbackManager.Factory.create();
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );*/
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tic_tac_toe, menu);
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

    public void newgame(View view){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String level = SP.getString("level", "1");
        if(level.equals("1")) {
            Intent intent = new Intent(TicTacToeActivity.this, NewgameActivity.class);
            startActivity(intent);
        }
        else if(level.equals("2"))
        {
            Intent intent = new Intent(TicTacToeActivity.this, AdvancedgameActivity.class);
            startActivity(intent);
        }
    }

    public void options(View view) {
        Intent intent = new Intent(TicTacToeActivity.this, optionActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

   /* @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences settings = getSharedPreferences("color", 0); //1
        String s1 = settings.getString("cc", null);
        if(s1 != null){
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearFirst);
            linearLayout.setBackgroundColor(Integer.parseInt(s1));
        }else{
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearFirst);
            linearLayout.setBackgroundColor(0xffffff00);
        }
    }*/

}
