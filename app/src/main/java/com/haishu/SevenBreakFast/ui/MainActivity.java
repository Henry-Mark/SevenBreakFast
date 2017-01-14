package com.haishu.SevenBreakFast.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.AppManager;
import com.lidroid.xutils.ViewUtils;

public class MainActivity extends TabActivity {

    private View view;
    private TabHost mTabHost;
    private int[] mTabImage = new int[]{R.drawable.selector_tabitem_image_home,
            R.drawable.selector_tabitem_image_order, R.drawable.selector_tabitem_image_mine};
    private int[] mTabText = new int[]{R.string.home, R.string.order, R.string.mine};
    private String[] mTabTag = new String[]{"tab1", "tab2", "tab3"};
    private Class<?>[] mTabClass = new Class<?>[]{HomeActivity.class, OrderActivity.class, MineActivity.class};
    private int flag = 0;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        this.mTabHost = this.getTabHost();
        this.mTabHost.setup();

        //设置显示的图像和文字
        for (int i = 0; i < mTabClass.length; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.tab_main_item, null);
            ((ImageView) view.findViewById(R.id.tabwidget_item_image)).setImageResource(mTabImage[i]);
            ((TextView) view.findViewById(R.id.tabwidget_item_text)).setText(mTabText[i]);
            this.mTabHost.addTab(this.mTabHost.newTabSpec(mTabTag[i]).setIndicator(view).setContent(new Intent(this, mTabClass[i])));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = getIntent().getIntExtra("FLAG", 0);

        if (flag == 0) {

        } else {
            initUI(flag);
        }
    }

    private void initUI(int flag) {
        //设置默认选中项
        mTabHost.setCurrentTab(flag);
    }

    /**
     * 退出
     */

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)   {
            //这里处理逻辑代码
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
                finish();
                System.exit(0);

            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
