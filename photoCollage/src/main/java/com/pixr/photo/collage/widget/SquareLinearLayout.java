package com.pixr.photo.collage.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class SquareLinearLayout extends LinearLayout {

	public SquareLinearLayout(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
	}

	public SquareLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

	}

	int squareDim = 1000000000;

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int w = this.getMeasuredWidth();

		setMeasuredDimension(w, w);

	}
}