#Common4Android
  Common methods for android developer.
  
##Overview

ClassName                  | Description  
---------------------------|--------------------------------------
HP_AnimationUtils.java     | ......       
HP_BitmapEffectUtils.java  | ......       
HP_BitmapUtils.java        | ......  
HP_NetWorkUtils.java       | ...... 
HP_SystemIntentUtils.java  | ......           

##Example Usage

###HP_NetWorkUtils.java

#### - Bitmap Request
	HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
	String url = "";
	netWorkUtils.getAsyncRequestBitmap(url,new OnRequestBitmapFinished() {
			
		public void onRequestBitmapFinished(String resultStr, Bitmap bitmap,
				boolean isSuccess) {
			// TODO Auto-generated method stub
			
		}
	});
	
#### - Data Request	
	HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
	String url = "";
	netWorkUtils.getAsyncRequestData(url, new OnRequestDataFinished() {
			
		public void onRequestDataFinished(String resultStr, byte[] data,
				boolean isSuccess) {
			// TODO Auto-generated method stub
			
		}
	});
	
#### - JSONObject Request	
	HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
	String url = "";
	netWorkUtils.getAsyncRequestJSONObject(url, new OnRequestJSONObjectFinished() {
			
		public void onRequestJSONObjectFinished(String resultStr,
				JSONObject jsonObject, boolean isSuccess) {
			// TODO Auto-generated method stub
					
		}
	});
	
###HP_SystemIntentUtils.java

#### - PhoneCall
	HP_SystemIntentUtils systemIntentUtils = new HP_SystemIntentUtils();
	Intent intent = systemIntentUtils.getPhoneCallIntent(1391391313);
	this.startActivity(intent);
		
#### - SendMessage

	HP_SystemIntentUtils systemIntentUtils = new HP_SystemIntentUtils();
	Intent intent = systemIntentUtils.getSendMessageIntent("Test Message.");
	this.startActivity(intent);
