package com.hsy.filedown;

/**
 *
 * @author syhuang
 * @date 2018/5/4
 */

public class OtaResult
{
    private int Status;
    private String DownloadUrl;
    private String Message;
    private String MD5;
    private String VersionNum;

    public int getStatus()
    {
        return Status;
    }

    public void setStatus(int status)
    {
        Status = status;
    }

    public String getDownloadUrl()
    {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        DownloadUrl = downloadUrl;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        Message = message;
    }

    public String getMD5()
    {
        return MD5;
    }

    public void setMD5(String MD5)
    {
        this.MD5 = MD5;
    }

    public String getVersionNum()
    {
        return VersionNum;
    }

    public void setVersionNum(String versionNum)
    {
        VersionNum = versionNum;
    }

    @Override
    public String toString()
    {
        return "OtaResult{" + "Status=" + Status + ", DownloadUrl='" + DownloadUrl + '\'' +
                ", Message='" + Message + '\'' + ", MD5='" + MD5 + '\'' + ", VersionNum='" +
                VersionNum + '\'' + '}';
    }
}
