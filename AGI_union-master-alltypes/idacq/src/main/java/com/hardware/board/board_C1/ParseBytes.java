package com.hardware.board.board_C1;

import com.hardware.board.board_C1.boardc1hardware.Constants_C1;
import com.hardware.util.ByteUtil;

/**
 * Created by john on 2017/7/20.
 */
public class ParseBytes {

    public static int GetMsgType(byte[] bytes) {
        byte[] sb = new byte[4];

        sb[0] = bytes[4];
        sb[1] = bytes[5];
        sb[2] = 0;
        sb[3] = 0;

        int i = ByteUtil.getInt(sb, 0);
        return i;

    }

    public static String GetMsgStrType(byte[] bytes) {
        byte[] sb = new byte[4];

        sb[0] = bytes[4];
        sb[1] = bytes[5];
        sb[2] = 0;
        sb[3] = 0;

        int i = ByteUtil.getInt(sb, 0);
        String str = "";
        switch (i)
        {
            case 0xF019:
                str = "基站状态上报";
                break;
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG:
                str = "功率配置下发";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_PWR1_DEREASE_ACK:
                str = "功率配置回复";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND:
                str = "心跳指示上发";
                break;
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_INIT_SUCC_RSP:
                str = "心跳指示回复";
                break;
            case Constants_C1.O_FL_LMT_TO_ENB_SYS_RxGAIN_CFG:
                str = "增益配置下发";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_RxGAIN_ACK:
                str = "增益配置回复";
                break;
            case Constants_C1.O_FL_LMT_TO_ENB_REBOOT_CFG:
                str = "复位命令下发";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_REBOOT_ACK:
                str = "复位命令回复";
                break;

            case Constants_C1.O_FL_LMT_TO_ENB_SET_ADMIN_STATE_CFG:
                str = "小区去激活配置";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_SET_ADMIN_STATE_ACK:
                str = "小区去激活回复";
                break;

            case Constants_C1.O_FL_LMT_TO_ENB_MEAS_UE_CFG:
                str = "定位配置下发";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_MEAS_UE_ACK:
                str = "定位配置回复";
                break;

            case Constants_C1.O_FL_LMT_TO_ENB_SYS_ARFCN_CFG:
                str = "频点配置下发";
                break;
            case Constants_C1.O_FL_ENB_TO_LMT_SYS_ARFCN_ACK:
                str = "频点配置回复";
                break;

        }


        //return "";
        return String.format("%s(0x%04X)", str, i);

        //return String.format("%s(0x%04X)", str, i);

    }

}
