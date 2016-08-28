package com.picperweek.common4android.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 
 * @author widebluesky
 *
 */
public class HotFixManager {

    private static Context mContext;

    private static final int BUF_SIZE = 2048;

    private File patchs;
    private File patchsOptFile;

    public HotFixManager(Context context) {
        this.mContext = context;
        this.patchs = new File(mContext.getFilesDir(), "patchs");// 存放补丁文件
        this.patchsOptFile = new File(mContext.getFilesDir(), "patchsopt");// 存放预处理补丁文件压缩处理后的dex文件
    }

    /**
     * 初始化版本号
     *
     * @param versionCode
     */
    public void init(String versionCode) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("fixbug", Context.MODE_PRIVATE);
        String oldVersionCode = sharedPreferences.getString("versionCode", null);
        if (oldVersionCode == null || !oldVersionCode.equalsIgnoreCase(versionCode)) {
            this.initPatchsDir();// 初始化补丁文件目录
            this.clearPaths();// 清楚所有的补丁文件
            sharedPreferences.edit().clear().putString("versionCode", versionCode).commit();// 存储版本号
        } else {
            this.loadPatchs();// 加载已经添加的补丁文件(.jar)
        }
    }

    /**
     * 读取补丁文件夹并加载
     */
    private void loadPatchs() {
        if (patchs.exists() && patchs.isDirectory()) {// 判断文件是否存在并判断是否是文件夹
            File patchFiles[] = patchs.listFiles();// 获取文件夹下的所有的文件
            for (int i = 0; i < patchFiles.length; i++) {
                if (patchFiles[i].getName().lastIndexOf(".jar") == patchFiles[i].getName().length() - 4) {// 仅处理.jar文件
                    this.loadPatch(patchFiles[i].getAbsolutePath());// 加载jar文件
                }
            }
        } else {
            this.initPatchsDir();
        }
    }

    /**
     * 加载单个补丁文件
     *
     * @param patchPath
     */
    private void loadPatch(String patchPath) {
        try {
            injectDexAtFirst(patchPath, patchsOptFile.getAbsolutePath());// 读取jar文件中dex内容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * patch所在文件目录
     *
     * @param patchPath
     */
    public void addPatch(String patchPath) {
        File inFile = new File(patchPath);
        File outFile = new File(patchs, inFile.getName());
        this.copyFile(outFile, inFile);
        this.loadPatch(patchPath);
    }

    /**
     * 移除所有的patch文件
     */
    public void removeAllPatch() {
        this.clearPaths();
    }

    /**
     * 清除所有的补丁文件
     */
    private void clearPaths() {
        if (patchs.exists() && patchs.isDirectory()) {
            File patchFiles[] = patchs.listFiles();
            for (int i = 0; i < patchFiles.length; i++) {
                if (patchFiles[i].getName().lastIndexOf(".jar") == patchFiles[i].getName().length() - 4) {
                    patchFiles[i].delete();
                }
            }
        }
    }

    /**
     * 初始化存放补丁的文件目录
     */
    private void initPatchsDir() {
        if (!this.patchs.exists()) {
            this.patchs.mkdirs();
        }
        if (!this.patchsOptFile.exists()) {
            this.patchsOptFile.mkdirs();
        }
    }

    /**
     * 复制文件从inFile到outFile
     *
     * @param outFile
     * @param inFile
     * @return
     */
    private boolean copyFile(File outFile, File inFile) {
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;
        try {
            MessageDigest digests = MessageDigest.getInstance("MD5");
            bis = new BufferedInputStream(new FileInputStream(inFile));
            dexWriter = new BufferedOutputStream(new FileOutputStream(outFile));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                digests.update(buf, 0, len);
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
            BigInteger bi = new BigInteger(1, digests.digest());
            String result = bi.toString(16);
            File toFile = new File(outFile.getParentFile(), result + ".jar");
            outFile.renameTo(toFile);
            return true;
        } catch (Exception e) {
            if (dexWriter != null) {
                try {
                    dexWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * 注入dex类方法
     * @param dexPath
     * @param defaultDexOptPath
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static void injectDexAtFirst(String dexPath, String defaultDexOptPath)
            throws NoSuchFieldException, IllegalAccessException,
            ClassNotFoundException {
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, defaultDexOptPath, dexPath, getPathClassLoader());// 把dexPath文件补丁处理后放入到defaultDexOptPath目录中
        Object baseDexElements = getDexElements(getPathList(getPathClassLoader()));// 获取当面应用Dex的内容
        Object newDexElements = getDexElements(getPathList(dexClassLoader));// 获取补丁文件Dex的内容
        Object allDexElements = combineArray(newDexElements, baseDexElements);// 把当前apk的dex和补丁文件的dex进行合并
        Object pathList = getPathList(getPathClassLoader());// 获取当前的patchList对象
        setField(pathList, pathList.getClass(), "dexElements", allDexElements);// 利用反射设置对象的值
    }

    /**
     * 获取类加载器
     * @return
     */
    private static PathClassLoader getPathClassLoader() {
        PathClassLoader pathClassLoader = (PathClassLoader) mContext.getClassLoader();
        return pathClassLoader;
    }

    /**
     * 利用反射获取到dexElements属性
     * @param paramObject
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }

    /**
     * 利用反射获取到pathList属性
     * @param baseDexClassLoader
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException, ClassNotFoundException {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 此方法是合并2个数组，把补丁dex中的内容放到数组最前，达到修复bug的目的
     *
     * @param firstArray
     * @param secondArray
     * @return
     */
    private static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
    }

    /**
     * 获得Field
     * @param obj
     * @param cl
     * @param field
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getField(Object obj, Class<?> cl, String field)
            throws NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);// 强制反射
        return localField.get(obj);// 获取值
    }

    /**
     * 设置Field
     * @param obj
     * @param cl
     * @param field
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setField(Object obj, Class<?> cl, String field,
                                Object value) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);// 强制反射
        localField.set(obj, value);// 设置值
    }
}
