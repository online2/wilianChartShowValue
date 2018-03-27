package com.db.williamchartdemo.barchart;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;
import com.db.williamchartdemo.CardController;
import com.db.williamchartdemo.R;

import java.text.DecimalFormat;


public class BarCardThree extends CardController {


    private final Context mContext;


    private final BarChartView mChart;

    private final String[] mLabels =
            {"", "", "", "", "", "", "", "", "", ""};

    private final float[][] mValues =
            {{0f, 3.7f, 4f, 8f, 4.5f, 4f, 5f, 7f, 10f, -14f},
                    {-3.5f, -4.7f, -5f, -9f, -5.5f, -5f, 6f, 8f, 11f, 13f}};


    public BarCardThree(CardView card, Context context) {

        super(card);

        mContext = context;
        mChart = (BarChartView) card.findViewById(R.id.chart);
    }


    @Override
    public void show(Runnable action) {

        super.show(action);

        Tooltip tip = new Tooltip(mContext);
        tip.setBackgroundColor(Color.parseColor("#CC7B1F"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1);
            tip.setEnterAnimation(alpha).setDuration(150);
            alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0);
            tip.setExitAnimation(alpha).setDuration(150);
        }

        BarSet dataset = new BarSet(mLabels, mValues[0]);
        dataset.setColor(Color.parseColor("#eb993b"));
        mChart.addData(dataset);
        //设置开启绘制文字
        mChart.setEnableDrawValue(true);
        //设置文字大小
        mChart.setValueTextSize(Tools.fromDpToPx(9));
        //设置文字颜色
        mChart.setValueColor(Color.parseColor("#000000"));
        //设置文字格式化和Y轴的使用的同一个代码
        mChart.setLabelsFormat(new DecimalFormat("##人"));
        mChart.setTooltips(tip)
                .setAxisBorderValues(-30,30)
                .show(new Animation().setInterpolator(new AccelerateDecelerateInterpolator())
                        .withEndAction(action));
    }


    @Override
    public void update() {

        super.update();

        mChart.dismissAllTooltips();
        if (firstStage) mChart.updateValues(0, mValues[1]);
        else mChart.updateValues(0, mValues[0]);
        mChart.notifyDataUpdate();
    }


    @Override
    public void dismiss(Runnable action) {

        super.dismiss(action);

        mChart.dismissAllTooltips();
        mChart.dismiss(new Animation().setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(action));
    }

}
