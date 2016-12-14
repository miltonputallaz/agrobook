package com.putallazmilton.agrobook2.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.socket.sockets;
import com.putallazmilton.agrobook2.views.fragments.homeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AccessToken.getCurrentAccessToken()==null){
            goLoginScreen();
        } else {
            sockets.getInstance().conectarSocket();
            homeFragment hf = new homeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main,hf).commit();
        }

    }

    private void goLoginScreen(){
        Intent inte=new Intent(this,LoginActivity.class);
        inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inte);
    }
}
