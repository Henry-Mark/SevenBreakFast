package com.haishu.SevenBreakFast.utils;

/**
 * Created by henry on 2016/3/7.
 */
public class StringCompare {
    /*
        判断两个字符串是否相等
     */
    public Boolean isStringEqual(String str1,String str2){
        if((!str1.isEmpty()) && (!str2.isEmpty())){
            return str1.equals(str2);
        }
        return false;
    }
}
