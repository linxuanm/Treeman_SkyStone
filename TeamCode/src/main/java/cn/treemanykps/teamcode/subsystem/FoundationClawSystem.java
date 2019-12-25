package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.Servo;

public class FoundationClawSystem {
    private Servo s1 = null;
    private Servo s2 = null;


    double servoPosMin = 0;
    double servoPosMax = 0.6;

    public void init(Servo s1, Servo s2){
        this.s1 = s1;
        this.s2 = s2;
        s1.setDirection(Servo.Direction.FORWARD);
        s1.setDirection(Servo.Direction.REVERSE);
    }

    public void close(){
        s1.setPosition(servoPosMax);
        s2.setPosition(servoPosMax);
    }
    public void open(){
        s1.setPosition(servoPosMin);
        s2.setPosition(servoPosMin);
    }
}
