package com.adrian.android.agileframework.abstracts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.adrian.android.agileframework.ApplicationProject;
import com.adrian.android.agileframework.R;

public abstract class BaseActivity extends FragmentActivity {

	private long exitTime = 0;
	public static boolean isRoot = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationProject.getInstance().addActivity(this);
		initView();
		setDataToView();
	}

	protected abstract void initView();

	protected abstract void setDataToView();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isRoot) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(),
							"Press again to quit the app", Toast.LENGTH_SHORT)
							.show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					ApplicationProject.getInstance().exit();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void replaceFragment(Fragment newFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.base_fragment_id, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void replaceFragment(Fragment newFragment, Bundle bundle) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		newFragment.setArguments(bundle);
		ft.replace(R.id.base_fragment_id, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void redirectFragment(Fragment newFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.base_fragment_id, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void redirectFragment(Fragment newFragment, Bundle bundle) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		newFragment.setArguments(bundle);
		ft.add(R.id.base_fragment_id, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}

}
