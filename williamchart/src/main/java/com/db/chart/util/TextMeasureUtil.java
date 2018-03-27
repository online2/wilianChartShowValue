package com.db.chart.util;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Create by: MrChao
 * Date: 2018/3/26
 * time: 12:56
 * 文字測量类
 */

public class TextMeasureUtil {

    public static int getTextWidth(Paint mPaint, String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static int getTextHeight(Paint mPaint, String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }
}
