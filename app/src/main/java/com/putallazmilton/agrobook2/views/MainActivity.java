package com.putallazmilton.agrobook2.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.socket.sockets;
import com.putallazmilton.agrobook2.views.fragments.homeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sockets.getInstance().conectarSocket();
        homeFragment hf = new homeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main,hf).commit();
    }
}
