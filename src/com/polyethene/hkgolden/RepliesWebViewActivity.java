package com.polyethene.hkgolden;

import org.jsoup.Jsoup;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RepliesWebViewActivity extends Activity {
	
	private WebView webView;
	String Message_ID = "0";
	String currentPage = "1";
	String Message_Title = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_replies_web_view);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_INDETERMINATE_ON);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		// Get data via the key
		Message_ID = extras.getString("Message_ID");
		currentPage = extras.getString("selectedPage");
		Message_Title = extras.getString("Message_Title");
		if (Message_Title != null) {
			ActionBar ab = getActionBar();
			ab.setTitle(Message_Title);
		}
		webView = (WebView) findViewById(R.id.webview1);
		webView.setVisibility(View.INVISIBLE);
		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d("onPageFinished", "yes");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ViewAuthorPanel');"
						+ "for(var i=0; i<divs.length; i++){"
						+ "var nextSibling = divs[i].nextSibling"
						+ "while (nextSibling && nextSibling.nodeType != 1)"
						+ "{nextSibling = nextSibling.nextSibling}"
						+ "{nextSibling.style.color='#FFFF00'}"
						+ "}})()");
//				webView.loadUrl("javascript:(function() { " + 
//						"var divs = document.getElementsByClassName('ViewQuote');"
//						+ "for(var i=0; i<divs.length; i++) "
//						+ "{divs[i].style.color='#FFFFFF'}"
//						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('MobileTopPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('View_PageSelectPanel2');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ViewTitle');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('FooterPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('FuncPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ReplyFunc');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('View_PageSelect');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].remove(0);divs[i].remove(divs[i].length - 1)}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('Image');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].click()}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ReplyBox');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.backgroundColor='#B6B6B4'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('View_PageSelectPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.backgroundColor='#B6B6B4'}"
						+ "})()");
				webView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ViewAuthorPanel');"
						+ "for(var i=0; i<divs.length; i++){"
						+ "var nextSibling = divs[i].nextSibling"
						+ "while (nextSibling && nextSibling.nodeType != 1)"
						+ "{nextSibling = nextSibling.nextSibling}"
						+ "{nextSibling.style.color='#FFFF00'}"
						+ "}})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('MobileTopPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('View_PageSelectPanel2');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ViewTitle');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('FooterPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('FuncPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ReplyFunc');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.display='none'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('View_PageSelect');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].remove(0);divs[i].remove(divs[i].length - 1)}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('Image');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].click()}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('ReplyBox');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.backgroundColor='#B6B6B4'}"
						+ "})()");
				webView.loadUrl("javascript:(function() { " + 
						"var divs = document.getElementsByClassName('View_PageSelectPanel');"
						+ "for(var i=0; i<divs.length; i++) "
						+ "{divs[i].style.backgroundColor='#B6B6B4'}"
						+ "})()");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("uri", Uri.parse(url).getHost());
				if (Uri.parse(url).getHost().equals("m.hkgolden.com")||Uri.parse(url).getHost().equals("m2.hkgolden.com")||Uri.parse(url).getHost().equals("m3.hkgolden.com")) {
					// This is my web site, so do not override; let my WebView load the
					// page
					currentPage = Uri.parse(url).getQueryParameter("page");
					return false;
				}
				// Otherwise, the link is not for a page on my site, so launch another
				// Activity that handles URLs
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
				return true;

			}
		});
		
		
//		HKGoldenHttpClient.get("http://m2.hkgolden.com/view.aspx", getRequestParamsByParams(Message_ID, currentPage), 
//
//				new AsyncHttpResponseHandler(){
//
//					@Override
//					public void onSuccess(String content) {
//						
//						org.jsoup.nodes.Document document = Jsoup.parse(content);
//						document.head().getElementsByTag("link").remove();
//						document.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", "style.css");
//						document.head().getElementsByTag("script").remove();
//						document.head().appendElement("script").attr("type", "text/javascript").attr("src", "mobile.js");
//						String data = document.outerHtml();
//						Log.d("htmlcode", data);
//						webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", null);
//						webView.setVisibility(View.VISIBLE);
//					}
//					
//				}
//			);
		
		final Activity myActivity = this;
		webView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				myActivity.setProgress(newProgress * 100);
				if(newProgress == 100)
				{
					
				}
			}
			
		});
		
		
		
		webView.loadUrl("http://m2.hkgolden.com/view.aspx?message="+Message_ID+"&page="+currentPage);
		Log.d("loadUrl", "yes");
	}

//	// To get RequestParams by page type and page no.
//	private RequestParams getRequestParamsByParams(String Message_ID, String page) {
//		RequestParams params = new RequestParams();
//		params.put("message", Message_ID);
//		params.put("page", page);
//		return params;
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.replies_web_view, menu);
		return true;
	}
	
	@Override
	// Handling clicks on action items
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reload_replies:
			webView.reload();
			return true;
		case R.id.action_reply_post:
			Intent intent = new Intent(getApplicationContext(), ReplyPostActivity.class);
			intent.putExtra("Message_ID", Message_ID);
			intent.putExtra("Message_Title", Message_Title);
			intent.putExtra("currentPage", currentPage);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		webView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		webView.onResume();
		super.onResume();
	}
	
}
