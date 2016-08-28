package com.picperweek.common4android.http.command;

import com.picperweek.common4android.api.HttpTag;
import com.picperweek.common4android.config.Constants;
import com.picperweek.common4android.http.HttpEngine.HttpCode;
import com.picperweek.common4android.receiver.NetStatusReceiver;
import com.picperweek.common4android.util.LogUtil;

/**
 * 
 * @author widebluesky
 *
 */
public class HttpDataRequest extends BaseHttpRequest {
	private HttpTag tag;
	
	private Object extraInfo;

	public HttpTag getTag() {
		return tag;
	}
	
	public void setTag(HttpTag tag) {
		this.tag = tag;
		this.setUrl(tag.getHttpUrl());
	}
	
	public Object getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Object extraInfo) {
		this.extraInfo = extraInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpDataRequest other = (HttpDataRequest) obj;
		if (tag != other.tag)
			return false;
		return true;
	}

	@Override
	public HttpCode prepareRequest() {
		// TODO Auto-generated method stub
		HttpCode code;
		if (NetStatusReceiver.netStatus == NetStatusReceiver.NETSTATUS_INAVAILABLE) {
			code = HttpCode.ERROR_NO_CONNECT;
			return code;
		}
		/**
		 * 查看是否已取消
		 */
		if (isCancelled()) {
			code = HttpCode.USER_CANCELLED;
			return code;
		}

		/**
		 * 检查URL是否正确，已经是否有用户信息
		 */
		code = checkUrlParams();
		if (code == HttpCode.STATUS_OK) {
			/**
			 * 加入用户身份信息
			 */
			if (isNeedAuth()) {
				addUserVerifyInfo();
			}

			/**
			 * 参数检查成功
			 */
			makeUrlWithSystemInfo();
		}
		LogUtil.d(Constants.APP_TAG, "[System]: URL = " + getUrl());
		return code;
	}

}
