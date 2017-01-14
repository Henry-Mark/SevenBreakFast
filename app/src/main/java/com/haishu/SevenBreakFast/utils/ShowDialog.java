package com.haishu.SevenBreakFast.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.ui.LoginActivity;

/**
 * Created by zyw on 2016/4/7.
 */
public class ShowDialog {
    /**
     * 提问框的 Listener
     *
     * @author Lei
     */
    // 因为本类不是activity所以通过继承接口的方法获取到点击的事件
    public interface OnClickYesListener {
        abstract void onClickYes();
    }

    /**
     * 提问框的 Listener
     */
    public interface OnClickNoListener {
        abstract void onClickNo();
    }

    public static void showQuestionDialog(Context context, String title,
                                          String text, final OnClickYesListener listenerYes,
                                          final OnClickNoListener listenerNo) {

        Builder builder = new Builder(context);

        if (!isBlank(text)) {
            // 此方法为dialog写布局
            final TextView textView = new TextView(context);
            textView.setText(text);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(10, 10, 10, 10);
            LinearLayout layout = new LinearLayout(context);

            layout.setPadding(10, 0, 0, 10);
            layout.addView(textView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            builder.setView(layout);
        }
        // 设置title
        builder.setTitle(title);
        // 设置确定按钮，固定用法声明一个按钮用这个setPositiveButton
        builder.setPositiveButton(context.getString(R.string.yes),
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 如果确定被点击
                        if (listenerYes != null) {
                            listenerYes.onClickYes();
                        }
                    }
                });
        // 设置取消按钮，固定用法声明第二个按钮要用setNegativeButton
        builder.setNegativeButton(context.getString(R.string.no),
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 如果取消被点击
                        if (listenerNo != null) {
                            listenerNo.onClickNo();
                        }
                    }
                });

        // 控制这个dialog可不可以按返回键，true为可以，false为不可以
        builder.setCancelable(false);
        // 显示dialog
        builder.create().show();

    }

    /**
     * 处理字符的方法
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static void showLogin(final Context mContext,int flag) {
        showQuestionDialog(mContext, "提示", "您还未登录，请您登录！！！", new OnClickYesListener() {
            @Override
            public void onClickYes() {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        }, new OnClickNoListener() {
            @Override
            public void onClickNo() {

            }
        });
    }
}
