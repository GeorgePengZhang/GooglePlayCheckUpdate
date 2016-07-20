package com.pachongzhang.mygoogleapptestdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private static final String AURA_TELPAD_MA7_TABLET = "Aura_TELPAD_MA7_tablet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		build();
	}

	@Override
	public void onClick(View v) {
		EditText numa = (EditText) findViewById(R.id.id_etnuma);
		EditText numb = (EditText) findViewById(R.id.id_etnumb);
		EditText numc = (EditText) findViewById(R.id.id_etnumc);

		String strNumA = numa.getText().toString();
		String strNumB = numb.getText().toString();

		if (TextUtils.isEmpty(strNumA)) {
			strNumA = (String) numa.getHint();
		}

		if (TextUtils.isEmpty(strNumB)) {
			strNumB = (String) numa.getHint();
		}

		int iNumA = Integer.valueOf(strNumA);
		int iNumB = Integer.valueOf(strNumB);
		int iNumC = iNumA + iNumB;

		numc.setText(iNumC + "");
	}

	private void build() {
		//�������жϣ�ֻ֧��ָ���Ļ���
		if (AURA_TELPAD_MA7_TABLET.equals(Build.MODEL)) {
			setContentView(R.layout.activity_main);
			
			Button equal = (Button) findViewById(R.id.id_equal);

			equal.setOnClickListener(this);
			
			checkUpdateApp();
		} else {
			setContentView(R.layout.notsupport_main);
			new AlertDialog.Builder(MainActivity.this, R.style.MyDialog).setTitle("Hint").setMessage("This app don't used this tablet!").setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).create().show();
		}
	}
	
	private void checkUpdateApp() {
		try {
			String packageName = getPackageName();
			//��Ϊ�����google����ַ�����������ʱ����Ҫ��ǽ����������������(ע���������Ҫ��֤��app�Ѿ��ϴ���google play)
			String url = "https://play.google.com/store/apps/details?id="+packageName;
			
			PackageInfo info = getPackageManager().getPackageInfo(packageName, 0);
			
			new CheckVersionAsyncTask(MainActivity.this, info.versionName).execute(url);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
