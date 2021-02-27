package com.example.project3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment_graph2 extends Fragment {
    private String roomID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment=inflater.inflate(R.layout.fragment_graph2, container, false);

      //  roomID=getArguments().getString("room_graph2");

        return fragment;
    }
}