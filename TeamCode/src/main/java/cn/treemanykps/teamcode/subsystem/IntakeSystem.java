package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class IntakeSystem {

    public DcMotor motorLeft;
    public DcMotor motorRiht;

    public double intakePower = 1;

    public void init(DcMotor motorLeft, DcMotor motorRiht){
        this.motorLeft = motorLeft;
        this.motorRiht = motorRiht;

        this.motorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        this.motorRiht.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intake(){
        this.motorLeft.setPower(intakePower);
        this.motorRiht.setPower(intakePower);
    }

    public void outPut(){
        this.motorLeft.setPower(-intakePower);
        this.motorRiht.setPower(-intakePower);
    }

    public void stop() {
        this.motorLeft.setPower(0);
        this.motorRiht.setPower(0);
    }
}
