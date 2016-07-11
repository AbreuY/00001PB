package com.pixr.photo.collage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pixr.photo.collage.grid.GridViewAdapter;
import com.pixr.photo.collage.horizontal_listview.HorizontalItemAdapter;
import com.pixr.photo.collage.view.BubbleInputDialog;
import com.pixr.photo.collage.view.BubbleTextView;
import com.pixr.photo.collage.view.StickerView;
import com.pixr.photo.collage.widget.CircleImageView;
import com.pixr.photo.collage.widget.PhotoSortrView;
import com.pixr.photo.collage.widget.SquareFrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollegeActivity extends Activity implements OnClickListener,
		OnTouchListener, OnItemClickListener, SeekBar.OnSeekBarChangeListener {

	private InterstitialAd interstitial;
	AdView mAdView;
	private AdRequest adRequest;


	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;

	private GridViewAdapter mAdapter;
	SquareFrameLayout squareFrameLayout;
	GridView gridView;
	ShapeDrawable sp;
	BubbleTextView selectedBubbleView;
	private int state = 0;
	private SeekBar mSeekBarForShape, mSeekBarForDensity;
	private View mView;
	private String[] strArrayAssetBG, strArrayAssetFrame,
			strArrayAssetStickerLove,strTextView;
	HorizontalListView hListView;
	private final int ACTION_REQUEST_GALLERY = 99,
			ACTION_REQUEST_CAMERA = 888888, CALL_CHOOSER_FROM_GRID_CLICK = 3;
	private boolean isFrameLoaded, isShare, isBGFrameFlag = false,
			isSticker = false, isShapeFlag = false, isColorPicker = false,
			isFrameFlag = false, isStickerFlag = false, isTextFlag=false;
	RelativeLayout rlScreenShot, rlSliderShapeView, rlColorPicker,rlTvText;
	Rect thumbRect;
	ImageView imgysc;
	// COLOR PICKER
	SeekBar redSeekBar, greenSeekBar, blueSeekBar;
	TextView redToolTip, greenToolTip, blueToolTip;
	int red, green, blue, seekBarLeft;
	public static int total_shots_allow;
int currentImageIndex = 0 ;

	private int _xDelta;
	private int _yDelta;


	int windowwidth;
	int windowheight;

	// photo related activities
	private static final int REQUEST_CAMERA = 1;
	private static final int REQUEST_CAMERA_PERMISSION = 1;
	private Point mSize;

	GradientDrawable gd = new GradientDrawable();

	RelativeLayout.LayoutParams params1, params2, params3, params4, params5,
			params6, params7, params8, params9;
	private ViewStub stub;
	private CircleImageView cIvOne, cIvTwo;
	private ImageView ivSelectorImage[] = new ImageView[10];
	private ImageView ivBG, ivFrame, ivBack, ivSave;
	private PhotoSortrView pView1, pView2, pView3, pView4, pView5, pView6,
			pView7, pView8, pView9, pSelected, pStickerView;
	private View includeView1, includeView2, includeView3, includeView4,
			includeView5, includeView6, includeView7, includeView8,
			includeView9;

	private TextView tvGrid, tvShape, tvBG, tvFrame, tvSticker, tvText;

	private EditText t1,t2,t3,t4,t5,t6,t7,t8,t9;

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


	GradientDrawable myGrad;
	private String imgPath;
	private Uri cameraUri;
	private int layout = R.layout.one;
	private LinearLayout llBackground;

	private View mAddBubble;
	private BubbleTextView mCurrentEditTextView;
	private ArrayList<View> mViews;
	private BubbleInputDialog mBubbleInputDialog;
	private StickerView mCurrentView;
	private int mFontColor = 0;

	private RelativeLayout mContentRootView;
	String textpref,fontspref,redpref,greenpref,bluepref,shaprepref,bgpref;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	//private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		 textpref = sharedpreferences.getString("text",null);
		if(textpref != null){

			textpref = sharedpreferences.getString("text",null);
		}
		else{
			textpref = "Enter Text";
		}

		fontspref = sharedpreferences.getString("font",null);
		if(fontspref != null)
		{
			fontspref = sharedpreferences.getString("font",null);

		}
		else{
			fontspref = "1";
		}
		redpref = sharedpreferences.getString("red",null);
		if(redpref != null)
		{
			redpref = sharedpreferences.getString("red",null);
		}
		else{
			redpref = "0";
		}
		bluepref = sharedpreferences.getString("blue",null);
		if(bluepref != null){
			bluepref = sharedpreferences.getString("blue",null);
		}
		else{
			bluepref = "0";
		}

		greenpref = sharedpreferences.getString("green",null);
		if(greenpref != null)
		{
			greenpref = sharedpreferences.getString("green",null);
		}
		else {
			greenpref = "0";
		}
		shaprepref = sharedpreferences.getString("shape",null);
		if(shaprepref != null){
			shaprepref = sharedpreferences.getString("shape",null);
		}
		else{
			shaprepref = "0";
			Log.d("shaprepref",shaprepref);
		}
		bgpref = sharedpreferences.getString("bg",null);
		if(bgpref != null){
			bgpref = sharedpreferences.getString("bg",null);
		}
		else{
			bgpref = "1";
		}



		Intent intent = getIntent();
		int id = intent.getIntExtra(Utils.GRID_ITEM_NO, 0);
		setLayoutBasedFromChooser(id);

		Display display = getWindowManager().getDefaultDisplay();
		mSize = new Point();
		display.getSize(mSize);
        requestForCameraPermission();

		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		windowheight = getWindowManager().getDefaultDisplay().getHeight();


		mViews = new ArrayList<>();
		mBubbleInputDialog = new BubbleInputDialog(this);
		mBubbleInputDialog.setCompleteCallBack(new BubbleInputDialog.CompleteCallBack() {
			@Override
			public void onComplete(View bubbleTextView, String str) {
				//str = "hi";
				((BubbleTextView) bubbleTextView).setText(str);

			}
		});



		//ParseInstallation.getCurrentInstallation().saveInBackground();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	private void setLayoutBasedFromChooser(int id) {
		System.out.println("Selected ID is : - " + id);
		switch (id) {
			case 0:
				layout = R.layout.one;
				total_shots_allow = 1;
				break;
			case 1:
				layout = R.layout.two;
				total_shots_allow = 2;
				break;
			case 2:
				layout = R.layout.three;
				total_shots_allow = 2;
				break;
			case 3:
				layout = R.layout.four;
				total_shots_allow = 4;
				break;
			case 4:
				layout = R.layout.five;
				total_shots_allow = 4;
				break;
			case 5:
				layout = R.layout.six;
				total_shots_allow = 4;
				break;
			case 6:
				layout = R.layout.seven;
				total_shots_allow = 4;
				break;
			case 7:
				layout = R.layout.eight;
				total_shots_allow = 4;
				break;
			case 8:
				layout = R.layout.nine;
				total_shots_allow = 3;
				break;
			case 9:
				layout = R.layout.ten;
				total_shots_allow = 3;
				break;
			case 10:
				layout = R.layout.eleven;
				total_shots_allow = 3;
				break;
			case 11:
				layout = R.layout.twelve;
				total_shots_allow = 3;
				break;
			case 12:
				layout = R.layout.thrteen;
				total_shots_allow = 5;
				break;
			case 13:
				layout = R.layout.fourteen;
				total_shots_allow = 4;
				break;
			case 14:
				layout = R.layout.fifteen;
				total_shots_allow = 4;
				break;
			case 15:
				layout = R.layout.sixteen;
				total_shots_allow = 4;
				break;
			case 16:
				layout = R.layout.seventeen;
				total_shots_allow = 4;
				break;
			case 17:
				layout = R.layout.eighteen;
				total_shots_allow = 3;
				break;
			case 18:
				layout = R.layout.nineteen;
				total_shots_allow = 3;
				break;
			case 19:
				layout = R.layout.tweenty;
				total_shots_allow = 6;
				break;
			case 20:
				layout = R.layout.tweenty_one;
				total_shots_allow = 6;
				break;
			case 21:
				layout = R.layout.tweenty_two;
				total_shots_allow = 4;
				break;
			case 22:
				layout = R.layout.twenty_three;
				total_shots_allow = 4;
				break;
			case 23:
				layout = R.layout.twenty_four;
				total_shots_allow = 4;
				break;
			case 24:
				layout = R.layout.twenty_five;
				total_shots_allow = 4;
				break;
			case 25:
				layout = R.layout.twenty_six;
				total_shots_allow = 5;
				break;
			case 26:
				layout = R.layout.twenty_seven;
				total_shots_allow = 5;
				break;
			case 27:
				layout = R.layout.twenty_eight;
				total_shots_allow = 4;
				break;
			case 28:
				layout = R.layout.twenty_nine;
				total_shots_allow = 4;
				break;
			case 29:
				layout = R.layout.thirty;
				total_shots_allow = 4;
				break;
			case 30:
				layout = R.layout.thirty_one;
				total_shots_allow = 4;
				break;
			case 31:
				layout = R.layout.thirty_two;
				total_shots_allow = 5;
				break;
			case 32:
				layout = R.layout.thirty_three;
				total_shots_allow = 4;
				break;
			case 33:
				layout = R.layout.thirty_four;
				total_shots_allow = 5;
				break;
			case 34:
				layout = R.layout.thirty_five;
				total_shots_allow = 5;
				break;
			case 35:
				layout = R.layout.thirty_six;
				total_shots_allow = 5;
				break;
			case 36:
				layout = R.layout.thirty_seven;
				total_shots_allow = 5;
				break;
			case 37:
				layout = R.layout.thirty_eight;
				total_shots_allow = 5;
				break;
			case 38:
				layout = R.layout.thirty_nine;
				total_shots_allow = 5;
				break;
			case 39:
				layout = R.layout.fourty;
				total_shots_allow = 6;
				break;
			case 40:
				layout = R.layout.fourty_one;
				total_shots_allow = 6;
				break;
			case 41:
				layout = R.layout.fourty_two;
				total_shots_allow = 5;
				break;
			case 42:
				layout = R.layout.fourty_three;
				total_shots_allow = 5;
				break;
			case 43:
				layout = R.layout.fourty_four;
				total_shots_allow = 5;
				break;
			case 44:
				layout = R.layout.fourty_five;
				total_shots_allow = 5;
				break;
			case 45:
				layout = R.layout.fourty_six;
				total_shots_allow = 4;
				break;
			case 46:
				layout = R.layout.fourty_seven;
				total_shots_allow = 4;
				break;
			case 47:
				layout = R.layout.fourty_eight;
				total_shots_allow = 5;
				break;
			case 48:
				layout = R.layout.fourty_nine;
				total_shots_allow = 5;
				break;
			case 49:
				layout = R.layout.fifty;
				total_shots_allow = 5;
				break;
			case 50:
				layout = R.layout.fifty_one;
				total_shots_allow = 5;
				break;
			case 51:
				layout = R.layout.fifty_two;
				total_shots_allow = 5;
				break;
			case 52:
				layout = R.layout.fifty_three;
				total_shots_allow = 5;
				break;
			case 53:
				layout = R.layout.fifty_four;
				total_shots_allow = 6;
				break;
			case 54:
				layout = R.layout.fifty_five;
				total_shots_allow = 6;
				break;
			case 55:
				layout = R.layout.fifty_six;
				total_shots_allow = 6;
				break;
			case 56:
				layout = R.layout.fifty_seven;
				total_shots_allow = 6;
				break;
			case 57:
				layout = R.layout.fifty_eight;
				total_shots_allow = 7;
				break;
			case 58:
				layout = R.layout.fifty_nine;
				total_shots_allow = 7;
				break;
			case 59:
				layout = R.layout.sixty;
				total_shots_allow = 7;
				break;
			case 60:
				layout = R.layout.sixty_one;
				total_shots_allow = 7;
				break;
			case 61:
				layout = R.layout.sixty_two;
				total_shots_allow = 7;
				break;
			case 62:
				layout = R.layout.sixty_three;
				total_shots_allow = 7;
				break;
			case 63:
				layout = R.layout.sixty_four;
				total_shots_allow = 7;
				break;
			case 64:
				layout = R.layout.sixty_five;
				total_shots_allow = 7;
				break;
			case 65:
				layout = R.layout.sixty_six;
				total_shots_allow = 7;
				break;
			case 66:
				layout = R.layout.sixty_seven;
				total_shots_allow = 7;
				break;
			case 67:
				layout = R.layout.sixty_eight;
				total_shots_allow = 8;
				break;
			case 68:
				layout = R.layout.sixty_nine;
				total_shots_allow = 8;
				break;
			case 69:
				layout = R.layout.seventy;
				total_shots_allow = 9;
				break;

		}
		setContentView(R.layout.college_main);
		initilizationView();

	}

	private void initilizationView() {

		squareFrameLayout = (SquareFrameLayout) findViewById(R.id.squareFrameLayout);
		stub = (ViewStub) findViewById(R.id.changeLayout);
		stub.setLayoutResource(layout);
		View inflated = stub.inflate();

		sp = new ShapeDrawable(new OvalShape());

		llBackground = (LinearLayout) inflated.findViewById(R.id.llBackground);

		pStickerView = (PhotoSortrView) findViewById(R.id.photoSorterStickerView);

		rlScreenShot = (RelativeLayout) findViewById(R.id.rlScreenShot);
		rlSliderShapeView = (RelativeLayout) findViewById(R.id.rlSliderShapeView);
		rlColorPicker = (RelativeLayout) findViewById(R.id.rlColorPicker);
		hListView = (HorizontalListView) findViewById(R.id.hlvCustomList);
		//rlTvText = (RelativeLayout) findViewById(R.id.ivTextValue);
		mContentRootView = (RelativeLayout) findViewById(R.id.rl_content_root);

		ivSave = (ImageView) findViewById(R.id.ivSave);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		//tvGrid = (TextView) findViewById(R.id.tvGrid);
		tvShape = (TextView) findViewById(R.id.tvShape);
		tvBG = (TextView) findViewById(R.id.tvBG);
		//tvFrame = (TextView) findViewById(R.id.tvFrame);
		tvSticker = (TextView) findViewById(R.id.tvColorPicker);

		tvText = (TextView) findViewById(R.id.tvText);

		includeView1 = (View) findViewById(R.id.view1);
		includeView2 = (View) findViewById(R.id.view2);
		includeView3 = (View) findViewById(R.id.view3);
		includeView4 = (View) findViewById(R.id.view4);
		includeView5 = (View) findViewById(R.id.view5);
		includeView6 = (View) findViewById(R.id.view6);
		includeView7 = (View) findViewById(R.id.view7);
		includeView8 = (View) findViewById(R.id.view8);
		includeView9 = (View) findViewById(R.id.view9);

		ivFrame = (ImageView) findViewById(R.id.ivFrame);

		//t1 = (EditText) findViewById(R.id.t1);
		mSeekBarForDensity = (SeekBar) findViewById(R.id.seekBarForDensity);
		mSeekBarForShape = (SeekBar) findViewById(R.id.seekBarForShape);

		mSeekBarForShape.setProgress(10);
		//mSeekBarForDensity.setProgress(Integer.parseInt(shaprepref));

		cIvOne = (CircleImageView) findViewById(R.id.cIvOne);
		cIvTwo = (CircleImageView) findViewById(R.id.cIvTwo);

		TextView v1 = (TextView) findViewById(R.id.textView6);
		v1.setText("Step 3: Edit your Layout");

		rlColorPicker.setAlpha(1.0f);
		if (includeView1 != null) {
			pView1 = (PhotoSortrView) includeView1
					.findViewById(R.id.photoSortrViewOne);
			pView1.setBackgroundColor(Color.DKGRAY);
			params1 = (RelativeLayout.LayoutParams) pView1.getLayoutParams();

//


		}
		if (includeView2 != null) {
			pView2 = (PhotoSortrView) includeView2
					.findViewById(R.id.photoSortrViewTwo);
			pView2.setBackgroundColor(Color.DKGRAY);
		//	t2 = (EditText) findViewById(R.id.t2);


			params2 = (RelativeLayout.LayoutParams) pView2.getLayoutParams();





//			ivSelectorImage[1] = (ImageView) includeView2
//					.findViewById(R.id.ivTwo);
		}
		if (includeView3 != null) {
			pView3 = (PhotoSortrView) includeView3
					.findViewById(R.id.photoSortrViewThree);
			pView3.setBackgroundColor(Color.DKGRAY);
			params3 = (RelativeLayout.LayoutParams) pView3.getLayoutParams();




//			ivSelectorImage[2] = (ImageView) includeView3
//					.findViewById(R.id.ivThree);
		}

		if (includeView4 != null) {
			pView4 = (PhotoSortrView) includeView4
					.findViewById(R.id.photoSortrViewFour);
			pView4.setBackgroundColor(Color.DKGRAY);


			params4 = (RelativeLayout.LayoutParams) pView4.getLayoutParams();


//			ivSelectorImage[3] = (ImageView) includeView4
//					.findViewById(R.id.ivFour);
		}

		if (includeView5 != null) {
			pView5 = (PhotoSortrView) includeView5
					.findViewById(R.id.photoSortrViewFive);
			pView5.setBackgroundColor(Color.DKGRAY);


			params5 = (RelativeLayout.LayoutParams) pView5.getLayoutParams();

//			ivSelectorImage[4] = (ImageView) includeView5
//					.findViewById(R.id.ivFive);
		}

		if (includeView6 != null) {
			pView6 = (PhotoSortrView) includeView6
					.findViewById(R.id.photoSortrViewSix);
			pView6.setBackgroundColor(Color.DKGRAY);

			params6 = (RelativeLayout.LayoutParams) pView6.getLayoutParams();

//			ivSelectorImage[5] = (ImageView) includeView6
//					.findViewById(R.id.ivSix);
		}

		if (includeView7 != null) {
			pView7 = (PhotoSortrView) includeView7
					.findViewById(R.id.photoSortrViewSeven);
			pView7.setBackgroundColor(Color.DKGRAY);

			params7 = (RelativeLayout.LayoutParams) pView7.getLayoutParams();

//			ivSelectorImage[6] = (ImageView) includeView7
//					.findViewById(R.id.ivSeven);
		}

		if (includeView8 != null) {
			pView8 = (PhotoSortrView) includeView8
					.findViewById(R.id.photoSortrViewEight);
			pView8.setBackgroundColor(Color.DKGRAY);

			params8 = (RelativeLayout.LayoutParams) pView8.getLayoutParams();

//			ivSelectorImage[7] = (ImageView) includeView8
//					.findViewById(R.id.ivEight);
		}

		if (includeView9 != null) {
			pView9 = (PhotoSortrView) includeView9
					.findViewById(R.id.photoSortrViewNine);
			pView9.setBackgroundColor(Color.DKGRAY);

			params9 = (RelativeLayout.LayoutParams) pView9.getLayoutParams();


//			ivSelectorImage[8] = (ImageView) includeView9
//					.findViewById(R.id.ivNine);
		}

		ivBack.setOnClickListener(this);

		redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
		greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
		blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);

		redToolTip = (TextView) findViewById(R.id.redToolTip);
		greenToolTip = (TextView) findViewById(R.id.greenToolTip);
		blueToolTip = (TextView) findViewById(R.id.blueToolTip);

		redSeekBar.setOnSeekBarChangeListener(this);
		greenSeekBar.setOnSeekBarChangeListener(this);
		blueSeekBar.setOnSeekBarChangeListener(this);

		redSeekBar.setProgress(Integer.parseInt(redpref.toString()));
		greenSeekBar.setProgress(Integer.parseInt(greenpref.toString()));
		blueSeekBar.setProgress(Integer.parseInt(bluepref.toString()));

		seekBarLeft = redSeekBar.getPaddingLeft();


		for (int i = 0; i < ivSelectorImage.length; i++) {
			if (ivSelectorImage[i] != null) {
				ivSelectorImage[i].setOnClickListener(this);
			}
		}

		if (cIvOne != null) {
			cIvTwo.setOnClickListener(this);
		}

