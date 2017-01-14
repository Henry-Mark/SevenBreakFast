package com.haishu.SevenBreakFast.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haishu.SevenBreakFast.app.Constant;

import com.haishu.SevenBreakFast.entity.AddressList;
import com.haishu.SevenBreakFast.entity.BuildInfo;
import com.haishu.SevenBreakFast.entity.Coupon;
import com.haishu.SevenBreakFast.entity.CustomService;
import com.haishu.SevenBreakFast.entity.HotFood;
import com.haishu.SevenBreakFast.entity.ImageInfo;
import com.haishu.SevenBreakFast.entity.Order;
import com.haishu.SevenBreakFast.entity.OrderDetails;
import com.haishu.SevenBreakFast.entity.Place;

import com.haishu.SevenBreakFast.entity.FoodClassify;

import com.haishu.SevenBreakFast.entity.Plurality;
import com.haishu.SevenBreakFast.entity.SettlementInfo;
import com.haishu.SevenBreakFast.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class jsonUtils {

    public String str;

    public jsonUtils(String str) {
        this.str = str;
    }

    public String getFromJson(String key) {
        JSONTokener jsonParser = new JSONTokener(str);
        String value = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
            if (key.equals("result")) {
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    value = jsonObj.getString("msgCode");
                }

            } else {
                value = jsonObject.getString(key);
//            return value;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    //获取登录信息
    public static List<User> getLogin(Context mContext, JSONObject jsonResult) {
        List<User> users = new ArrayList<>();
        try {
            JSONArray data = jsonResult
                    .getJSONArray("result");
            Type type = new TypeToken<ArrayList<User>>() {
            }.getType();
            Gson gson = new Gson();
            users = gson.fromJson(data.toString(), type);

            String userInfo = gson.toJson(users);
            SPUtils.put(mContext, Constant.LOGIN_URL, userInfo);  //将数据存本地

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<Place> getPlace(Context mContext, JSONObject jsonObject) {
        List<Place> places = new ArrayList<>();
        try {
            JSONArray data = jsonObject.getJSONArray("result");
            Type type = new TypeToken<ArrayList<Place>>() {

            }.getType();
            Gson gson = new Gson();
            places = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return places;
    }

    //获取食物列表
    public static List<FoodClassify> getFoodList(JSONObject result) {
        List<FoodClassify> fclassify = new ArrayList<>();
        try {
            JSONArray data = result.getJSONArray("result");
            Type type = new TypeToken<ArrayList<FoodClassify>>() {
            }.getType();
            Gson gson = new Gson();
            fclassify = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fclassify;
    }

    public static List<Plurality> getPluralityList(JSONObject result) {
        List<Plurality> pluralities = new ArrayList<>();
        try {
            JSONArray data = result.getJSONArray("result");
            Type type = new TypeToken<ArrayList<Plurality>>() {
            }.getType();
            Gson gson = new Gson();
            pluralities = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pluralities;
    }

    //获取热门推荐列表
    public static List<HotFood> getHotFood(JSONObject jsonResult) {
        List<HotFood> hotFoods = new ArrayList<>();
        try {
            JSONArray data = jsonResult.getJSONArray("result");
            Type type = new TypeToken<ArrayList<HotFood>>() {
            }.getType();
            Gson gson = new Gson();
            hotFoods = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hotFoods;
    }

    //获取收货地址
    public static List<AddressList> getAddress(JSONObject jsonResult) {
        List<AddressList> address = new ArrayList<>();
        try {
            JSONArray data = jsonResult.getJSONArray("result");
            Type type = new TypeToken<ArrayList<AddressList>>() {
            }.getType();
            Gson gson = new Gson();
            address = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return address;
    }

    //获取用户配送信息
    public static List<SettlementInfo> getSettlementInfo(JSONObject json) {
        List<SettlementInfo> settlementInfos = new ArrayList<>();
        try {
            JSONArray data = json.getJSONArray("result");
            Type type = new TypeToken<ArrayList<SettlementInfo>>() {
            }.getType();
            Gson gson = new Gson();
            settlementInfos = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return settlementInfos;
    }

    //解析优惠券数据
    public static List<Coupon> getCouponData(JSONObject json) {
        List<Coupon> coupons = new ArrayList<>();
        try {
            JSONArray data = json.getJSONArray("result");
            Type type = new TypeToken<ArrayList<Coupon>>() {
            }.getType();
            Gson gson = new Gson();
            coupons = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coupons;
    }

    //解析客服中心数据，并存在list
    public static List<CustomService> getCustomService(JSONObject json) {
        List<CustomService> customs = new ArrayList<>();
        try {
            JSONArray data = json.getJSONArray("result");
            Type type = new TypeToken<ArrayList<CustomService>>() {
            }.getType();
            Gson gson = new Gson();
            customs = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customs;
    }

    //获取广告数据并解析
    public static List<ImageInfo> getBanner(JSONObject json) {
        List<ImageInfo> images = new ArrayList<>();
        try {
            JSONArray data = json.getJSONArray("result");
            Type type = new TypeToken<ArrayList<ImageInfo>>() {
            }.getType();
            Gson gson = new Gson();
            images = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }

    //获取宿舍楼，并解析
    public static List<BuildInfo> getBuild(JSONObject json) {
        List<BuildInfo> builds = new ArrayList<>();
        try {
            JSONArray data = json.getJSONArray("result");
            Type type = new TypeToken<ArrayList<BuildInfo>>() {
            }.getType();
            Gson gson = new Gson();
            builds = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builds;
    }

    //获取订单列表数据，并解析
    public static List<Order> getOrder(JSONObject jsonResult) {
        List<Order> orders = new ArrayList<>();
        try {
            JSONArray data = jsonResult.getJSONArray("result");
            Type type = new TypeToken<ArrayList<Order>>() {
            }.getType();
            Gson gson = new Gson();
            orders = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }

    //获取订单详情，并解析
    public static List<OrderDetails> getOrderDetails(JSONObject json) {
        List<OrderDetails> oDetails = new ArrayList<>();
        try {
            JSONArray data = json.getJSONArray("result");
            Type type = new TypeToken<ArrayList<OrderDetails>>() {
            }.getType();
            Gson gson = new Gson();
            oDetails = gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return oDetails;
    }
}