package com.haishu.SevenBreakFast.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zyw on 2016/3/23.
 */
public class BannerDetails extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.titleBar)
    private TitleBarView titleBarView;
    @ViewInject(R.id.bannerContent)
    private WebView coutent;

    private int bannerId;
    private String bannerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_details);
        ViewUtils.inject(this);

        initData();
        initBar();

    }

    private void initData() {
        bannerContent = getIntent().getStringExtra("bannerContent");
        initView();
    }

    private void initBar() {
        titleBarView.setImgLeftResource(R.mipmap.btn_left_jiantou);
        titleBarView.setTvLeftText("广告详情");
        titleBarView.setLyLeftOnclickListener(this);

    }

    private void initView() {
        coutent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        coutent.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        String htmlData = bannerContent;
        htmlData = htmlData.replaceAll("&amp;", "");
        htmlData = htmlData.replaceAll("quot;", "\"");
        htmlData = htmlData.replaceAll("lt;", "<");
        htmlData = htmlData.replaceAll("gt;", ">");
        htmlData = htmlData.replace("<img", "<img width=\"100%\"");

        coutent.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_titlebar_ly_left:
                finish();
                break;
        }
    }
}
