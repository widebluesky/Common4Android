#Common4Android v2.0

  Common methods for android developer v2.0.

##Overview
###-基类
ClassName                  | Description  
---------------------------|--------------------------------------
BaseApplication.java       | Application基类，启动SDCard监听、网络状态监听。
BaseActiivty.java          | Activity基类，封装常用方法及Activity管理。

###-管理器                       
ClassName                  | Description  
---------------------------|--------------------------------------
ActivityManager.java       | Activity管理工具类，可以获得当前引用启动的Activity实例。
HotFixManager.java         | 热修复patch加载工具类。
SystemBarTintManager.java  | 沉浸式管理。
ThemeSettingsHelper.java   | 主题模式切换管理（日间/夜间模式）。
LRUCache.java              | LRUCache。
ThreadPoolManager.java     | 应用线程池管理。

###-网络处理                       
ClassName                  | Description  
---------------------------|--------------------------------------
HttpTag.java               | Api接口配置类。
TaskManager.java           | 网络任务管理类。
HttpDataRequest.java       | 通用请求类。
HttpPostRequest.java       | Post请求类。

###-工具类
ClassName                  | Description  
---------------------------|--------------------------------------
AnimationUtil.java         | 动画效果工具类，提供缩放、透明度、位移、旋转动画方法。
AppInfoUtil.java           | 应用信息工具类，获取应用版本号、版本编码。
BitmapCacheUtil.java       | Bitmap缓存工具类，封装bitmap本地存储方法。
BitmapEffectUtil.java      | Bitmap特效实现类，封装bitmap特效实现方法，如：老照片、RGB偏移等。
BitmapUtil.java            | Bitmap常用工具类，Bitmap数据类型转换、圆角、缩放、倒影。
ConvertUtil.java           | 转换工具类，进行对象的类型转换。
DateUtil.java              | 日期工具类，日期转换生肖、日期转换星座、日期相互转换。
DesUtil.java               | DES加密工具类。
DeviceUtil.java            | 设备信息获取工具类，获得设备型号、设备生产厂商、屏幕尺寸、GPS状态、wifi状态等。 
DialogUtil.java            | 弹窗工具类，ProgressDialog，AlertDialog，Toast弹出封装。
FileUtil.java              | 文件工具类，文件常用方法，获得文件大小、文件大小转换。
MD5Util.java               | MD5加密工具类。
RegexUtil.java             | 常用正则表达式工具类。
SDCardUtil.java            | SD卡信息管理工具类。
SharedPreferencesUtil.java | SharedPreferences工具类。 
StringUtil.java            | 字符串处理工具类。
SystemIntentUtil.java      | 系统Intent工具类，常用的系统Intent跳转函数，如：打电话、发短信等。 

##Example Usage

#### - NetWork
#### Create a Get Request
	HttpGetRequest request = new HttpGetRequest();
	request.setTag(HttpTag.TEST);
	request.setSort(Constants.REQUEST_METHOD_GET);
	request.setGzip(true);
	request.setRetry(false);
	request.setNeedAuth(false);
	TaskManager.startHttpDataRequset(request, new HttpDataResponse() {
			
		@Override
		public void onHttpRecvOK(HttpTag tag, Object extraInfo, Object result) {
			DialogUtil.showToast(MainActivity.this, (String) result, Toast.LENGTH_LONG);
		}
			
		@Override
		public void onHttpRecvError(HttpTag tag, HttpCode retCode, String msg) {
			DialogUtil.showToast(MainActivity.this, "onHttpRecvError retCode:" + retCode + " msg:" + msg, Toast.LENGTH_LONG);
		}
			
		@Override
		public void onHttpRecvCancelled(HttpTag tag) {
			DialogUtil.showToast(MainActivity.this, "onHttpRecvCancelled", Toast.LENGTH_LONG);
		}
	});
	
#### Create a Post Request
	HttpPostRequest request = new HttpPostRequest();
	request.setTag(HttpTag.TEST);
	request.setSort(Constants.REQUEST_METHOD_POST); // application/x-www-form-urlencoded
	//request.setSort(Constants.REQUEST_METHOD_POST_MULTIPLE); // multipart/form-data
	request.setGzip(true);
	request.setRetry(false);
	request.setNeedAuth(false);
	TaskManager.startHttpDataRequset(request, new HttpDataResponse() {
			
		@Override
		public void onHttpRecvOK(HttpTag tag, Object extraInfo, Object result) {
			DialogUtil.showToast(MainActivity.this, (String) result, Toast.LENGTH_LONG);
		}
			
		@Override
		public void onHttpRecvError(HttpTag tag, HttpCode retCode, String msg) {
			DialogUtil.showToast(MainActivity.this, "onHttpRecvError retCode:" + retCode + " msg:" + msg, Toast.LENGTH_LONG);
		}
			
		@Override
		public void onHttpRecvCancelled(HttpTag tag) {
			DialogUtil.showToast(MainActivity.this, "onHttpRecvCancelled", Toast.LENGTH_LONG);
		}
	});
  
  
## Contact

  E-mail：widebluesky@qq.com
  
  WeChat：widebluesky
  
<p align="center" >
  <img width="300" height="300" src="https://raw.githubusercontent.com/widebluesky/Common4Android/master/wechat_qrcode.jpg" alt="Common4Android" title="Common4Android">
</p>
  
  
