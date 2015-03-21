package com.fay.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fay.activity.MessageActivity;
import com.fay.message.R;

/**
 * the adapter of the ListView
 * 
 * @author Fay {@link 1940125001@qq.com}
 */
public class ListMessageAdapter extends BaseAdapter {
	private String TAG = "ListMessageAdapter";
	private Context context = null;
	private Holder holder = null;
	private LayoutInflater inflater = null;

	// the last position clicked
	private int mLastPosition = -1;

	// check whether a touch action is finish
	private boolean loadFinish = false;

	// the position of click and move, start and end point
	private Point startPoint, endPoint;

	// the animation of removing the item
	private Animation animation = null;

	// the children item is common
	private final int TYPE_ITEM = 0;

	// the children item is searching
	private final int TYPE_SEARCH = 1;

	// the count of children item's type
	private final int TYPE_COUNT = TYPE_SEARCH + 1;

	// data container
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	public ListMessageAdapter(Context context) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.startPoint = new Point();
		this.endPoint = new Point();
		animation = AnimationUtils.loadAnimation(context, R.anim.push_out);
	}

	public void setData(ArrayList<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setDataTemp(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void clearData() {
		list.clear();
		notifyDataSetChanged();
	}

	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? TYPE_SEARCH : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		if (null == convertView) {
			holder = new Holder();
			switch (type) {
			case TYPE_SEARCH:
				convertView = inflater.inflate(R.layout.search, null);
				holder.searchview = (LinearLayout) convertView
						.findViewById(R.id.search);
				break;
			case TYPE_ITEM:
				convertView = inflater
						.inflate(R.layout.list_item_message, null);
				holder.linearlayout = (LinearLayout) convertView
						.findViewById(R.id.message_linear);
				holder.content = (TextView) convertView
						.findViewById(R.id.message_detail);
				holder.delete = (TextView) convertView
						.findViewById(R.id.message_delete);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final int chickPosition = position;

		switch (type) {
		case TYPE_SEARCH:
			// hidden the top view
			holder.searchview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MessageActivity.hiddenTop();
				}
			});
			break;
		case TYPE_ITEM:
			holder.content.setText("消息" + list.get(chickPosition).get("index") + ":你好哦，我想你咯");
			final int finalPosition = position;
			if (position == mLastPosition) {
				holder.delete.setVisibility(View.VISIBLE);
			} else {
				holder.delete.setVisibility(View.GONE);
			}

			// 删除
			final View view = holder.linearlayout;

			holder.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					view.startAnimation(animation);
					animation.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation arg0) {
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {
						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							list.remove(chickPosition);
							mLastPosition = -1;
							notifyDataSetChanged();
						}
					});
				}
			});

			holder.linearlayout.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						loadFinish = false;
						startPoint.set((int) event.getX(), (int) event.getY());
						break;
					case MotionEvent.ACTION_MOVE:
						endPoint.set((int) event.getX(), (int) event.getY());
						if (Math.abs(endPoint.x - startPoint.x) > 30) {
							if (loadFinish == false) {
								loadFinish = true;
								if (finalPosition != mLastPosition) {
									mLastPosition = finalPosition;
								} else {
									mLastPosition = -1;
								}
								notifyDataSetChanged();
							} else {
								return true;
							}
							return true;
						}
						if (Math.abs(endPoint.y - startPoint.y) > 30) {
							return false;
						}
						break;
					case MotionEvent.ACTION_UP:
						break;
					}
					return false;
				}
			});

			holder.linearlayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mLastPosition != -1) {
						mLastPosition = -1;
						notifyDataSetChanged();
					} else {
						Toast.makeText(context, "您点击了 -> " + chickPosition, 2000).show();
					}
				}
			});
			break;
		}
		return convertView;
	}

	private class Holder {
		TextView content;
		TextView delete;
		LinearLayout linearlayout;
		LinearLayout searchview;
	}

}
