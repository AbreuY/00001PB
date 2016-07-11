package com.pixr.photo.collage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pixr.photo.collage.grid.GridActivity;
import com.pixr.photo.collage.horizontal_listview.HorizontalItemAdapter;

import java.io.File;

/**
 * Created by mithramedia on 01/07/16.
 */
public class AdminActivity extends Activity implements AdapterView.OnItemClickListener,SeekBar.OnSeekBarChangeListener,View.OnClickListener {
     HorizontalListView hListView,hListView1,hListView2;
    private String[] strArrayAssetBG,strArrayAssetBackground,strArrayFonts;
    FrameLayout bgback,frameLayout,frameLayout1;
    public boolean isLayout = false, setLay = false, fontlay = false;
    SharedPreferences prefs;
    Editor editor;
    int red, green, blue,shape;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    private SeekBar seekbarshape,seekbarred,seekbargreen,seekbarblue;
    private EditText inputtext;
    private Button save,butlayout,butbackground,butfonts;
    public int lay,r1,g1,b1,s1;
    public String layout1,red_color,green_color,blue_color,shape_size,text1;


//    Context context = getActivity();
//    SharedPreferences sharedPref = context.getSharedPreferences(
//            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.adminview1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        isLayout = true;
        setLay = false;
        fontlay = false;

        hListView = (HorizontalListView) findViewById(R.id.hlvCustomList);
       // hListView1 = (HorizontalListView) findViewById(R.id.hlvCustomList1);
       // hListView2 = (HorizontalListView) findViewById(R.id.hlvCustomList2);
       // hListView.setVisibility(View.GONE);
//        hListView1.setVisibility(View.GONE);
//        hListView2.setVisibility(View.GONE);

        seekbarshape = (SeekBar) findViewById(R.id.shapeseekbar);
        seekbarred = (SeekBar) findViewById(R.id.redseekbar);
        seekbargreen = (SeekBar) findViewById(R.id.greenseekbar);
        seekbarblue = (SeekBar) findViewById(R.id.blueseekbar);


        seekbarshape.setOnSeekBarChangeListener(this);
        seekbarred.setOnSeekBarChangeListener(this);
        seekbargreen.setOnSeekBarChangeListener(this);
        seekbarblue.setOnSeekBarChangeListener(this);


        layout1 = sharedpreferences.getString("Layout",null);
        if(layout1 != null){
            layout1 = sharedpreferences.getString("Layout",null);
            lay = Integer.parseInt(layout1.toString());
            Log.d("layout",layout1);
        }
        else{
            layout1 = sharedpreferences.getString("Layout","0");
        }



         red_color = sharedpreferences.getString("red",null);
         green_color = sharedpreferences.getString("green",null);
        blue_color = sharedpreferences.getString("blue",null);
        shape_size = sharedpreferences.getString("shape",null);


        if(red_color != null && green_color != null && blue_color != null && shape_size != null){
             r1 = Integer.parseInt(red_color.toString());
             g1 = Integer.parseInt(green_color.toString());
             b1 = Integer.parseInt(blue_color.toString());
             s1 = Integer.parseInt(shape_size.toString());
            Log.d("ShapeSize",String.valueOf(r1));
        }
        else{
                r1 = 0;
                g1 = 0;
                b1 = 0;
                s1 = 0;
            Log.d("ShapeSize",String.valueOf(s1));
        }




        seekbarshape.setProgress(r1);
        seekbarred.setProgress(g1);
        seekbargreen.setProgress(b1);
        seekbarblue.setProgress(s1);

        inputtext = (EditText) findViewById(R.id.textbubble);
        save = (Button) findViewById(R.id.savedata);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textforimage = inputtext.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("text", textforimage);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), GridActivity.class);
                intent.putExtra(Utils.GRID_ITEM_NO,lay);

                startActivity(intent);
            }
        });

        butlayout = (Button) findViewById(R.id.ButtonLayout);
        butbackground = (Button) findViewById(R.id.ButtonBackground);
        butfonts = (Button) findViewById(R.id.ButtonFonts);


        bgback = (FrameLayout) findViewById(R.id.bg);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout1 = (FrameLayout) findViewById(R.id.frameLayout2);



        if (hListView != null) {

            hListView.setOnItemClickListener(this);
        }

//        if(hListView1 != null){
//
//            hListView1.setOnItemClickListener(this);
//        }

