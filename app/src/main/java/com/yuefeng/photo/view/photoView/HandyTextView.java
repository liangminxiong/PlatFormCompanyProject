package com.yuefeng.photo.view.photoView;

import android.content.Context;
import android.util.AttributeSet;

public class HandyTextView extends android.support.v7.widget.AppCompatTextView {

	public HandyTextView(Context context) {
		super(context);
	}

	public HandyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HandyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (text == null) {
			text = "";
		}
		super.setText(text, type);
	}
}
