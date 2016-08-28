package com.picperweek.common4android.config;

public class Constants {

	public final static boolean IS_TEST = true;

	// public final static int SDK_VERSION_CODE = 1;
	// public final static String SDK_VERSION_NAME = "SDK_YOUYANG";

	/**
	 * 应用基本常量
	 */
	public final static String APP_NAME = "Mobile";
	public final static String APP_TAG = APP_NAME;
	public final static String APP_MAIN_PATH = "Common4Android";
	//
	//
	// public static final int READ_BUFFER = 1024;

	public static final String UTF8 = "UTF-8";

	/**
	 * 网络请求方式
	 */
	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_PUT = "PUT";
	public static final String REQUEST_METHOD_DELETE = "DELETE";
	public static final String REQUEST_METHOD_POST_MULTIPLE = "POST_MULTIPLE";

	/**
	 * 线程优先级 线程优先级MAX_PRIORITY = 10;NORM_PRIORITY = 5;MIN_PRIORITY = 1;
	 */
	public final static int THREAD_PRIORITY = 3;
	//
	//
	// /**
	// * 找回密码，访问地址
	// */
	// public static final String URL_FORGET_PASS =
	// "http://wappass.baidu.com/passport/?getpass";
	//
	public static final String HTTP_DATA_NONET = "无法连接到网络\n请稍后再试";
	public static final String HTTP_DATA_BUSY = "网络不稳定\n请稍后再试";
	public static final String HTTP_DATA_CONNECT_TIMEOUT = "连接超时\n请稍后再试";
	public static final String HTTP_DATA_USER_NOCHECK = "用户验证失败\n请重新登录";
	public static final String HTTP_SERVICE_ERROR = "服务器繁忙\n请稍后再试";
	public static final String HTTP_DATA_URL_ERROR = "服务器地址错误";
	public static final String HTTP_DATA_FAIL = "数据解析失败\n请检查当前网络状态";
	public static final String HTTP_URL_ILLEAGLE = "网络链接地址不合法";
	public static final String HTTP_NET_TIPS_TEXT = "无法连接到网络，请检查网络设置";

	public final static int TAG_TYPE_STRING = 1;
	public final static int TAG_TYPE_GSON = 2;
	public final static int TAG_TYPE_JSON_OBJECT = 3;
	public final static int TAG_TYPE_JSON_ARRAY = 4;

	//
	// /**
	// * 客服电话
	// */
	// public static final String PHONE_NUMBER_CUSTOM_SERVICE = "010-59059588";
	//
	// /**
	// * SDK DATA CODE
	// */
	// public static final int SDK_API_CODE_SUCCESS = 1;
	// public static final int SDK_API_CODE_ERROR = 2;
	// public static final int SDK_API_CODE_PAYING = 3;
	// public static final int SDK_API_CODE_CANCEL = 4;
	//
	// /**
	// * Extra Key
	// */
	// public static final String EXTRA_KEY_PASS_ID = "pass_id";
	// public static final String EXTRA_KEY_USER_ID = "user_id";
	// public static final String EXTRA_KEY_USER_NAME = "user_name";
	// public static final String EXTRA_KEY_GAME_ID = "game_id";
	// public static final String EXTRA_KEY_GAME_NAME = "game_name";
	// public static final String EXTRA_KEY_SERVER_ID = "server_id";
	// public static final String EXTRA_KEY_LOGIN_NAME = "login_name";
	// public static final String EXTRA_KEY_AMOUNT = "amount";
	// public static final String EXTRA_KEY_RETURN_ATTACH = "return_attach";
	// public static final String EXTRA_KEY_CHARGE_VALUE = "charge_value";
	// public static final String EXTRA_KEY_BDUSS = "bduss";
	//
	// public static final String EXTRA_KEY_TITLE = "title";
	// public static final String EXTRA_KEY_CUSTOMER_ID = "customer_id";
	// public static final String EXTRA_KEY_NOTIFY_URL = "notify_url";
	// public static final String EXTRA_KEY_ORDER_ID = "order_id";
	// public static final String EXTRA_KEY_ORDER_CREATE_TIME =
	// "order_create_time";
	// public static final String EXTRA_KEY_URL = "url";
	// public static final String EXTRA_KEY_EXT_DATA = "ext_data";
	// public static final String EXTRA_KEY_ITEM_INFO = "item_info";
	//
	// /**
	// * RequestCode
	// */
	// public static final int REQUEST_CODE_PAYMENT = 100;
	//
	// /**
	// * ResultCode
	// */
	// public static final int RESULT_CODE_PAYMENT_FINISHED = 100;

	/**
	 * Event Message
	 */
	public static final String MSG_OK = "ok";
	// public static final String MSG_RESPONSE_USER_ID_IS_EMPTY = "response
	// user_id is empty";
	// public static final String MSG_RESPONSE_USER_NAME_IS_EMPTY = "response
	// user_name is empty";
	// public static final String MSG_RESPONSE_INVALID_USER_ID= "response
	// invalid user_id";
	// public static final String MSG_RESPONSE_ERROR_CODE = "response error
	// code";
	// public static final String MSG_RESPONSE_ERROR = "response error";
	// public static final String MSG_ERROR_NETWORK_CONNECTION = "network
	// connection";
	// public static final String MSG_ERROR_INVALID_USERNAME = "invalid
	// username";
	// public static final String MSG_ERROR_INVALID_PASSWORD = "invalid
	// password";
	// public static final String MSG_ERROR_NEED_LOGIN_PROTECTION= "nedd login
	// protection";
	// public static final String MSG_ERROR_USERNAME_EXIST = "username exist";
	// public static final String MSG_ERROR_INVALID_VERIFY_CODE = "invalid
	// verify code";
	// public static final String MSG_ERROR_INVALID_PARAMETER = "invalid
	// parameter";
	// public static final String MSG_ERROR_VERIFY_CODE_EXPIRED = "verify code
	// expired";
	// public static final String MSG_ERROR_PHONE_NUM_BINDED = "phone number
	// binded";
	// public static final String MSG_ERROR_ANTI_CHEAT = "anti cheat";
	// public static final String MSG_ERROR_NEED_ACTIVATE_EMAIL = "need activate
	// email";
	// public static final String MSG_ERROR_SYSTEM = "system error";
	// public static final String MSG_ERROR_COMMON = "common error";
	// public static final String MSG_ERROR_ACTIVE_CODE_SYSTEM = "active code
	// system error";
	// public static final String MSG_ERROR_TESTING_GAME = "testing game refuse
	// to login";
	// public static final String MSG_ERROR_NEED_ACTIVE_CODE = "need active
	// code";
	// public static final String MSG_ERROR_INVALID_ACTIVE_CODE = "invalid
	// active code";
	// public static final String MSG_ERROR_INVALID_APP_KEY = "invalid appkey";
	// public static final String MSG_ERROR_INVALID_SIGN_OR_PARAMS = "invilid
	// sign or params";
	// public static final String MSG_OPERATION_CANCEL = "operation cancel";
}
