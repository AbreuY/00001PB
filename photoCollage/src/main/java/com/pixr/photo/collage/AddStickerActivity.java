package com.pixr.photo.collage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pixr.photo.collage.horizontal_listview.HorizontalItemAdapter;
import com.pixr.photo.collage.widget.PhotoSortrView;
import com.pixr.photo.collage.widget.SquareFrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddStickerActivity extends Activity implements OnItemClickListener {

	private ImageView ivCollage, ivSave, ivBack;
	private HorizontalListView hListSticker;
	private PhotoSortrView pStickerView;
	private String[] strArrayAssetSticker;
	com.pixr.photo.collage.widget.SquareFrameLayout squareFrameLayout;
	private boolean isShare = false;
	private boolean isSaveOrShareClicked = false;
	private TextView tvRemoveSticker;
	String path = "";
	private InterstitialAd interstitial;
	AdView mAdView;
	private AdRequest adRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sticker_view);

//		adRequest = new AdRequest.Builder().build();
//		interstitial = new InterstitialAd(this);
//		interstitial.setAdUnitId(getResources().getString(
//				R.string.full_screen_ad_unit_id));
//		mAdView = (AdView) findViewById(R.id.adView);
//		mAdView.loadAd(adRequest);

		ivCollage = (ImageView) findViewById(R.id.ivFrame);
		ivSave = (ImageView) findViewById(R.id.ivSave);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tvRemoveSticker = (TextView) findViewById(R.id.ivRemoveSticker);
		pStickerView = (PhotoSortrView) findViewById(R.id.photoSorterStickerView);
		squareFrameLayout = (SquareFrameLayout) findViewById(R.id.squareFrameLayout);

		Intent intent = getIntent();
		path = intent.getStringExtra(Utils.SAVED_FILE_PATH_FOR_STICKER);
		ImageLoader.getInstance().displayImage("file:///" + path, ivCollage);

		hListSticker = (HorizontalListView) findViewById(R.id.hlvCustomList);
		hListSticker.setOnItemClickListener(this);

		strArrayAssetSticker = loadImagesFromAssets(Utils.ASSET_STICKER_SUB_LOVE);

		hListSticker.setAdapter(new HorizontalItemAdapter(
				AddStickerActivity.this, strArrayAssetSticker));

		ivSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(AddStickerActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.save_share);
				TextView tvSave = (TextView) dialog.findViewById(R.id.tvSave);
				TextView tvShare = (TextView) dialog.findViewById(R.id.tvShare);

				tvSave.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						isShare = false;
						new StoreImage().execute();
						dialog.dismiss();

					}

				});

				tvShare.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						isShare = true;
						new StoreImage().execute();
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});

		ivBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isSaveOrShareClicked) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							AddStickerActivity.this);
					builder.setMessage("Want to go back, changes will be lost?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
											AddStickerActivity.this.finish();
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();

										}
									});
					builder.show();
				} else {
					AddStickerActivity.this.finish();
				}
			}
		});

		tvRemoveSticker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pStickerView.unloadImages();
			}
		});
	}

	private void removeTempImage() {
		try {
			if (!path.equals("")) {
				new File(path).delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				AddStickerActivity.this);
		builder.setMessage("Want to go back, changes will be lost?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								removeTempImage();
								dialog.dismiss();
								AddStickerActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (pStickerView != null) {
			pStickerView.loadImages(
					AddStickerActivity.this,
					ImageLoader.getInstance().loadImageSync(
							"assets://" + strArrayAssetSticker[position]));
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

	class StoreImage extends AsyncTask<Void, Void, Void> {
		String fileSavedPath;
		ProgressDialog pDialog;

		@Override
		protected void onPostExecute(Void result) {

			pDialog.dismiss();
			if (isShare) {
				Intent shareimage = new Intent(
						android.content.Intent.ACTION_SEND);
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
				startActivity(Intent.createChooser(shareimage, "Share Image"));
			} else {
				Toast.makeText(AddStickerActivity.this,
						"Image is saved at " + fileSavedPath,
						Toast.LENGTH_SHORT).show();
			}
			removeTempImage();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				pDialog = new ProgressDialog(new ContextThemeWrapper(
						AddStickerActivity.this,
						android.R.style.Theme_Holo_Dialog));
			} else {
				pDialog = new ProgressDialog(AddStickerActivity.this);
			}

			if (!isShare) {
				isSaveOrShareClicked = true;
				pDialog.setTitle("Saving Image");
				pDialog.setMessage("Please wait, while image is saving");
			} else {
				isSaveOrShareClicked = true;
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
				MediaScannerConnection.scanFile(AddStickerActivity.this,
						new String[] { fileSavedPath },
						new String[] { "image/png" }, null);
			} catch (Exception e) {
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(AddStickerActivity.this,
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

}