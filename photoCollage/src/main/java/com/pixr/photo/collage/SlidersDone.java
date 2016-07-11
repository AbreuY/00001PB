package com.pixr.photo.collage;

import android.content.Intent;
import android.os.Bundle;

import com.abanoub.walkthrough.WalkthroughActivity;
import com.abanoub.walkthrough.WalkthroughItem;

/**
 * Created by mithramedia on 21/06/16.
 */
public class SlidersDone extends WalkthroughActivity {

   // int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WalkthroughItem page1 = new WalkthroughItem(R.drawable.twitter_final,getString(R.string.walkthrough_1_title),getString(R.string.walkthrough_3_details));
//        WalkthroughItem page2 = new WalkthroughItem(R.drawable.twitter_final,getString(R.string.walkthrough_2_title),getString(R.string.walkthrough_2_details));
//        WalkthroughItem page3 = new WalkthroughItem(R.drawable.youtube_final,getString(R.string.walkthrough_3_title),getString(R.string.walkthrough_3_details));
        page1.setBackgroundColorID(R.color.orange);
        page1.setTitleColorID(R.color.black);
        page1.setSubTitleColorID(R.color.black);
        page1.setSkipColorID(R.color.red);
        Intent intent = getIntent();

     //   id = intent.getIntExtra(Utils.GRID_ITEM_NO, 0);

//        page2.setBackgroundColorID(R.color.orange);
//        page2.setTitleColorID(R.color.black);
//        page2.setSubTitleColorID(R.color.black);
//        page2.setSkipColorID(R.color.red);
//
//        page3.setBackgroundColorID(R.color.orange);
//        page3.setTitleColorID(R.color.black);
//        page3.setSubTitleColorID(R.color.black);
//        page3.setSkipColorID(R.color.red);

        setProgressType(DOTS_TYPE);
        setTransitionType(ACCORDION_TRANSFORMER);

        addPage(page1);

//        addPage(page2);
//
//        addPage(page3);

    }
    @Override
    public void onFinish() {
        Intent intent = new Intent(this, SplashActivity.class);
    //    intent.putExtra(Utils.GRID_ITEM_NO, id);

        startActivity(intent);
        super.onFinish();
    }
}
