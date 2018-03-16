package com.example.jbtang.agi_union.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by 刘洋旭 on 2017/5/10.
 */
public class MyProgress extends ProgressBar {
    String text;
    Paint mPaint;
    public MyProgress(Context context) {
        super(context);
        initText();
    }

    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public MyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        this.mPaint.setTextSize(30);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }

    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);

    }
    private void setText(){
        setText(this.getProgress());
    }
    private void setText(int progress){

        int i = (progress * 100)/this.getMax();
        this.text = String.valueOf(i);

    }
}
