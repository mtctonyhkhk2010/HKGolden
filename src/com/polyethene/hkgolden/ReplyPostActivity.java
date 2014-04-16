package com.polyethene.hkgolden;

import java.io.IOException;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReplyPostActivity extends Activity {

	PostResult result = null;
	String Message_ID = "0";
	String Message_Title = "0";
	String currentPage = "1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_post);
		TextView usernameTextView = (TextView)findViewById(R.id.usernametext);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (this);
		usernameTextView.setText(prefs.getString("username", "username"));
		Bundle extras = getIntent().getExtras();
		Message_ID = extras.getString("Message_ID");
		ActionBar ab = getActionBar();
		Message_Title = extras.getString("Message_Title");
		ab.setTitle(Message_Title);
		currentPage = extras.getString("currentPage");
	}
	
	// To get RequestParams by page type and page no.
		private RequestParams getRequestParamsByParams() {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (this);
			RequestParams params = new RequestParams();
			params.put("ft", "BW");
			params.put("mt", "Y");
			params.put("username", prefs.getString("username", "username"));
			params.put("pass", prefs.getString("password", "password"));
			params.put("title", "N");
			EditText content = (EditText)findViewById(R.id.contentEdit);
			params.put("body", content.getText().toString());
			params.put("id", Message_ID);
			params.put("returntype", "json");
			return params;
		}
		
	// HTTP client, get json and makelist
		public void PostReplyHttpClient(View view) {
			
//			setProgressBarIndeterminateVisibility(true);
			
			final ObjectMapper mapper = new ObjectMapper();
			HKGoldenHttpClient.get("http://apps.hkgolden.com/android_api/v_1_0/post_101202.aspx",
					getRequestParamsByParams(),
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							try {
								result = mapper.readValue(response,
										PostResult.class);
								if(result.success)
								{
									Toast.makeText(getApplicationContext(), "reply success", Toast.LENGTH_LONG).show();
									Intent intent = new Intent(getApplicationContext(), RepliesWebViewActivity.class);
									intent.putExtra("Message_ID", Message_ID);
									intent.putExtra("selectedPage", currentPage);
									intent.putExtra("Message_Title", Message_Title);
									startActivity(intent);
								}
								else {
									Toast.makeText(getApplicationContext(), "reply fail", Toast.LENGTH_LONG).show();
								}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reply_post, menu);
		return true;
	}
	
	public void AddClown(View view)
	{
		EditText content = (EditText)findViewById(R.id.contentEdit);
		content.append(":o) ");
	}
	
	public void AddSosad(View view)
	{
		EditText content = (EditText)findViewById(R.id.contentEdit);
		content.append("[sosad] ");
	}
}
