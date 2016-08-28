package com.picperweek.common4android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.zip.GZIPInputStream;

import org.apache.http.util.ByteArrayBuffer;

import com.picperweek.common4android.config.Constants;
import com.picperweek.common4android.receiver.ExternalStorageReceiver;

/**
 * 
 * @author widebluesky
 *
 */
public class FileUtil {

	private static final int BUFFER_SIZE = 4096;

	public static byte[] gzipDecoder(byte[] src) {
		ByteArrayInputStream is = new ByteArrayInputStream(src);
		GZIPInputStream gis = null;
		byte[] desbyte = null;
		ByteArrayBuffer buffer = new ByteArrayBuffer(0);
		try {
			gis = new GZIPInputStream(is);
			int byteRead = 0;
			byte[] tempBytes = new byte[BUFFER_SIZE];

			while ((byteRead = gis.read(tempBytes)) != -1) {
				buffer.append(tempBytes, 0, byteRead);
			}
			desbyte = buffer.toByteArray();
		} catch (IOException e) {
			LogUtil.e(Constants.APP_TAG, "Gzip exception: " + e.toString());
			desbyte = buffer.toByteArray();
			if (desbyte != null && desbyte.length > 0) {
				return desbyte;
			} else {
				return null;
			}
		} finally {
			try {
				is.close();
				if (gis != null) {
					gis.close();
				}
			} catch (IOException e) {
				LogUtil.e(Constants.APP_TAG, e.toString());
				return null;
			}
		}

		return desbyte;
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 创建目录和文件， 如果目录或文件不存在，则创建出来
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 创建后的文件
	 * @throws IOException
	 */
	public static synchronized File makeDIRAndCreateFile(String filePath) throws Exception {
		// Auto-generated method stub

		if (!ExternalStorageReceiver.isSDCardMounted) {
			throw new Exception("没有sd卡");
		}

		File file = new File(filePath);
		String parent = file.getParent();

		File parentFile = new File(parent);
		if (!parentFile.exists()) {
			if (parentFile.mkdirs()) {
				file.createNewFile();
			} else {
				throw new IOException("创建目录失败！");
			}
		} else {
			if (!file.exists()) {
				file.createNewFile();
			}
		}
		return file;
	}

	public static long getFileSizes(File f) {// 取得文件大小
		long s = 0;
		try {
			if (f.exists()) {
				FileInputStream fileInputStream = new FileInputStream(f);
				s = fileInputStream.available();
			} else {
				f.createNewFile();
				System.out.println("文件不存在");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	// 递归
	public static long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}
}
