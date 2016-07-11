/**
 * PhotoSorterView.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * TODO: Add OpenGL acceleration.
 * 
 * Released under the Apache License v2.
 */
package com.pixr.photo.collage.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.pixr.photo.collage.widget.MultiTouchController.MultiTouchObjectCanvas;
import com.pixr.photo.collage.widget.MultiTouchController.PointInfo;
import com.pixr.photo.collage.widget.MultiTouchController.PositionAndScale;

import java.util.ArrayList;

public class PhotoSortrView extends View implements
		MultiTouchObjectCanvas<MultiTouchEntity> {

	// drawing and canvas paint
	private Paint drawPaint, canvasPaint, colorPaint;
	// initial color
	private int paintColor = Color.TRANSPARENT;
	// canvas
	private Canvas drawCanvas;
	// canvas bitmap
	private Bitmap canvasBitmap;
 	Point centerPoint;
	GestureDetector gestureDetector;

	RoundClippingLinearLayout rcLL;
	// public PhotoSortrView(Context context, AttributeSet attrs) {
	// super(context, attrs);

	Rect rectf = new Rect();
	// }
	private ArrayList<MultiTouchEntity> imageIDs = new ArrayList<MultiTouchEntity>();

	// --

	private MultiTouchController<MultiTouchEntity> multiTouchController = new MultiTouchController<MultiTouchEntity>(
			this);

	// --

	private PointInfo currTouchPoint = new PointInfo();

	private boolean mShowDebugInfo = true;

	private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

	private int mUIMode = UI_MODE_ROTATE;

	// --

	private static final float SCREEN_MARGIN = 100;

	// ---------------------------------------------------------------------------------------------------

	public PhotoSortrView(Context context) {
		this(context, null);
	}

	public PhotoSortrView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		setupDrawing();
	}

	public PhotoSortrView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// loadImages(context);
	}

	private void init(Context context) {
		colorPaint = new Paint();
		colorPaint.setColor(Color.BLACK);
		colorPaint.setStyle(Style.FILL);
		setBackgroundColor(Color.TRANSPARENT);
		this.getLocalVisibleRect(rectf);
		int width = this.getResources().getDisplayMetrics().widthPixels;
		Log.d("YWIDTH1", String.valueOf(width));
		int height = this.getResources().getDisplayMetrics().heightPixels;
		Log.d("YHEIGHT", String.valueOf(height));
		centerPoint = new Point(width,height);
		Log.d("CENTERX",String.valueOf(centerPoint.x));
		rcLL = new RoundClippingLinearLayout(context);

	}

	/** Called by activity's onResume() method to load the images */
	public void loadImages(Context context, Bitmap mDrawable) {

		float scaleSize =1024;
		float cx = getWidth();/// 2;   //yuvraj
		Log.d("cx", Float.toString(cx));
		float cy = getHeight();// / 2;
		Log.d("cy", Float.toString(cy));
		int width = this.getResources().getDisplayMetrics().widthPixels/4;
		Log.d("YWIDTH", String.valueOf(mDrawable.getWidth()));
		int height = this.getResources().getDisplayMetrics().heightPixels/4;
		centerPoint = new Point(width,height);
		float scaleHeight = cx/mDrawable.getHeight(); //y*2 (float)centerPoint.y / (float)
		float scaleWidth = cy/mDrawable.getWidth(); //x*2  (float)centerPoint.x / (float)
		float scale = Math.min(scaleWidth,scaleHeight);

		if(cx>cy){
			scaleSize = cx;

		}

		float originalWidth = mDrawable.getWidth();
		float originalHeight = mDrawable.getHeight();
		float newWidth = -1;
		float newHeight = -1;
		float multFactor = -1.0F;
		if(originalHeight > originalWidth) {
			newHeight = scaleSize ;
			multFactor = (float) originalWidth/(float) originalHeight;
			newWidth = (int) (newHeight*multFactor);
		} else if(originalWidth > originalHeight) {
			newWidth = scaleSize ;
			multFactor = (float) originalHeight/ (float)originalWidth;
			newHeight = (int) (newWidth*multFactor);
		} else if(originalHeight == originalWidth) {
			newHeight = scaleSize ;
			newWidth = scaleSize ;
		}
		Bitmap resizedImage = Bitmap.createBitmap(mDrawable,0,0,
				(int)(newWidth ), (int)(newHeight ),null,false);   // multiplies by scale

//		Bitmap resizedImage = Bitmap.createBitmap(mDrawable,0,0,
//				(int)(mDrawable.getWidth()*scale ), (int)(mDrawable.getHeight()*scale ),null,false);   // multiplies by scale

		imageIDs.add(new ImageEntity(new BitmapDrawable(context.getResources(),
				mDrawable)));

		//Log.d("cy",cy);

		imageIDs.get(imageIDs.size() - 1).load(context, cx/2, cy/2);

		invalidate();
	}

	public void removeAllImages() {

		imageIDs.removeAll(imageIDs);
		invalidate();
	}

	/**
	 * Called by activity's onPause() method to free memory used for loading the
	 * images
	 */
	public void unloadImages() {
		if (imageIDs.size() > 0) {
			imageIDs.remove(imageIDs.size() - 1);
		}

		invalidate();
	}

	// ---------------------------------------------------------------------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		int n = imageIDs.size();
		for (int i = 0; i < n; i++) {
			imageIDs.get(i).draw(canvas);

		}

		 rcLL.invalidate();
	}

	// ---------------------------------------------------------------------------------------------------

	public void trackballClicked() {
		mUIMode = (mUIMode + 1) % 3;
		invalidate();
	}

	/** Pass touch events to the MT controller */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		requestLayout();

		return multiTouchController.onTouchEvent(event);
	}

	/**
	 * Get the image that is under the single-touch point, or return null
	 * (canceling the drag op) if none
	 */
	public MultiTouchEntity getDraggableObjectAtPoint(PointInfo pt) {
		float x = pt.getX(), y = pt.getY();
		int n = imageIDs.size();
		for (int i = n - 1; i >= 0; i--) {
			ImageEntity im = (ImageEntity) imageIDs.get(i);
			if (im.containsPoint(x, y))
				return im;
		}
		return null;
	}

	/**
	 * Select an object for dragging. Called whenever an object is found to be
	 * under the point (non-null is returned by getDraggableObjectAtPoint()) and
	 * a drag operation is starting. Called with null when drag op ends.
	 */
	public void selectObject(MultiTouchEntity img, PointInfo touchPoint) {
		currTouchPoint.set(touchPoint);
		if (img != null) {
			// Move image to the top of the stack when selected
			drawPaint.setColor(Color.TRANSPARENT);
			imageIDs.remove(img);
			imageIDs.add(img);
		} else {
			// Called with img == null when drag stops.
		}
		invalidate();
	}

	/**
	 * Get the current position and scale of the selected image. Called whenever
	 * a drag starts or is reset.
	 */
	public void getPositionAndScale(MultiTouchEntity img,
			PositionAndScale objPosAndScaleOut) {

		// FIXME affine-izem (and fix the fact that the anisotropic_scale part
		// requires averaging the two scale factors)
		objPosAndScaleOut.set(img.getCenterX(), img.getCenterY(),
				(mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
				(img.getScaleX() + img.getScaleY()) / 2,
				(mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getScaleX(),
				img.getScaleY(), (mUIMode & UI_MODE_ROTATE) != 0,
				img.getAngle());
	}

	/** Set the position and scale of the dragged/stretched image. */
	public boolean setPositionAndScale(MultiTouchEntity img,
			PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
		currTouchPoint.set(touchPoint);
		boolean ok = ((ImageEntity) img).setPos(newImgPosAndScale);
		if (ok)
			invalidate();
		return ok;
	}

	public boolean pointInObjectGrabArea(PointInfo pt, MultiTouchEntity img) {
		return false;
	}

	private void setupDrawing() {

		// prepare for drawing and setup paint stroke properties
		// drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(10);
		drawPaint.setStyle(Paint.Style.STROKE);
		// drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);

	}

	// size assigned to view
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w > 0 && h > 0) {

			super.onSizeChanged(w, h, oldw, oldh);
			canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			drawCanvas = new Canvas(canvasBitmap);
		}
	}

	// draw the view - will be called after touch event

	// register user touches as drawing action

	// update color
	public void setColor(String newColor) {
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	public void clearCanvas() {
		drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		invalidate();

	}

	public void setTranspertColor() {
		drawPaint.setColor(Color.TRANSPARENT);
	}
}
