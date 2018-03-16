package com.hardware.board.board_C1.boardc1hardware;

/**
 * Created by usr on 2017/7/16.
 */
//这里放置了一些和C1板子相关的宏定义
public class Constants_C1 {


    public static final int FrameHeader = 0x5555AAAA;



    public static final int WorkMode_TDD = 0x00FF;
    public static final int WorkMode_FDD = 0xFF00;
    //宏定义

    public static final int C_MAX_IMSI_LEN = 17; /*IMSI 数据长度*/
    public static final int C_MAX_IMEI_LEN = 17; /*IMEI 数据长度*/
    public static final int C_MAX_REM_ARFCN_NUM = 10; /*最大的扫频频点数目*/
    public static final int C_MAX_INTRA_NEIGH_NUM = 32;  /*最大的同频邻区数目*/
    public static final int C_MAX_COLLTECTION_INTRA_CELL_NUM = 8;  /*最大的同频采集小区数目*/
    public static final int C_MAX_CONTROL_PROC_UE_NUM = 10;  /*管控名单配置时每次最大可以添加/删除的 UE 数*/
    public static final int C_MAX_CONTROL_LIST_UE_NUM = 200;/*管控名单中可以含有的最大 UE 数*/
    public static final int C_MAX_LOCATION_BLACKLIST_UE_NUM = 50; /*定位黑名单中可以含有的最大 UE 数*/


    //TODO  以下两个瞎写的 要改
    public static final int MAX_INTER_FREQ_LST = 10; /*瞎写的要改*/
    public static final int MAX_INTER_FREQ_NGH = 10; /*瞎写的要改*/

    //消息类型定义：

