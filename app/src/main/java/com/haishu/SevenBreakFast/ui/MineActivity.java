package com.haishu.SevenBreakFast.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haishu.SevenBreakFast.R;
import com.haishu.SevenBreakFast.app.BaseActivity;
import com.haishu.SevenBreakFast.app.Constant;
import com.haishu.SevenBreakFast.app.MyApplication;
import com.haishu.SevenBreakFast.entity.User;
import com.haishu.SevenBreakFast.utils.AppUtility;
import com.haishu.SevenBreakFast.utils.SPUtils;
import com.haishu.SevenBreakFast.utils.FileOperate;
import com.haishu.SevenBreakFast.utils.ShowDialog;
import com.haishu.SevenBreakFast.view.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MineActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_head_logined)
    private CircleImageView mHead;
    @ViewInject(R.id.noLogin)
    private TextView noLogin;
    @ViewInject(R.id.logined)
    private TextView logined;
    @ViewInject(R.id.tv_user_logined)
    private RelativeLayout tv_user_logined;
    @ViewInject(R.id.logined)
    private TextView mUsername;
    @ViewInject(R.id.tv_commended_logined)
    private TextView mCommoded;   //推荐奖
    @ViewInject(R.id.tv_coupon_logined)
    private TextView mCoupon;        //优惠券
    @ViewInject(R.id.rl_address_logined)
    private RelativeLayout mAddress;
    @ViewInject(R.id.rl_plurality_logined)
    private RelativeLayout mPlurality;    //兼职
    @ViewInject(R.id.rl_offers_logined)
    private RelativeLayout mOffers;         //优惠
    //    @ViewInject(R.id.rl_message_logined)
//    private RelativeLayout mMessage;
//    @ViewInject(R.id.rl_update_logined)
//    private RelativeLayout mUpdate;
    @ViewInject(R.id.rl_customer_logined)
    private TextView mCostomer;


    private List<User> users;
    private String imageDir = null;
    private Bitmap bitMap;
    private int user_id = -1;
    private FileOperate fo = new FileOperate();
    private String image_uri = Environment.getExternalStorageDirectory() + "/7zao/head.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ViewUtils.inject(this);

        init();
