package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue Loading Nanjing", group="Test")
public class Ftc2020AutoBlueLoading extends Ftc2020Auto {
    public Ftc2020AutoBlueLoading(){
        this.startPosition = STATUS_BL_START;
        this.parkPosition = PARK_WALL;
        this.runStatus =STATUS_BL_START;
    }
}
