package org.firstinspires.ftc.teamcode.subsystem;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.teamcode.subsystem.drive.DriveInterface;
import org.firstinspires.ftc.teamcode.subsystem.video.LocationDetection;
import org.firstinspires.ftc.teamcode.subsystem.video.StoneDetection;

public class RobotNavigate {

    DriveInterface drive = null;
    StoneDetection stoneDetection = null;
    LocationDetection locationDetection = null;

    OpenGLMatrix currentLocation = null;


}
