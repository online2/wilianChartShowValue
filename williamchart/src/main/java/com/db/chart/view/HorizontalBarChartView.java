/*
 * Copyright 2015 Diogo Bernardino
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.db.chart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.model.ChartSet;

import java.util.ArrayList;


/**
 * Implements a {@link com.db.chart.view.HorizontalBarChartView} extending {@link
 * com.db.chart.view.ChartView}
 */
public class HorizontalBarChartView extends BaseBarChartView {


    public HorizontalBarChartView(Context context, AttributeSet attrs) {

        super(context, attrs);

        setOrientation(Orientation.HORIZONTAL);
        setMandatoryBorderSpacing();
    }


    public HorizontalBarChartView(Context context) {

        super(context);

        setOrientation(Orientation.HORIZONTAL);
        setMandatoryBorderSpacing();
    }

    @Override
    public void onDrawChart(Canvas canvas, ArrayList<ChartSet> data) {

        final int nSets = data.size();
        final int nEntries = data.get(0).size();

        float offset;
        BarSet barSet;
        Bar bar;

        for (int i = 0; i < nEntries; i++) {

            // Set first offset to draw a group of bars
            offset = data.get(0).getEntry(i).getY() - drawingOffset;

            for (int j = 0; j < nSets; j++) {

                barSet = (BarSet) data.get(j);
                bar = (Bar) barSet.getEntry(i);

                // If entry value is 0 it won't be drawn
                if (!barSet.isVisible()) continue;

                // Style it!
                if (!bar.hasGradientColor()) mStyle.barPaint.setColor(bar.getColor());
                else
                    mStyle.barPaint.setShader(new LinearGradient(this.getZeroPosition(), bar.getY(), bar.getX(), bar.getY(), bar.getGradientColors(), bar.getGradientPositions(), Shader.TileMode
                            .MIRROR));
                applyShadow(mStyle.barPaint, barSet.getAlpha(), bar.getShadowDx(), bar.getShadowDy(), bar.getShadowRadius(), bar.getShadowColor());

                // Draw background
                if (mStyle.hasBarBackground) drawBarBackground(canvas, this.getInnerChartLeft(), offset, this.getInnerChartRight(), (offset + barWidth));

                // TODO: 2018/3/15 当是调用notifyDataUpdate时，如：老数据数据有负值，但新数据没有负值 getZeroPosition的值不会改变，反之相同，只能重新设置一下数据才可以
                //绘制value文字
                if (getEnableDrawValue()) {
                    String value = style.getLabelsFormat().format(bar.getValue());
                    float textW = mStyle.valuePaint.measureText(value);

                    Paint.FontMetrics fontMetrics = mStyle.valuePaint.getFontMetrics();
                    float textY = offset + barWidth / 2 + ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);

                    if (bar.getValue() > 0 && bar.getX()>getZeroPosition()) {
                        float right = bar.getX() - textW * 1.2f;
                        drawBarValue(canvas, right + textW * 0.15f, textY, value);
                        drawBar(canvas, this.getZeroPosition(), offset, right, offset + barWidth);
                    } else if (bar.getValue() <0 && bar.getX()<getZeroPosition()) {
                        float left = bar.getX() + textW;
                        drawBarValue(canvas, left - textW * 1.1f, textY, value);
                        drawBar(canvas, left, offset, this.getZeroPosition(), offset + barWidth);
                    } else {
                        drawBarValue(canvas, bar.getX(), textY, value);
                        drawBar(canvas, this.getZeroPosition(), offset, bar.getX(), offset + barWidth);
                    }
                } else {
                    // Draw bar
                    if (bar.getValue() >= 0) // Positive/正
                        drawBar(canvas, this.getZeroPosition(), offset, bar.getX(), offset + barWidth);
                    else // Negative/负
                        drawBar(canvas, bar.getX(), offset, this.getZeroPosition(), offset + barWidth);
                }

                offset += barWidth;

                // If last bar of group no set spacing is necessary
                if (j != nSets - 1) offset += mStyle.setSpacing;
            }
        }
    }

    @Override
    protected void onPreDrawChart(ArrayList<ChartSet> data) {

        // In case of only on entry
        if (data.get(0).size() == 1) {
            mStyle.barSpacing = 0;
            calculateBarsWidth(data.size(), 0, this.getInnerChartBottom() - this.getInnerChartTop() - this.getBorderSpacing() * 2);
            // In case of more than one entry
        } else calculateBarsWidth(data.size(), data.get(0).getEntry(1).getY(), data.get(0).getEntry(0).getY());

        calculatePositionOffset(data.size());
    }

    @Override
    void defineRegions(ArrayList<ArrayList<Region>> regions, ArrayList<ChartSet> data) {

        int nSets = data.size();
        int nEntries = data.get(0).size();

        float offset;
        BarSet barSet;
        Bar bar;

        for (int i = 0; i < nEntries; i++) {

            // Set first offset to draw a group of bars
            offset = data.get(0).getEntry(i).getY() - drawingOffset;

            for (int j = 0; j < nSets; j++) {

                barSet = (BarSet) data.get(j);
                bar = (Bar) barSet.getEntry(i);

                if (bar.getValue() > 0 && (int) bar.getX() != (int) this.getZeroPosition())
                    regions.get(j).get(i).set((int) this.getZeroPosition(), (int) offset, (int) bar.getX(), (int) (offset + barWidth));
                else if (bar.getValue() < 0 && (int) bar.getX() != (int) this.getZeroPosition())
                    regions.get(j).get(i).set((int) bar.getX(), (int) offset, (int) this.getZeroPosition(), (int) (offset + barWidth));
                else // If bar.getValue() == 0, force region to 1 pixel
                    regions.get(j).get(i).set((int) this.getZeroPosition() - 1, (int) offset, (int) this.getZeroPosition(), (int) (offset + barWidth));

                // If last bar of group no set spacing is necessary
                if (j != nSets - 1) offset += mStyle.setSpacing;
            }
        }
    }

}