    public static final int O_FL_LMT_TO_ENB_SYS_MODE_CFG = 0xF001;
    public static final int O_FL_ENB_TO_LMT_SYS_MODE_ACK = 0xF002;
    public static final int O_FL_LMT_TO_ENB_SYS_ARFCN_CFG = 0xF003;
    public static final int O_FL_ENB_TO_LMT_SYS_ARFCN_ACK = 0xF004;
    public static final int O_FL_ENB_TO_LMT_UE_INFO_RPT = 0xF005;
    public static final int O_FL_LMT_TO_ENB_MEAS_UE_CFG = 0xF006;
    public static final int O_FL_ENB_TO_LMT_MEAS_UE_ACK = 0xF007;
    public static final int O_FL_ENB_TO_LMT_MEAS_INFO_RPT = 0xF008;
    public static final int O_FL_LMT_TO_ENB_REM_CFG = 0xF009;
    public static final int O_FL_ENB_TO_LMT_REM_INFO_RPT = 0xF00A;
    public static final int O_FL_LMT_TO_ENB_REBOOT_CFG = 0xF00B;
    public static final int O_FL_ENB_TO_LMT_REBOOT_ACK = 0xF00C;
    public static final int O_FL_LMT_TO_ENB_SET_ADMIN_STATE_CFG = 0xF00D;
    public static final int O_FL_ENB_TO_LMT_SET_ADMIN_STATE_ACK = 0xF00E;
    public static final int O_FL_ENB_TO_LMT_SYS_INIT_SUCC_IND = 0xF010;
    public static final int O_FL_LMT_TO_ENB_SYS_INIT_SUCC_RSP = 0xF011;
    public static final int O_FL_LMT_TO_ENB_SYS_RxGAIN_CFG = 0xF013;
    public static final int O_FL_ENB_TO_LMT_SYS_RxGAIN_ACK = 0xF014;
    public static final int O_FL_LMT_TO_ENB_SYS_PWR1_DEREASE_CFG = 0xF015;
    public static final int O_FL_ENB_TO_LMT_SYS_PWR1_DEREASE_ACK = 0xF016;
    public static final int O_FL_LMT_TO_ENB_REDIRECT_INFO_CFG = 0xF017;
    public static final int O_FL_ENB_TO_LMT_REDIRECT_INFO_ACK = 0xF018;
    public static final int O_FL_LMT_TO_ENB_GET_ENB_STATE = 0xF01A;
    public static final int O_FL_ENB_TO_LMT_ENB_STATE_IND = 0xF019;
    public static final int O_FL_LMT_TO_ENB_IP_CFG = 0xF01B;
    public static final int O_FL_ENB_TO_LMT_IP_CFG_ACK = 0xF01C;
    public static final int O_FL_LMT_TO_ENB_RESET_CFG = 0xF01D;
    public static final int O_FL_ENB_TO_LMT_RESET_ACK = 0xF01E;
    public static final int O_FL_LMT_TO_ENB_SET_SYS_TMR = 0xF01F;
    public static final int O_FL_ENB_TO_LMT_SET_SYS_TMR_ACK = 0xF020;
    public static final int O_FL_LMT_TO_ENB_SET_QRXLEVMIN = 0xF021;
    public static final int O_FL_ENB_TO_LMT_SET_QRXLEVMIN_ACK = 0xF022;
    public static final int O_FL_LMT_TO_ENB_REM_MODE_CFG = 0xF023;
    public static final int O_FL_ENB_TO_LMT_REM_MODE_CFG_ACK = 0xF024;
    public static final int O_FL_LMT_TO_ENB_LMTIP_CFG = 0xF025;
    public static final int O_FL_ENB_TO_LMT_LMTIP_CFG_ACK = 0xF026;
    public static final int O_FL_LMT_TO_ENB_GET_ARFCN = 0xF027;
    public static final int O_FL_ENB_TO_LMT_ARFCN_IND = 0xF028;
    public static final int O_FL_LMT_TO_ENB_GPS_PP1S_CFG = 0xF029;
    public static final int O_FL_ENB_TO_LMT_GPS_PP1S_ACK = 0xF02A;
    public static final int O_FL_LMT_TO_ENB_BASE_INFO_QUERY = 0xF02B;
    public static final int O_FL_ENB_TO_LMT_BASE_INFO_QUERY_ACK = 0xF02C;
    public static final int O_FL_LMT_TO_ENB_SYNC_INFO_QUERY = 0xF02D;
    public static final int O_FL_ENB_TO_LMT_SYNC_INFO_QUERY_ACK = 0xF02E;
    public static final int O_FL_LMT_TO_ENB_CELL_STATE_INFO_QUERY = 0xF02F;
    public static final int O_FL_ENB_TO_LMT_CELL_STATE_INFO_QUERY_ACK = 0xF030;
    public static final int O_FL_LMT_TO_ENB_RXGAIN_POWER_DEREASE_QUERY = 0xF031;
    public static final int O_FL_ENB_TO_LMT_RXGAIN_POWER_DEREASE_QUERY_ACK = 0xF032;
    public static final int O_FL_LMT_TO_ENB_ENB_IP_QUERY = 0xF033;
    public static final int O_FL_ENB_TO_LMT_ENB_IP_QUERY_ACK = 0xF034;
    public static final int O_FL_LMT_TO_ENB_QRXLEVMIN_VALUE_QUERY = 0xF035;
    public static final int O_FL_ENB_TO_LMT_QRXLEVMIN_VALUE_QUERY_ACK = 0xF036;
    public static final int O_FL_LMT_TO_ENB_REM_CFG_QUERY = 0xF037;
    public static final int O_FL_ENB_TO_LMT_REM_CFG_QUERY_ACK = 0xF038;
    public static final int O_FL_LMT_TO_ENB_CONTROL_UE_LIST_CFG = 0xF039;
    public static final int O_FL_ENB_TO_LMT_CONTROL_UE_LIST_CFG_ACK = 0xF03A;
    public static final int O_FL_LMT_TO_ENB_SELF_ACTIVE_CFG_PWR_ON = 0xF03B;
    public static final int O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_ACK = 0xF03C;
    public static final int O_FL_LMT_TO_ENB_MEAS_UE_CFG_QUERY = 0xF03D;
    public static final int O_FL_ENB_TO_LMT_MEAS_UE_CFG_QUERY_ACK = 0xF03E;
    public static final int O_FL_LMT_TO_ENB_REDIRECT_INFO_CFG_QUERY = 0xF03F;
    public static final int O_FL_ENB_TO_LMT_REDIRECT_INFO_CFG_QUERY_ACK = 0xF040;
    public static final int O_FL_LMT_TO_ENB_SELF_ACTIVE_CFG_PWR_ON_QUERY = 0xF041;
    public static final int O_FL_ENB_TO_LMT_SELF_ACTIVE_CFG_PWR_ON_QUERY_ACK = 0xF042;
    public static final int O_FL_LMT_TO_ENB_CONTROL_LIST_QUERY = 0xF043;
    public static final int O_FL_ENB_TO_LMT_CONTROL_LIST_QUERY_ACK = 0xF044;
    public static final int O_FL_LMT_TO_ENB_SYS_LOG_LEVL_SET = 0xF045;
    public static final int O_FL_ENB_TO_LMT_SYS_LOG_LEVL_SET_ACK = 0xF046;
    public static final int O_FL_LMT_TO_ENB_SYS_LOG_LEVL_QUERY = 0xF047;
    public static final int O_FL_ENB_TO_LMT_SYS_LOG_LEVL_QUERY_ACK = 0xF048;
    public static final int O_FL_LMT_TO_ENB_TDD_SUBFRAME_ASSIGNMENT_SET = 0xF049;
    public static final int O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_SET_ACK = 0xF04A;
    public static final int O_FL_LMT_TO_ENB_TDD_SUBFRAME_ASSIGNMENT_QUERY = 0xF04B;
    public static final int O_FL_ENB_TO_LMT_TDD_SUBFRAME_ASSIGNMENT_QUERY_ACK = 0xF04C;
    public static final int O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_CFG = 0xF053;
    public static final int O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_CFG_ACK = 0xF054;
    public static final int O_FL_LMT_TO_ENB_LOCATION_UE_BLACKLIST_QUERY = 0xF055;
    public static final int O_FL_ENB_TO_LMT_LOCATION_UE_BLACKLIST_QUERY_ACK = 0xF056;
    public static final int O_FL_LMT_TO_ENB_TAU_ATTACH_REJECT_CAUSE_CFG = 0xF057;
    public static final int O_FL_ENB_TO_LMT_TAU_ATTACH_REJECT_CAUSE_CFG_ACK = 0xF058;
    public static final int O_FL_LMT_TO_ENB_FREQ_OFFSET_CFG = 0xF059;
    public static final int O_FL_ENB_TO_LMT_FREQ_OFFSET_CFG_ACK = 0xF05A;
    public static final int O_FL_ENB_TO_LMT_ALARMING_TYPE_IND = 0xF05B;
    public static final int O_O_FL_LMT_TO_ENB_GPS_LOCATION_QUERY = 0xF05C;
    public static final int O_O_FL_ENB_TO_LMT_GPS_LOCATION_QUERY_ACK = 0xF05D;
    public static final int O_FL_LMT_TO_ENB_SYNCARFCN_CFG = 0xF05E;
    public static final int O_FL_LMT_TO_ENB_SYNCARFCN_CFG_ACK = 0xF05F;
    public static final int O_FL_LMT_TO_ENB_SECONDARY_PLMNS_SET = 0xF060;
    public static final int O_FL_ENB_TO_LMT_SECONDARY_PLMNS_SET_ACK = 0xF061;
    public static final int O_FL_LMT_TO_ENB_SECONDARY_PLMNS_QUERY = 0xF062;
    public static final int O_FL_ENB_TO_LMT_SECONDARY_PLMNS_QUERY_ACK = 0xF063;
    public static final int O_FL_LMT_TO_ENB_RA_ACCESS_QUERY = 0xF065;
    public static final int O_FL_ENB_TO_LMT_RA_ACCESS_QUERY_ACK = 0xF066;
    public static final int O_FL_LMT_TO_ENB_RA_ACCESS_EMPTY_REQ = 0xF067;
    public static final int O_FL_ENB_TO_LMT_RA_ACCESS_EMPTY_REQ_ACK = 0xF068;
    public static final int O_FL_LMT_TO_ENB_TAU_ATTACH_REJECT_CAUSE_QUERY = 0xF06B;
    public static final int O_FL_ENB_TO_LMT_TAU_ATTACH_REJECT_CAUSE_QUERY_ACK = 0xF068C;


    public static final int O_FL_LMT_TO_ENB_SYS_ARFCN_MOD = 0xF080;
    public static final int O_FL_ENB_TO_LMT_SYS_ARFCN_MOD_ACK = 0xF081;


}
