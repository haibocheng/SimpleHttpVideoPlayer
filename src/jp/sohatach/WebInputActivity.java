package jp.sohatach;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebInputActivity extends Activity {
	private WebView webView;
	private String web_url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_input);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		
		Intent i = getIntent();
		if(i != null) {
			web_url = i.getExtras().getString("web_url");
			webView.loadUrl(web_url);
		}
	}

	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	
	    	generateDialog(this, url);
	        return true;
	    }

	    /**
	     * �J�����I���_�C�A���O�̕\��
	     * @param url �I�����ꂽURL
	     */
	    private void generateDialog(final MyWebViewClient viewClient, final String url) {
	    	final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	        		WebInputActivity.this);
	        // �\�����ڂ̔z��
	        final CharSequence[] methods = { "Open as Link", "Play as Video" };
	        // �^�C�g����ݒ�
	        alertDialogBuilder.setTitle("Please choose how to open.");
	        // �\�����ڂƃ��X�i�̐ݒ�
	        alertDialogBuilder.setItems(methods,
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        if(which == 0) {
	                        	//�X�^�b�N�ɐςݏグ�邽�ߐV���ɐ�������i�߂�L�[�Ŗ߂��悤�ɂ��邽�߁j
	    						Intent i = new Intent(WebInputActivity.this, WebInputActivity.class);
	    						i.putExtra("web_url", url);
	    						startActivity(i);
	                			//webView.loadUrl(url);
	                        }else{
	                        	viewClient.playVideo(url);
	                        }
	                    }
	                });

	        // �_�C�A���O��\��
	        alertDialogBuilder.create().show();
	    }

		/**
	     * player��URL��n���Đ�����B
	     * @param url ����t�@�C����URL
	     */
	    protected void playVideo(String url) {
	    	Intent i = new Intent(WebInputActivity.this, SimpleHttpVideoPlayerActivity.class);
	    	i.putExtra("video_url", url);
	    	startActivity(i);
		}

	}
}
