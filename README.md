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

#### - doAsyncGetRequest
	HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
	netWorkUtils.doAsyncGetRequest(url, new OnRequestFinished() {
			
		@Override
		public void onSuccess(HttpRequestBase httpRequest, HttpResponse httpResponse) throws Exception {
				// TODO Auto-generated method stub
				
		}
			
		@Override
		public void onFailure(HttpRequestBase httpRequest,
					HttpResponse httpResponse, Exception e) {
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
