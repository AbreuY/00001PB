package com.pixr.photo.collage.grid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pixr.photo.collage.CollegeActivity;
import com.pixr.photo.collage.R;
import com.pixr.photo.collage.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridActivity extends Activity {

	private GridViewAdapter mAdapter;
	GridView gridView;
	boolean choosedFromGridClickFlag = false;

	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences sharedpreferences;
	private InterstitialAd interstitial;
	AdView mAdView;
	private AdRequest adRequest;
	public String SSS;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_collage_layout);

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		SSS = sharedpreferences.getString("Layout",null);

		if(SSS == null){SSS="";}
		Log.d("LayoutValue",SSS);


		displayFullScreenAds();

		Intent intent = getIntent();
		if (intent != null) {
			choosedFromGridClickFlag = intent.getBooleanExtra(
					Utils.CALL_CHOOSER_FROM_GRID_CLICK, false);
		}

		gridView = (GridView) findViewById(R.id.gridView);
		try {



			mAdapter = new GridViewAdapter(GridActivity.this,
					listFiles(Utils.ASSET_COLLEGE_FRAME));
			gridView.setAdapter(mAdapter);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		try
//		{
//			// get input stream
//			InputStream ims = getAssets().open("avatar.jpg");
//			// load image as Drawable
//			Drawable d = Drawable.createFromStream(ims, null);
//			// set image to ImageView
//
//			//mImage.setImageDrawable(d);
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				GridViewItem item = mAdapter.mItems.get(position);
				Log.d("ITEM--", String.valueOf(item));
				String filename=item.icon;
				String[] parts = filename.split("\\.(?=[^\\.]+$)");
				String part1 = parts[0]; // 004
				String part2 = parts[1];
				int valueOfImage = Integer.parseInt(part1);
				Log.d("part1",part2);
				Log.d("valueofimage",String.valueOf(valueOfImage));
				if (!choosedFromGridClickFlag) {

					if (Utils.debug) {
						System.out.println("Call from start");
					}

					//Intent intent = new Intent(GridActivity.this,
					//CollegeActivity.class);
					Intent intent = new Intent(GridActivity.this,
							CollegeActivity.class);
					intent.putExtra(Utils.GRID_ITEM_NO, position);
					startActivity(intent);
					GridActivity.this.finish();
				} else {

					if (Utils.debug) {
						System.out.println("Call from Choosed From Click Grid");
					}

					Intent intent = new Intent();
					intent.putExtra(Utils.GRID_ITEM_NO, position);
					setResult(RESULT_OK, intent);
					GridActivity.this.finish();
				}

			}
		});
	}


	private List<GridViewItem> listFiles(String dirFrom) throws IOException {
		Resources res = getResources(); // if you are in an activity
		AssetManager am = res.getAssets();
		String fileList[] = am.list(dirFrom);

		ArrayList<GridViewItem> gridViewItems = new ArrayList<GridViewItem>();
		if (fileList != null) {
			String S2 = SSS + ".png";
			for (int i = 0; i < fileList.length; i++){
				if (fileList[i].contains(".png")) {
int indexImg = i+1;

					String theDesiredFile = indexImg + ".png";
//					if(theDesiredFile.equals(fileList[i])){
//						Log.d("---YSC----", fileList[i]);
//						gridViewItems.add(new GridViewItem(theDesiredFile));
//						Log.d("filename",theDesiredFile);
//
//					}
					gridViewItems.add(new GridViewItem(theDesiredFile));

				}
				if (Utils.debug) {
					//Log.d("", fileList[i]);
				}
			}
		}
		return gridViewItems;
	}

	private void displayFullScreenAds() {
//		interstitial.loadAd(adRequest);
//		interstitial.setAdListener(new AdListener() {
//			public void onAdLoaded() {
//				interstitial.show();
//			}
//		});
	}

}