//		if (tvGrid != null) {
//			tvGrid.setOnClickListener(this);
//		}

//		if (tvFrame != null) {
//			tvFrame.setOnClickListener(this);
//		}

		if (tvBG != null) {
			tvBG.setOnClickListener(this);
		}

		if (tvShape != null) {
			tvShape.setOnClickListener(this);
		}

		if (tvSticker != null) {
			tvSticker.setOnClickListener(this);
		}

		if (hListView != null) {
			hListView.setOnItemClickListener(this);
		}

		if(tvText !=null){
			tvText.setOnClickListener(this);
		}
		if (ivSave != null) {
			ivSave.setOnClickListener(this);
		}


		// if (mSeekBarForShape != null) {
		// mSeekBarForShape.incrementProgressBy(10);
		// mSeekBarForShape.setMax(100);
		// mSeekBarForShape.setProgress(10);
		//
		// mSeekBarForShape
		// .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		//
		// @Override
		// public void onProgressChanged(SeekBar seekBar,
		// int progress, boolean fromUser) {
		// gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		// gd.setGradientRadius(progress*
		// getResources().getDisplayMetrics().density);
		// if (pView1 != null) {
		// System.out.println("progress   = " + progress);
		// pView1.setBackground(sp);
		// pView1.invalidate();
		// pView1.requestLayout();
		// }
		// if (pView2 != null) {
		// pView2.setBackground(gd);
		// pView2.requestLayout();
		// }
		// if (pView3 != null) {
		// pView3.setBackground(gd);
		// pView3.requestLayout();
		// }
		// if (pView4 != null) {
		// pView4.setBackground(gd);
		// pView4.requestLayout();
		// }
		// if (pView5 != null) {
		// pView5.requestLayout();
		// }
		// if (pView6 != null) {
		// pView6.requestLayout();
		// }
		// if (pView7 != null) {
		// pView7.requestLayout();
		// }
		// if (pView8 != null) {
		// pView8.requestLayout();
		// }
		// if (pView9 != null) {
		// pView9.requestLayout();
		// }
		// }
		//
		// @Override
		// public void onStartTrackingTouch(SeekBar seekBar) {
		//
		// }
		//
		// @Override
		// public void onStopTrackingTouch(SeekBar seekBar) {
		//
		// }
		// });
		// }

		if (mSeekBarForDensity != null) {
			mSeekBarForDensity.incrementProgressBy(10);
			mSeekBarForDensity.setProgress(10);
			mSeekBarForDensity.setMax(100);
			changeFrameThickNess(10);

			mSeekBarForDensity
					.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

						@Override
						public void onProgressChanged(SeekBar seekBar,
													  int progress, boolean fromUser) {

							changeFrameThickNess(progress);
							//changeFrameThickNess(Integer.parseInt(shaprepref));
							// RoundClippingLinearLayout.mCornerRadius =
							// progress;
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}
					});
		}
	}

	public void visibleSelectorImage() {
		for (int i = 0; i < ivSelectorImage.length; i++) {
			if (ivSelectorImage[i] != null) {
				ivSelectorImage[i].setVisibility(View.VISIBLE);
			}
		}
	}

	public void invisibleSelectorImage() {
		for (int i = 0; i < ivSelectorImage.length; i++) {
			if (ivSelectorImage[i] != null) {
				ivSelectorImage[i].setVisibility(View.GONE);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//			case R.id.ivOne:
//				pSelected = pView1;
//				//onGalleryClicked();
//			//	OpenCamera();
//				break;
//			case R.id.ivTwo:
//				pSelected = pView2;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivThree:
//				pSelected = pView3;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivFour:
//				pSelected = pView4;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivFive:
//				pSelected = pView5;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivSix:
//				pSelected = pView6;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivSeven:
//				pSelected = pView7;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivEight:
//				pSelected = pView8;
//				//onGalleryClicked();
//				//OpenCamera();
//				break;
//			case R.id.ivNine:
//				pSelected = pView9;
//				//onGalleryClicked();
//			//	OpenCamera();
//				break;
			//case R.id.tvGrid:
//				Intent intent = new Intent(CollegeActivity.this, GridActivity.class); //
//				intent.putExtra(Utils.CALL_CHOOSER_FROM_GRID_CLICK, true);
//				startActivityForResult(intent, CALL_CHOOSER_FROM_GRID_CLICK);
			//	break;
			case R.id.tvShape:
				if (rlSliderShapeView != null) {
					rlSliderShapeView.setVisibility(View.VISIBLE);
					setTextOk();
					resetFlag();
					isShapeFlag = true;
				}
				setTextOk();
				break;
//			case R.id.tvFrame:
//				setTextOk();
//				resetFlag();
//				isFrameFlag = true;
//				loadBG_OR_STICKER_OR_Frame(Utils.ASSET_FRAME);
//				break;
			case R.id.tvColorPicker:
				setTextOk();
				resetFlag();
				isColorPicker = true;
				rlColorPicker.setVisibility(View.VISIBLE);
				break;
			case R.id.tvBG:
				setTextOk();
				resetFlag();
				isBGFrameFlag = true;
				loadBG_OR_STICKER_OR_Frame(Utils.ASSET_BG);
				break;
			case R.id.tvText:
				setTextOk();
				resetFlag();
				isFrameFlag = true;
				loadBG_OR_STICKER_OR_Frame(Utils.ASSET_FONTS);
				//isBGFrameFlag = true;
				isTextFlag=true;
				mFontColor++;
				addBubble();
				break;

			case R.id.ivSave:
				ivSave.setImageResource(R.drawable.save);
				hListView.setVisibility(View.GONE);
				rlSliderShapeView.setVisibility(View.GONE);
				rlColorPicker.setVisibility(View.GONE);
				mContentRootView.setVisibility(View.VISIBLE);
				//rlTvText.setVisibility(View.VISIBLE);
				Log.d("SIZE", String.valueOf(mViews.size()));



				//rlTvText.setVisibility(View.GONE);
				//tvText.setVisibility(View.GONE);

				if (!isBGFrameFlag && !isColorPicker && !isFrameFlag
						&& !isShapeFlag) {

					for (int i = 0; i < mViews.size(); i++) {
						final View child = mViews.get(i);


						((BubbleTextView) child).setInEdit(false);

						child.setVisibility(View.VISIBLE);
						((RelativeLayout) child.getParent()).removeView(child);
						rlScreenShot.addView(child);

					}


					final Dialog dialog = new Dialog(CollegeActivity.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawable(
							new ColorDrawable(Color.TRANSPARENT));
					dialog.setContentView(R.layout.save_share_add_picture);

							resetFlag();
							isShare = true;
							dialog.dismiss();
							new StoreImage().execute();
//							resetFlag();
//							dialog.dismiss();
//							isShare = true;
//							//isSticker = true;
//							isTextFlag = true;
//							new StoreImage().execute();

					Intent intent = new Intent(this, SplashActivity.class);
					//intent.putExtra("isShare",true);
					startActivity(intent);
					//finish();
//
//					resetFlag();
//							isShare = true;
//							dialog.dismiss();
//							new StoreImage().execute();

//					TextView tvSave = (TextView) dialog.findViewById(R.id.tvSave);
//					TextView tvShare = (TextView) dialog.findViewById(R.id.tvShare);
//					TextView tvAddSticker = (TextView) dialog
//							.findViewById(R.id.tvAddSticker);
//
//					tvSave.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							resetFlag();
//							isShare = false;
//							dialog.dismiss();
//							new StoreImage().execute();
//						}
//					});
//
//					tvShare.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							resetFlag();
//							isShare = true;
//							dialog.dismiss();
//							new StoreImage().execute();
//						}
//					});
//
//					tvAddSticker.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
////							resetFlag();
////							dialog.dismiss();
////							isSticker = true;
////							new StoreImage().execute();
//						}
//					});
					//dialog.show();

				}
				resetFlag();
				break;
			case R.id.ivBack:
//				Intent intentBack = new Intent(CollegeActivity.this, GridActivity.class); //yuvraj
//				intentBack.putExtra(Utils.CALL_CHOOSER_FROM_GRID_CLICK, true);
//				startActivityForResult(intentBack, CALL_CHOOSER_FROM_GRID_CLICK);
				break;
		}

	}

	private void addBubble() {
		final BubbleTextView bubbleTextView = new BubbleTextView(this,Color.WHITE, 0);
		bubbleTextView.setText(textpref);
		bubbleTextView.changeFont(Integer.parseInt(fontspref));
		bubbleTextView.setImageResource(R.drawable.bubble_7_rb);
		selectedBubbleView = bubbleTextView;

		bubbleTextView.setOperationListener(new BubbleTextView.OperationListener() {
			@Override
			public void onDeleteClick() {
				mViews.remove(bubbleTextView);
				mContentRootView.removeView(bubbleTextView);
			}

			@Override
			public void onEdit(BubbleTextView bubbleTextView) {
				if (mCurrentView != null) {
					mCurrentView.setInEdit(false);
				}
				mCurrentEditTextView.setInEdit(false);
				mCurrentEditTextView = bubbleTextView;
				mCurrentEditTextView.setInEdit(true);
			}

			@Override
			public void onClick(BubbleTextView bubbleTextView) {
				selectedBubbleView = bubbleTextView;
				mBubbleInputDialog.setBubbleTextView(bubbleTextView);
				mBubbleInputDialog.show();
			}

			@Override
			public void onTop(BubbleTextView bubbleTextView) {
				int position = mViews.indexOf(bubbleTextView);
				if (position == mViews.size() - 1) {
					return;
				}
				BubbleTextView textView = (BubbleTextView) mViews.remove(position);
				mViews.add(mViews.size(), textView);
			}

			@Override
			public void onColorChange(BubbleTextView bubbleTextView) {

			}




		});
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		mContentRootView.addView(bubbleTextView, lp);

		mViews.add(bubbleTextView);
		setCurrentEdit(bubbleTextView);
	}

	private void setCurrentEdit(BubbleTextView bubbleTextView) {
		if (mCurrentView != null) {
			mCurrentView.setInEdit(false);
		}
		if (mCurrentEditTextView != null) {
			mCurrentEditTextView.setInEdit(false);
		}
		mCurrentEditTextView = bubbleTextView;
		mCurrentEditTextView.setInEdit(true);
	}

    public void viewForIndex(int index) {
        // TODO Auto-generated method stub
        switch (index) {
            case 0:
                pSelected = pView1;
                //onGalleryClicked();
                //	OpenCamera();
                break;
            case 1:
                pSelected = pView2;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 2:
                pSelected = pView3;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 3:
                pSelected = pView4;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 4:
                pSelected = pView5;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 5:
                pSelected = pView6;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 6:
                pSelected = pView7;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 7:
                pSelected = pView8;
                //onGalleryClicked();
                //OpenCamera();
                break;
            case 8:
                pSelected = pView9;
                //onGalleryClicked();
                //	OpenCamera();
                break;



        }

    }

	private void setTextOk() {
		ivSave.setImageResource(R.drawable.ok);
	}

	private void loadBG_OR_STICKER_OR_Frame(String path) {
		hListView.setVisibility(View.VISIBLE);
		if (isFrameFlag) {
			if (strArrayAssetFrame == null) {
				strArrayAssetFrame = loadImagesFromAssets(path);
			}
			hListView.setAdapter(new HorizontalItemAdapter(
					CollegeActivity.this, strArrayAssetFrame));

		} else if (isStickerFlag) {
			if (strArrayAssetStickerLove == null) {
				strArrayAssetStickerLove = loadImagesFromAssets(path);
			}
			hListView.setAdapter(new HorizontalItemAdapter(
					CollegeActivity.this, strArrayAssetStickerLove));

		} else if (isBGFrameFlag) {
			if (strArrayAssetBG == null) {
				strArrayAssetBG = loadImagesFromAssets(path);
			}

			hListView.setAdapter(new HorizontalItemAdapter(
					CollegeActivity.this, strArrayAssetBG));

		}
		else if(isTextFlag){
			if(strTextView == null){
				strTextView = loadImagesFromAssets(path);
			}
			hListView.setAdapter(new HorizontalItemAdapter(CollegeActivity.this,strTextView));

		}

	}

	private void resetFlag() {
		isBGFrameFlag = false;
		isFrameFlag = false;
		isStickerFlag = false;
		isColorPicker = false;
		isShapeFlag = false;
		isSticker = false;
		isTextFlag = false;

	}

	private String[] loadImagesFromAssets(String path) {
		try {
			Resources res = getResources(); // if you are in an activity
			AssetManager am = res.getAssets();
			String fileList[] = am.list(path);

			String[] gridViewItems = new String[fileList.length];
			if (fileList != null) {
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].contains(".png")
							|| fileList[i].contains(".jpg")) {
						gridViewItems[i] = path + File.separator + fileList[i];
					}
					if (Utils.debug) {
						Log.d("", fileList[i]);
					}
				}
			}
			return gridViewItems;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void OpenCamera() {

		Log.d("No Of Images", String.valueOf(total_shots_allow));
		//requestForCameraPermission();


	}

	private void onGalleryClicked() {

		Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
		intentGallery.setType("image/*");
		Intent chooser = Intent
				.createChooser(intentGallery, "Choose a Picture");
		startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
	}

	private Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM/", "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		this.imgPath = file.getAbsolutePath();
		return imgUri;
	}

	public void requestForCameraPermission() {
       // launch();
		final String permission = "android.permission.CAMERA";//Manifest.permission.CAMERA;
		if (ContextCompat.checkSelfPermission(CollegeActivity.this, permission)
				!= PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(CollegeActivity.this, permission)) {
				showPermissionRationaleDialog("Test", permission);
			} else {
				requestForPermission(permission);
			}
		} else {
			launch();
		}
	}

	private void showPermissionRationaleDialog(final String message, final String permission) {
        Log.d("No Of Images showPP", String.valueOf(total_shots_allow));

        new AlertDialog.Builder(CollegeActivity.this)
				.setMessage(message)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CollegeActivity.this.requestForPermission(permission);
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.create()
				.show();
	}

	private void requestForPermission(final String permission) {
        Log.d("No Of Images req", String.valueOf(total_shots_allow) +"**" +permission);
		ActivityCompat.requestPermissions(CollegeActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
	}

	private void launch() {

		Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
		startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CAMERA_PERMISSION:
				final int numOfRequest = grantResults.length;
				final boolean isGranted = numOfRequest == 1
						&& PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
				if (isGranted) {
					launch();
				}
				break;

			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				Uri photoUri = data.getData();
				// Get the bitmap in according to the width of the device
				Log.d("MyImageX", String.valueOf(mSize.x));
				Log.d("MyImageY", String.valueOf(mSize.y));
				Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.y);
                viewForIndex(currentImageIndex);
                pSelected.removeAllImages();
				//yuvraj
                pSelected.loadImages(
                        CollegeActivity.this,
                        bitmap);
//                pSelected.loadImages(
//                        CollegeActivity.this,
//                        decodeFileFromUri(
//                                Uri.parse(getPath(data.getData())), false));
                currentImageIndex++;

               if(currentImageIndex < total_shots_allow) {

                  // ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);


                   requestForCameraPermission();
               }
			}
			//super.onActivityResult(requestCode, resultCode, data);
/*
switch (requestCode) {
case ACTION_REQUEST_GALLERY:
//				if (pSelected != null) {
//					pSelected.removeAllImages();
//					pSelected.loadImages(
//							CollegeActivity.this,
//							decodeFileFromUri(
//									Uri.parse(getPath(data.getData())), false));
//				}
break;

case ACTION_REQUEST_CAMERA:
//				if (pSelected != null) {
//					pSelected.removeAllImages();
//					pSelected.loadImages(CollegeActivity.this,
//							decodeFileFromUri(cameraUri, true));
//
//				}
break;

case CALL_CHOOSER_FROM_GRID_CLICK:
//				if (data != null) {
//					redSeekBar.setProgress(0);
//					greenSeekBar.setProgress(0);
//					blueSeekBar.setProgress(0);
//					int id = data.getIntExtra(Utils.GRID_ITEM_NO, 0);
//					setLayoutBasedFromChooser(id);
//				}
}
*/

		}

	}

	private Bitmap decodeFileFromUri(Uri selectedImageUri, boolean fromCamera) {
		try {
			Bitmap bitmap;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			if (fromCamera) {
				BitmapFactory.decodeFile(imgPath, options);
				options.inSampleSize = calculateInSampleSize(options);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(this.imgPath, options);
				ExifInterface exif = new ExifInterface(this.imgPath);
				String orientString = exif
						.getAttribute(ExifInterface.TAG_ORIENTATION);
				int orientation = orientString != null ? Integer
						.parseInt(orientString)
						: ExifInterface.ORIENTATION_NORMAL;
				int rotationAngle = 0;
				if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
					rotationAngle = 90;
				if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
					rotationAngle = 180;
				if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
					rotationAngle = 270;
				System.out.println("ROTATION ANGLE  =   " + rotationAngle);
				Matrix matrix = new Matrix();
				matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2,
						(float) bitmap.getHeight() / 2);

				bitmap = Bitmap.createBitmap(bitmap, 0, 0, options.outWidth,
						options.outHeight, matrix, true);
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(this.imgPath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {

							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return bitmap;
			} else {
				BitmapFactory.decodeFile(String.valueOf(selectedImageUri),
						options);
				options.inSampleSize = calculateInSampleSize(options);
				options.inJustDecodeBounds = false;
				return BitmapFactory.decodeFile(
						String.valueOf(selectedImageUri), options);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void changeFrameThickNess(int progress) {
		if (params1 != null) {
			params1.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params1.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params1.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView1.setLayoutParams(params1);
			pView1.invalidate();
		}
		if (params2 != null) {
			params2.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params2.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params2.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView2.setLayoutParams(params2);
		}
		if (params3 != null) {
			params3.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params3.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params3.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView3.setLayoutParams(params3);
		}
		if (params4 != null) {
			params4.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params4.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params4.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView4.setLayoutParams(params4);
		}

		if (params5 != null) {
			params5.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params5.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params5.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView5.setLayoutParams(params5);
		}

		if (params6 != null) {
			params6.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params6.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params6.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView6.setLayoutParams(params6);
		}

		if (params7 != null) {
			params7.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params7.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params7.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView7.setLayoutParams(params7);
		}
		if (params8 != null) {
			params8.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params8.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params8.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView8.setLayoutParams(params8);
		}

		if (params9 != null) {
			params9.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			params9.height = RelativeLayout.LayoutParams.MATCH_PARENT;
			params9.setMargins(progress / 2, progress / 2, progress / 2,
					progress / 2);
			pView9.setLayoutParams(params9);
		}

	}

	private int calculateInSampleSize(BitmapFactory.Options options) {
		Resources res = getResources();

		DisplayMetrics metrics = res.getDisplayMetrics();
		int reqWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.max(metrics.widthPixels, metrics.heightPixels) : Math.min(
				metrics.widthPixels, metrics.heightPixels);
		int reqHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.min(metrics.widthPixels, metrics.heightPixels) : Math.max(
				metrics.widthPixels, metrics.heightPixels);

		reqWidth = (int) (reqWidth); // / 1.5);
		reqHeight = (int) (reqWidth); // / 1.5);

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height/2; // / 2;
			final int halfWidth = width/2; // / 2;  yuvraj

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param //context The context.
	 * @param uri       The Uri to query.
	 * @author paulburke
	 */
	private String getPath(final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat
				&& DocumentsContract.isDocumentUri(CollegeActivity.this, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};

				return getDataColumn(contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param //context     The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Uri uri, String selection,
								String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};

		try {
			cursor = CollegeActivity.this.getContentResolver().query(uri,
					projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	@Override
	public void onStart() {
		super.onStart();

//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		//client.connect();
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"College Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app URL is correct.
//				Uri.parse("android-app://com.pixr.photo.collage/http/host/path")
//		);
//		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"College Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app URL is correct.
//				Uri.parse("android-app://com.pixr.photo.collage/http/host/path")
//		);
//		AppIndex.AppIndexApi.end(client, viewAction);
//		client.disconnect();
	}

	class StoreImage extends AsyncTask<Void, Void, Void> {
		String fileSavedPath;
		ProgressDialog pDialog;

		@Override
		protected void onPostExecute(Void result) {

			visibleSelectorImage();

			pDialog.dismiss();
			if (isSticker) {
				Intent intent = new Intent(CollegeActivity.this,
						AddStickerActivity.class);
				intent.putExtra(Utils.SAVED_FILE_PATH_FOR_STICKER,
						fileSavedPath);
				startActivity(intent);
				isSticker = false;

			} else if (isShare) {
				Intent shareimage = new Intent(
						Intent.ACTION_SEND);
				shareimage.setType("*/*");// for all
				shareimage.putExtra(Intent.EXTRA_STREAM,
						Uri.parse("file://" + fileSavedPath));
				shareimage
						.putExtra(
								Intent.EXTRA_TEXT,
								"I have shared this using "
										+ getString(R.string.app_name)
										+ " app, download it at http://play.google.com/store/apps/details?id="
										+ getPackageName());
				startActivity(Intent.createChooser(shareimage, "Share Collage"));
			} else {
				Toast.makeText(CollegeActivity.this,
						"Collage is saved at " + fileSavedPath,
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			invisibleSelectorImage();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				pDialog = new ProgressDialog(
						new ContextThemeWrapper(CollegeActivity.this,
								android.R.style.Theme_Holo_Dialog));
			} else {
				pDialog = new ProgressDialog(CollegeActivity.this);
			}

			if (isSticker) {
				pDialog.setTitle("Please wait");
				pDialog.setMessage("Preparing for Sticker");
			} else if (!isShare) {
				pDialog.setTitle("Saving Image");
				pDialog.setMessage("Please wait, while image is saving");
			} else {
				pDialog.setTitle("Share Image");
				pDialog.setMessage("Please wait, preparing image for share");
			}
			pDialog.setCancelable(false);
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root + File.separator
					+ getResources().getString(R.string.app_name));
			myDir.mkdirs();

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyyMMdd_HH_mm_ss");
			String currentTimeStamp = dateFormat.format(new Date());

			String fname = "Frame_" + currentTimeStamp + "" + ".png";
			File file = new File(myDir, fname);
			fileSavedPath = file.getAbsolutePath();
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);

				getBitmapFromView(squareFrameLayout).compress(
						Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
				MediaScannerConnection.scanFile(CollegeActivity.this,
						new String[]{fileSavedPath},
						new String[]{"image/png"}, null);
			} catch (Exception e) {
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(CollegeActivity.this,
								"Some thing wrong happen", Toast.LENGTH_SHORT)
								.show();
						pDialog.dismiss();
					}
				});
			}
			return null;
		}

	}

	public Bitmap getBitmapFromView(View view) {
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		else
			canvas.drawColor(Color.TRANSPARENT);
		view.draw(canvas);
		return returnedBitmap;
	}

	@Override
	public void onBackPressed() {
		if (isBGFrameFlag || isFrameFlag || isStickerFlag || isColorPicker || isShapeFlag || isTextFlag) {
			ivSave.setImageResource(R.drawable.save);
			hListView.setVisibility(View.GONE);
			rlColorPicker.setVisibility(View.GONE);
			rlSliderShapeView.setVisibility(View.GONE);
			//rlTvText.setVisibility(View.GONE);
			resetFlag();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									dialog.dismiss();
									CollegeActivity.this.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									dialog.cancel();

								}
							});
			builder.show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		if (Utils.debug) {
			System.out.println("isBGFrameFlag = " + isBGFrameFlag);
			System.out.println("isFrameFlag = " + isFrameFlag);
			System.out.println("isStickerFlag = " + isStickerFlag);
			System.out.println("isText =" + isTextFlag);
		}
		if (Utils.debug) {
			// System.out.println("Path = " + strArrayAssetBG[position] + ", "
			// + stub);
		}
		if (isBGFrameFlag) {
			for (int i = 0; i < mViews.size(); i++) {
				final View child = mViews.get(i);
				((BubbleTextView) child).setInEdit(false);
				child.setVisibility(View.VISIBLE);
//				if(mFontColor == i+1){
//					((BubbleTextView) child).changeFont(position);
//
//				}
				//Log.d("child ki Value",String.valueOf(mViews.get(i)));

			}


			//}
			if (llBackground != null) {
				llBackground.setBackground(new BitmapDrawable(getResources(),
						ImageLoader.getInstance().loadImageSync(
								"assets://" + strArrayAssetBG[position]))); //strArrayAssetBG[position]
			}
		} else if (isFrameFlag) {
			if (squareFrameLayout != null) {
				//yuyu
				for (int i = 0; i < mViews.size(); i++) {
					final View child = mViews.get(i);
					if(child.equals(selectedBubbleView)){}else {
						((BubbleTextView) child).setInEdit(false);
					}
					child.setVisibility(View.VISIBLE);
					((BubbleTextView) selectedBubbleView).changeFont(position);
				}
//				ivFrame.setBackground(new BitmapDrawable(getResources(),
//						ImageLoader.getInstance().loadImageSync(
//								"assets://" + strArrayAssetFrame[position])));
			}

		} else if (isStickerFlag) {
			if (pStickerView != null) {
				pStickerView.loadImages(
						CollegeActivity.this,
						ImageLoader.getInstance().loadImageSync(
								"assets://"
										+ strArrayAssetStickerLove[position]));
			}

		}
		else if(isTextFlag){
//			if(mCurrentEditTextView !=null){
//				mContentRootView.setVisibility(View.VISIBLE);
//			}
							for (int i = 0; i < mViews.size(); i++) {
					final View child = mViews.get(i);
					((BubbleTextView) child).setInEdit(false);
					child.setVisibility(View.VISIBLE);
					((BubbleTextView) child).changeFont(i);
				}


		}
		// hListView.setVisibility(View.GONE);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
								  boolean fromUser) {

		if (seekBar.getId() == R.id.redSeekBar) {

			red = progress;
			thumbRect = seekBar.getThumb().getBounds();

			redToolTip.setX(seekBarLeft + thumbRect.left);

			if (progress < 10)
			{
				redToolTip.setText("  " + red);
				//mFontColor.setBackgroundColor(Color.RED); //yuvi
//				for (int i = 0; i < mViews.size(); i++) {
//					final View child = mViews.get(i);
//					((BubbleTextView) child).setInEdit(false);
//					child.setVisibility(View.VISIBLE);
//					child.setBackgroundColor(progress);
//				}

			}
			else if (progress < 100)
				redToolTip.setText(" " + red);
			else
				redToolTip.setText(red + "");

		} else if (seekBar.getId() == R.id.greenSeekBar) {

			green = progress;
			thumbRect = seekBar.getThumb().getBounds();

			greenToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
			if (progress < 10)
				greenToolTip.setText("  " + green);
			else if (progress < 100)
				greenToolTip.setText(" " + green);
			else
				greenToolTip.setText(green + "");

		} else if (seekBar.getId() == R.id.blueSeekBar) {

			blue = progress;
			thumbRect = seekBar.getThumb().getBounds();

			blueToolTip.setX(seekBarLeft + thumbRect.left);
			if (progress < 10)
				blueToolTip.setText("  " + blue);
			else if (progress < 100)
				blueToolTip.setText(" " + blue);
			else
				blueToolTip.setText(blue + "");

		}

		llBackground.setBackgroundColor(Color.rgb(red, green, blue));


	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}
