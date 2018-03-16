package com.example.abc.testui;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.Settings;

import com.example.abc.testui.ui.Global;
import com.example.jbtang.agi_union.R;

import java.util.HashMap;

/**
 * Created by abc on 2017/9/20.
 */

public class SoundPlayer implements Runnable {

    private Thread thread;

    public SoundPlayer(){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        LoadMusic();  //语音播报 初始化
        LoadMusic2();

    }

    //语音播报
    private SoundPool sp;
    HashMap<Integer, Integer> sounddata;
    Boolean isLoaded = false;
    private void LoadMusic() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sounddata = new HashMap<Integer, Integer>();


        sounddata.put(0, sp.load(Global.activity, R.raw.s0, 1));
        sounddata.put(1, sp.load(Global.activity, R.raw.s1, 1));
        sounddata.put(2, sp.load(Global.activity, R.raw.s2, 1));
        sounddata.put(3, sp.load(Global.activity, R.raw.s3, 1));
        sounddata.put(4, sp.load(Global.activity, R.raw.s4, 1));
        sounddata.put(5, sp.load(Global.activity, R.raw.s5, 1));
        sounddata.put(6, sp.load(Global.activity, R.raw.s6, 1));
        sounddata.put(7, sp.load(Global.activity, R.raw.s7, 1));
        sounddata.put(8, sp.load(Global.activity, R.raw.s8, 1));
        sounddata.put(9, sp.load(Global.activity, R.raw.s9, 1));
        sounddata.put(10, sp.load(Global.activity, R.raw.s10, 1));

        sounddata.put(11, sp.load(Global.activity, R.raw.s11, 1));
        sounddata.put(12, sp.load(Global.activity, R.raw.s12, 1));
        sounddata.put(13, sp.load(Global.activity, R.raw.s13, 1));
        sounddata.put(14, sp.load(Global.activity, R.raw.s14, 1));
        sounddata.put(15, sp.load(Global.activity, R.raw.s15, 1));
        sounddata.put(16, sp.load(Global.activity, R.raw.s16, 1));
        sounddata.put(17, sp.load(Global.activity, R.raw.s17, 1));
        sounddata.put(18, sp.load(Global.activity, R.raw.s18, 1));
        sounddata.put(19, sp.load(Global.activity, R.raw.s19, 1));
        sounddata.put(20, sp.load(Global.activity, R.raw.s20, 1));


        sounddata.put(21, sp.load(Global.activity, R.raw.s21, 1));
        sounddata.put(22, sp.load(Global.activity, R.raw.s22, 1));
        sounddata.put(23, sp.load(Global.activity, R.raw.s23, 1));
        sounddata.put(24, sp.load(Global.activity, R.raw.s24, 1));
        sounddata.put(25, sp.load(Global.activity, R.raw.s25, 1));
        sounddata.put(26, sp.load(Global.activity, R.raw.s26, 1));
        sounddata.put(27, sp.load(Global.activity, R.raw.s27, 1));
        sounddata.put(28, sp.load(Global.activity, R.raw.s28, 1));
        sounddata.put(29, sp.load(Global.activity, R.raw.s29, 1));
        sounddata.put(30, sp.load(Global.activity, R.raw.s30, 1));

        sounddata.put(31, sp.load(Global.activity, R.raw.s31, 1));
        sounddata.put(32, sp.load(Global.activity, R.raw.s32, 1));
        sounddata.put(33, sp.load(Global.activity, R.raw.s33, 1));
        sounddata.put(34, sp.load(Global.activity, R.raw.s34, 1));
        sounddata.put(35, sp.load(Global.activity, R.raw.s35, 1));
        sounddata.put(36, sp.load(Global.activity, R.raw.s36, 1));
        sounddata.put(37, sp.load(Global.activity, R.raw.s37, 1));
        sounddata.put(38, sp.load(Global.activity, R.raw.s38, 1));
        sounddata.put(39, sp.load(Global.activity, R.raw.s39, 1));
        sounddata.put(40, sp.load(Global.activity, R.raw.s40, 1));

        sounddata.put(41, sp.load(Global.activity, R.raw.s41, 1));
        sounddata.put(42, sp.load(Global.activity, R.raw.s42, 1));
        sounddata.put(43, sp.load(Global.activity, R.raw.s43, 1));
        sounddata.put(44, sp.load(Global.activity, R.raw.s44, 1));
        sounddata.put(45, sp.load(Global.activity, R.raw.s45, 1));
        sounddata.put(46, sp.load(Global.activity, R.raw.s46, 1));
        sounddata.put(47, sp.load(Global.activity, R.raw.s47, 1));
        sounddata.put(48, sp.load(Global.activity, R.raw.s48, 1));
        sounddata.put(49, sp.load(Global.activity, R.raw.s49, 1));
        sounddata.put(50, sp.load(Global.activity, R.raw.s50, 1));

        sounddata.put(51, sp.load(Global.activity, R.raw.s51, 1));
        sounddata.put(52, sp.load(Global.activity, R.raw.s52, 1));
        sounddata.put(53, sp.load(Global.activity, R.raw.s53, 1));
        sounddata.put(54, sp.load(Global.activity, R.raw.s54, 1));
        sounddata.put(55, sp.load(Global.activity, R.raw.s55, 1));
        sounddata.put(56, sp.load(Global.activity, R.raw.s56, 1));
        sounddata.put(57, sp.load(Global.activity, R.raw.s57, 1));
        sounddata.put(58, sp.load(Global.activity, R.raw.s58, 1));
        sounddata.put(59, sp.load(Global.activity, R.raw.s59, 1));
        sounddata.put(60, sp.load(Global.activity, R.raw.s60, 1));

