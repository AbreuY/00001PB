package com.pixr.photo.collage.horizontal_listview;

import android.content.Context;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pixr.photo.collage.R;

/**
 * An array adapter that knows how to render views when given CustomData classes
 */
public class HorizontalItemAdapter extends ArrayAdapter<String> {
	private LayoutInflater mInflater;
	String[] imageList;

	public HorizontalItemAdapter(Context context, String[] values) {
		super(context, R.layout.horizontal_data_view, values);
		imageList = values;
		mInflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return imageList.length;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			// Inflate the view since it does not exist
			convertView = mInflater.inflate(R.layout.horizontal_data_view,
					parent, false);

			// Create and save off the holder in the tag so we get quick access
			// to inner fields
			// This must be done for performance reasons
			holder = new Holder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.ivHorizontalItem);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage("assets://" + imageList[position],
				holder.imageView);

		return convertView;
	}

	/** View holder for the views we need access to */
	private static class Holder {
		public ImageView imageView;
	}
}
