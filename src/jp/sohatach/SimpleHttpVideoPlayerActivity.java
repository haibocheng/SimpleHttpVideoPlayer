package jp.sohatach;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

public class SimpleHttpVideoPlayerActivity extends Activity {
	private String mediaPath;
	private VideoView videoView;
	private static final String VIDEO_PATH = "VIDEO_PATH";
	private static final String WEB_PATH = "WEB_PATH";
	public static final int WEB_REQUEST_CODE = 0;
	public static final int RESULT_OK = 0;
	
	/**
	 * ����t�@�C����URL���ڎw��
	 */
	private static final int VIDEO_URL = 1;
	/**
	 * ����t�@�C���ւ̃����N���܂�Web�y�[�W�̎w��
	 */
	private static final int WEB_URL = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		videoView = (VideoView) findViewById(R.id.videoView);
		
		Intent i = getIntent();
		if(i != null) {
			String url = i.getStringExtra("video_url");
			if(url != null && !url.trim().equals("")) {
				mediaPath = url;
				playVideo();
			}
		}
		
		//mediaPath = "http://www.gomplayer.jp/support/down/mp4_mpeg4_aac.mp4";
		//mediaPath = "/sdcard/mpeg4.mp4";
//
//		if (mediaPath != null) {
//			playVideo();
//		}
	}

	/**
	 * Video�̍Đ�
	 */
	private void playVideo() {
		videoView.setVideoPath(mediaPath);
		videoView.setMediaController(new MediaController(this));
		videoView.requestFocus();
		videoView.start();
	}

	
	/**
	 * �I�v�V�������j���[�̍쐬
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);

		menu.add(0 , Menu.FIRST , Menu.NONE , "URL����");
		menu.add(0 , Menu.FIRST + 1 ,Menu.NONE , "Web����I��");

		return ret;
	}
	
	/**
	 * �I�v�V�������j���[�I�����̏���
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == Menu.FIRST){
			//URL����
			showInputVideoUrlDialog();
		}else{
			//WebURL����
			showInputWebUrlDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * ����URL���͂̂��߂̃_�C�A���O�\��
	 */
	private void showInputVideoUrlDialog() {
		_showDialog(VIDEO_URL);
	}
	
	/**
	 * ����t�@�C���ւ̃����N������Web�y�[�W��URL���͂̂��߂̃_�C�A���O�\��
	 */
	private void showInputWebUrlDialog() {
		_showDialog(WEB_URL);
	}
	
	/**
	 * URL���͂̂��߂̃_�C�A���O�\��
	 */
	private void _showDialog(int type) {
	    final EditText editText = new EditText(this);
	    final int inputType = type;
	    final SharedPreferences pref = getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE);

	    //�O����͂���URL�̎擾
	    String prevUrl = "";
	    if(VIDEO_URL == inputType) {
	    	prevUrl = pref.getString(VIDEO_PATH, "");
	    }else{
	    	prevUrl = pref.getString(WEB_PATH, "");
	    }
	    editText.setText(prevUrl);
	    
	    final AlertDialog alertDialog = new AlertDialog.Builder(this)
	        .setTitle("input URL")
	        .setView(editText)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String url = editText.getText().toString().trim();
					if("".equals(url)){
						return;
					}
					if(VIDEO_URL == inputType) {
						//���͂��ꂽ�l���o���Ă���
						pref.edit().putString(VIDEO_PATH, url).commit();

						mediaPath = url;
						playVideo();
					}else{
						//���͂��ꂽ�l���o���Ă���
						pref.edit().putString(WEB_PATH, url).commit();
						String tmp = pref.getString(WEB_PATH, "");

						Intent i = new Intent(SimpleHttpVideoPlayerActivity.this, WebInputActivity.class);
						i.putExtra("web_url", url);
						startActivity(i);
						//startActivityForResult(i, WEB_REQUEST_CODE);
						//webView.loadUrl(url);
					}
				}
			}).setNegativeButton("Cancel", null)
	        .create();
	    
	    //�_�C�A���O�N��
	    alertDialog.show();
	}
	
//	/**
//	 * Web���͉�ʂ���̖߂菈��
//	 */
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(WEB_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
//			if(data != null) {
//				String url = data.getStringExtra("video_url");
//				if(url != null && !url.trim().equals("")) {
//					mediaPath = url;
//					playVideo();
//				}
//			}
//		}
//	}
}