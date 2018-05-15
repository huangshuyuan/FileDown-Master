package com.hsy.filedown;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;


/**
 * Author: syhuang
 * Date:  2018/4/11
 */
public class FilePathUtils {


    /**
     * 默认的根目录
     */
    public static final String DEFAULT_ROOT_PATH = "/mnt/download/file_down/";

    /**
     * 默认的缩略图目录
     */
    public static final String DEFAULT_SCREENSHOT_PATH = "/mnt/kc_screenshot/";

    /**
     * 小数的格式化
     */
    public static final DecimalFormat FORMAT     = new DecimalFormat("####.##");
    public static final DecimalFormat FORMAT_ONE = new DecimalFormat("####.#");


    /**
     * 根据文件路径获取文件的名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件的根目录
     *
     * @return
     */
    public static String getRootDirPath() {
        String path = DEFAULT_ROOT_PATH;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/file_down/";
        }
        return path;
    }

    /**
     * 获取文件缩略图目录
     *
     * @return
     */
    public static String getScreenShotDirPath() {
        String path = DEFAULT_SCREENSHOT_PATH;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/kc_screenshot/";
        }
        return path;
    }

    /**
     * 获取指定的文件夹路径
     *
     * @param type @@See FileInfo.java
     * @return
     */
    public static String getSpecifyDirPath(int type) {
        String dirPath = getRootDirPath();

        //        switch (type) {
        //            case FileInfo.TYPE_APK: {
        //                dirPath = dirPath + "apk/";
        //                break;
        //            }
        //            case FileInfo.TYPE_JPG: {
        //                dirPath = dirPath + "jpg/";
        //                break;
        //            }
        //            case FileInfo.TYPE_MP3: {
        //                dirPath = dirPath + "mp3/";
        //                break;
        //            }
        //            case FileInfo.TYPE_MP4: {
        //                dirPath = dirPath + "mp4/";
        //                break;
        //            }
        //            default:
        //                dirPath = dirPath + "other/";
        //                break;
        //        }

        return dirPath;
    }


    /**
     * 生成本地文件路径
     *
     * @param fileName
     * @return
     */
    public static File getLocalFile(String fileName) {
        String dirPath = getRootDirPath();
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile, fileName);
        return file;
    }


    /**
     * 根据传入的byte数量转换为对应的byte, Kbyte, Mbyte, Gbyte单位的字符串
     *
     * @param size byte数量
     * @return
     */
    public static String getFileSize(long size) {
        if (size < 0) { //小于0字节则返回0
            return "0B";
        }

        double value = 0f;
        if ((size / 1024) < 1) { //0 ` 1024 byte
            return size + "B";
        } else if ((size / (1024 * 1024)) < 1) {//0 ` 1024 kbyte

            value = size / 1024f;
            return FORMAT.format(value) + "KB";
        } else if (size / (1024 * 1024 * 1024) < 1) {                  //0 ` 1024 mbyte
            value = (size * 100 / (1024 * 1024)) / 100f;
            return FORMAT.format(value) + "MB";
        } else {                  //0 ` 1024 mbyte
            value = (size * 100l / (1024l * 1024l * 1024l)) / 100f;
            return FORMAT.format(value) + "GB";
        }
    }


    /**
     * 转换为流量数组
     * String[0] 为数值
     * String[1] 为单位
     * 1024 ===》》》 1 k
     *
     * @param size
     * @return
     */
    public static String[] getFileSizeArrayStr(long size) {
        String[] result = new String[2];
        if (size < 0) { //小于0字节则返回0
            result[0] = "0";
            result[1] = "B";
            return result;
        }

        double value = 0f;
        if ((size / 1024) < 1) { //0 ` 1024 byte
            result[0] = FORMAT_ONE.format(size);
            result[1] = "B";
            //            return  size + "B";
        } else if ((size / (1024 * 1024)) < 1) {//0 ` 1024 kbyte
            value = size / 1024f;
            result[0] = FORMAT_ONE.format(value);
            result[1] = "KB";
            //            return  FORMAT.format(value) + "KB";
        } else if (size / (1024 * 1024 * 1024) < 1) {                  //0 ` 1024 mbyte
            value = (size * 100 / (1024 * 1024)) / 100f;
            result[0] = FORMAT_ONE.format(value);
            result[1] = "MB";
            //            return  FORMAT.format(value) + "MB";
        } else {                  //0 ` 1024 mbyte
            value = (size * 100l / (1024l * 1024l * 1024l)) / 100f;
            result[0] = FORMAT_ONE.format(value);
            result[1] = "GB";
            //            return  FORMAT.format(value) + "GB";
        }

        return result;
    }

    /**
     * 转换为时间数组
     * String[0] 为数值
     * String[1] 为单位
     * 61 ===》》》 1.05秒
     *
     * @param second
     * @return
     */
    public static String[] getTimeByArrayStr(long second) {
        String[] result = new String[2];
        if (second < 0) { //小于0字节则返回0
            result[0] = "0";
            result[1] = "秒";
            return result;
        }

        double value = 0.0f;
        if (second / (60f * 1000f) < 1) { //秒
            result[0] = String.valueOf(second / 1000);
            result[1] = "秒";
            //            return  size + "B";
        } else if ((second / (60f * 60f * 1000f)) < 1) {//分
            value = second / (60f * 1000f);
            result[0] = FORMAT_ONE.format(value);
            result[1] = "分";
            //            return  FORMAT.format(value) + "KB";
        } else {                              //时
            value = second / (60f * 60f * 1000f);
            result[0] = FORMAT_ONE.format(value);
            result[1] = "时";
        }

        return result;
    }


    /**
     * 获取Apk文件的Log图标
     *
     * @param context
     * @param apk_path
     * @return
     */
    public static Drawable getApkThumbnail(Context context, String apk_path) {
        if (context == null) {
            return null;
        }

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            /**获取apk的图标 */
            appInfo.sourceDir = apk_path;
            appInfo.publicSourceDir = apk_path;
            if (appInfo != null) {
                Drawable apk_icon = appInfo.loadIcon(pm);
                return apk_icon;
            }
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     * @Description 获取专辑封面
     */
    public static Bitmap createAlbumArt(final String filePath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }


    //----------------------------
    //1.压缩功能， 压缩到指定大小
    //2.Drawable --->>> Bitmap
    //3.Bitmap   --->>> byte[]
    //4.Bitmap   --->>> 生成图片


    /**
     * Drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //建立对应的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * Bitmap转ByteArray
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap 写入到SD卡
     *
     * @param bitmap
     * @param resPath
     * @return
     */
    public static boolean bitmapToSDCard(Bitmap bitmap, String resPath) {
        if (bitmap == null) {
            return false;
        }
        File resFile = new File(resPath);
        try {
            FileOutputStream fos = new FileOutputStream(resFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Bitmap压缩到指定的千字节数（比方说图片要压缩成32K，则传32）
     *
     * @param srcBitmap
     * @param maxKByteCount 比方说图片要压缩成32K，则传32
     * @return
     */
    public static Bitmap compressBitmap(Bitmap srcBitmap, int maxKByteCount) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            int option = 98;
            while (baos.toByteArray().length / 1024 >= maxKByteCount && option > 0) {
                baos.reset();
                srcBitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                option -= 2;
            }
        } catch (Exception e) {

        }
        //        bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(bais, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 压缩图片到指定的文件去————注意，图片尺寸没变，变的只是文件大小（图片的位深度改变了）
     *
     * @param srcBitmap
     * @param maxKByteCount 最大千字节数（比方说图片要压缩成32K，则传32）
     * @param targetPath    目标图片地址
     * @throws IOException
     */
    public static boolean compressBitmap(Bitmap srcBitmap, int maxKByteCount, String targetPath) {
        boolean result = false;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int option = 98;
            while (baos.toByteArray().length / 1024 >= maxKByteCount && option > 0) {
                baos.reset();
                srcBitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                option -= 2;
            }
            byte[] bitmapByte = baos.toByteArray();

            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.write(bitmapByte);

            result = true;

            try {
                fos.close();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!srcBitmap.isRecycled()) {
                srcBitmap.recycle();
                srcBitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //----------------------------


    /**
     * 获取接收到文件数量
     *
     * @return
     */
    public static int getReceiveFileCount() {
        int count = 0;
        File rootDir = new File(getRootDirPath());
        if (rootDir != null) {
            count = getFileCount(rootDir);
        }
        return count;
    }

    /**
     * 获取指定文件夹下面的文件数
     *
     * @param rootDir
     * @return
     */
    public static int getFileCount(File rootDir) {
        int count = 0;
        if (rootDir != null && rootDir.exists()) {
            for (File file : rootDir.listFiles()) {
                if (file.isDirectory()) {
                    count = count + getFileCount(file);
                } else {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * 获取接收到全部的文件大小
     *
     * @return
     */
    public static String getReceiveFileListTotalLength() {
        long total = 0;
        File rootDir = new File(getRootDirPath());
        if (rootDir != null) {
            total = getFileLength(rootDir);
        }
        return getFileSize(total);
    }

    /**
     * 递归获取指定文件夹的大小
     *
     * @param rootDir
     * @return
     */
    public static long getFileLength(File rootDir) {
        long len = 0;
        if (rootDir != null && rootDir.exists()) {
            for (File f : rootDir.listFiles()) {
                if (f.isDirectory()) {
                    len = len + getFileLength(f);
                } else {
                    len = len + f.length();
                }
            }
        }
        return len;
    }


    /**
     * 判断文件的缩略图是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isExistScreenShot(String fileName) {
        File file = new File(FilePathUtils.getScreenShotDirPath() + fileName);
        if (file.exists()) {
            return true;
        }
        return false;
    }


}
