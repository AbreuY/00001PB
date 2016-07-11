package com.pixr.photo.collage;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.pixr.photo.collage.model.AutoResizeTextView;


/**
 * Created by cheungchingai on 6/15/15.
 */
public class StickerTextView extends StickerView{
    private AutoResizeTextView tv_main;
    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if(tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        //tv_main.setTextSize(22);
        tv_main.setTextColor(Color.WHITE);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(100);
        tv_main.setShadowLayer(0, 0, 0, Color.BLACK);
        tv_main.setMaxLines(1);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        params.setMargins(20,50,0,0);

        tv_main.setLayoutParams(params);
        if(getImageViewFlip()!=null)
            getImageViewFlip().setVisibility(View.GONE);
        return tv_main;
    }

    public void setText(String text){
        if(tv_main!=null)
            tv_main.setText(text);
    }

    public String getText(){
        if(tv_main!=null)
            return tv_main.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }
}
