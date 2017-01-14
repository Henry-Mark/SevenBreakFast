package com.haishu.SevenBreakFast.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;

/**
 * 自定义titlebar
 *
 * @author Administrator
 */
public class TitleBarView extends RelativeLayout {

    public TitleBarView(Context context) {
        super(context);
        initView(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private RelativeLayout title;
    private TextView tv_left, tv_center, tv_right;
    private LinearLayout ly_left, ly_right;
    private ImageView img_left, img_right_one, img_right_two;
    private CheckBox cBox;
    private Button btn_right;

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.common_titlebar, this);
        title = (RelativeLayout) findViewById(R.id.common_titlebar);

        tv_left = (TextView) findViewById(R.id.common_titlebar_tv_left);
        tv_center = (TextView) findViewById(R.id.common_titlebar_tv_center);
        tv_right = (TextView) findViewById(R.id.common_titlebar_tv_right);
        // LinearLayout作为点击控件
        ly_left = (LinearLayout) findViewById(R.id.common_titlebar_ly_left);
        ly_right = (LinearLayout) findViewById(R.id.common_titlebar_ly_right);

        img_left = (ImageView) findViewById(R.id.common_titlebar_img_left);
        img_right_one = (ImageView) findViewById(R.id.common_titlebar_img_right_one);
        img_right_two = (ImageView) findViewById(R.id.common_titlebar_img_right_two);

        cBox = (CheckBox) findViewById(R.id.common_titlebar_checkBox);

        btn_right = (Button) findViewById(R.id.common_titlebar_btn_right);
        selfadaption();
    }

    // DimensUitl dimensUitl;

    /**
     * 自适应
     */
    private void selfadaption() {
        // dimensUitl = new DimensUitl();
        // title.setLayoutParams(dimensUitl.getParamsByRelativeLayout(title,
        // Constants.SCALE_TITLE_HEIGHT));
    }

    /**
     * @param param
     */
    public void setBtnRightText(String param) {
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText(param);
    }

    /**
     * @param resId
     */
    public void setBtnRightText(int resId) {
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText(resId);
    }

    /**
     * @param resId
     */
    public void setBtnRightBackground(int resId) {
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setBackgroundResource(resId);
    }

    /**
     * 设置checkbox
     *
     * @param param null为隐藏
     */
    public void setCheckBox(String param) {
        if (param != null) {
            cBox.setText(param);
            cBox.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.GONE);
        } else {
            tv_right.setVisibility(View.VISIBLE);
            cBox.setVisibility(View.GONE);
        }
    }

    /**
     * 设置checkbox 致空则影藏
     *
     * @param resId
     */
    public void setCheckBox(int resId) {
        if (String.valueOf(resId) != null) {
            cBox.setText(resId);
            cBox.setVisibility(View.VISIBLE);
            tv_right.setVisibility(View.GONE);
        } else {
            tv_right.setVisibility(View.VISIBLE);
            cBox.setVisibility(View.GONE);
        }
    }

    /**
     * 设置图片可见性
     *
     * @param visible_img_left  View.VISIBLE/View.GONE
     * @param visable_img_right View.VISIBLE/View.GONE
     */
    public void setVisible(int visible_img_left, int visable_img_right) {
        img_left.setVisibility(visible_img_left);
        img_right_two.setVisibility(visable_img_right);
    }

    /**
     * 设置popwindow
     *
     * @param pop
     * @param title
     */
    public void setPopWindow(PopupWindow pop, TitleBarView title) {
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.showAsDropDown(title, title.getWidth() / 2, 0);
        // pop.setAnimationStyle(R.style.popwin_anim_style);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.update();
    }

    public void setLyLeftBackgroundResource(int resId) {
        ly_left.setBackgroundResource(resId);
    }

    public void setLyRightBackgroundResource(int resId) {
        ly_right.setBackgroundResource(resId);
    }

    /**
     * 设置titlebar 背景
     *
     * @param resId
     */
    public void setTitleBackgroundResource(int resId) {
        title.setBackgroundResource(resId);
    }

    /**
     * 设置左边图片资源
     *
     * @param resId
     */
    public void setImgLeftResource(int resId) {
        img_left.setImageResource(resId);
    }

    /**
     * 设置右边1图片资源
     *
     * @param resId
     */
    public void setImgRightOneResource(int resId) {
        img_right_one.setImageResource(resId);
    }

    /**
     * 设置右边2图片资源
     *
     * @param resId
     */
    public void setImgRightTwoResource(int resId) {
        img_right_two.setImageResource(resId);
    }

    /**
     * 设置左边文字
     *
     * @param resId
     */
    public void setTvLeftText(int resId) {
        tv_left.setText(resId);
    }

    public void setTvLeftTextColor(ColorStateList colorStateList) {
        tv_left.setTextColor(colorStateList);
    }

    public void setTvRightTextColor(int resId) {
        tv_right.setTextColor(resId);
    }

    public void setTvCenterTextColor(int resId) {
        tv_center.setTextColor(resId);
    }

    public void setTvCenterTextColor(ColorStateList colorStateList) {
        tv_center.setTextColor(colorStateList);
    }

    /**
     * 设置左边文字
     *
     * @param param
     */
    public void setTvLeftText(String param) {
        tv_left.setText(param);
    }

    public void setTvLeftTextSize(float size) {
        tv_left.setTextSize(size);
    }

    /**
     * 设置中间文字
     *
     * @param resId
     */
    public void setTvCenterText(int resId) {
        tv_center.setText(resId);
    }

    /**
     * 设置中间文字
     *
     * @param param
     */
    public void setTvCenterText(String param) {
        tv_center.setText(param);
    }

    public void setTvCenterTextSize(float size) {
        tv_center.setTextSize(size);
    }

    /**
     * 设置右边文字
     *
     * @param resId
     */
    public void setTvRightText(int resId) {
        tv_right.setText(resId);
    }

    /**
     * 设置右边文字
     *
     * @param param
     */
    public void setTvRightText(String param) {
        tv_right.setText(param);
    }

    /**
     * 右边图片的动画
     *
     * @param animation
     */
    public void startImgRightAnimation(Animation animation) {
        img_right_two.startAnimation(animation);
    }

    /**
     * 监听事件
     */

    public void setLyLeftOnclickListener(OnClickListener listener) {
        ly_left.setOnClickListener(listener);
    }

    public void setLyRightOnclickListener(OnClickListener listener) {
        ly_right.setOnClickListener(listener);
    }

    public void setImgLeftOnclickListener(OnClickListener listener) {
        img_left.setOnClickListener(listener);
    }

    public void setImgRightOneOnclickListener(OnClickListener listener) {
        img_right_one.setOnClickListener(listener);

    }

    public void setImgRightTwoOnclickListener(OnClickListener listener) {
        img_right_two.setOnClickListener(listener);
    }

    public void setBtnRightOnclickListener(OnClickListener listener) {
        btn_right.setOnClickListener(listener);

    }

    public void setTvRightOnclickListener(OnClickListener listener) {
        tv_right.setOnClickListener(listener);
    }
}
