package com.polyethene.hkgolden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.Bundle;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryActivity extends Activity {
	ArrayList<HashMap<String, String>> historyList = null;
	ListView list = null;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (historyList == null)
		{
			historyList = new ArrayList<HashMap<String,String>>();
		}
		SharedPreferences history = getSharedPreferences("history", 0);
		try {
			historyList = (ArrayList<HashMap<String, String>>) ObjectSerializer.deserialize(history.getString("history", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_history);
		list = (ListView) findViewById(R.id.historyListView);
		makelist(historyList);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (historyList == null)
		{
			historyList = new ArrayList<HashMap<String,String>>();
		}
		SharedPreferences history = getSharedPreferences("history", 0);

		try {
			historyList = (ArrayList<HashMap<String, String>>) ObjectSerializer.deserialize(history.getString("history", ObjectSerializer.serialize(new ArrayList<HashMap<String,String>>())));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_history);
		list = (ListView) findViewById(R.id.historyListView);
		makelist(historyList);
	}
	
	
	private void makelist(final ArrayList<HashMap<String, String>> historyListForDisplay) {
		// Log.d("test12345", firstPage1.toString());
		// 绑定XML中的ListView，作为Item的容器
		// ListView list = (ListView) findViewById(R.id.listView1);
		Collections.reverse(historyListForDisplay);
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				historyListForDisplay,// 数据来源
				R.layout.history_list_item,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "Message_Title" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.historyTitle });
		// 添加并且显示
		list.setAdapter(mSchedule);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				    int position, long id) {
				
				
				HashMap<String, String> historyMap = new HashMap<String, String>();
				historyMap.put("Message_ID", historyListForDisplay.get(position).get("Message_ID"));
				historyMap.put("Message_Title", historyListForDisplay.get(position).get("Message_Title"));
				historyMap.put("Replies", historyListForDisplay.get(position).get("Replies"));
				
				Intent intent = new Intent(getApplicationContext(), RepliesWebViewActivity.class);
				intent.putExtra("selectedPage", "1");
//				intent.putExtra("Total_Replies", historyListForDisplay.get(position).get("Replies"));
				intent.putExtra("Message_ID", historyListForDisplay.get(position).get("Message_ID"));
				intent.putExtra("Message_Title", historyListForDisplay.get(position).get("Message_Title"));
				Log.d("Total_Replies", historyListForDisplay.get(position).get("Replies"));
				Log.d("Message_ID", historyListForDisplay.get(position).get("Message_ID"));
				Log.d("Message_Title", historyListForDisplay.get(position).get("Message_Title"));
				
				if (historyList.contains(historyMap)) {
					if(historyList.remove(historyMap))
					{
						historyList.add(historyMap);
					}
				}
				else {
					historyList.add(historyMap);
				}
				
				SharedPreferences history = getSharedPreferences("history", 0);
				SharedPreferences.Editor editor = history.edit();
				ObjectMapper mapper = new ObjectMapper();
				try {
					String jsonString = mapper.writeValueAsString(historyList);
					editor.putString("history", jsonString);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				try {
					editor.putString("history", ObjectSerializer.serialize(historyList));
				} catch (IOException e) {
					e.printStackTrace();
				}
				editor.commit();
				
				
				startActivity(intent);
			}
			
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

}
