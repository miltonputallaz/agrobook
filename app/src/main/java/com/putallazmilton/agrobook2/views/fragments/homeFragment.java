package com.putallazmilton.agrobook2.views.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.adapters.homeAdapter;
import com.putallazmilton.agrobook2.models.Problema;
import com.putallazmilton.agrobook2.socket.sockets;
import com.putallazmilton.agrobook2.views.agregarActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;



public class homeFragment extends Fragment {
    private Socket mSocket;
    RecyclerView rv;

    ArrayList<Problema> problemas =new ArrayList<>();

    public homeFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =inflater.inflate(R.layout.fragment_home, container, false);

        rv= (RecyclerView) view.findViewById(R.id.recyclerviewhome);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        FloatingActionButton fab= (FloatingActionButton) view.findViewById(R.id.floatbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),agregarActivity.class);

                startActivity(intent);
            }
        });


      rv.setLayoutManager(llm);

        homeAdapter ha=new homeAdapter(R.layout.item,getActivity());
        rv.setAdapter(ha);

        sockets.getInstance().setActivityhome(getActivity());
        sockets.getInstance().setContexthome(getContext());
        sockets.getInstance().setRv(rv);
        return view;


    }













}
