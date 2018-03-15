首先非常感谢[diogobernardino 提供的 WilliamChart图表控件](https://github.com/diogobernardino/WilliamChart)<br>
因为项目基本都是图表控件，结合了好多最终修了一些里面源码，增加了图表提供绘制Value的方法<br>
1：提供了HorizontalBarChartView 在bar顶显示对应value值，具体使用方法可以看BarCardTow里面<br>
        设置方法如下<br>
                  //设置开启绘制文字<br>
                     mChart.setEnableDrawValue(true);<br>
                  //设置文字大小<br>
                     mChart.setValueTextSize(Tools.fromDpToPx(9));<br>
                  //设置文字颜色<br>
                     mChart.setValueColor(Color.parseColor("#000000"));<br>
                 //设置文字格式化和Y轴的使用的同一个代码<br>
                     mChart.setLabelsFormat(new DecimalFormat("##人"));<br>
        效果图<br>
                ![image](https://github.com/online2/wilianChartShowValue/blob/master/mobile/src/main/res/drawable-xhdpi/horbar_icon.png) <br><br>      
        *注意* <br>
        
        在修改的时候发现，当是调用notifyDataUpdate时，<br>
        如：老数据数据有负值，但新数据没有负值 getZeroPosition的值不会改变，反之相同，只能重新设置一下数据才可以<br>
        

2：提供了LineChartView 的绘制Value方法 <br>
        设置方法如下 <br>
                       //设置开启绘制文字<br>
                         .setEnableDrawValue(true) <br>
                      //设置文字颜色<br>
                         .setValueColor(Color.parseColor("#ffffff"))<br>
                      //设置文字大小<br>
                         .setValueTextSize(Tools.fromDpToPx(10)); <br>
                       //设置文字格式化和Y轴的使用的同一个代码<br>
                         mChart..setLabelsFormat(new DecimalFormat("##人"))<br>
        效果图<br>
               ![image](https://github.com/online2/wilianChartShowValue/blob/master/mobile/src/main/res/drawable-xhdpi/linbar_icon.png) <br><br>
       
    
    