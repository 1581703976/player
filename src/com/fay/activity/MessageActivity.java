package com.fay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.accounts.OnAccountsUpdateListener;
import android.app.UiAutomation.OnAccessibilityEventListener;
import android.content.Intent;
import android.media.MediaPlayer.OnTimedTextListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView.OnEditorActionListener;

import com.fay.adapter.ListMessageAdapter;
import com.fay.message.R;
import com.fay.widget.PullDownView;
import com.fay.widget.PullDownView.OnPullDownListener;

/**
 * @author Fay
 * {@link 1940125001@qq.com}
 */
public class MessageActivity extends BaseActivity implements OnPullDownListener,OnGestureListener{
	private ListView mListView = null;
	private ListMessageAdapter adapter = null;
	private PullDownView mPullDownView = null;
	private ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private static RelativeLayout topLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		topLayout = (RelativeLayout) findViewById(R.id.message_top);
		initListView();
		GestureDetector gg = new GestureDetector(this,this);
//		GestureDetector gg=new GestureDetector(this, new OnGestureListener() {
//			
//			@Override
//			public boolean onSingleTapUp(MotionEvent e) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public void onShowPress(MotionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//					float distanceY) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public void onLongPress(MotionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//					float velocityY) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public boolean onDown(MotionEvent e) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
	}

	/**
	 * show the top view
	 */
	public static void showTop() {
		topLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * hidden the top view
	 */
	public static void hiddenTop() {
		topLayout.setVisibility(View.GONE);
	}

	/**
	 * initialize the ListView
	 */
	private void initListView() {
		mPullDownView = (PullDownView) this.findViewById(R.id.message_listview);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		adapter = new ListMessageAdapter(MessageActivity.this);
		adapter.setData(data);
		mListView.setAdapter(adapter);
		mListView.setVisibility(View.GONE);
		mPullDownView.enableAutoFetchMore(false, 1);
		mPullDownView.setHideFooter();
		// mPullDownView.setShowFooter();
		mPullDownView.setHideHeader();
		mPullDownView.setShowHeader();
		mPullDownView.setHideFooter();
		onLoad();
	}

	/**
	 * load the data firstly
	 */
	private void onLoad() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				ArrayList<Map<String, Object>> loaddatas = new ArrayList<Map<String, Object>>();
				for (int index = 0; index < 10; index++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("index", index);
					loaddatas.add(map);
					map = null;
				}
				if (null == loaddatas || loaddatas.equals(null)) {
					msg = mUIHandler.obtainMessage(WHAT_DID_FAILED);// 加载数据失败
				} else {
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					msg.obj = loaddatas;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mPullDownView.RefreshComplete();
				Message msg = new Message();
				// Test data
				ArrayList<Map<String, Object>> loaddatas = new ArrayList<Map<String, Object>>();
				for (int index = 0; index < 50; index++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("index", index);
					loaddatas.add(map);
					map = null;
				}
				if (null == loaddatas || loaddatas.equals(null)) {
					msg = mUIHandler.obtainMessage(WHAT_DID_FAILED);// 加载数据失败
				} else {
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					msg.obj = loaddatas;
				}
				msg.sendToTarget();
			}
		}).start();

	}

	@Override
	public void onMore() {

	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_FAILED:
				break;
			case WHAT_DID_LOAD_DATA: 
				ArrayList<Map<String, Object>> loaddatas = (ArrayList<Map<String, Object>>) msg.obj;
				data.clear();
				data.addAll(loaddatas);
				mListView.setVisibility(View.VISIBLE);
				// Animation
				Animation animation = AnimationUtils.loadAnimation(
						MessageActivity.this, R.anim.list_anim);
				LayoutAnimationController controller = new LayoutAnimationController(
						animation, 10);
				mListView.setLayoutAnimation(controller);
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (1 == requestCode) {
			if (1 == resultCode) {
				showTop();
			}
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}


}