//        initHead();
    }

    public void init() {
        noLogin.setOnClickListener(this);
        logined.setOnClickListener(this);
        tv_user_logined.setOnClickListener(this);
        mHead.setOnClickListener(this);
        mCommoded.setOnClickListener(this);
        mCoupon.setOnClickListener(this);
        mAddress.setOnClickListener(this);
        mPlurality.setOnClickListener(this);
//        mMessage.setOnClickListener(this);
//        mUpdate.setOnClickListener(this);
        mCommoded.setOnClickListener(this);
        mCostomer.setOnClickListener(this);
        mOffers.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_logined:
                if (user_id == -1) {
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(mContext);
                }
                break;
            case R.id.noLogin:
                if (null == users || users.size() == 0) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.putExtra(Constant.FALG,Constant.FLAG_MINE);
                    startActivity(intent);
                }
                break;
            case R.id.tv_commended_logined:
                Toast.makeText(getApplicationContext(), "暂无推荐奖", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_coupon_logined:
                if (users == null || users.size() == 0) {
                    ShowDialog.showLogin(mContext,Constant.FLAG_MINE);
                } else {
                    Intent intent = new Intent(mContext, CouponActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_user_logined:
                if (users == null || users.size() == 0) {
                    ShowDialog.showLogin(mContext,Constant.FLAG_MINE);
                } else {
                    Intent site_intent = new Intent(MineActivity.this, SiteActivity.class);
                    startActivity(site_intent);
                }
                break;
            case R.id.rl_address_logined:
                if (users == null || users.size() == 0) {
                    ShowDialog.showLogin(mContext,Constant.FLAG_MINE);
                } else {
                    Intent address_intent = new Intent(mContext, AddressActivity.class);
                    address_intent.putExtra("addtoaddress", "addtoaddress");
                    startActivity(address_intent);
                }
                break;
            case R.id.rl_offers_logined:
                if (users == null || users.size() == 0) {
                    ShowDialog.showLogin(mContext,Constant.FLAG_MINE);
                } else {
                    Intent intent_cou = new Intent(mContext, CouponActivity.class);
                    startActivity(intent_cou);
                }
                break;
            case R.id.rl_plurality_logined:
                if (users == null || users.size() == 0) {
                    ShowDialog.showLogin(mContext,Constant.FLAG_MINE);
                } else {
                    Intent plurality_intent = new Intent(this, PluralityInfoActivity.class);
                    startActivity(plurality_intent);
                }
                break;
            case R.id.rl_update_logined:
                Toast.makeText(mContext, "暂无更新", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_customer_logined:
                Intent costomer_intent = new Intent(this, CustomerServiceActivity.class);
                startActivity(costomer_intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        users = AppUtility.getSputilsUser(mContext);
        //获取用户信息，如果没有显示未登录，有则显示昵称，没有昵称显示手机号
        if (null == users || users.size() == 0) {
            mHead.setImageResource(R.mipmap.head_round);
            mUsername.setText("未登录");
            noLogin.setVisibility(View.VISIBLE);
            logined.setVisibility(View.GONE);
        } else {
            if (users.get(0).getUser_nick().isEmpty()) {
                MyApplication.imageLoader.displayImage(users.get(0).getUser_img(), mHead);
                mUsername.setText(users.get(0).getUser_phone());
                user_id = users.get(0).getUser_id();
                noLogin.setVisibility(View.GONE);
                logined.setVisibility(View.VISIBLE);
            } else {
                mUsername.setText(users.get(0).getUser_nick());
                user_id = users.get(0).getUser_id();
                noLogin.setVisibility(View.GONE);
                logined.setVisibility(View.VISIBLE);
            }
            MyApplication.imageLoader.displayImage(users.get(0).getUser_img(), mHead);
        }
    }

    //弹出选择照片
    public void showDialog(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(R.layout.common_buttom_getphoto);
        // 设置宽高
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.AnimBottom);
        // 设置监听
        Button takephoto = (Button) window.findViewById(R.id.iv_takephoto_bp);
        RelativeLayout bg = (RelativeLayout) window.findViewById(R.id.bg);
        Button choosepic = (Button) window.findViewById(R.id.iv_choosepic_bp);
        Button cancel = (Button) window.findViewById(R.id.iv_cancle_bp);
        bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        takephoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {//判断是否有SD卡
                    doTakePhoto();// 用户点击了从照相机获取
                } else {
                    Toast.makeText(getApplicationContext(), "没有SD卡", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });
        choosepic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dopickphoto();
                dialog.cancel();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    /*
     * 选择系统图片
	 */
    public void dopickphoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(Constant.IMAGE_UNSPECTFIED);
        Intent wrapperIntent = Intent.createChooser(intent, null);
        startActivityForResult(wrapperIntent, Constant.PHOTO_ZOOM);
    }

    /**
     * 拍照获取图片
     */
    protected void doTakePhoto() {

        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/7zao";
        File file = new File(path);
        if (!file.exists())
            file.mkdir();

        imageDir = "temp.jpg";
        Intent intent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(path, imageDir)));
        startActivityForResult(intent, Constant.TAKE_PHOTO);
    }


    // 图片缩放
    public void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, Constant.IMAGE_UNSPECTFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constant.PHOTO_RESULT);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PHOTO_ZOOM:
                    Uri selectedImageUri = data.getData();
                    photoZoom(selectedImageUri);
                    break;
                case Constant.TAKE_PHOTO:
                    File picture = new File(Environment.getExternalStorageDirectory() + "/7zao/" + imageDir);
                    photoZoom(Uri.fromFile(picture));
                    break;
                case Constant.PHOTO_RESULT:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitMap = extras.getParcelable("data");
                    }
                    fo.saveBitmap("head", bitMap);
                    upload(image_uri);
                    break;
                default:
                    break;
            }
        }
    }

    //上传用户头像
    public void upload(String uri) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("singleFile", new File(uri));
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,
                Constant.UPLOAD_URL,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.d("upload", "success..." + responseInfo.result);

                        try {
                            JSONObject jsonResult = new JSONObject(responseInfo.result);
                            if (AppUtility.getStatus(mContext, jsonResult)) {
                                mHead.setImageBitmap(bitMap);
                                String imgURL = AppUtility.getImgUrl(mContext, jsonResult);
//                                SPUtils.put(mContext, "img_url", imgURL);

                                List<User> users1 = new ArrayList<User>();
                                User user = new User();
                                user.setUser_id(users.get(0).getUser_id());
                                user.setUser_nick(users.get(0).getUser_nick());
                                user.setUser_phone(users.get(0).getUser_phone());
                                user.setUser_img(imgURL);
                                users1.add(user);

                                String userInfo = new Gson().toJson(users1);
                                SPUtils.put(mContext, Constant.LOGIN_URL, userInfo);

                                modefyUserHead("img", imgURL, String.valueOf(user_id));
                            } else {
                                Toast.makeText(mContext, "设置头像失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.d("upload", "fail...");
                    }
                });
    }

    //更改用户头像
    public void modefyUserHead(String name, String value, final String user_id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", name);
        params.addBodyParameter("value", value);
        params.addBodyParameter("userId", user_id);

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);
        http.send(HttpRequest.HttpMethod.POST, Constant.MODIFY_USERINFO, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.d("success", responseInfo.result);
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = new JSONObject(responseInfo.result);
                            if (AppUtility.getStatus(mContext, jsonResult)) {
                                fo.reName("head", "head" + user_id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i(".....Failure", s.toString());
                        Toast.makeText(mContext, "头像设置失败", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
