package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="TreemanRedBuilding_Red", group="Autonomous")
public class TreemanRedBuilding_Red extends Ftc2020Auto {

    public TreemanRedBuilding_Red(){
        this.startPosition = STATUS_RB_START;
        this.parkPosition = PARK_WALL;
        this.runStatus =STATUS_RB_START;
    }
}
