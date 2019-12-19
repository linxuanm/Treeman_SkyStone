package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Red Building Nanjing", group="Test")
public class Ftc2020AutoRedBuilding extends Ftc2020Auto {

    public Ftc2020AutoRedBuilding(){
        this.startPosition = STATUS_RB_START;
        this.parkPosition = PARK_WALL;
        this.runStatus =STATUS_RB_START;
    }
}
