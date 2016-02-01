package com.bruce.rxdemo.network.github;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Display 37Signals authentication dialog.
 * 
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 * 
 */
public class GithubDialog extends Dialog {

	static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
	static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;

	private String mUrl;
	private OAuthDialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;

	private static final String TAG = "GithubDialog";

	public GithubDialog(Context context, String url,
			OAuthDialogListener listener) {
		super(context);

		mUrl = url;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");
        mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
        //如果没有获取结果,不能点击外部消失
        setCanceledOnTouchOutside(false);
		setUpTitle();
        setUpWebView();

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
				: DIMENSIONS_LANDSCAPE;

		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
						* scale + 0.5f)));

	}

	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mTitle = new TextView(getContext());
		mTitle.setText("GitHub");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(Color.BLACK);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mContent.addView(mTitle);
	}

	@SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView() {

        //WebView通用设置参数等
//        WebSettings settings = webview.getSettings();
//        settings.setJavaScriptEnabled(true);//启用js
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);//js和android交互
//        String cacheDirPath = PathCommonDefines.WEBVIEW_CACHE;
//        settings.setAppCachePath(cacheDirPath); //设置缓存的指定路径
//        settings.setAllowFileAccess(true); // 允许访问文件
//        settings.setAppCacheEnabled(true); //设置H5的缓存打开,默认关闭
//        settings.setUseWideViewPort(true);//设置webview自适应屏幕大小
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//设置，可能的话使所有列的宽度不超过屏幕宽度
//        settings.setLoadWithOverviewMode(true);//设置webview自适应屏幕大小
//        settings.setDomStorageEnabled(true);//设置可以使用localStorage
//        settings.setSupportZoom(false);//关闭zoom按钮
//        settings.setBuiltInZoomControls(false);//关闭zoom

		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new OAuthWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}

	private class OAuthWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirecting URL " + url);

            //只有当授权成功后 切回调url 已 自定义的callbackUrl开头才自己处理
			if (url.startsWith(GithubApp.mCallbackUrl)) {
				String urls[] = url.split("=");
				mListener.onComplete(urls[1]);
				GithubDialog.this.dismiss();
				return true;
			}
			return false;
		}

        @Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.d(TAG, "Page error: " + description);

			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onError(description);
			GithubDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "Loading URL: " + url);

			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				mTitle.setText(title);
			}
			mSpinner.dismiss();
		}

	}

	public interface OAuthDialogListener {
         void onComplete(String accessToken);
         void onError(String error);
	}

}