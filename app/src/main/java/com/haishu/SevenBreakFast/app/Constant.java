package com.haishu.SevenBreakFast.app;

/**
 * Created by zyw on 2016/3/8.
 */
public class Constant {
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAILURE = 400;

    /**
     * 订单状态
     * <p/>
     * 0 已取消
     * 1 代付款
     * 2 已付款
     * 3 待发货
     * 4 配送中
     * 5 已完成
     * 6 待退款
     */
    public static final int cancle = 0;

    /**
     * 早餐 夜宵 超市标识id
     */
    public static final int ZAOCAN_ID = 1;
    public static final int YEXIAO_ID = 2;
    public static final int SUPPER_ID = 3;
    /**
     * 早餐 夜宵 超市标识
     */
    public static final String ZAOCAN = "breakfast";
    public static final String YEXIAO = "snacks";
    public static final String SUPPER = "supper";
    /**
     * serviceId 标识
     * placeId 标识
     * userId 标识
     */
    public static final String SERVICE_ID = "serviceId";
    //    public static final String PLACEID = "placeId";
    public static final String USER_ID = "userId";
    public static final String ADDRESS_ID = "addressId";
    /**
     * 地点id跟地点name标识
     */
    public static final String PLACE_ID = "placeId";
    public static final String PLACE_NAME = "placeName";
    /**
     * 选择地址和优惠价标识
     */
    public static final int CHOICE_ADDRESS = 0;
    public static final int VOUCHERS = 1;
    /**
     * 兼职id与name
     */
    public static final String JOB_ID = "job_id";
    public static final String JOB_NAME = "job_name";
    /**
     * 自定义标识
     */
    public static final String FOOD_CLASSIFY = "foodClassify";

    /**
     * phone number for Customer Service
     */
    public static final String PHONE_NUM = "10086";
    public static final String PHONE_NUM1 = "10010";

    /**
     * 登录后回到我的页面 标记
     */
    public static final int FLAG_HOME = 0;
    public static final int FLAG_ORDER = 1;
    public static final int FLAG_MINE = 2;
    public static final int FLAG_DEFAULT = 3;
    public static final String FALG = "flag";

    /**
     * seven break url
     * seven break url
     * 调用照片功能 标记
     */
    public static final int PHOTO_ZOOM = 10;
    public static final int TAKE_PHOTO = 11;
    public static final int PHOTO_RESULT = 12;
    public static final String IMAGE_UNSPECTFIED = "image/*";
    /**
     * seven break url
     */
    public static final String SEVENBREAK_URL = "http://120.55.72.54:8080/qizao/useract";

//    public static final String SEVENBREAK_URL = "http://192.168.0.101:8080/qizao/useract";
    /**
     * login
     */
    public static final String LOGIN_URL = SEVENBREAK_URL + "/loginByPhone.do";
    /**
     * loginByMessage
     */
    public static final String LOGIN_MSG_URL = SEVENBREAK_URL + "/loginByMessage.do";

    /**
     * get code
     */

    public static final String CODE_GET_URL = SEVENBREAK_URL + "/getShortMessage.do";

    /**
     * register
     */
    public static final String REGISTER_URL = SEVENBREAK_URL + "/addUserByPhone.do";

    /**
     * 获取商品数据
     */
    public static final String FOOD_URL = SEVENBREAK_URL + "/getProductList.do";

