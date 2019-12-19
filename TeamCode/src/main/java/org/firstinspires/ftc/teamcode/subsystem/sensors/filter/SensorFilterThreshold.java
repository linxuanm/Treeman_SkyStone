package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


/***
 * 限幅滤波法（又称程序判断滤波法）
 *
 * 方法：
 * 根据经验判断,确定两次采样允许的最大偏差值（设为A）
 * 每次检测到新值时判断：
 * 如果本次值与上次值之差<=A,则本次值有效
 * 如果本次值与上次值之差>A,则本次值无效,放弃本次值,用上次值代替本次值
 *
 * 优点：
 * 能有效克服因偶然因素引起的脉冲干扰
 *
 * 缺点
 * 无法抑制那种周期性的干扰
 * 平滑度差
 */
public class SensorFilterThreshold {

    double maxError = 1;

    double curValue = 0.0;

    public SensorFilterThreshold(){
        init();
    }

    public SensorFilterThreshold(double error){
        maxError = error;
    }

    public void init(){

    }

    public double input(double value){
        if(Math.abs(value - curValue)<maxError)
            curValue = value;
        return curValue;
    }

    public double getValue(){
        return curValue;
    }

}
