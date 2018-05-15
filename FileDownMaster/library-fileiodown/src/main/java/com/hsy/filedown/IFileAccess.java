package com.hsy.filedown;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

/**
 * 负责文件的存储
 *
 * @author syhuang
 * @date 2018/5/4
 */
public class IFileAccess implements Serializable
{
    RandomAccessFile oSavedFile;
    long nPos;

    public IFileAccess() throws IOException
    {
        this("", 0);
    }

    public IFileAccess(String sName, long nPos) throws IOException
    {
        oSavedFile = new RandomAccessFile(sName, "rw");
        this.nPos = nPos;
        oSavedFile.seek(nPos);
    }

    public synchronized int write(byte[] b, int nStart, int nLen)
    {
        int n = -1;
        try
        {
            oSavedFile.write(b, nStart, nLen);
            n = nLen;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return n;
    }
}