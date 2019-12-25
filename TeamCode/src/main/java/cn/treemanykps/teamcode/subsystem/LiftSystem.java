package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LiftSystem {
    private DcMotor motor = null;
    private int initPosition;

    public LiftSystem(){

    }

    public void init(DcMotor motor){
        this.motor = motor;
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        initPosition = motor.getCurrentPosition();
    }

    public void up(double power){
        motor.setPower(power);
    }
    public void up() {
        motor.setPower(0.4);
    }
    public void up(int level){

    }

    public void down(double power){
        motor.setPower(power);
    }

    public void down(){
        motor.setPower(-0.4);
    }

    public void down(int level){

    }
    public void stop(){
        motor.setPower(0);
    }

}
