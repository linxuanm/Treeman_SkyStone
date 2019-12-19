package org.firstinspires.ftc.teamcode.subsystem.drive;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface DriveInterface {


    void init(DcMotor leftFront, DcMotor leftRear, DcMotor rightFront, DcMotor rightRear);

    void drive(double y, double x, double yaw);

    void go(double speed);

    boolean go(double speed, double targetAngle, double angle);

    void turn(double speed);

    boolean turn(double speed, double targetAngle, double angle);

    void drift(double speed);

    boolean drift(double speed, double targetAngle, double angle);

    void stop();
}
