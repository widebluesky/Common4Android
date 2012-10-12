package com.hiputto.common4android.util;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;

public class HP_SystemIntentUtils {
	
	public Intent getGoogleSearchIntent(String searchStr){
		//1.从google搜索内容 
		Intent intent = new Intent(); 
		intent.setAction(Intent.ACTION_WEB_SEARCH); 
		intent.putExtra(SearchManager.QUERY,searchStr);
		return intent;
	}
	
	public Intent getOpenExplorerIntent(String url){
		//2.浏览网页 
		android.net.Uri uri = android.net.Uri.parse(url); 
		Intent intent  = new Intent(Intent.ACTION_VIEW,uri); 
		return intent;
	}
	
	public Intent getShowMapIntent(float latitude,float longtitude){
		//3.显示地图 geo : "geo:38.899533,-77.036476"
		String geo = "geo:"+latitude+","+longtitude;
		android.net.Uri uri = android.net.Uri.parse(geo); 
		Intent intent = new Intent(Intent.ACTION_VIEW,uri); 
		return intent;
	}
	
//	4.路径规划 
//	Uri uri = Uri.parse("http://maps.google.com/maps?f=dsaddr=startLat%20startLng&daddr=endLat%20endLng&hl=en"); 
//	Intent it = new Intent(Intent.ACTION_VIEW,URI); 
//	startActivity(it); 

	public Intent getCallPhoneIntent(long number){
		//5.拨打电话 
		Uri uri = Uri.parse("tel:"+number); 
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		return intent;   
	}
	
	public Intent getSendMessageIntent(String text){
		//6.调用发短信的程序 
		Intent intent = new Intent(Intent.ACTION_VIEW);    
		intent.putExtra("sms_body", text);    
		intent.setType("vnd.android-dir/mms-sms");    
		return intent;
	}
	
//		7.发送短信 
//		Uri uri = Uri.parse("smsto:"+number);    
//		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);    
//		intent.putExtra("sms_body", "The SMS text");    
//		startActivity(intent); 
//		String body="this is sms demo"; 
//		Intent mmsintent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", number, null)); 
//		mmsintent.putExtra(Messaging.KEY_ACTION_SENDTO_MESSAGE_BODY, body); 
//		mmsintent.putExtra(Messaging.KEY_ACTION_SENDTO_COMPOSE_MODE, true); 
//		mmsintent.putExtra(Messaging.KEY_ACTION_SENDTO_EXIT_ON_SENT, true); 
//		startActivity(mmsintent); 
//
//	8.发送彩信 
//	Uri uri = Uri.parse("content://media/external/images/media/23");    
//	Intent it = new Intent(Intent.ACTION_SEND);    
//	it.putExtra("sms_body", "some text");    
//	it.putExtra(Intent.EXTRA_STREAM, uri);    
//	it.setType("image/png");    
//	startActivity(it); 
//	StringBuilder sb = new StringBuilder(); 
//	sb.append("file://"); 
//	sb.append(fd.getAbsoluteFile()); 
//	Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mmsto", number, null)); 
//	// Below extra datas are all optional. 
//	intent.putExtra(Messaging.KEY_ACTION_SENDTO_MESSAGE_SUBJECT, subject); 
//	intent.putExtra(Messaging.KEY_ACTION_SENDTO_MESSAGE_BODY, body); 
//	intent.putExtra(Messaging.KEY_ACTION_SENDTO_CONTENT_URI, sb.toString()); 
//	intent.putExtra(Messaging.KEY_ACTION_SENDTO_COMPOSE_MODE, composeMode); 
//	intent.putExtra(Messaging.KEY_ACTION_SENDTO_EXIT_ON_SENT, exitOnSent); 
//	startActivity(intent); 

//	9.发送Email 
//	Uri uri = Uri.parse("mailto:xxx@abc.com"); 
//	Intent it = new Intent(Intent.ACTION_SENDTO, uri); 
//	startActivity(it); 
	
//	Intent it = new Intent(Intent.ACTION_SEND);    
//	it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");    
//	it.putExtra(Intent.EXTRA_TEXT, "The email body text");    
//	it.setType("text/plain");    
//	startActivity(Intent.createChooser(it, "Choose Email Client")); 
	
//	Intent it=new Intent(Intent.ACTION_SEND);      
//	String[] tos={"me@abc.com"};      
//	String[] ccs={"you@abc.com"};      
//	it.putExtra(Intent.EXTRA_EMAIL, tos);      
//	it.putExtra(Intent.EXTRA_CC, ccs);      
//	it.putExtra(Intent.EXTRA_TEXT, "The email body text");      
//	it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");      
//	it.setType("message/rfc822");      
//	startActivity(Intent.createChooser(it, "Choose Email Client"));    

//	Intent it = new Intent(Intent.ACTION_SEND);    
//	it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");    
//	it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/mysong.mp3");    
//	sendIntent.setType("audio/mp3");    
//	startActivity(Intent.createChooser(it, "Choose Email Client")); 
//	10.播放多媒体   
//	Intent it = new Intent(Intent.ACTION_VIEW); 
//	Uri uri = Uri.parse("file:///sdcard/song.mp3"); 
//	it.setDataAndType(uri, "audio/mp3"); 
//	startActivity(it); 
//	Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "1");    
//	Intent it = new Intent(Intent.ACTION_VIEW, uri);    
//	startActivity(it); 

