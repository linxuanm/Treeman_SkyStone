package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="TreemanBlueDepot_Red", group="autonomous")
public class TreemanBlueDepot_Red extends Ftc2020Auto {
    public TreemanBlueDepot_Red(){
        this.startPosition = STATUS_BL_START;
        this.parkPosition = PARK_WALL;
        this.runStatus =STATUS_BL_START;
    }
}
