
package com.xc.framework.bitmap.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

import com.xc.framework.bitmap.BitmapLoader;
import com.xc.framework.bitmap.BitmapOtherUtil;
import com.xc.framework.utils.XCIOUtil;
/**
 * @author ZhangXuanChen
 * @date 2015-9-25
 * @package com.xc.framework.bitmap.download
 * @description
 */
public class DefaultDownloader extends Downloader {

	private static final String TAG = "DefaultDownloader";

	/**
	 * Download bitmap to outputStream by uri.
	 * 
	 * @param uri
	 *            file path, assets path(assets/xxx) or http url.
	 * @param outputStream
	 * @param task
	 * @return The expiry time stamp or -1 if failed to download.
	 */
	@Override
	public long downloadToStream(String uri, OutputStream outputStream, final BitmapLoader.BitmapLoadTask<?> task) {

		if (task == null || task.isCancelled() || task.getTargetContainer() == null)
			return -1;

		URLConnection urlConnection = null;
		BufferedInputStream bis = null;

		BitmapOtherUtil.trustAllHttpsURLConnection();

		long result = -1;
		long fileLen = 0;
		long currCount = 0;
		try {
			if (uri.startsWith("/")) {
				FileInputStream fileInputStream = new FileInputStream(uri);
				fileLen = fileInputStream.available();
				bis = new BufferedInputStream(fileInputStream);
				result = System.currentTimeMillis() + this.getDefaultExpiry();
			} else if (uri.startsWith("assets/")) {
				InputStream inputStream = this.getContext().getAssets().open(uri.substring(7, uri.length()));
				fileLen = inputStream.available();
				bis = new BufferedInputStream(inputStream);
				result = Long.MAX_VALUE;
			} else {
				final URL url = new URL(uri);
				urlConnection = url.openConnection();
				urlConnection.setConnectTimeout(this.getDefaultConnectTimeout());
				urlConnection.setReadTimeout(this.getDefaultReadTimeout());
				bis = new BufferedInputStream(urlConnection.getInputStream());
				result = urlConnection.getExpiration();
				result = result < System.currentTimeMillis() ? System.currentTimeMillis() + this.getDefaultExpiry() : result;
				fileLen = urlConnection.getContentLength();
			}

			if (task.isCancelled() || task.getTargetContainer() == null)
				return -1;

			byte[] buffer = new byte[4096];
			int len = 0;
			BufferedOutputStream out = new BufferedOutputStream(outputStream);
			while ((len = bis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				currCount += len;
				if (task.isCancelled() || task.getTargetContainer() == null)
					return -1;
				task.updateProgress(fileLen, currCount);
			}
			out.flush();
		} catch (Throwable e) {
			result = -1;
			Log.e(TAG, e.getMessage(), e);
		} finally {
			XCIOUtil.close(bis);
		}
		return result;
	}
}
