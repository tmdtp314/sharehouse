package com.example.project3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Talkadapter extends BaseAdapter {
    private Context co;
    private int layout;
    private ArrayList<TalkVO> data;
    private LayoutInflater inflater;
    private String preDate = "";
    private String Dayname = "";

    public Talkadapter(Context co, int layout, ArrayList<TalkVO> data) {
        this.co = co;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) co.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView tv_result = convertView.findViewById(R.id.tv_result);

//        vo.getName().equals(tv_result.getText().toString())

        if (convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }

        TalkVO vo = data.get(position);

        TextView tv_date = convertView.findViewById(R.id.tv_date);

        TextView tv_mytime = convertView.findViewById(R.id.tv_mytime);
        TextView tv_mymsg = convertView.findViewById(R.id.tv_mymsg);

        TextView tv_othername = convertView.findViewById(R.id.tv_othername);
        TextView tv_othermsg = convertView.findViewById(R.id.tv_othermsg);
        TextView tv_othertime = convertView.findViewById(R.id.tv_othertime);


        ImageView img = convertView.findViewById(R.id.imgID);

        if (preDate.equals(vo.getDate())){
            tv_date.setVisibility(View.GONE);
            preDate = "";
        }else{
            tv_date.setText(vo.getDate());
            tv_date.setVisibility(View.VISIBLE);
            preDate = "";
        }

        if (vo.getName().equals("윤승주")) {

            tv_mymsg.setVisibility(View.VISIBLE);
            tv_mytime.setVisibility(View.VISIBLE);
            tv_othermsg.setVisibility(View.GONE);
            tv_othername.setVisibility(View.GONE);
            tv_othertime.setVisibility(View.GONE);
            img.setVisibility(View.GONE);


            tv_mymsg.setText(vo.getMsg());
            tv_mytime.setText(vo.getTime());
            tv_othername.setText("");

        } else {
            tv_mymsg.setVisibility(View.GONE);
            tv_mytime.setVisibility(View.GONE);
            tv_othermsg.setVisibility(View.VISIBLE);
            tv_othername.setVisibility(View.VISIBLE);
            tv_othertime.setVisibility(View.VISIBLE);



            tv_othermsg.setText(vo.getMsg());
            tv_othername.setText(vo.getName());
            tv_othertime.setText(vo.getTime());


        }

        preDate = this.data.get(position).getDate();
        Log.v("tag_data", preDate + "뜸?");

        return convertView;
    }


}
