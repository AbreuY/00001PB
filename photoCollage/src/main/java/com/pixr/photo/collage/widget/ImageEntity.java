/*
 * Code based off the PhotoSortrView from Luke Hutchinson's MTPhotoSortr
 * example (http://code.google.com/p/android-multitouch-controller/)
 *
 * License:
 *   Dual-licensed under the Apache License v2 and the GPL v2.
 */
package com.pixr.photo.collage.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class ImageEntity extends MultiTouchEntity {

//	private static final double INITIAL_SCALE_FACTOR = 0.15;
	private static final double INITIAL_SCALE_FACTOR = 0.5;
	private Paint paint;
	private transient Drawable mDrawable;
	BitmapShader shader;
	static Resources res;

	public ImageEntity(Drawable resourceId) {
		mDrawable = resourceId;
	}

	public ImageEntity(ImageEntity e, Resources res) {
		super(res);
		this.res = res;
		mScaleX = e.mScaleX;
		mScaleY = e.mScaleY;
		mCenterX = e.mCenterX;
		mCenterY = e.mCenterY;
		mAngle = e.mAngle;

	}

	public void draw(Canvas canvas) {
		canvas.save();

		float dx = (mMaxX + mMinX) / 2;
		float dy = (mMaxY + mMinY) / 2;

		mDrawable.setBounds((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY);

		canvas.translate(dx, dy);
		canvas.rotate(mAngle * 180.0f / (float) Math.PI);
		canvas.translate(-dx, -dy);

		mDrawable.draw(canvas);

		canvas.restore();
	}

	public static BitmapDrawable getRoundedCornerBitmap(Bitmap bitmap, int pixels)
			throws NullPointerException {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = Color.parseColor("#000000");
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return new BitmapDrawable(res, output);
	}

	/**
	 * Called by activity's onPause() method to free memory used for loading the
	 * images
	 */
	@Override
	public void unload() {
		this.mDrawable = null;
	}

	/** Called by activity's onResume() method to load the images */
	@Override
	public void load(Context context, float startMidX, float startMidY) {
		Resources res = context.getResources();
		getMetrics(res);

		mStartMidX = startMidX;
		mStartMidY = startMidY;

		mWidth = mDrawable.getIntrinsicWidth();
		mHeight = mDrawable.getIntrinsicHeight();
		//DisplayMetrics dm = new DisplayMetrics();
		//context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//int width = dm.widthPixels;
		//int height = width * mainImage.getHeight() / mainImage.getWidth();
		float centerX;
		float centerY;
		float scaleX;
		float scaleY;
		float angle;
		float scaleFactorNew;
		if (mFirstLoad) {
			centerX = startMidX;
			centerY = startMidY;

			float scaleFactor = startMidX*2/mWidth;//Math.min(mDisplayWidth, mWidth)/ Math.max(mDisplayWidth, mWidth);
//			float scaleFactor = (float) (Math
//					.max(mDisplayWidth, mDisplayHeight)
//					/ (float) Math.max(mWidth, mHeight) * INITIAL_SCALE_FACTOR);
			scaleX = scaleY = scaleFactor;
			angle = 0.0f;

			mFirstLoad = false;
		} else {
			centerX = mCenterX;
			centerY = mCenterY;
			scaleX = mScaleX;
			scaleY = mScaleY;
			angle = mAngle;
		}
		setPos(centerX, centerY, scaleX, scaleY, mAngle);
	}
}
