package com.xc.framework.utils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author ZhangXuanChen
 * @date 2015-11-22
 * @package com.xc.framework.utils
 * @description 应用相关工具类
 */
public class XCAppUtil {
	/**
	 * 显示输入法
	 * 
	 * @param context
	 *            上下文
	 */
	public static void showInputKeyboard(Context context) {
		try {
			View view = ((Activity) context).getWindow().peekDecorView();
			if (view != null && view.getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
				imm.showSoftInput(view, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param context
	 *            上下文
	 */
	public static void hideInputKeyboard(Context context) {
		try {
			View view = ((Activity) context).getWindow().peekDecorView();
			if (view != null && view.getWindowToken() != null) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取应用名称
	 * 
	 * @param context
	 *            上下文
	 * @return 应用名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			return (String) packageManager.getApplicationLabel(appInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 *            上下文
	 * @return 版本号
	 */
	public static String getAppVersion(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 判断指定软件是否安装
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            应用包名
	 * @return 是否安装
	 */
	public static boolean isAppInstalled(Context context, String packageName) {
		try {
			if (context != null && !XCStringUtil.isEmpty(packageName)) {
				PackageManager packageManager = context.getPackageManager();
				// 获取所有已安装程序的包信息
				List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
				List<String> packageNameList = new ArrayList<String>();
				if (packageInfoList != null) {
					for (int i = 0; i < packageInfoList.size(); i++) {
						String packName = packageInfoList.get(i).packageName;
						packageNameList.add(packName);
					}
				}
				// 判断包名列表中是否有目标程序的包名
				return packageNameList.contains(packageName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断指定应用是否启动
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            应用包名
	 * @return 是否启动
	 */
	public static boolean isAppRunning(Context context, String packageName) {
		try {
			if (context != null && !XCStringUtil.isEmpty(packageName)) {
				final int maxRunningTaskNum = 200;
				ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(maxRunningTaskNum);
				for (RunningTaskInfo runningTaskInfo : runningTaskList) {
					if (runningTaskInfo.topActivity.getPackageName().equals(packageName) && runningTaskInfo.baseActivity.getPackageName().equals(packageName)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断指定服务是否启动
	 * 
	 * @param context
	 *            上下文
	 * @param serviceName
	 *            服务名
	 * @return 是否启动
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		try {
			if (context != null && !XCStringUtil.isEmpty(serviceName)) {
				final int maxRunningServiceNum = 200;
				ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningServiceInfo> runningServiceList = activityManager.getRunningServices(maxRunningServiceNum);
				for (RunningServiceInfo runningServiceInfo : runningServiceList) {
					if (runningServiceInfo.service.getClassName().equals(serviceName)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 打开其他APP
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            目标APP包名
	 * @param activityName
	 *            目标APP的目标Activity名
	 */
	public static void setAppIntent(Context context, String packageName, String activityName) {
		try {
			if (packageName != null && !"".equals(packageName) && activityName != null && !"".equals(activityName)) {
				ComponentName componetName = new ComponentName(packageName, activityName);// 这个是另外一个应用程序的包名,要启动的Activity
				Intent intent = new Intent();
				intent.setComponent(componetName);
				context.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有APP信息
	 * 
	 * @param context
	 *            上下文
	 * @return APP信息集合
	 */
	public static List<ResolveInfo> getAllAppInfo(Context context) {
		try {
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> appList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
			return appList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定app信息
	 * 
	 * @param context
	 *            上下文
	 * @param appName
	 *            APP名称
	 * @return APP信息
	 */
	public static ActivityInfo getAppInfo(Context context, String appName) {
		try {
			List<ResolveInfo> appList = getAllAppInfo(context);
			if (appList != null && !appList.isEmpty() && appName != null && !"".equals(appName)) {
				for (int i = 0; i < appList.size(); i++) {
					String appNameTemp = appList.get(i).loadLabel(context.getPackageManager()).toString();
					// String packageName =
					// appList.get(i).activityInfo.packageName;
					// String mainName = appList.get(i).activityInfo.name;
					// Log.i(TAG, "appName:" + appName + "\n" + "packageName:" +
					// packageName + "\n" + "mainName:" + mainName);
					if (appName.equals(appNameTemp)) {
						return appList.get(i).activityInfo;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取AndroidManifest.xml中,Application下的<meta-data>元素值
	 * 
	 * @param context
	 *            上下文
	 * @param metaDataName
	 *            <meta-data>key值
	 * @return <meta-data>value值
	 */
	public static String getMetaDataValue(Context context, String metaDataName) {
		if (!XCStringUtil.isEmpty(metaDataName)) {
			try {
				ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				return appInfo.metaData.getString("metaDataName");
			} catch (NameNotFoundException e) {
			}
		}
		return "";
	}

	/**
	 * 安装APP
	 * 
	 * @param context
	 *            上下文
	 * @param uri
	 *            uri
	 */
	public static void installApp(Context context, Uri uri) {
		if (context != null && uri != null) {
			try {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(uri, "application/vnd.android.package-archive");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 安装APP
	 * 
	 * @param context
	 *            上下文
	 * @param downloadId
	 *            下载唯一id
	 */
	@SuppressLint("NewApi")
	public static void installApp(Context context, long downloadId) {
		if (context != null && downloadId >= 0) {
			try {
				DownloadManager mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				if (mDownloadManager != null) {
					Uri uri = mDownloadManager.getUriForDownloadedFile(downloadId);
					if (uri != null) {
						installApp(context, uri);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 隐藏底部虚拟按键，并且全屏
	 * 
	 * @param context
	 *            上下文
	 */
	public static void hideBottomUIMenu(Context context) {
		if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
			View v = ((Activity) context).getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else if (Build.VERSION.SDK_INT >= 19) {
			View decorView = ((Activity) context).getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}
}
