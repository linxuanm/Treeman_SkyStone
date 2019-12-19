package org.firstinspires.ftc.teamcode.subsystem.sensors.filter;


import java.util.ArrayList;
import java.util.List;

/***
 * 卡尔曼滤波法
 *
 * 
 */
public class SensorAccFilterKalman {

    // Kalman Filter
    public static final double VARIANCE = 0.05;
    public static final double FILTER_GAIN = 0.8;

    private double[] noiseVariances;       // Noise variances
    private double[] predictedVariances;   // Predicted variances
    private double[] predictedValues;      // Predicted values
    private double[] sensorSingleData;      // Predicted values

    private boolean isInitialised;

    public SensorAccFilterKalman(){
        init();
    }

    private void init() {
        noiseVariances = new double[3];
        predictedVariances = new double[3];
        predictedValues = new double[3];
        isInitialised = false;
    }

    public double[] filter(double[] sensorSingleData) {
        this.sensorSingleData = sensorSingleData;
        double[] values = new double[3];
        values[0]=sensorSingleData[0];
        values[1]=sensorSingleData[1];
        values[2]=sensorSingleData[2];
        return process(values);
    }

    private double[] init(double[] initValues) {
        for (int i = 0; i < initValues.length; i++) {
            noiseVariances[i]= VARIANCE;
            predictedVariances[i]= noiseVariances[i];
            predictedValues[i]= initValues[i];
        }
        isInitialised = true;

        sensorSingleData[0]=initValues[0];
        sensorSingleData[1]=initValues[1];
        sensorSingleData[2]=initValues[2];

        return sensorSingleData;
    }

    private double[] process(double[] measurementValues) {
        if (!isInitialised) {
            return init(measurementValues);
        }

        double[] correctedValues = new double[3];
        for (int i = 0; i < measurementValues.length; i++) {
            // compute the Kalman gain
            double kalmanGain = predictedVariances[i] / (predictedVariances[i] + noiseVariances[i]);

            // update the sensor prediction with the measurement
            double correctedValue = FILTER_GAIN * predictedValues[i] + (1.0 - FILTER_GAIN) *
                    measurementValues[i] + kalmanGain * (measurementValues[i] - predictedValues[i]);

            // predict next variance and value
            predictedVariances[i]= predictedVariances[i] * (1.0 - kalmanGain);
            predictedValues[i]=correctedValue;

            correctedValues[i]=correctedValue;
        }

        sensorSingleData[0]=correctedValues[0];
        sensorSingleData[1]=correctedValues[1];
        sensorSingleData[2]=correctedValues[2];

        return sensorSingleData;

    }

}
