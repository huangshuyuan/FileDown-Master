package com.hsy.filedown;

/**
 * 要下载的文件信息(下载任务)
 * 如文件保存的目录，名字，抓取文件的 URL 等
 *
 * @author syhuang
 * @date 2018/5/4
 */
public class SiteInfoBean {
    /**
     * 文件URL资源
     */
    private String sSiteURL;

    /**
     * 文件保存的路径(不包含文件名)
     */
    private String sFilePath = FilePathUtils.getRootDirPath();

    /**
     * 文件名
     */
    private String sFileName;

    /**
     * 下载线程个数
     */
    private int nSplitter;

    public SiteInfoBean() {
        // default value of nSplitter is 5
        this("", "", "", 5);
    }

    /**
     * @param sURL      文件资源URL
     * @param sPath     文件保存的路径(不包含文件名)
     * @param sName     文件名
     * @param nSpiltter 下载线程个数
     */
    public SiteInfoBean(String sURL, String sPath, String sName, int nSpiltter) {
        sSiteURL = sURL;
        sFilePath = sPath;
        sFileName = sName;
        this.nSplitter = nSpiltter;
    }

    /**
     * @param sURL      文件资源URL
     * @param sName     文件名
     * @param nSpiltter 下载线程个数
     */
    public SiteInfoBean(String sURL, String sName, int nSpiltter) {
        sSiteURL = sURL;
        sFileName = sName;
        this.nSplitter = nSpiltter;
    }

    public String getSSiteURL() {
        return sSiteURL;
    }

    public void setSSiteURL(String value) {
        sSiteURL = value;
    }

    /**
     * 获取文件保存的路径
     *
     * @return
     */
    public String getSFilePath() {
        return sFilePath;
    }

    public void setSFilePath(String value) {
        sFilePath = value;
    }

    /**
     * 获取文件名
     *
     * @return
     */
    public String getSFileName() {
        return sFileName;
    }

    public void setSFileName(String value) {
        sFileName = value;
    }

    /**
     * 分割成的子文件个数
     *
     * @return
     */
    public int getNSplitter() {
        return nSplitter;
    }

    public void setNSplitter(int nCount) {
        nSplitter = nCount;
    }
}
