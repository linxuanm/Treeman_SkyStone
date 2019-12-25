package org.firstinspires.ftc.teamcode.subsystem.sensors;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystem.sensors.filter.SensorFilterRecurrenceAvg;

public class DistanceDetection {

    private DistanceSensor sensorRange;
    private Rev2mDistanceSensor sensorTimeOfFlight;

    private SensorFilterRecurrenceAvg filter ;

    public DistanceDetection(){
        filter = new SensorFilterRecurrenceAvg(3);
    }

    public void init(HardwareMap hardwareMap){
        // you can use this as a regular DistanceSensor.
        sensorRange = hardwareMap.get(DistanceSensor.class, "sensor_range");

        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        sensorTimeOfFlight = (Rev2mDistanceSensor)sensorRange;

    }

    public double dectect(){
        filter.input(sensorRange.getDistance(DistanceUnit.MM));
        return filter.getValue();
    }
}
