package com.dong.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dong.ui.R;

public class ColorNoteLinearLayout extends LinearLayout {
    ImageView imageView;
    TextView textView;
    int color;
    String labelStr;

    public ColorNoteLinearLayout(Context context, int color, String labelStr) {
        super(context);
    }

    public ColorNoteLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View myView = LayoutInflater.from(context).inflate(R.layout.color_item, this);
        imageView = (ImageView) myView.findViewById(R.id.item_img_color);
        imageView.setBackgroundColor(color);
        textView = (TextView) myView.findViewById(R.id.item_color_label);
        textView.setText(labelStr != null ? labelStr : "");
    }
}