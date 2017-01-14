package com.haishu.SevenBreakFast.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.NetUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zyw on 2016/3/31.
 */
public class WelcomeActivity extends BaseActivity {
    @ViewInject(R.id.welcome)
    private ImageView welcome_image;
    @ViewInject(R.id.welcome_title)
    private TextView welcome_title;
    private String vercode;
    private String url;
    private String name;
    private File file;

    private static final int WHAT_START = 2;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    openFile(file);
                    break;
                case 2:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewUtils.inject(this);

        initData();
    }

    private void initData() {
        if (NetUtil.isNetworkAvailable(mContext)) {
            getVersionCode();
        } else {
            Message message = new Message();
            message.what = 2;
            // 往handler发送一条消息 更改button的text属性
            mHandler.sendEmptyMessageDelayed(WHAT_START, 1500);
        }
    }

    /**
     * 获取服务器版本信息
     */
    private void getVersionCode() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("osName", "android");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, Constant.UPDATE_VERTIONCODE_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject json = new JSONObject(responseInfo.result);
                    if (AppUtility.getStatus(mContext, json)) {
                        JSONArray jsonArray = json.getJSONArray("result");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        name = obj.getString("app_name");
                        vercode = obj.getString("version_number");
                        url = obj.getString("path");
                        String code = AppUtility.getVersionCode(mContext) + "." + AppUtility.getAppVersionName(mContext);

                        int code1 = Integer.parseInt(vercode.replace(".", ""));
                        int code2 = Integer.parseInt(code.replace(".", ""));

                        //code1 <= code2 不更新，否则更新
                        if (code1 <= code2) {
                            welcome_image.setVisibility(View.VISIBLE);
                            welcome_title.setVisibility(View.GONE);
                            Message message = new Message();
                            message.what = 2;
                            // 往handler发送一条消息 更改button的text属性
                            mHandler.sendEmptyMessageDelayed(WHAT_START, 1500);

                        } else {
                            welcome_image.setVisibility(View.GONE);
                            welcome_title.setVisibility(View.VISIBLE);
                            file = downFile(url);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 后台在下面一个Apk 下载完成后返回下载好的文件
     *
     * @param httpUrl
     * @return
     */
    private File downFile(final String httpUrl) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    FileOutputStream fileOutputStream = null;
                    InputStream inputStream;
                    if (connection.getResponseCode() == 200) {
                        inputStream = connection.getInputStream();

                        if (inputStream != null) {
                            file = getFile(httpUrl);
                            fileOutputStream = new FileOutputStream(file);
                            byte[] buffer = new byte[1024];
                            int length = 0;

                            while ((length = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, length);
                            }
                            fileOutputStream.close();
                            fileOutputStream.flush();
                        }
                        inputStream.close();
                    }

                    System.out.println("已经下载完成");
                    // 往handler发送一条消息 更改button的text属性
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    mHandler.sendMessage(message);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return file;
    }

    /**
     * 根据传过来url创建文件
     */
    private File getFile(String url) {
        File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getFilePath(url));
        return files;
    }

    /**
     * 截取出url后面的apk的文件名
     *
     * @param url
     * @return
     */
    private String getFilePath(String url) {
        return url.substring(url.lastIndexOf("/"), url.length());
    }

    //打开APK程序代码

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);

    }
}
