package com.example.abc.testui;

/**
 * Created by abc on 2017/9/3.
 */

public class RealTimeUIParamValues {
    public static final String[] SPINNERDATAS = {"TDD", "FDD"};
    // 1 --> 模式确定 按钮点击
    // 2 --> 重启 按钮点击
    // 3 --> 开始 按钮点击
    // 4 --> 停止 按钮点击
    public static final int[] BTNCLICKEDTAG = {1, 2, 3, 4};

    private String mode;  //模式 参数
    private ParamSetValue paramSetValue;  //参数设置 界面参数

    private int position;

    public RealTimeUIParamValues(){
        mode = SPINNERDATAS[0];
        paramSetValue = null;

        position = 0;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addPosition(int value){
        this.position += value;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public ParamSetValue getParamSetValue() {
        return paramSetValue;
    }

    public void setParamSetValue(ParamSetValue paramSetValue) {
        this.paramSetValue = paramSetValue;
    }
}
