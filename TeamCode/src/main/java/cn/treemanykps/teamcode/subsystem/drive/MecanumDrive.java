package org.firstinspires.ftc.teamcode.subsystem.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/***
 *
 */
public class MecanumDrive {
    public DcMotor leftFrontDrive = null;
    public DcMotor leftRearDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightRearDrive = null;

    public ElapsedTime timer = new ElapsedTime();
    public int runMillSecs = -1;

    private double turnAngleThres = 20;
    private double goAngleThres = 5;

    private double leftFrontPower;
    private double leftRearPower;
    private double rightFrontPower;
    private double rightRearPower;

    public void init(DcMotor leftFront, DcMotor leftRear, DcMotor rightFront, DcMotor rightRear) {

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


    /***
     *
     * @param y 前进后退;大于0向前，小于0向后；
     * @param x 左右平移；大于0向右，小于0向左；
     * @param yaw 左右旋转；大于0右转，小于0左转；
     */
    public void drive(double y, double x, double yaw) {
//        if (Math.abs(x) < 0.3) {
//            x = 0;
//        }
//        if (Math.abs(y) < 0.3) {
//            y = 0;
//        }
//
        leftFrontPower = y + x + yaw;
        leftRearPower = -y - x + yaw;
        rightFrontPower = -y + x + yaw;
        rightRearPower = y - x + yaw;

        rightFrontPower = Range.clip(rightFrontPower, -1, 1);
        leftFrontPower = Range.clip(leftFrontPower, -1, 1);
        leftRearPower = Range.clip(leftRearPower, -1, 1);
        rightRearPower = Range.clip(rightRearPower, -1, 1);

        // write the values to the motors
        //loop();
    }

    /***
     * 直行。
     * @param speed 速度；
     */
    public void go(double speed) {
        drive(speed, 0, 0);
    }

    /**
     * 按指定角度直行。
     *
     * @param speed        速度；
     * @param targetAngle  目标角度；
     * @param currentAngle 当前角度；
     */
    public boolean go(double speed, double targetAngle, double currentAngle) {
        if (Math.abs(speed) < 0.001) {
            drive(0, 0, 0);
            return true;
        }

        speed = Range.clip(speed, -1, 1);
        if (Math.abs(speed) > 0.9)
            speed *= 0.9;

        double angle = targetAngle - currentAngle;
        angle = Range.clip(angle, -goAngleThres, goAngleThres);
        double angleDelta = (angle / goAngleThres) * 0.10;

        drive(speed, 0, angleDelta);
        return false;
    }

    /**
     * 转弯
     *
     * @param speed 速度；
     */
    public void turn(double speed) {
        drive(0, 0, speed);
    }

    /**
     * 转弯到指定角度
     *
     * @param speed        速度；
     * @param targetAngle  目标角度；
     * @param currentAngle 当前角度；
     */
    public boolean turn(double speed, double targetAngle, double currentAngle) {
        if (Math.abs(speed) < 0.001) {
            drive(0, 0, 0);
            return true;
        }
        double angleDelta = Range.clip((targetAngle - currentAngle), -turnAngleThres, turnAngleThres) / turnAngleThres;
        drive(0, 0, speed * angleDelta);
        return Math.abs(targetAngle - currentAngle) < 0.5;
    }

    /**
     * 平移
     *
     * @param speed 速度；
     */
    public void drift(double speed) {
        drive(0, speed, 0);
    }

    /**
     * 平移；
     *
     * @param speed        速度；
     * @param targetAngle  目标角度；
     * @param currentAngle 当前角度；
     */
    public boolean drift(double speed, double targetAngle, double currentAngle) {
        if (Math.abs(speed) < 0.001) {
            drive(0, 0, 0);
            return true;
        }
        speed = Range.clip(speed, -1, 1);
        if (Math.abs(speed) > 0.8)
            speed *= 0.8;

        double angle = targetAngle - currentAngle;
        angle = Range.clip(angle, -goAngleThres, goAngleThres);
        double angleDelta = (angle / goAngleThres) * 0.25;

        drive(0, speed, angleDelta);
        return false;
    }

    public void stop() {
        drive(0, 0, 0);
    }

    public void loop(){

        rightFrontDrive.setPower(rightFrontPower);
        leftFrontDrive.setPower(leftFrontPower);
        rightRearDrive.setPower(leftRearPower);
        leftRearDrive.setPower(rightRearPower);


    }
}
