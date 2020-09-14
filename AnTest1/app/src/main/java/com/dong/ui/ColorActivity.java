package com.dong.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dong.ui.widget.ColorNoteLinearLayout;

public class  ColorActivity extends Activity {
    private LinearLayout ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        findView();
    }

    private void findView() {
        ll = (LinearLayout) findViewById(R.id.color_main_ll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        Log.d("dong", "[ColorActivity]:[33] " + (ll != null));
        if (ll != null) {
            //ll.removeAllViews();


//            ll.addView(new ColorNoteLinearLayout(this, Color.YELLOW, "yellow"), params);
//            ll.addView(new ColorNoteLinearLayout(this, Color.GREEN, "green"), params);
            ll.addView(getItemView( Color.YELLOW, "yellow"));
            ll.addView(getItemView( Color.GREEN, "green"));
            ll.addView(getItemView( Color.BLUE, "blue"));
            ll.addView(getItemView( Color.parseColor("#ff8877"), "#ff8877"));
        }
    }

    private View getItemView(int  color, String labelStr) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        View view = getLayoutInflater().inflate(R.layout.color_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_img_color);
        imageView.setBackgroundColor(color);
        TextView textView = (TextView) view.findViewById(R.id.item_color_label);
        textView.setText(labelStr != null ? labelStr : "");
        return view;
    }
}
