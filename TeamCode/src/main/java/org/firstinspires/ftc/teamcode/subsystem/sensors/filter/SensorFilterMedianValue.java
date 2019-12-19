package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


/***
 * 中位值滤波法
 *
 * 方法：
 * 连续采样N次（N取奇数）
 * 把N次采样值按大小排列
 * 取中间值为本次有效值
 *
 * 优点：
 * 能有效克服因偶然因素引起的波动干扰
 * 对温度、液位的变化缓慢的被测参数有良好的滤波效果
 *
 * 缺点：
 * 对流量、速度等快速变化的参数不宜
 */
public class SensorFilterMedianValue {

    int maxAvgCount = 5;
    int currAvgCount = 0;
    int insertIndex = 0;
    Double sum = 0.0;
    Double avg = 0.0;

    Double[] dataQueue ;

    public SensorFilterMedianValue(){
        init();
    }

    public SensorFilterMedianValue(int avgCount){
        maxAvgCount = avgCount;
        init();
    }

    public void init(){
        dataQueue = new Double[maxAvgCount];
        insertIndex = 0;
    }

    public double input(double value){
        if(currAvgCount < maxAvgCount){
            currAvgCount++;
        }else{
            sum -= dataQueue[insertIndex-1];
        }
        sum +=value;
        avg = sum / currAvgCount;
        dataQueue[insertIndex] = value;

        insertIndex++;
        if(insertIndex>=maxAvgCount){
            insertIndex = 0;
        }

        return avg;
    }

    public double getValue(){
        return avg;
    }

}
