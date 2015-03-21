package com.fay.activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
/**
 * @author Fay
 * {@link 1940125001@qq.com}
 */
public class BaseActivity extends Activity {
	/**Handler What加载数据完毕**/
	public static final int WHAT_DID_LOAD_DATA = 0;
	/**Handler What更新数据完毕**/
	public static final int WHAT_DID_REFRESH = 1;
	/**Handler What更多数据完毕**/
	public static final int WHAT_DID_MORE = 2;
	/**Handler What加载数据失败**/
	public static final int WHAT_DID_FAILED = 3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}
	
	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

	protected void showLongToast(int pResId) {
		showLongToast(getString(pResId));
	}
	
	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected boolean hasExtra(String pExtraKey) {
		if (getIntent() != null) {
			return getIntent().hasExtra(pExtraKey);
		}
		return false;
	}
	
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
		
	}
	
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	
	protected void openActivityForResult(Class<?> pClass) {
		openActivityForResult(pClass, null);
	}
	
	protected void openActivityForResult(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivityForResult(intent, 1);
	}
	
	public void finish() {
		super.finish();
	}
	
	public void defaultFinish() {
		super.finish();
	}

	// 判断返回
	boolean isBack = false;

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isBack = true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void onPause() {
		if (isBack) {
			isBack = false;
		}
		super.onPause();
	}

}
