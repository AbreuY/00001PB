package com.pixr.photo.collage.grid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pixr.photo.collage.R;
import com.pixr.photo.collage.Utils;

import java.io.File;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
	private Context mContext;
	public List<GridViewItem> mItems;

	public static final String MyPREFERENCES = "MyPrefs";

	SharedPreferences sharedpreferences;


	public GridViewAdapter(Context context, List<GridViewItem> items) {
		mContext = context;
		mItems = items;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			// inflate the GridView item layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.gridview_item, parent,
					false);

			// initialize the view holder
			viewHolder = new ViewHolder();
			viewHolder.ivIcon = (ImageView) convertView
					.findViewById(R.id.ivIcon);
			convertView.setTag(viewHolder);
		} else {
			// recycle the already inflated view
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// update the item view
		GridViewItem item = mItems.get(position);
//		sharedpreferences = sharedpreferences
//
//		String SSS = sharedpreferences.getString("Layout",null);
		ImageLoader.getInstance().displayImage(
				"assets://" + Utils.ASSET_COLLEGE_FRAME + File.separator
						+ item.icon, viewHolder.ivIcon);
		Log.d("---THIS IS IT----", item.icon);

//		ImageLoader.getInstance().displayImage(
//				"assets://" + Utils.ASSET_COLLEGE_FRAME + File.separator
//						+ (position+1)+".png", viewHolder.ivIcon);

		return convertView;

	}

	/**
	 * The view holder design pattern prevents using findViewById() repeatedly
	 * in the getView() method of the adapter.
	 * 
	 * @see http 
	 *      ://developer.android.com/training/improving-layouts/smooth-scrolling
	 *      .html#ViewHolder
	 */
	private static class ViewHolder {
		ImageView ivIcon;
	}
}