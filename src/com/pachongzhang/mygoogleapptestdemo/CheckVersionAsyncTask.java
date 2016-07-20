package com.pachongzhang.mygoogleapptestdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class CheckVersionAsyncTask extends AsyncTask<String, Void, Boolean> {
    
    Context context;
    String currentVersion; // Google Play 上的版本

    String oldVersion;     // 目前的 app 版本

    
    public CheckVersionAsyncTask(Context context, String version) {
        this.context = context;
        this.oldVersion = version;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        String url = params[0];   //請在 excecute 時傳入你 google play 的 url 位置，不是 market 喔，要 http

        
        HttpPost post = new HttpPost(url);

        AndroidHttpClient client = AndroidHttpClient.newInstance("android");
        
        
        BufferedReader reader = null; 
        try {
            HttpResponse reponse = client.execute(post);
            
            reader = new BufferedReader(new InputStreamReader(reponse.getEntity().getContent()));
            String line;
            String content = "";
            Pattern p = Pattern.compile("\"softwareVersion\"\\W*([\\d\\.]+)");
            while( ( line = reader.readLine() ) != null ){
                Matcher matcher = p.matcher(line);
                if( matcher.find() ){
                    Log.v("ids", "ver.:" + matcher.group(1));
                    
                    currentVersion = matcher.group(1);
                }
                content += line;
            }
            /*
            * 檢查版本，在此使用的是我們自定的版本名稱 <app version name>，並不是版本號 <app version code>
            * 請特別注意這一點
            */
            
            if (currentVersion.compareTo(oldVersion) > 0)
                result = true;
            Log.v("ids", content);
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.v("ids", "close reader");
            try {
                if( reader != null ) {
                    reader.close();
                }
                if (client != null) {
                	client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            // 檢查到有新的版本，做下一個動作
        	new AlertDialog.Builder(context, R.style.MyDialog).setTitle("Update").setMessage("App have update,please update!").setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
				}
			}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setPositiveButton("update", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateApp();
				}
			}).create().show();
        }
    }

	private void updateApp() {
		String packageName = context.getPackageName();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id="+packageName));
		context.startActivity(intent);
	}
}