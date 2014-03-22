package com.adrian.android.agileframework.abstracts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseFragment extends Fragment {

	protected LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	protected abstract void initView(View v);

	protected abstract void setDataToView();
	
	
}
