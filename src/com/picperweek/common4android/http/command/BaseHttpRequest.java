package com.picperweek.common4android.http.command;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.picperweek.common4android.http.HttpEngine.HttpCode;
import com.picperweek.common4android.util.LogUtil;
import com.picperweek.common4android.util.StringUtil;

/**
 * 网络请求基类
 * @author widebluesky
 *
 */
public abstract class BaseHttpRequest {

	private static final String TAG = BaseHttpRequest.class.getSimpleName();

	private String url;

	private Map<String, String> urlParams = null;

	private Map<String, String> headParams = null;

	private boolean gzip = true;

	/**
	 * 是否需要身份认证
	 */
	private boolean needAuth = true;

	/**
	 * Http类型
	 */
	private String sort;

	/**
	 * 是否重试
	 */
	private boolean retry = false;

	/**
	 * 是否取消本次请求
	 */
	private boolean cancelled = false;

	/**
	 * 发起请求后统一调用，<组装URL。。。>
	 * 
	 * @return 返回HttpCode错误码
	 */
	abstract public HttpCode prepareRequest();

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Map<String, String> getUrlParams() {
		return urlParams;
	}

	public Map<String, String> getHeadParams() {
		return headParams;
	}

	public boolean isGzip() {
		return gzip;
	}

	public boolean isNeedAuth() {
		return needAuth;
	}

	public void setUrlParams(Map<String, String> urlParams) {
		this.urlParams = urlParams;
	}

	public void setHeadParams(Map<String, String> headParams) {
		this.headParams = headParams;
	}

	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}

	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	/**
	 * 向urlParams添加一个数据
	 * 
	 * @param key
	 * @param value
	 */
	public void addUrlParams(String key, String value) {
		if (this.urlParams == null) {
			this.urlParams = new HashMap<String, String>();
		}
		this.urlParams.put(key, value);
	}

	/**
	 * 向headParams添加一个数据
	 * 
	 * @param key
	 * @param value
	 */
	public void addHeadParams(String key, String value) {
		if (this.headParams == null) {
			this.headParams = new HashMap<String, String>();
		}
		this.headParams.put(key, value);
	}

	/**
	 * 检测URL的有效性
	 * 
	 * @return
	 */
	protected HttpCode checkUrlParams() {
		if (this.url == null || this.url.equals("")) {
			return HttpCode.ERROR_NET_ACCESS;
		}

		return HttpCode.STATUS_OK;
	}
	
	/**
	 * 加入用户身份信息
	 */
	protected void addUserVerifyInfo() {
//		if (UserDBHelper.getInstance().getUserInfo() != null) {
//			addHeadParams("cookie", UserDBHelper.getInstance().getUserInfo().creatCookieStr());
//		}
	}


	/**
	 * 正常逻辑下返回组装后的URL，包含版本信息等
	 * 
	 * @return
	 */
	protected void makeUrlWithSystemInfo() {
		if (this.urlParams == null) {
			this.urlParams = new HashMap<String, String>();
		}
		StringBuilder sb = new StringBuilder();
		if (this.url.contains("?")) {
			sb.append(this.url + "&");
		} else {
			sb.append(this.url);
		}
		try {
			/*
			 * 进行统一url参数添加
			 *  */
//			this.urlParams.put(REQ_PARAM_DEV_ID, URLEncoder.encode(MobileUtil.getImei()));
//			this.urlParams.put(REQ_PARAM_GUID, URLEncoder.encode(MobileUtil.getImei()));
//			this.urlParams.put(REQ_PARAM_CHANNEL, URLEncoder.encode(MobileUtil.getChannel() + ""));
			
//			Context context = SDKApi.getInstance().getContext();
//			StringBuffer devuaBuf = new StringBuffer();
//			devuaBuf.append(HOUSE_MOD);
//			devuaBuf.append("_");
//			devuaBuf.append(DeviceUtil.getScreenWidth(context));
//			devuaBuf.append("_");
//			devuaBuf.append(DeviceUtil.getScreenHeight(context));
//			devuaBuf.append("_");
//			devuaBuf.append(DeviceUtil.getBuild_MANUFACTURER() + DeviceUtil.getBuild_PRODUCT());
//			devuaBuf.append("_");
//			devuaBuf.append(DeviceUtil.getVersionCode(context));
//			devuaBuf.append("_");
//			devuaBuf.append(DeviceUtil.getVersionName(context));
//			devuaBuf.append("_");
//			devuaBuf.append("Android" + DeviceUtil.getBuild_VERSION_SDK());
//			this.urlParams.put(REQ_PARAM_DEVUA, URLEncoder.encode(devuaBuf.toString()));
//			this.urlParams.put(REQ_PARAM_APPNAME, URLEncoder.encode(APP_NAME));
			

			Iterator<String> iterator = this.urlParams.keySet().iterator();
			int size = this.urlParams.keySet().size();
			int index = 0;
			while (iterator.hasNext()) {
				index++;
				Object key = iterator.next();
				String value = this.urlParams.get(key);
				LogUtil.v(TAG, "key=[" + key.toString() + "], value=[" + value + "]");
				// 在参数不为空的情况下组装，否则服务器会报错401
				if (index == size) {
					if (!"".equals(value)) {
						sb.append(key + "=" + StringUtil.urlEncode(value));
					}
				} else {
					if (!"".equals(value)) {
						sb.append(key + "=" + StringUtil.urlEncode(value) + "&");
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			LogUtil.e(TAG, e.getMessage());
		}

		/**
		 * 合成URL
		 */
		this.url = sb.toString();
	}

	@Override
	public String toString() {
		return "BaseHttpRequest [url=" + url + ", urlParams=" + urlParams + ", headParams=" + headParams + ", gzip=" + gzip + ", needAuth=" + needAuth + ", sort=" + sort + ", retry=" + retry + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headParams == null) ? 0 : headParams.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((urlParams == null) ? 0 : urlParams.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseHttpRequest other = (BaseHttpRequest) obj;
		if (headParams == null) {
			if (other.headParams != null)
				return false;
		} else if (!headParams.equals(other.headParams))
			return false;
		if (sort == null) {
			if (other.sort != null)
				return false;
		} else if (!sort.equals(other.sort))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (urlParams == null) {
			if (other.urlParams != null)
				return false;
		} else if (!urlParams.equals(other.urlParams))
			return false;
		return true;
	}
}
