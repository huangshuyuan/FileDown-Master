package filedowntest.hsy.com.filedown_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hsy.filedown.FilePathUtils;
import com.hsy.filedown.SiteFileFetch;
import com.hsy.filedown.SiteInfoBean;
import com.othershe.dutil.DUtil;
import com.othershe.dutil.callback.DownloadCallback;
import com.othershe.dutil.download.DownloadManger;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logText = findViewById(R.id.logText);
    }

    /**
     * 文件断点续传下载，原生代码写的
     *
     * @param view
     */

    public void downLoadFile1(View view) {
        downLoadFileTest1();
    }

    /**
     * 参考：https://github.com/Othershe/DUtil
     * 底层用的okhttp 文件支持断点续传下载
     * <p>
     * DUtil
     * <p>
     * <p>
     * 一个基于okhttp的文件下载、上传工具
     * 下载：支持多线程、断点续传下载，以及下载管理，原理、以及用法
     * 上传：支持表单形式上传、直接将文件作为请求体上传，原理、以及用法
     *
     * @param view
     */
    public void downLoadFile2(View view) {
        downLoadFileTest2();
    }

    private void downLoadFileTest1() {
        try {
            final String url = "http://imtt.dd.qq.com/16891/2DDBAAE459B2A287835183921B063840.apk?fsname=android.CloseMyNet_1.0_1.apk&csr=1bbd";
            String name = "test1.apk";
            try {

                SiteInfoBean bean = new SiteInfoBean(
                        url,
                        FilePathUtils.getRootDirPath(), name, 5);
                final SiteFileFetch fileFetch = new SiteFileFetch(bean);
                fileFetch.start();

                fileFetch.setLoadProgressListener(new SiteFileFetch.LoadProgressListener() {
                    @Override
                    public void onProgressUpdate(final long percent, final long length) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int progress = (int) ((percent) * 100 / length);
                                logText.setText("进度：" + progress + "%");
                            }
                        });

                    }

                    @Override
                    public void onCompleteLoad() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                logText.setText(logText.getText() + "\n下载完成");
                            }
                        });

                    }

                    @Override
                    public void onError(final int error, final String m) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                logText.setText(logText.getText() + "\n下载出错");
                            }
                        });

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoadFileTest2() {
        final String url = "http://imtt.dd.qq.com/16891/2DDBAAE459B2A287835183921B063840.apk?fsname=android.CloseMyNet_1.0_1.apk&csr=1bbd";
        String name = "test2.apk";
        final File file = new File(FilePathUtils.getRootDirPath() + name);

        final DownloadManger downloadManger = DUtil.init(this)
                .url(url)
                .path(FilePathUtils.getRootDirPath())
                .name(name)
                .childTaskCount(5)
                .build()
                .start(new DownloadCallback() {

                    @Override
                    public void onStart(long currentSize, long totalSize, float progress) {
                        logText.setText(logText.getText() + "\n开始下载");
                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize, float progress) {
                        logText.setText(progress + "%");
                    }

                    @Override
                    public void onPause() {
                        logText.setText(logText.getText() + "\n暂停");
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onFinish(File file) {
                        logText.setText(logText.getText() + "\n下载完成");
                    }

                    @Override
                    public void onWait() {
                    }

                    @Override
                    public void onError(String error) {
                        logText.setText(logText.getText() + "\n下载出错");
                    }
                });

    }


}
