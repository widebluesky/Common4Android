package com.hiputto.common4android.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.os.StatFs;

public class HP_SDCardUtils {
	public static boolean isSDCardExist() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static long getSDCardAllSize() {
		if (isSDCardExist()) {
			// 取得SD卡文件路径
			File path = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(path.getPath());
			// 获取单个数据块的大小(Byte)
			long blockSize = sf.getBlockSize();
			// 获取所有数据块数
			long allBlocks = sf.getBlockCount();
			// 返回SD卡大小
			// return allBlocks * blockSize; //单位Byte
			// return (allBlocks * blockSize)/1024; //单位KB
			return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
		} else {
			return 0;
		}

	}

	public static long getSDCardFreeSize() {
		if (isSDCardExist()) {
			// 取得SD卡文件路径
			File path = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(path.getPath());
			// 获取单个数据块的大小(Byte)
			long blockSize = sf.getBlockSize();
			// 空闲的数据块的数量
			long freeBlocks = sf.getAvailableBlocks();
			// 返回SD卡空闲大小
			// return freeBlocks * blockSize; //单位Byte
			// return (freeBlocks * blockSize)/1024; //单位KB
			return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
		} else {
			return 0;
		}
	}

	public static boolean isExistInSDCard(String path) {
		if (isSDCardExist()) {
			File file = new File(path);
			return file.exists();
		} else {
			return false;
		}
	}

	public static File getFileWithPath(String path) {
		if (isSDCardExist()) {
			File file = new File(path);
			return file;
		} else {
			return null;
		}
	}

	public static String getSDCardPath() {
		if (isSDCardExist()) {
			File file = Environment.getExternalStorageDirectory();
			return file.getPath();
		} else {
			return null;
		}
	}

	public static String getMobileMemory() {

		// Runtime runtime = Runtime.getRuntime();
		// try {
		// runtime.exec("mount -o remount rw /system");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		return Environment.getRootDirectory().getPath();
	}
}
