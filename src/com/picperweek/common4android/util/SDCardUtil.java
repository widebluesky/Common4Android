package com.picperweek.common4android.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SDCardUtil {

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean isSDCardExist() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 获得SD卡存储空间大小
	 * 
	 * @return
	 */
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

	/**
	 * 获得SD卡剩余空间
	 * 
	 * @return
	 */
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

	/**
	 * 文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExistInSDCard(String path) {
		if (isSDCardExist()) {
			File file = new File(path);
			return file.exists();
		} else {
			return false;
		}
	}

	/**
	 * 获得文件
	 * 
	 * @param path
	 * @return
	 */
	public static File getFileWithPath(String path) {
		if (isSDCardExist()) {
			File file = new File(path);
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 获得SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		if (isSDCardExist()) {
			File file = Environment.getExternalStorageDirectory();
			return file.getPath();
		} else {
			return null;
		}
	}

	/**
	 * 获得手机内存路径
	 * 
	 * @return
	 */
	public static String getMobileMemoryPath() {

		return Environment.getRootDirectory().getPath();

	}

}
