package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


/***
 * 限幅平均滤波法
 *
 * 方法：
 * 相当于“限幅滤波法”+“递推平均滤波法”
 * 每次采样到的新数据先进行限幅处理,
 * 再送入队列进行递推平均滤波处理
 *
 * 优点：
 * 融合了两种滤波法的优点 _
 * 对于偶然出现的脉冲性干扰,可消除由于脉冲干扰所引起的采样值偏差
 *
 * 缺点：
 * 比较浪费RAM
 */
public class SensorFilterThresholdRecurrenceAvg {

    int maxAvgCount = 5;
    int currAvgCount = 0;
    int insertIndex = 0;
    Double sum = 0.0;
    Double avg = 0.0;

    Double[] dataQueue ;

    public SensorFilterThresholdRecurrenceAvg(){
        init();
    }

    public SensorFilterThresholdRecurrenceAvg(int avgCount){
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
