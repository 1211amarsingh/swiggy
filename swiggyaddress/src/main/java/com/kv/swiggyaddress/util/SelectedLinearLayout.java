package com.kv.swiggyaddress.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SelectedLinearLayout extends LinearLayout {
	private int selectedPosition = -1;

	public SelectedLinearLayout(Context context, AttributeSet attrs,
                                int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr);
	}

	public SelectedLinearLayout(Context context, AttributeSet attrs,
                                int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SelectedLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectedLinearLayout(Context context) {
		super(context);
	}

	public void setSelected(int position) {
		selectedPosition = position;
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			view.setEnabled(!(i == position));
			setSelected(view, i == position);
		}
	}

	public int getSelected() {
		return selectedPosition;
	}

	private void setSelected(View view, boolean selected) {
		view.setSelected(selected);

		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View chieldView = ((ViewGroup) view).getChildAt(i);

				setSelected(chieldView, selected);
			}
		}

	}

}