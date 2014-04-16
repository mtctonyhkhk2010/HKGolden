package com.polyethene.hkgolden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.*;

public class PostListActivity extends Activity {
	TopicList topicList = null;
	ArrayList<HashMap<String, String>> topicListForDisplay = new ArrayList<HashMap<String, String>>();
	ListView list = null;
	Integer postAdded = 30;
	ArrayList<HashMap<String, String>> historyList = null;
	Boolean Firstload = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
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
		
		setContentView(R.layout.activity_post_list);
		list = (ListView) findViewById(R.id.listView1);
		
		list.setOnScrollListener(new EndlessScrollListener());
		topicHttpClient("BW", "1");

	}
	private Toast backtoast;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub		
        if(backtoast!=null&&backtoast.getView().getWindowToken()!=null) {
            finish();
        } else {
            backtoast = Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
	}

	private void makelist(TopicList firstPage1) {
		// Log.d("test12345", firstPage1.toString());
		// 绑定XML中的ListView，作为Item的容器
		// ListView list = (ListView) findViewById(R.id.listView1);


		
		if (firstPage1 == null) {
			topicListForDisplay.clear();
		} else {
			for (int i = 0; i < firstPage1.topic_list.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Message_ID", firstPage1.topic_list.get(i).Message_ID);
				map.put("ItemTitle", firstPage1.topic_list.get(i).Message_Title);
				map.put("ItemText", firstPage1.topic_list.get(i).Author_Name);
				map.put("Rating", " (Rating:"
						+ firstPage1.topic_list.get(i).Rating + ")");
				map.put("Replies", "Replies:"
						+ firstPage1.topic_list.get(i).Total_Replies);
				
				if(!topicListForDisplay.contains(map))
				{
					topicListForDisplay.add(map);
				}
			}
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				topicListForDisplay,// 数据来源
				R.layout.post_list_item,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "ItemTitle", "ItemText", "Rating", "Replies" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.ItemTitle, R.id.ItemText, R.id.Rating,
						R.id.Replies });
		// 添加并且显示
		int firstVisible = list.getFirstVisiblePosition();
		mSchedule.notifyDataSetChanged();
		if(Firstload)
		{
			list.setAdapter(mSchedule);
		}
		
		list.setSelection(firstVisible);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				    int position, long id) {
				
				
				goToReplies(topicListForDisplay, position, 1);
			}
			
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				String[] pageForSelection = new String[Integer.parseInt(topicListForDisplay.get(position).get("Replies").substring(8))/25+1];
				for (int i = 0; i < pageForSelection.length; i++) {
					pageForSelection[i] = i+1+"";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(PostListActivity.this);
			    builder.setTitle("Select Page")
			           .setItems(pageForSelection, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			               // The 'which' argument contains the index position
			               // of the selected item
			            	   goToReplies(topicListForDisplay, position, which+1);
			           }
			    });
			    builder.show();
				return true;
			}
		});
	}
	private void goToReplies(ArrayList<HashMap<String, String>> topicListForDisplay, Integer position, Integer page)
	{
		if (page == null) {
			page = 1;
		}
		HashMap<String, String> historyMap = new HashMap<String, String>();
		historyMap.put("Message_ID", topicListForDisplay.get(position).get("Message_ID"));
		historyMap.put("Message_Title", topicListForDisplay.get(position).get("ItemTitle"));
		historyMap.put("Replies", topicListForDisplay.get(position).get("Replies"));
		Log.d("Replies", topicListForDisplay.get(position).get("Replies"));
		if (historyList.contains(historyMap)) {
			historyList.remove(historyMap);
			historyList.add(historyMap);
		}
		else {
			historyList.add(historyMap);
		}
		
		SharedPreferences history = getSharedPreferences("history", 0);
		SharedPreferences.Editor editor = history.edit();
		try {
			editor.putString("history", ObjectSerializer.serialize(historyList));
		} catch (IOException e) {
			e.printStackTrace();
		}
		editor.commit();
		
//		Intent intent = new Intent(getApplicationContext(), RepliesActivity.class);
//		intent.putExtra("selectedPage", page+"");
//		intent.putExtra("Total_Replies", topicListForDisplay.get(position).get("Replies"));
//		intent.putExtra("Message_ID", topicListForDisplay.get(position).get("Message_ID"));
//		intent.putExtra("Message_Title", topicListForDisplay.get(position).get("ItemTitle"));
//		startActivity(intent);
		Intent intent = new Intent(getApplicationContext(), RepliesWebViewActivity.class);
		intent.putExtra("Message_ID", topicListForDisplay.get(position).get("Message_ID"));
		intent.putExtra("selectedPage", page+"");
		intent.putExtra("Message_Title", topicListForDisplay.get(position).get("ItemTitle"));
		startActivity(intent);
	}
	
	
	@Override
	// create action bar
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_list, menu);
		return true;
	}

	@Override
	// Handling clicks on action items
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reload:
			reloadTopicList();
			return true;
		case R.id.action_history:
			loadHistory();
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void reloadTopicList() {
		makelist(null);
		list.setOnScrollListener(new EndlessScrollListener());
		topicHttpClient("BW", "1");
	}
	
	private void loadHistory() {
		Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
		startActivity(intent);
	}

	// To get RequestParams by page type and page no.
	private RequestParams getRequestParamsByParams(String type, String page) {
		RequestParams params = new RequestParams();
		params.put("type", type);
		params.put("page", page);
		params.put("pagesize", "30");
		params.put("filtermode", "N");
		params.put("sensormode", "N");
		params.put("returntype", "json");
		return params;
	}

	// HTTP client, get json and makelist
	private void topicHttpClient(String type, String page) {
		Log.d("page", page);
		
		setProgressBarIndeterminateVisibility(true);
		
		final ObjectMapper mapper = new ObjectMapper();
		HKGoldenHttpClient.get("http://apps.hkgolden.com/android_api/v_1_0/newTopics.aspx",
				getRequestParamsByParams(type, page),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						try {
							topicList = mapper.readValue(response,
									TopicList.class);

							makelist(topicList);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						Toast.makeText(getApplicationContext(), "Can't connect", Toast.LENGTH_LONG).show();;
						super.onFailure(arg0, arg1, arg2, arg3);
					}
					
					@Override
					public void onFinish() {
						setProgressBarIndeterminateVisibility(false);
						super.onFinish();
					}
				});
	}

	// For endless scroll
	public class EndlessScrollListener implements OnScrollListener {

		private int visibleThreshold = 0;
		private int currentPage = 0;
		private int previousTotal = 0;
		private boolean loading = true;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
//			if (loading) {
//				Log.d("totalItemCount", totalItemCount + "");
//				Log.d("previousTotal", previousTotal + "");
//				if (totalItemCount > previousTotal) {
//					postAdded = totalItemCount - previousTotal;
//					loading = false;
//					previousTotal = totalItemCount;
//					currentPage++;
//					// Log.d("totalItemCount", totalItemCount+"");
//				}
//			}
//			if (!loading
//					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//				// I load the next page of gigs using a background task,
//				// but you can call any function here.
//				// new LoadGigsTask().execute(currentPage + 1);
//				Log.d("currentPage", currentPage + "");
//				topicHttpClient("BW", currentPage + 1 + "");
//				loading = true;
//			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
		        if (view.getLastVisiblePosition() >= view.getCount() - 1 - 0) {
		            currentPage++;
		            //load more list items:
		            topicHttpClient("BW", currentPage + 1 + "");
		        }
		    }
		}
	}
}
