package org.firstinspires.ftc.teamcode.subsystem.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class OmniDrive implements DriveInterface {
    public DcMotor leftFrontDrive = null;
    public DcMotor leftRearDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightRearDrive = null;


    public void init(DcMotor leftFront, DcMotor leftRear, DcMotor rightFront, DcMotor rightRear ){

        leftFrontDrive = leftFront;
        leftRearDrive = leftRear;
        rightFrontDrive = rightFront;
        rightRearDrive = rightRear;

        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void drive(double y, double x, double yaw){

        //
        double FrontLeft = -y - x - yaw;
        double BackLeft = -y + x - yaw;

        double FrontRight = y - x - yaw;
        double BackRight = y + x - yaw;

        // clip the right/left values so that the values never exceed +/- 1
        FrontRight = Range.clip(FrontRight, -1, 1);
        FrontLeft = Range.clip(FrontLeft, -1, 1);
        BackLeft = Range.clip(BackLeft, -1, 1);
        BackRight = Range.clip(BackRight, -1, 1);

        // write the values to the motors
        rightFrontDrive.setPower(FrontRight);
        leftFrontDrive.setPower(FrontLeft);
        rightRearDrive.setPower(BackLeft);
        leftRearDrive.setPower(BackRight);
    }

    public void go(double speed){
        drive(speed,0,0);
    }

    public boolean go(double speed, double targetAngle, double currentAngle){
        drive(speed,0,0);
        return false;
    }

    public void turn(double speed){
        drive(0,0,speed);
    }

    public boolean turn(double speed, double targetAngle, double currentAngle){
        drive(0,0,speed);
        return false;
    }

    public void drift(double speed){
        drive(0,speed,0);
    }

    public boolean drift(double speed, double targetAngle, double angle){

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        return false;
    }

    @Override
    public void stop() {
        drive(0,0,0);
    }
}
