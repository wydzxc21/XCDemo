package com.xc.framework.https.interfaces;

/**
 * @author ZhangXuanChen
 * @date 2015-9-16
 * @package com.frame.net
 * @description 处理网络请求结果的回调接口
 */
public interface RequestCallback {

	/**
	 * 请求结果
	 * 
	 * @param requestCode
	 * @param requestResult
	 */
	public void onResult(int what, String response);

}