//            if(hListView2 != null){
//
//                hListView2.setOnItemClickListener(this);
//            }


        text1 = sharedpreferences.getString("text",null);
        if(text1 != null){
            text1 = sharedpreferences.getString("text",null);
        }
        else{
            text1 = "Default";
        }

        if(butlayout != null){
            loadBG_OR_STICKER_OR_Frame(Utils.ASSET_COLLEGE_FRAME);
            butlayout.setOnClickListener(this);

        }
        if(butbackground != null){
            loadBG_OR_STICKER_OR_Frame(Utils.ASSET_BG);
            butbackground.setOnClickListener(this);
        }
        if(butfonts != null){
            loadBG_OR_STICKER_OR_Frame(Utils.ASSET_FONTS);
            butfonts.setOnClickListener(this);
        }
    }


    private void loadBG_OR_STICKER_OR_Frame(String path) {

        hListView.setVisibility(View.VISIBLE);
        if (isLayout) {
            if (strArrayAssetBG == null) {

                strArrayAssetBG = loadImagesFromAssets1(path);

            }
            hListView.setAdapter(new HorizontalItemAdapter(
                    AdminActivity.this, strArrayAssetBG));
        } else if (setLay) {
            if (strArrayAssetBackground == null) {

                strArrayAssetBackground = loadImagesFromAssets1(path);
            }
            hListView.setAdapter(new HorizontalItemAdapter(AdminActivity.this, strArrayAssetBackground));
        } else if (fontlay) {
            if (strArrayFonts == null) {

                strArrayFonts = loadImagesFromAssets1(path);
            }
            hListView.setAdapter(new HorizontalItemAdapter(AdminActivity.this, strArrayFonts));
        }
    }





    private String[] loadImagesFromAssets(String path) {
        try {
            Resources res = getResources(); // if you are in an activity
            AssetManager am = res.getAssets();
            String fileList[] = am.list(path);

            String[] gridViewItems = new String[fileList.length];
            if (fileList != null) {
                for (int i = 0; i < fileList.length; i++) {
                    String file =(i)+".png";
                    if (fileList[i].contains(".jpg") || fileList[i].contains(".png")) {
                        gridViewItems[i] = path + File.separator +file;
                    }
                    if (Utils.debug)
                    {
                        Log.d("---UI---", fileList[i]);
                    }
                }
            }
            return gridViewItems;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] loadImagesFromAssets1(String path) {
        try {
            Resources res1 = getResources(); // if you are in an activity
            AssetManager am1 = res1.getAssets();
            String fileList1[] = am1.list(path);

            String[] gridViewItems1 = new String[fileList1.length];
            if (fileList1 != null) {
                for (int i = 0; i < fileList1.length; i++) {
                    if (fileList1[i].contains(".png")
                            || fileList1[i].contains(".jpg")) {
                        gridViewItems1[i] = path + File.separator + fileList1[i];
                    }
                    if (Utils.debug)
                    {
                        Log.d("---UI---", fileList1[i]);
                    }
                }
            }
            return gridViewItems1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(isLayout && !setLay && !fontlay) {
                if (bgback != null) {

                    Log.d("Item No", String.valueOf(position));
                    bgback.setBackground(new BitmapDrawable(getResources(),
                            ImageLoader.getInstance().loadImageSync(
                                    "assets://" + strArrayAssetBG[position])));

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Layout", String.valueOf(position));
                    editor.commit();

                }
            }
        else if(!isLayout && setLay && !fontlay){
                if (frameLayout != null)
                {

                    Log.d("Item No",String.valueOf(position));
                    frameLayout.setBackground(new BitmapDrawable(getResources(),
                            ImageLoader.getInstance().loadImageSync(
                                    "assets://" + strArrayAssetBackground[position])));

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("bg", String.valueOf(position));
                    editor.commit();

                }
            }

        else if(!isLayout && !setLay && fontlay){

                if (frameLayout1 != null)
                {

                    Log.d("Item No",String.valueOf(position));
                    frameLayout1.setBackground(new BitmapDrawable(getResources(),
                            ImageLoader.getInstance().loadImageSync(
                                    "assets://" + strArrayFonts[position])));

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("font", String.valueOf(position));
                    editor.commit();

                }

            }







        }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar.getId() == R.id.redseekbar ){
            red = progress;

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("red", String.valueOf(red));
            editor.commit();

        }
        else if(seekBar.getId() == R.id.greenseekbar){
            green = progress;

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("green", String.valueOf(green));
            editor.commit();
        }
        else if(seekBar.getId() == R.id.blueseekbar){
            blue = progress;

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("blue",String.valueOf(blue));
            editor.commit();
        }
        else if(seekBar.getId() == R.id.shapeseekbar){
            shape = progress;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("shape", String.valueOf(shape));
            editor.commit();

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.ButtonLayout:
                isLayout = true;
                setLay = false;
                fontlay = false;
                hListView.setVisibility(View.VISIBLE);
                loadBG_OR_STICKER_OR_Frame(Utils.ASSET_COLLEGE_FRAME);
               // hListView1.setVisibility(View.INVISIBLE);
               // hListView2.setVisibility(View.INVISIBLE);
                break;
            case R.id.ButtonBackground:
                isLayout = false;
                setLay = true;
                fontlay = false;
                hListView.setVisibility(View.VISIBLE);
                loadBG_OR_STICKER_OR_Frame(Utils.ASSET_BG);
                //hListView1.setVisibility(View.VISIBLE);
                //hListView2.setVisibility(View.INVISIBLE);

                break;
            case R.id.ButtonFonts:
                isLayout = false;
                setLay = false;
                fontlay = true;
                hListView.setVisibility(View.VISIBLE);
                loadBG_OR_STICKER_OR_Frame(Utils.ASSET_FONTS);
                //hListView1.setVisibility(View.INVISIBLE);
                //hListView2.setVisibility(View.VISIBLE);


                break;

        }
    }
}
