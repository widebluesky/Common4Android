#Common4Android

SOURCE TREE TEST
  Common methods for android developer.
  
  注：此版本库版本并不是最新，目前正在不断优化中，需要最新版本的同学请e-mail:widebluesky@foxmail.com
  
##Overview
###-基类

ClassName                  | Description  
---------------------------|--------------------------------------
HP_BaseActiivty.java       | Activity基类，封装常用方法及Activity管理。

###-管理器                       
ClassName                  | Description  
---------------------------|--------------------------------------
HP_ActivityManager.java    | Activity管理工具类，可以获得当前引用启动的Activity实例。
HP_DefaultThreadPool.java  | 应用线程池管理。
HP_ImageFileCache.java     | 图片文件物理缓存管理。
HP_ImageMemoryCache.java   | 图片文件内存缓存管理。

###-自定义组件
ClassName                  | Description  
---------------------------|--------------------------------------
HP_ImageView.java          | 自定义ImageView组件，集成异步下载图片方法。
HP_PullToRefreshListView   | 自定义ListView组件，集成下拉刷新效果。     
   
###-工具类
ClassName                  | Description  
---------------------------|--------------------------------------
HP_AnimationUtils.java     | 动画效果工具类，提供缩放、透明度、位移、旋转动画方法。
HP_AppUtils.java           | 应用信息工具类，获取应用版本号、版本编码。
HP_AsyncTaskUtils.java     | AsyncTask继承封装类，方便生成、启动AsycTask。 
HP_BitmapCacheUtils.java   | Bitmap缓存工具类，封装bitmap本地存储方法。
HP_BitmapEffectUtils.java  | Bitmap特效实现类，封装bitmap特效实现方法，如：老照片、RGB偏移等。
HP_BitmapUtils.java        | Bitmap常用工具类，Bitmap数据类型转换、圆角、缩放、倒影。
HP_ConvertUtils.java       | 转换工具类，进行对象的类型转换。
HP_DateUtils.java          | 日期工具类，日期转换生肖、日期转换星座、日期相互转换。
HP_DesUtil.java            | DES加密工具类。
HP_DeviceUtils.java        | 设备信息获取工具类，获得设备型号、设备生产厂商、屏幕尺寸、GPS状态、wifi状态等。 
HP_DialogUtils.java        | 弹窗工具类，ProgressDialog，AlertDialog，Toast弹出封装。
HP_FileUtils.java          | 文件工具类，文件常用方法，获得文件大小、文件大小转换。
HP_MD5Utils.java           | MD5加密工具类。
HP_NetUtils.java           | 网络请求工具类，Thread实现。
HP_NetWorkUtils.java       | 网络请求工具类，AsyncTask实现。 
HP_RegexUtils.java         | 常用正则表达式工具类。
HP_SDCardUtils.java        | SD卡信息管理工具类。
HP_SharedPreferencesUtils.java      | SharedPreferences工具类。 
HP_StringUtils.java        | 字符串处理工具类。
HP_SystemIntentUtils.java  | 系统Intent工具类，常用的系统Intent跳转函数，如：打电话、发短信等。           

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
