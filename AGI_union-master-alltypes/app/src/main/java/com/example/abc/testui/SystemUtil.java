package com.example.abc.testui;


import android.content.Context;

import java.util.regex.Pattern;

/**
 * 调用系统应用工具类
 * Created by z2wenfa on 2016/3/23.
 */
public class SystemUtil {

    /**
     * 根据手机的分辨率从 DP 的单位 转成为PX(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 通过正则表达式，判断字符串中是不是数字
     * @param str
     * @return
     */
    public static boolean is15bitsNumeric(String str){
        if (str.length()!=15){
            return false;
        }
        else {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
    }

}
