package com.hsy.filedown;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

/**
 *
 * @author syhuang
 * @date 2018/5/4
 */
public class FileOperator {
    private static final String TAG       = FileOperator.class.getSimpleName();
    private static final int    BYTE_SIZE = 1024;

    private FileOperator() {
    }

    public static boolean copyDirectory(File originDirectory, File targetDirectory) throws Exception {
        boolean result = false;
        if (originDirectory != null && originDirectory.exists() && targetDirectory != null) {
            Log.d(TAG, "copyDir-->orgdir=" + originDirectory.getAbsolutePath() + "; tagdir=" +
                    targetDirectory.getAbsolutePath() + "; exist=" + targetDirectory.exists());
            if (!targetDirectory.exists()) {
                result = targetDirectory.mkdirs();
                if (!result) {
                    Log.e(TAG, "copyDir-->madir failed");
                    return result;
                }
            } else {
                File[] targetFileList = targetDirectory.listFiles();
                if (targetFileList != null) {
                    for (File file : targetFileList) {
                        Log.d(TAG, "delete " + file.getName());
                        if (file.isDirectory()) {
                            deleteFile(file);
                        }
                    }
                }
            }
            File[] fileList = originDirectory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        copyFile(file, targetDirectory);
                    } else if (file.isDirectory()) {
                        File directory =
                                new File(targetDirectory.getAbsolutePath() + "/" + file.getName());
                        if (directory.exists()) {
                            Log.d(TAG, "delete " + directory.getName());
                            deleteFile(directory);
                        }
                        result = copyDirectory(file, directory);
                        if (!result) {
                            return result;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void copyFile(File originFile, File targetDirectory) throws Exception {
        File newFile = new File(targetDirectory.getAbsolutePath() + "/" + originFile.getName());
        Log.d(TAG, "copyFile-->orgfile=" + originFile.getAbsolutePath() + "; tagfile=" +
                newFile.getAbsolutePath() + "; exist=" + newFile.exists());
        if (newFile.exists()) {
            boolean result = newFile.delete();
            if (!result) {
                Log.e(TAG, "copyFile-->file=" + newFile.getAbsolutePath() + " can't be deleted. ");
                return;
            }
        }
        try {
            FileInputStream fin = new FileInputStream(originFile);
            BufferedInputStream bin = new BufferedInputStream(fin);
            Log.d(TAG, "pout = " + targetDirectory + "/" + originFile.getName());
            FileOutputStream pout =
                    new FileOutputStream(targetDirectory + "/" + originFile.getName());
            BufferedOutputStream bout = new BufferedOutputStream(pout);
            byte[] buffer = new byte[BYTE_SIZE];
            while (true) {
                int byteRead = bin.read(buffer);
                if (byteRead == -1) {
                    break;
                }
                bout.write(buffer, 0, byteRead);
            }
            bout.close();
            pout.close();
            bin.close();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("file copy exception!");
        }
    }

    public static void deleteFile(File file) {
        if (file != null) {
            if (file.isFile()) {
                boolean result = file.delete();
                if (!result) {
                    Log.e(TAG,
                            "deleteFile-->file=" + file.getAbsolutePath() + " can't be deleted. ");
                }
            } else if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile != null && childFile.length > 0) {
                    for (File f : childFile) {
                        deleteFile(f);
                    }
                }
                boolean result = file.delete();
                if (!result) {
                    Log.e(TAG, "deleteFile-->directory=" + file.getAbsolutePath() + " can't be " +
                            "deleted.");
                }
            }
        }
    }

    public static void deleteContainedFile(File file) {
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile != null && childFile.length > 0) {
                for (File f : childFile) {
                    deleteFile(f);
                }
            }
        }
    }

    public static String readFile(File file) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (file == null || !file.exists()) {
            return stringBuffer.toString();
        }
        try {
            FileInputStream out = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(out, "utf-8");
            int ch;
            while ((ch = isr.read()) != -1) {
                stringBuffer.append((char) ch);
            }
            out.close();
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static boolean writeFile(File file, String content) {
        byte[] bt = content.getBytes(Charset.forName("UTF-8"));
        FileOutputStream in = null;
        try {
            in = new FileOutputStream(file);
            in.write(bt, 0, bt.length);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getFormatSize(File f) throws Exception {
        long size = getFolderSize(f);
        DecimalFormat df = new DecimalFormat("######0.0000");
        double result = (double) size / BYTE_SIZE / BYTE_SIZE / BYTE_SIZE;
        return df.format(result) + "GB";
    }

    public static long getFolderSize(File f) throws Exception {
        long size = 0;
        if (f != null && f.exists()) {
            File[] fileList = f.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    File file = fileList[i];
                    if (file != null) {
                        if (file.isDirectory()) {
                            size = size + getFolderSize(file);
                        } else {
                            size = size + getFileSize(file);
                        }
                    }
                }
            }
        }
        return size;
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return size;
    }

}
