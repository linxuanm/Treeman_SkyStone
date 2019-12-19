package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue Building Nanjing", group="Test")
public class Ftc2020AutoBlueBuilding extends Ftc2020Auto {
    public Ftc2020AutoBlueBuilding(){
        this.startPosition = STATUS_BB_START;
        this.parkPosition = PARK_WALL;
        this.runStatus = STATUS_BB_START;
    }
}
