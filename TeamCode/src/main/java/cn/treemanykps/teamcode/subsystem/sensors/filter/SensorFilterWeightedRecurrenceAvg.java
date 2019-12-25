package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


/***
 * 加权递推平均滤波法
 *
 * 方法：
 * 是对递推平均滤波法的改进,即不同时刻的数据加以不同的权
 * 通常是,越接近现时刻的数据,权取得越大。
 * 给予新采样值的权系数越大,则灵敏度越高,但信号平滑度越低
 *
 * 优点：
 * 适用于有较大纯滞后时间常数的对象
 * 和采样周期较短的系统
 *
 * 缺点：
 * 对于纯滞后时间常数较小,采样周期较长,变化缓慢的信号
 * 不能迅速反应系统当前所受干扰的严重程度,滤波效果差
 */
public class SensorFilterWeightedRecurrenceAvg {

    int maxAvgCount = 5;
    int currAvgCount = 0;
    int insertIndex = 0;
    Double sum = 0.0;
    Double avg = 0.0;

    Double[] dataQueue ;

    public SensorFilterWeightedRecurrenceAvg(){
        init();
    }

    public SensorFilterWeightedRecurrenceAvg(int avgCount){
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
