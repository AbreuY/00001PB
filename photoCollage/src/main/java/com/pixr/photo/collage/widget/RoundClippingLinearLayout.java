package com.pixr.photo.collage.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RoundClippingLinearLayout extends RelativeLayout {

	private Paint drawPaint;
	private Paint roundPaint;
	private boolean flag;
	public static int mCornerRadius = 1;
	BitmapShader shader;
	private RectF bounds;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public RoundClippingLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		onInit();
	}

	public RoundClippingLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		onInit();
	}

	public RoundClippingLinearLayout(Context context) {
		super(context);
		onInit();
	}

	protected void onInit() {
		drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		flag = true;
		setWillNotDraw(false);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);

		if (w != oldw && h != oldh) {
			bounds = new RectF(0, 0, w, h);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		System.out.println("Dispatch Draw Called");
// Draw from asset		
//		final Options bitmapOptions = new Options();
//		AssetManager assetManager = mContext.getAssets();
//		bitmapOptions.inMutable = true;
//		InputStream inputStream = assetManager.open("bg/1.jpg");
//		Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
//				bitmapOptions);
//
//		Canvas c = new Canvas(bitmap);
		Bitmap bitmap = Bitmap.createBitmap((int) bounds.width(),
				(int) bounds.height(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		super.dispatchDraw(c);

		BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		Paint paint = new Paint();
//		paint.setColor(Color.TRANSPARENT);
		paint.setAntiAlias(true);
		paint.setShader(shader);

		canvas.drawRoundRect(bounds, mCornerRadius * 3, mCornerRadius * 3,
				paint);
	//	invalidate();
	}

}
