package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


/***
 * 中位值平均滤波法（又称防脉冲干扰平均滤波法）
 *
 * 方法：
 * 相当于“中位值滤波法”+“算术平均滤波法”
 * 连续采样N个数据,去掉一个最大值和一个最小值
 * 然后计算N-2个数据的算术平均值
 * N值的选取：3~14
 *
 * 优点：
 * 融合了两种滤波法的优点
 * 对于偶然出现的脉冲性干扰,可消除由于脉冲干扰所引起的采样值偏差
 *
 * 缺点：
 * 测量速度较慢,和算术平均滤波法一样
 * 比较浪费RAM
 */
public class SensorFilterMedianAvg {

    int maxAvgCount = 5;
    int currAvgCount = 0;
    int insertIndex = 0;
    double sum = 0.0;
    double avg = 0.0;

    double[] dataQueue ;

    public SensorFilterMedianAvg(){
        init();
    }

    public SensorFilterMedianAvg(int avgCount){
        maxAvgCount = avgCount;
        init();
    }

    public void init(){
        dataQueue = new double[maxAvgCount];
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
