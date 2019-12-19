package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;

/**
 * 一阶滞后滤波法
 *
 * 方法：
 * 取a=0~1
 * 本次滤波结果=（1-a）本次采样值+a上次滤波结果
 *
 * 优点：
 * 对周期性干扰具有良好的抑制作用
 * 适用于波动频率较高的场合
 *
 * 缺点：
 * 相位滞后,灵敏度低
 * 滞后程度取决于a值大小
 * 不能消除滤波频率高于采样频率的1/2的干扰信号
 *
 * */

public class SensorFilterFirstOrderLag {

    int currAvgCount = 0;

    double curValue = 0.0;
    double per =0.7;
    double previous =0.0;

    public SensorFilterFirstOrderLag(){

    }

    public SensorFilterFirstOrderLag(double per){
        this.per = per;
    }

    public void init(){}


    public double input(double value){
        if(currAvgCount < 1){
            previous = value;
            curValue = value;
            currAvgCount = 1;
        }
        curValue +=previous * (1-per) + value * per;
        previous = value;
        return curValue;
    }

    public double getValue(){
        return curValue;
    }

}
