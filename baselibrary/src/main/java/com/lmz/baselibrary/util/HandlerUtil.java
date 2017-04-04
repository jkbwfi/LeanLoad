package com.lmz.baselibrary.util;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil
{

	private static Handler mHandler = new Handler(Looper.getMainLooper());

	public static void runOnUiThread(Runnable r)
	{
		synchronized (mHandler)
		{
			mHandler.post(r);
		}
	}

	public static void runOnUiThreadFrontOfQueue(Runnable r)
	{
		synchronized (mHandler)
		{
			mHandler.postAtFrontOfQueue(r);
		}
	}

	public static void runOnUiThreadAtTime(Runnable r, long uptimeMillis)
	{
		synchronized (mHandler)
		{
			mHandler.postAtTime(r, uptimeMillis);
		}
	}

	public static void runOnUiThreadAtTime(Runnable r, Object msgObj, long uptimeMillis)
	{
		synchronized (mHandler)
		{
			mHandler.postAtTime(r, msgObj, uptimeMillis);
		}
	}

	public static void runOnUiThreadDelayed(Runnable r, long delayMillis)
	{
		synchronized (mHandler)
		{
			mHandler.postDelayed(r, delayMillis);
		}
	}
	/**
	 * 判断当前线程是否是UI线程.
	 *
	 * @return
	 */
	public static boolean isUIThread()
	{
		return Looper.getMainLooper() == Looper.myLooper();
	}
}
