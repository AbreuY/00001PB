package com.pixr.photo.collage;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.parse.Parse;
import com.parse.PushService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CollegeApplication extends Application {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		super.onCreate();


//		String valid_until = "7/7/2016";
//		Boolean catalog_outdated=false;
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		Date strDate = null; // sdf.parse(valid_until);
//		try {
//			strDate = sdf.parse(valid_until);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		if (new Date().after(strDate)) {
//			catalog_outdated = true;
//		}
//
//		if(!catalog_outdated) {
			initImageLoader(getApplicationContext());

			// Initialize the Parse SDK.
			Parse.initialize(this, "zwrS5czzt975t1sn17jb8w1jB67kQ4RpvovbvWM3",
					"xMNCVcGHlJWqsXtjOr1J7I5a4n2uvgnolyN1o7ta");

		//}
		// Specify an Activity to handle all pushes by default.
		//PushService.startServiceIfRequired(this);
	}

	public static void initImageLoader(Context context) {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(600)) // default
				.build();
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.writeDebugLogs(); // Remove for release app
		config.defaultDisplayImageOptions(options);
		ImageLoader.getInstance().init(config.build());
	}
}
