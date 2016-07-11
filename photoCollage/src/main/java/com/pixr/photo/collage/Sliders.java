package com.pixr.photo.collage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abanoub.walkthrough.WalkthroughActivity;
import com.abanoub.walkthrough.WalkthroughItem;
import com.pixr.photo.collage.grid.GridActivity;

/**
 * Created by mithramedia on 14/06/16.
 */
public class Sliders extends WalkthroughActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.slider);
//
//        TextView b11 = (TextView) findViewById(R.id.login);
//
        LinearLayout layout = (LinearLayout) findViewById(R.id.yuvti);
        layout.setGravity(Gravity.TOP);
        TextView b11 = new TextView(this);
        b11.setText("Login");
        b11.setTextColor(Color.BLACK);
        b11.setTextSize(Float.parseFloat("20.0"));
        b11.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

       // b11.setGravity(Gravity.RIGHT);
     //   layout.addView(b11);

        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
            }
        });



        WalkthroughItem page1 = new WalkthroughItem(R.drawable.youtube_final,getString(R.string.walkthrough_1_title),getString(R.string.walkthrough_1_details));


        page1.setBackgroundColorID(R.color.orange);
        page1.setTitleColorID(R.color.black);

        page1.setSubTitleColorID(R.color.black);
        page1.setSkipColorID(R.color.red);



        setProgressType(DOTS_TYPE);
        setTransitionType(ACCORDION_TRANSFORMER);

        addPage(page1);


    }
    @Override
    public void onFinish() {
        Intent intent = new Intent(this, GridActivity.class);
        startActivity(intent);
        super.onFinish();
    }
}
