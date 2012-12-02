#Common4Android
  Common methods for android developer.
  
##Overview

ClassName                  | Description  
---------------------------|--------------------------------------
HP_AnimationUtils.java     | center       
HP_BitmapEffectUtils.java  | center       
HP_BitmapUtils.java        | center  
HP_NetWorkUtils.java       | center 
HP_SystemIntentUtils.java  | center           

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

#### - CallPhoneIntent
	HP_SystemIntentUtils systemIntentUtils = new HP_SystemIntentUtils();
	Intent intent = systemIntentUtils.getPhoneCallIntent(1391391313);
	this.startActivity(intent);
		
#### - SendMessageIntent

	HP_SystemIntentUtils systemIntentUtils = new HP_SystemIntentUtils();
	Intent intent = systemIntentUtils.getSendMessageIntent("Test Message.");
	this.startActivity(intent);
