package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="TreemanBlueBuilding_Blue", group="Test")
public class TreemanBlueBuilding_Blue extends Ftc2020Auto {
    public TreemanBlueBuilding_Blue(){
        this.startPosition = STATUS_BB_START;
        this.parkPosition = PARK_WALL;
        this.runStatus = STATUS_BB_START;
    }
}
