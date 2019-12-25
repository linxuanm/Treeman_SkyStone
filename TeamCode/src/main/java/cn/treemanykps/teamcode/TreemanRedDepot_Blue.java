package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="TreemanRedDepot_Blue", group="Test")
public class TreemanRedDepot_Blue extends Ftc2020Auto {

    public TreemanRedDepot_Blue(){
        this.startPosition = STATUS_RL_START;
        this.parkPosition = PARK_WALL;
        this.runStatus = STATUS_RL_START;
    }
}
