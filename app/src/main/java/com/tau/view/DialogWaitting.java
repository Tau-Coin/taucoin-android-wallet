package com.tau.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mofei.tau.R;


public class DialogWaitting {
	

	private Dialog mDialog = null;
	private TextView mTextView = null;
	
	public DialogWaitting(Context context) {
		mDialog = new Dialog(context, R.style.dialog_translucent);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mDialog.setContentView(R.layout.dialog_waiting);
		mTextView = (TextView)mDialog.findViewById(R.id.waittingText);

	}

	public void setOnCancelListener(DialogInterface.OnCancelListener listener){
		mDialog.setOnCancelListener(listener);
	}


	public void setCancelable(boolean cancelable){
		mDialog.setCancelable(cancelable);
	}
	
	public void show() {
		mTextView.setText("load...");
		try {
			mDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			if( e instanceof WindowManager.BadTokenException ){
					//eat it
			}else{
				throw  e;
			}
		}
	}
	
	public void show(String hint) {
		mTextView.setText(hint);
		try {
			mDialog.show();
		}catch (Exception e) {
			e.printStackTrace();
			if( e instanceof WindowManager.BadTokenException ){
				//eat it
			}else{
				throw  e;
			}
		}

	}
	
	public void show(@StringRes int hint) {
		mTextView.setText(hint);
		try {
			mDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
			if( e instanceof WindowManager.BadTokenException ){
				//eat it
			}else{
				throw  e;
			}
		}
	}
	
	public void dismiss() {
		mDialog.dismiss();
	}

	public boolean isShowing() {
		return mDialog.isShowing();
	}
}
