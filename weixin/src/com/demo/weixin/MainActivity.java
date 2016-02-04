package com.demo.weixin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.adapter.ViewPagerAdapter;
import com.demo.fragment.ChatMainTabFragment;
import com.demo.fragment.ContactTabFragment;
import com.demo.fragment.ContactTabFragment.ConnWithActivity;
import com.demo.fragment.FindTabFragment;
import com.demo.fragment.FindTabFragment.FindTabFragmentToActivity;
import com.demo.fragment.MeTabFragment;
import com.demo.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ConnWithActivity,FindTabFragmentToActivity {

	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAapter;

	private LinearLayout mChatLinearLayout;
	private LinearLayout mContactLinearLayout;
	private LinearLayout mFindLinearLayout;
	private LinearLayout mMeLinearLayout;
	

	private LinearLayout mmChatLinearLayout;
	private LinearLayout mmContactLinearLayout;
	private LinearLayout mmFindLinearLayout;
	private LinearLayout mmMeLinearLayout;
	
	private TextView mChatTextView;
	private TextView mContactTextView;
	private TextView mFindTextView;
	private TextView mMeTextView;
	
	private ImageView mChatImageView;
	private ImageView mContactImageView;
	private ImageView mFindImageView;
	private ImageView mMeImageView;

	private ImageView mTabLine;

	private BadgeView mBadgeViewForChat;

	private int widthScreen1_4;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ò���ʾϵͳActionBar
		setContentView(R.layout.activity_main);
		
		initView();
		initTabLine();

		
	}

	private void initTabLine() {
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		widthScreen1_4 = displayMetrics.widthPixels / 4;

		LayoutParams lp = mTabLine.getLayoutParams();
		lp.width = widthScreen1_4;
		mTabLine.setLayoutParams(lp);
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.id_viewpager);

		mChatTextView = (TextView) findViewById(R.id.tv_chat);
		mContactTextView = (TextView) findViewById(R.id.tv_contact);
		mFindTextView = (TextView) findViewById(R.id.tv_find);
		mMeTextView = (TextView) findViewById(R.id.tv_me);
		
		mChatImageView = (ImageView) findViewById(R.id.iv_chat);
		mContactImageView = (ImageView) findViewById(R.id.iv_contact);
		mFindImageView = (ImageView) findViewById(R.id.iv_find);
		mMeImageView = (ImageView) findViewById(R.id.iv_me);

		mTabLine = (ImageView) findViewById(R.id.iv_tabline);

		List<Fragment> list = new ArrayList<Fragment>();
		list.add(new ChatMainTabFragment());
		list.add(new ContactTabFragment());
		list.add(new FindTabFragment());
		list.add(new MeTabFragment());

		viewPagerAapter = new ViewPagerAdapter(getSupportFragmentManager(),
				list); // ����ViewPager��������

		viewPager.setAdapter(viewPagerAapter); // ����������
		//viewPager.fakeDragBy(10);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				Toast.makeText(MainActivity.this, position + "",
						Toast.LENGTH_SHORT).show();
				resetTextViewColor(); // �Ȱ����嶼����Ϊ��ɫ
				resetImageViewSrc();   //�Ȱ�ͼ�궼����Ϊ��ɫ
				switch (position) {
				case 0:
					if (mBadgeViewForChat != null) {
						mChatLinearLayout.removeView(mBadgeViewForChat);
					}
					mBadgeViewForChat = new BadgeView(MainActivity.this);
					mBadgeViewForChat.setBadgeCount(13);
					mBadgeViewForChat.setTargetView(mmChatLinearLayout);
					mChatTextView.setTextColor(Color.parseColor("#008000"));
					mChatImageView.setImageResource(R.drawable.chat);
					break;
				case 1:
					mContactTextView.setTextColor(Color.parseColor("#008000"));
					mContactImageView.setImageResource(R.drawable.contact);
					break;
				case 2:
					mFindTextView.setTextColor(Color.parseColor("#008000"));
					mFindImageView.setImageResource(R.drawable.find);
					break;
				case 3:
					mMeTextView.setTextColor(Color.parseColor("#008000"));
					mMeImageView.setImageResource(R.drawable.me);
					break;
				}
			}

			private void resetImageViewSrc() {
				mChatImageView.setImageResource(R.drawable.chat1);
				mContactImageView.setImageResource(R.drawable.contact1);
				mFindImageView.setImageResource(R.drawable.find1);
				mMeImageView.setImageResource(R.drawable.me1);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffPx) {
				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
						.getLayoutParams();
				lp.leftMargin = (int) ((position + positionOffset) * widthScreen1_4);
				mTabLine.setLayoutParams(lp);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mChatLinearLayout = (LinearLayout) findViewById(R.id.ll_chat);
		mContactLinearLayout = (LinearLayout) findViewById(R.id.ll_contact);
		mFindLinearLayout = (LinearLayout) findViewById(R.id.ll_find);
		mMeLinearLayout = (LinearLayout) findViewById(R.id.ll_me);
		
		mmChatLinearLayout = (LinearLayout) findViewById(R.id.ll_chat_small);
		mmContactLinearLayout = (LinearLayout) findViewById(R.id.ll_contact_small);
		mmFindLinearLayout = (LinearLayout) findViewById(R.id.ll_find_small);
		mmMeLinearLayout = (LinearLayout) findViewById(R.id.ll_me_small);

		BottomLayoutListener listener = new BottomLayoutListener();// ���õײ���ť���¼�����

		mChatLinearLayout.setOnClickListener(listener);
		mContactLinearLayout.setOnClickListener(listener);
		mFindLinearLayout.setOnClickListener(listener);
		mMeLinearLayout.setOnClickListener(listener);

	}

	private void resetTextViewColor() {
		mChatTextView.setTextColor(Color.parseColor("#A6A6A6"));
		mContactTextView.setTextColor(Color.parseColor("#A6A6A6"));
		mFindTextView.setTextColor(Color.parseColor("#A6A6A6"));
		mMeTextView.setTextColor(Color.parseColor("#A6A6A6"));
	}

	class BottomLayoutListener implements OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_chat:
				viewPager.setCurrentItem(0, true);
				Toast.makeText(MainActivity.this, "0", Toast.LENGTH_SHORT)
						.show();
				break;
			case R.id.ll_contact:
				viewPager.setCurrentItem(1, true);
				Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT)
						.show();
				break;
			case R.id.ll_find:
				viewPager.setCurrentItem(2, true);
				Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT)
						.show();
				break;
			case R.id.ll_me:
				viewPager.setCurrentItem(3, true);
				Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	@Override
	public void toActivity(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void toActivity() {
	}
}
