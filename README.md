##重要实现部分代码
- 版本解析部分
```
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
```
- app链接Play Store部分
```
private void updateApp() {
		String packageName = context.getPackageName();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id="+packageName));
		context.startActivity(intent);
	}
```

可以参考我的博客:
