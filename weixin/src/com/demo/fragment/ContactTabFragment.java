package com.demo.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.contact.CharacterParser;
import com.demo.contact.ClearEditText;
import com.demo.contact.PinyinComparator;
import com.demo.contact.SideBar;
import com.demo.contact.SideBar.OnTouchingLetterChangedListener;
import com.demo.contact.SortAdapter;
import com.demo.contact.SortModel;
import com.demo.weixin.R;

public class ContactTabFragment extends Fragment {

	private View view;

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private ConnWithActivity connWithActivity;

	public interface ConnWithActivity{       //定义一个接口，用来跟Activity通信，实现低耦合
		public void toActivity(String str);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view =  inflater.inflate(R.layout.fragment_contacttab, container, false);
		initViews();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		connWithActivity = (ConnWithActivity)activity;
	}

	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				connWithActivity.toActivity(((SortModel)adapter.getItem(position)).getName());
				//Toast.makeText(getActivity(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});

		SourceDateList = filledData(getResources().getStringArray(R.array.date));

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(adapter);


		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);

		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
}