	public Intent getUninstalAPKIntent(String packageName){
		//11.uninstall apk 
		Uri uri = Uri.fromParts("package", packageName, null);    
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);    
		return intent;
	}
	
	public Intent getInstalAPKIntent(String packageName){
//		12.install apk 
		Uri installUri = Uri.fromParts("package", packageName, null); 
		Intent intent = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri); 
		return intent;
	}


//
//	13. 打开照相机 
//	    <1>Intent i = new Intent(Intent.ACTION_CAMERA_BUTTON, null); 
//	           this.sendBroadcast(i); 
//	     <2>long dateTaken = System.currentTimeMillis(); 
//	            String name = createName(dateTaken) + ".jpg"; 
//	            fileName = folder + name; 
//	            ContentValues values = new ContentValues(); 
//	            values.put(Images.Media.TITLE, fileName); 
//	            values.put("_data", fileName); 
//	            values.put(Images.Media.PICASA_ID, fileName); 
//	            values.put(Images.Media.DISPLAY_NAME, fileName); 
//	            values.put(Images.Media.DESCRIPTION, fileName); 
//	            values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, fileName); 
//	            Uri photoUri = getContentResolver().insert( 
//	                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
//	             
//	            Intent inttPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
//	            inttPhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); 
//	            startActivityForResult(inttPhoto, 10); 
//
//	14.从gallery选取图片 
//	  Intent i = new Intent(); 
//	            i.setType("image/*"); 
//	            i.setAction(Intent.ACTION_GET_CONTENT); 
//	            startActivityForResult(i, 11); 
//
//	15. 打开录音机 
//	   Intent mi = new Intent(Media.RECORD_SOUND_ACTION); 
//	            startActivity(mi); 
//
//	16.显示应用详细列表       
//	Uri uri = Uri.parse("market://details?id=app_id");         
//	Intent it = new Intent(Intent.ACTION_VIEW, uri);         
//	startActivity(it);         
//	//where app_id is the application ID, find the ID          
//	//by clicking on your application on Market home          
//	//page, and notice the ID from the address bar      
//
//	刚才找app id未果，结果发现用package name也可以 
//	Uri uri = Uri.parse("market://details?id=<packagename>"); 
//	这个简单多了 
//
//	17寻找应用       
//	Uri uri = Uri.parse("market://search?q=pname:pkg_name");         
//	Intent it = new Intent(Intent.ACTION_VIEW, uri);         
//	startActivity(it); 
//	//where pkg_name is the full package path for an application       
//
//	18打开联系人列表 
//	            <1>            
//	           Intent i = new Intent(); 
//	           i.setAction(Intent.ACTION_GET_CONTENT); 
//	           i.setType("vnd.android.cursor.item/phone"); 
//	           startActivityForResult(i, REQUEST_TEXT); 
//
//	            <2> 
//	            Uri uri = Uri.parse("content://contacts/people"); 
//	            Intent it = new Intent(Intent.ACTION_PICK, uri); 
//	            startActivityForResult(it, REQUEST_TEXT); 
//
//	19 打开另一程序 
//	Intent i = new Intent(); 
//	            ComponentName cn = new ComponentName("com.yellowbook.android2", 
//	                    "com.yellowbook.android2.AndroidSearch"); 
//	            i.setComponent(cn); 
//	            i.setAction("android.intent.action.MAIN"); 
//	            startActivityForResult(i, RESULT_OK); 
//
//	20.调用系统编辑添加联系人（高版本SDK有效）： 
//	Intent it = new Intent(Intent.ACTION_INSERT_OR_EDIT); 
//	                it.setType("vnd.android.cursor.item/contact"); 
//	                // it.setType(Contacts.CONTENT_ITEM_TYPE); 
//	                it.putExtra("name", "myName"); 
//	                it.putExtra(android.provider.Contacts.Intents.Insert.COMPANY,  "organization"); 
//	                it.putExtra(android.provider.Contacts.Intents.Insert.EMAIL, "email"); 
//	                it.putExtra(android.provider.Contacts.Intents.Insert.PHONE,"homePhone"); 
//	                it.putExtra( android.provider.Contacts.Intents.Insert.SECONDARY_PHONE, 
//	                                "mobilePhone"); 
//	                it.putExtra(  android.provider.Contacts.Intents.Insert.TERTIARY_PHONE, 
//	                                "workPhone"); 
//	                it.putExtra(android.provider.Contacts.Intents.Insert.JOB_TITLE,"title"); 
//	                startActivity(it); 
//
//	21.调用系统编辑添加联系人（全有效）： 
//	Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT); 
//	            intent.setType(People.CONTENT_ITEM_TYPE); 
//	            intent.putExtra(Contacts.Intents.Insert.NAME, "My Name"); 
//	            intent.putExtra(Contacts.Intents.Insert.PHONE, "+1234567890"); 
//	            intent.putExtra(Contacts.Intents.Insert.PHONE_TYPE, Contacts.PhonesColumns.TYPE_MOBILE); 
//	            intent.putExtra(Contacts.Intents.Insert.EMAIL, "com@com.com"); 
//	            intent.putExtra(Contacts.Intents.Insert.EMAIL_TYPE,                    Contacts.ContactMethodsColumns.TYPE_WORK); 
//	            startActivity(intent); 
//
//	22(更新) 
//	    //直接打电话出去   
//	    Uri uri = Uri.parse("tel:0800000123");   
//	    Intent it = new Intent(Intent.ACTION_CALL, uri);   
//	    startActivity(it);   
//	    //用這個，要在 AndroidManifest.xml 中，加上   
//	    //<uses-permission id="android.permission.CALL_PHONE" /> 
//
//	23.最基本的share 信息的intent，可以传一段text信息到各个手机上已安装程序：如SMS，Email，sina微波，米聊，facebook，twitter等等 
//
//	                Intent it = new Intent(Intent.ACTION_SEND); 
//	                it.putExtra(Intent.EXTRA_TEXT, "The email subject text"); 
//	                it.setType("text/plain"); 
//	                startActivity(Intent.createChooser(it, "Choose Email Client")); 
//	          
//	24.调用skype 的intent 
//
//	方法1：老版，新版不可用，可能是因为skype的activity结构变动： 
//
////	        Intent sky = new Intent("android.intent.action.CALL_PRIVILEGED"); 
////	        sky.setClassName("com.skype.raider", 
////	                "com.skype.raider.contactsync.ContactSkypeOutCallStartActivity"); 
////	        sky.setData(Uri.parse("tel:" + phone)); 
////	        startActivity(sky); 
//
//	方法2：打开到skype的main page： 
//
////	        PackageManager packageManager = getActivity().getPackageManager(); 
////	        Intent skype = packageManager.getLaunchIntentForPackage("com.skype.raider"); 
////	        skype.setData(Uri.parse("tel:65465446")); 
////	        startActivity(skype); 
//
//	方法3：传入号码，直接进入skype拨号画面并打电话： 
//
//	        Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED");   
//	        intent.setClassName("com.skype.raider", 
//	        "com.skype.raider.Main"); 
//	        intent.setData(Uri.parse("tel:" + phone));   
//	        startActivity(intent);    
}
