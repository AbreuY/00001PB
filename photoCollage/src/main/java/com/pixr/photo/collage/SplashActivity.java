package com.pixr.photo.collage;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.parse.Parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends Activity {
	protected boolean _isActive = true;
	protected int _splashTime = 3000; // SplashActivity will be visible for 2s
	final String TAG = "SplashActivity";
	Boolean catalog_outdated=false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String valid_until = "12/7/2016";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date strDate = null; // sdf.parse(valid_until);
		try {
			strDate = sdf.parse(valid_until);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (new Date().after(strDate)) {
			catalog_outdated = true;
		}

		if(!catalog_outdated) {



		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);


		ImageView v1 = (ImageView) findViewById(R.id.icon_view);
		v1.setVisibility(View.INVISIBLE);

		try {
			final VideoView videoHolder = (VideoView)this.findViewById(R.id.videoView1);
			Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);

//			DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//			android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoHolder.getLayoutParams();
//			params.width =  metrics.widthPixels;
//			params.height = metrics.heightPixels;
//			params.leftMargin = 0;
//			videoHolder.setLayoutParams(params);

			videoHolder.setVideoURI(video);

			videoHolder.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					//loadMain();
					videoHolder.start();
				}
			});
			videoHolder.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					videoHolder.setVisibility(View.INVISIBLE);

					loadMain();
					return true;
					//return false;
				}
			});
			videoHolder.start();

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
			loadMain();
		}



//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.splash);
//
//		// a separate thread to manage splash screen
//		final Thread splashThread = new Thread() {
//			public void run() {
//				try {
//					int wait = 0;
//					while (_isActive && (_splashTime > wait)) { // will show
//																// only on the
//																// first time
//						sleep(100);
//						if (_isActive) {
//							wait += 100;
//						}
//					}
//				} catch (InterruptedException e) {
//					Log.d(TAG, e.getMessage());
//
//				} finally {
//					Intent mainActivity = new Intent(SplashActivity.this,
//							GridActivity.class);
//					startActivity(mainActivity);
//					finish();
//				}
//			}
//		};
//		splashThread.start();

		}
	}


	protected void loadMain() {
//
		if(catalog_outdated) {
			return ;
		}
//		ImageView v1 = (ImageView) findViewById(R.id.icon_view);
//		v1.setVisibility(View.VISIBLE);

		Intent intent = new Intent(this, Sliders.class);
		startActivity(intent);
		finish();
	}
	// if a user clicks on a back btnOrder, do not show splash screen

	public boolean onTouchEvent(MotionEvent event) {
		if(catalog_outdated) {
			return false;
		}
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_isActive = false;
		}
		return true;
	}
}