        sounddata.put(61, sp.load(Global.activity, R.raw.s61, 1));
        sounddata.put(62, sp.load(Global.activity, R.raw.s62, 1));
        sounddata.put(63, sp.load(Global.activity, R.raw.s63, 1));
        sounddata.put(64, sp.load(Global.activity, R.raw.s64, 1));
        sounddata.put(65, sp.load(Global.activity, R.raw.s65, 1));
        sounddata.put(66, sp.load(Global.activity, R.raw.s66, 1));
        sounddata.put(67, sp.load(Global.activity, R.raw.s67, 1));
        sounddata.put(68, sp.load(Global.activity, R.raw.s68, 1));
        sounddata.put(69, sp.load(Global.activity, R.raw.s69, 1));
        sounddata.put(70, sp.load(Global.activity, R.raw.s70, 1));

        sounddata.put(71, sp.load(Global.activity, R.raw.s71, 1));
        sounddata.put(72, sp.load(Global.activity, R.raw.s72, 1));
        sounddata.put(73, sp.load(Global.activity, R.raw.s73, 1));
        sounddata.put(74, sp.load(Global.activity, R.raw.s74, 1));
        sounddata.put(75, sp.load(Global.activity, R.raw.s75, 1));
        sounddata.put(76, sp.load(Global.activity, R.raw.s76, 1));
        sounddata.put(77, sp.load(Global.activity, R.raw.s77, 1));
        sounddata.put(78, sp.load(Global.activity, R.raw.s78, 1));
        sounddata.put(79, sp.load(Global.activity, R.raw.s79, 1));
        sounddata.put(80, sp.load(Global.activity, R.raw.s80, 1));

        sounddata.put(81, sp.load(Global.activity, R.raw.s81, 1));
        sounddata.put(82, sp.load(Global.activity, R.raw.s82, 1));
        sounddata.put(83, sp.load(Global.activity, R.raw.s83, 1));
        sounddata.put(84, sp.load(Global.activity, R.raw.s84, 1));
        sounddata.put(85, sp.load(Global.activity, R.raw.s85, 1));
        sounddata.put(86, sp.load(Global.activity, R.raw.s86, 1));
        sounddata.put(87, sp.load(Global.activity, R.raw.s87, 1));
        sounddata.put(88, sp.load(Global.activity, R.raw.s88, 1));
        sounddata.put(89, sp.load(Global.activity, R.raw.s89, 1));
        sounddata.put(90, sp.load(Global.activity, R.raw.s90, 1));

        sounddata.put(91, sp.load(Global.activity, R.raw.s91, 1));
        sounddata.put(92, sp.load(Global.activity, R.raw.s92, 1));
        sounddata.put(93, sp.load(Global.activity, R.raw.s93, 1));
        sounddata.put(94, sp.load(Global.activity, R.raw.s94, 1));
        sounddata.put(95, sp.load(Global.activity, R.raw.s95, 1));
        sounddata.put(96, sp.load(Global.activity, R.raw.s96, 1));
        sounddata.put(97, sp.load(Global.activity, R.raw.s97, 1));
        sounddata.put(98, sp.load(Global.activity, R.raw.s98, 1));
        sounddata.put(99, sp.load(Global.activity, R.raw.s99, 1));
        sounddata.put(100, sp.load(Global.activity, R.raw.s100, 1));



        //sounddata.put(2, sp.load(Global.activity, R.raw.mp32, 1));
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool sound,int sampleId,int status){

                if (sounddata.get(100)==sampleId) {
                    isLoaded = true;
                }

            }
        });
    }


    //语音播报
    private SoundPool sp2;
    HashMap<Integer, Integer> sounddata2;
    Boolean isLoaded2 = false;
    private void LoadMusic2() {
        sp2 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sounddata2 = new HashMap<Integer, Integer>();


        sounddata2.put(0, sp2.load(Global.activity, R.raw.online, 1));
        sounddata2.put(1, sp2.load(Global.activity, R.raw.offline, 1));




        //sounddata.put(2, sp.load(Global.activity, R.raw.mp32, 1));
        sp2.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool sound,int sampleId,int status){

                if (sounddata2.get(1)==sampleId) {
                    isLoaded2 = true;
                }

            }
        });
    }

    //语音播报
    public void playSound(int sound) {
        if (isLoaded && isLoaded2) {
            AudioManager am = (AudioManager) Global.activity
                    .getSystemService(Context.AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volumnRatio = volumnCurrent / audioMaxVolumn;

            sp.play(sounddata.get(sound),
                    volumnRatio,// 左声道音量
                    volumnRatio,// 右声道音量
                    1, // 优先级
                    0,// 循环播放次数
                    1);// 回放速度，该值在0.5-2.0之间 1为正常速度
        }
    }



    //语音播报
    public void playSoundOnLine() {
        if (isLoaded && isLoaded2) {
            AudioManager am = (AudioManager) Global.activity
                    .getSystemService(Context.AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volumnRatio = volumnCurrent / audioMaxVolumn;

            sp2.play(sounddata2.get(0),
                    volumnRatio,// 左声道音量
                    volumnRatio,// 右声道音量
                    1, // 优先级
                    0,// 循环播放次数
                    1);// 回放速度，该值在0.5-2.0之间 1为正常速度
        }
    }

    //语音播报
    public void playSoundOffLine() {
        if (isLoaded && isLoaded2) {
            AudioManager am = (AudioManager) Global.activity
                    .getSystemService(Context.AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volumnRatio = volumnCurrent / audioMaxVolumn;

            sp2.play(sounddata2.get(1),
                    volumnRatio,// 左声道音量
                    volumnRatio,// 右声道音量
                    1, // 优先级
                    0,// 循环播放次数
                    1);// 回放速度，该值在0.5-2.0之间 1为正常速度
        }
    }
}
