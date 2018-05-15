package com.hsy.filedown;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件下载子线程(负责文件的其中一部分内容下载)
 *
 * @author syhuang
 * @date 2018/5/4
 */
public class FileSplitterFetch extends Thread {
    String TAG = FileSplitterFetch.class.getSimpleName();
    /**
     * File   URL
     */
    String sURL;
    /**
     * File Snippet Start Position
     */
    long   nStartPos;
    /**
     * File Snippet End Position
     */
    long   nEndPos;
    int    nThreadID;
    /**
     * Downing is over
     */
    boolean     bDownOver   = false;
    /**
     * Stop identical
     */
    boolean     bStop       = false;
    /**
     * File Access interface
     */
    IFileAccess fileAccessI = null;

    /**
     * @param sURL   文件资源URL
     * @param sName  要保存的文件名(完整路径,绝对路径)
     * @param nStart 文件指针开始位置
     * @param nEnd   文件指针结束位置
     * @param id     线程ID
     * @throws IOException
     */
    public FileSplitterFetch(String sURL, String sName, long nStart, long nEnd, int id)
            throws IOException {
        this.sURL = sURL;
        this.nStartPos = nStart;
        this.nEndPos = nEnd;
        nThreadID = id;
        fileAccessI = new IFileAccess(sName, nStartPos);
    }

    @Override
    public void run() {
        while (nStartPos < nEndPos && !bStop) {
            try {
                URL url = new URL(sURL);

                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestProperty("User-Agent", "NetFox");

                String sProperty = "bytes=" + nStartPos + "-";
                httpConnection.setRequestProperty("RANGE", sProperty);

                Log.i(TAG, sProperty);

                int responseCode = httpConnection.getResponseCode();
                Log.i(TAG, "responseCode --> " + responseCode);
                InputStream input = null;
                if (responseCode == 200 || responseCode == 206) {
                    input = httpConnection.getInputStream();
                } else if (responseCode >= 400) {
                    if (onErrorListener != null) {
                        onErrorListener.onError();
                    }
                }

                int batchSize = 10240;
                byte[] batchArray = new byte[batchSize];
                int nRead;

//                long skipL = input.skip(nStartPos);
                long skipL = skipBytesFromStream(input, nStartPos);
                Log.i(TAG, "Thread " + nThreadID + " nStartPos --> " + nStartPos + ", skipL = " +
                        skipL);
                while ((nRead = input.read(batchArray, 0, batchSize)) > 0 && nStartPos < nEndPos &&
                        !bStop) {
                    nStartPos += fileAccessI.write(batchArray, 0, nRead);
                    if (batchSize > nEndPos - nStartPos + 1) {
                        batchSize = (int) (nEndPos - nStartPos + 1);
                        batchArray = new byte[batchSize];
                    }
                }

                Log.i(TAG, "Thread " + nThreadID + " is over!" + ",nStartPos=" + nStartPos +
                        ",nEndPos=" + nEndPos);
                bDownOver = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印回应的头信息
     *
     * @param con
     */
    public void logResponseHead(HttpURLConnection con) {
        for (int i = 1; ; i++) {
            String header = con.getHeaderFieldKey(i);
            if (header != null)
            // responseHeaders.put(header,httpConnection.getHeaderField(header));
            {
                Log.i(TAG, header + " : " + con.getHeaderField(header));
            } else {
                break;
            }
        }
    }

    /**
     * 重写了Inpustream 中的skip(long n) 方法，将数据流中起始的n 个字节跳过
     */
    private long skipBytesFromStream(InputStream inputStream, long n) {
        long remaining = n;
        // SKIP_BUFFER_SIZE is used to determine the size of skipBuffer
        int SKIP_BUFFER_SIZE = 2048*5;
        // skipBuffer is initialized in skip(long), if needed.
        byte[] skipBuffer = null;
        int nr = 0;
        if (skipBuffer == null) {
            skipBuffer = new byte[SKIP_BUFFER_SIZE];
        }
        byte[] localSkipBuffer = skipBuffer;
        if (n <= 0) {
            return 0;
        }
        while (remaining > 0) {
            try {
                nr = inputStream.read(localSkipBuffer, 0,
                        (int) Math.min(SKIP_BUFFER_SIZE, remaining));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }
        return n - remaining;
    }

    private OnErrorListener onErrorListener;

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    /**
     * 错误回调
     */
    public interface OnErrorListener {
        /**
         * 方法
         */
        void onError();
    }

    public void splitterStop() {
        bStop = true;
    }
}
