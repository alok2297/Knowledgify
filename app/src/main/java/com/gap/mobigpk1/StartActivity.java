package com.gap.mobigpk1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_start);

        SystemClock.sleep(1000);

        Intent intent=null;
        if(getIntent().hasExtra("category")){
            intent=new Intent(StartActivity.this,MainActivity.class);
            intent.putExtra("targetFragment", "Notification");
        }
        else{
            intent = new Intent(this,LoginActivity.class);
        }
        startActivity(intent);
        finish();

//         sharedPreferences = getSharedPreferences("FIRSTRUN",MODE_PRIVATE);
//         if(check()){
//             SharedPreferences.Editor myEdit = sharedPreferences.edit();
//             myEdit.putString("Language", "H");
//             myEdit.commit();
//         }

    }

    private boolean check(){
        String value = sharedPreferences.getString("Language",null);
        return value ==null;
    }
}