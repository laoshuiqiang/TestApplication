package com.demo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.weixin.R;

public class FindTabFragment extends Fragment {
	
	FindTabFragmentToActivity findTabFragmentToActivity ;
	
	public interface FindTabFragmentToActivity{
		public void toActivity();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_findtab, container, false);
		Button button = (Button) view.findViewById(R.id.btn_friend_circle);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//findTabFragmentToActivity.toActivity();
			}
		});
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		findTabFragmentToActivity = (FindTabFragmentToActivity)activity;
	}
}
