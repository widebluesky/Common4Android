#Common4Android
  Common methods for android developer.
  
##Overview

ClassName                  | Description  
---------------------------|--------------------------------------
HP_AnimationUtils.java     | center       
HP_BitmapEffectUtils.java  | center       
HP_BitmapUtils.java        | center      
HP_SystemIntentUtils.java  | center      
HP_AsyncTaskManager.java   | center       
HP_NetWorkAsyncTask.java   | center      
HP_NetWorkAsyncTask.java   | center      
HP_NetWorkUtils.java       | center     

##Example Usage

###HP_AnimationUtils.java  

    $ printf "Hello, world.\n"
    
###HP_BitmapEffectUtils.java
###HP_BitmapUtils.java

###HP_SystemIntentUtils.java

###HP_AsyncTaskManager.java
###HP_NetWorkAsyncTask.java
###HP_NetWorkUtils.java

	HP_NetWorkUtils netWorkUtils = new HP_NetWorkUtils();
	String url = "";
	netWorkUtils.getAsyncRequestBitmap(url,new OnRequestBitmapFinished() {
			
		public void onRequestBitmapFinished(String resultStr, Bitmap bitmap,
				boolean isSuccess) {
			// TODO Auto-generated method stub
			
		}
	});