package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Red Loading Nanjing", group="Test")
public class Ftc2020AutoRedLoading extends Ftc2020Auto {

    public Ftc2020AutoRedLoading(){
        this.startPosition = STATUS_RL_START;
        this.parkPosition = PARK_WALL;
        this.runStatus = STATUS_RL_START;
    }
}