    /**
     * 修改用户密码/modifyPassword
     */
    public static final String MODIFY_PASSWORD_URL = SEVENBREAK_URL + "/modifyPassword.do";
    /**
     * 获取地点
     */
    public static final String GET_PLACE_URL = SEVENBREAK_URL + "/getPlaceList.do";
    /**
     * 获取订单列表
     */
    public static final String GETORDER_URL = SEVENBREAK_URL + "/getUserOrderList.do";
    /**
     * 获取用户收货地址
     */
    public static final String GETADDRESS_URL = SEVENBREAK_URL + "/getUserAddress.do";
    /**
     * 添加收货地址
     */
    public static final String ADDTOADDRESS_URL = SEVENBREAK_URL + "/addUserAddress.do";
    /**
     * upload
     */
    public static final String UPLOAD_URL = SEVENBREAK_URL + "/addSingleFile.do";
    /**
     * 修改用户信息
     */
    public static final String MODIFY_USERINFO = SEVENBREAK_URL + "/updateUserInfo.do";
    /**
     * 获取热门推荐
     */
    public static final String HOTFOOD_URL = SEVENBREAK_URL + "/getHotProductListByPlace.do";
    /**
     * 修改用户信息
     */
    public static final String CHANGEUSERINFO_URL = SEVENBREAK_URL + "/updateUserInfo.do";
    /**
     * 获取收货地址详情
     */
    public static final String ADDRESSDETAILS_URL = SEVENBREAK_URL + "/getAddressDetail.do";
    /**
     * 删除用户收货地址
     */
    public static final String DELADDRESS_URL = SEVENBREAK_URL + "/deleteUserAddress.do";
    /**
     * 修改用户收货地址
     */
    public static final String MODIFY_USER_ADDRESS_URL = SEVENBREAK_URL + "/updateUserAddress.do";
    /**
     * 获取用户配送信息
     */
    public static final String SETTLEMENT_INFO_URL = SEVENBREAK_URL + "/getDeliveryTime.do";
    /**
     * 获取优惠券列表
     */
    public static final String COUPON_URL = SEVENBREAK_URL + "/getUserCouponList.do";
    /**
     * 获取用户可用优惠券信息
     */
    public static final String DO_COUPON_URL = SEVENBREAK_URL + "/getUsableCoupon.do";
    /**
     * 客服中心
     */
    public static final String CUSTOM_SERVICE_CENTER_URL = SEVENBREAK_URL + "/getCustomerServiceList.do";
    /**
     * 广告图片
     */
    public static final String BANNER_URL = SEVENBREAK_URL + "/getAdvertList.do";
    /**
     * 宿舍楼
     */
    public static final String BUILDING_URL = SEVENBREAK_URL + "/getBuildingList.do";
    /**
     * 兼职信息
     */
    public static final String Plurality_URL = SEVENBREAK_URL + "/getJobList.do";
    /**
     * 添加兼职人信息
     */
    public static final String AddPlurality_URL = SEVENBREAK_URL + "/addPartTimer.do";
    /**
     * 生成订单
     */
    public static final String ADD_ORDER_URL = SEVENBREAK_URL + "/addOrderInfo.do";
    /**
     * 获取订单详情
     */
    public static final String ORDER_DETAILS_URL = SEVENBREAK_URL + "/getOrderDetail.do";
    /**
     * 添加支付信息接口
     */
    public static final String CREATE_ORDER_URL = SEVENBREAK_URL + "/createOrderInfo.do";
    /**
     * 修改订单状态
     * 0 已取消
     * 3 代发货
     * 6带退款
     */
    public static final String UPDATE_ORDER_STATE_URL = SEVENBREAK_URL + "/updateOrderState.do";
    /**
     * 版本更新
     */
    public static final String UPDATE_VERTIONCODE_URL = SEVENBREAK_URL + "/getLastestVersion.do";
    /**
     * 设置默认地址
     */
    public static final String SETTING_DEFULT_URL = SEVENBREAK_URL + "/updateDefaultAddress.do";
    /**
     * 获取默认地址
     */
    public static final String GET_DEFAULT_ADDRESS = SEVENBREAK_URL + "/getDefaultAddress.do";
    /**
     * 申请退款
     */
    public static final String REFUND_MONEY_URL = SEVENBREAK_URL + "/applyForRefund.do";
    /**
     * 是否能继续支付
     */
    public static final String ISPAYSTATE_UTL = SEVENBREAK_URL + "/isInPaymentTime.do";
}
